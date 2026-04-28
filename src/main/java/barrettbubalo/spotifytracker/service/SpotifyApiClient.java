package barrettbubalo.spotifytracker.service;

import barrettbubalo.spotifytracker.model.Account;
import barrettbubalo.spotifytracker.repository.AccountRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class SpotifyApiClient {

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;

    @Value("${spotify.redirect-uri}")
    private String redirectUri;

    @Autowired
    private AccountRepository accountRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // ---------------------------------------------------------------
    // Token Management
    // ---------------------------------------------------------------

    /**
     * Exchanges the authorization code from Spotify's callback for
     * access and refresh tokens. Called once during the "Connect Spotify" flow.
     */
    public void exchangeCodeForTokens(Account account, String authorizationCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + encodeCredentials());

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", authorizationCode);
        body.add("redirect_uri", redirectUri);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
            "https://accounts.spotify.com/api/token",
            request,
            String.class
        );

        saveTokensFromResponse(account, response.getBody());
    }

    /**
     * Uses the refresh token to get a new access token when the current
     * one has expired. Called automatically before making API requests.
     */
    public void refreshAccessToken(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + encodeCredentials());

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", account.getRefreshToken());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
            "https://accounts.spotify.com/api/token",
            request,
            String.class
        );

        saveTokensFromResponse(account, response.getBody());
    }

    /**
     * Checks if the access token is expired or about to expire (within 5 minutes).
     * If so, refreshes it before returning.
     */
    private void ensureValidToken(Account account) {
        if (account.getTokenExpiresAt() == null ||
            account.getTokenExpiresAt().isBefore(LocalDateTime.now().plusMinutes(5))) {
            refreshAccessToken(account);
        }
    }

    // ---------------------------------------------------------------
    // Spotify API Calls
    // ---------------------------------------------------------------

    /**
     * Gets the current user's Spotify profile.
     * Returns the raw JSON response from /v1/me
     */
    public JsonNode getCurrentUserProfile(Account account) {
        return makeSpotifyRequest(account, "https://api.spotify.com/v1/me");
    }

    /**
     * Gets the user's recently played tracks (up to 50).
     * This is the primary endpoint for building listening history.
     *
     * @param limit  Number of tracks to return (max 50)
     * @param after  Unix timestamp in ms — only return tracks played after this time (optional, pass 0 to ignore)
     */
    public JsonNode getRecentlyPlayed(Account account, int limit, long after) {
        String url = "https://api.spotify.com/v1/me/player/recently-played?limit=" + limit;
        if (after > 0) {
            url += "&after=" + after;
        }
        return makeSpotifyRequest(account, url);
    }

    /**
     * Gets the user's top artists.
     *
     * @param timeRange  "short_term" (4 weeks), "medium_term" (6 months), or "long_term" (all time)
     * @param limit      Number of artists to return (max 50)
     */
    public JsonNode getTopArtists(Account account, String timeRange, int limit) {
        String url = "https://api.spotify.com/v1/me/top/artists"
            + "?time_range=" + timeRange
            + "&limit=" + limit;
        return makeSpotifyRequest(account, url);
    }

    /**
     * Gets the user's top tracks.
     *
     * @param timeRange  "short_term" (4 weeks), "medium_term" (6 months), or "long_term" (all time)
     * @param limit      Number of tracks to return (max 50)
     */
    public JsonNode getTopTracks(Account account, String timeRange, int limit) {
        String url = "https://api.spotify.com/v1/me/top/tracks"
            + "?time_range=" + timeRange
            + "&limit=" + limit;
        return makeSpotifyRequest(account, url);
    }

    /**
     * Gets audio features (danceability, energy, tempo, etc.) for a specific track.
     * Useful for interesting visualizations and stats.
     */
    public JsonNode getAudioFeatures(Account account, String trackSpotifyId) {
        String url = "https://api.spotify.com/v1/audio-features/" + trackSpotifyId;
        return makeSpotifyRequest(account, url);
    }

    /**
     * Gets details about a specific artist, including genre tags.
     */
    public JsonNode getArtist(Account account, String artistSpotifyId) {
        String url = "https://api.spotify.com/v1/artists/" + artistSpotifyId;
        return makeSpotifyRequest(account, url);
    }

    // ---------------------------------------------------------------
    // Helper Methods
    // ---------------------------------------------------------------

    /**
     * Makes an authenticated GET request to any Spotify API endpoint.
     * Automatically refreshes the token if expired.
     */
    private JsonNode makeSpotifyRequest(Account account, String url) {
        ensureValidToken(account);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + account.getAccessToken());

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            request,
            String.class
        );

        try {
            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Spotify API response", e);
        }
    }

    /**
     * Parses the token response from Spotify and saves the tokens
     * to the user's account in the database.
     */
    private void saveTokensFromResponse(Account account, String responseBody) {
        try {
            JsonNode json = objectMapper.readTree(responseBody);

            account.setAccessToken(json.get("access_token").asText());

            // Refresh token is only included in the initial authorization response,
            // not always in refresh responses. Only update if present.
            if (json.has("refresh_token")) {
                account.setRefreshToken(json.get("refresh_token").asText());
            }

            // expires_in is in seconds, so we add that to the current time
            int expiresIn = json.get("expires_in").asInt();
            account.setTokenExpiresAt(LocalDateTime.now().plusSeconds(expiresIn));

            accountRepository.save(account);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse token response", e);
        }
    }

    /**
     * Encodes client ID and secret as Base64 for the Authorization header.
     * Spotify requires this format for token requests.
     */
    private String encodeCredentials() {
        String credentials = clientId + ":" + clientSecret;
        return Base64.getEncoder().encodeToString(credentials.getBytes());
    }
}