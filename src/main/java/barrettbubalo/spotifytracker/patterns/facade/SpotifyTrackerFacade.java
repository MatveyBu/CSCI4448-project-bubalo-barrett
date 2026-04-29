package barrettbubalo.spotifytracker.patterns.facade;

import barrettbubalo.spotifytracker.model.Account;
import barrettbubalo.spotifytracker.service.SpotifyApiClient;
import barrettbubalo.spotifytracker.service.SpotifySyncService;
import barrettbubalo.spotifytracker.repository.AccountRepository;
import barrettbubalo.spotifytracker.repository.ListeningRecordRepository;
import barrettbubalo.spotifytracker.model.ListeningRecord;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * FACADE PATTERN IMPLEMENTATION
 * 
 * This Facade provides a simplified, unified interface to the complex
 * Spotify integration subsystem. It hides the complexity of:
 * - OAuth token management (SpotifyApiClient)
 * - Data synchronization (SpotifySyncService)
 * - Repository access (AccountRepository, ListeningRecordRepository)
 * 
 * Instead of clients calling multiple services directly, they now call
 * this single simplified Facade.
 * 
 * Benefits:
 * 1. Decoupling - Controllers don't need to know about all services
 * 2. Simplicity - High-level operations in a few method calls
 * 3. Maintainability - Changes to internal services don't affect clients
 * 4. Consistency - All Spotify operations go through one entry point
 */
@Service
public class SpotifyTrackerFacade {

    @Autowired
    private SpotifyApiClient spotifyApiClient;

    @Autowired
    private SpotifySyncService spotifySyncService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ListeningRecordRepository listeningRecordRepository;

    // ========================================================================
    // HIGH-LEVEL OPERATIONS (Simplified Interface)
    // ========================================================================

    /**
     * Complete Spotify OAuth flow:
     * 1. Exchanges authorization code for tokens
     * 2. Saves tokens to account
     * 3. Syncs user's recently played tracks
     * 
     * This encapsulates multiple steps into one simple call.
     * 
     * @param account User's account
     * @param authorizationCode Code from Spotify callback
     * @return Success message
     */
    public String completeSpotifyConnection(Account account, String authorizationCode) {
        // Step 1: Exchange code for tokens (handles internally)
        spotifyApiClient.exchangeCodeForTokens(account, authorizationCode);
        
        // Step 2: Sync recently played data (no need for client to know about this)
        syncUserListeningHistory(account);
        
        return "Spotify account fully connected and synced";
    }

    /**
     * Get all of a user's listening history stored locally.
     * Abstracts away repository layer complexity.
     * 
     * @param accountId User's database ID
     * @return List of listening records
     */
    public List<ListeningRecord> getUserListeningHistory(Long accountId) {
        return listeningRecordRepository.findByAccountId(accountId);
    }

    /**
     * Get listening history after a specific timestamp.
     * Useful for incremental syncs and filtering recent activity.
     * 
     * @param accountId User's database ID
     * @param after Only return records played after this time
     * @return Filtered listening records
     */
    public List<ListeningRecord> getUserListeningHistorySince(Long accountId, LocalDateTime after) {
        return listeningRecordRepository.findByAccountIdAndPlayedAtAfter(accountId, after);
    }

    /**
     * Sync user's recently played tracks from Spotify.
     * Handles all the complexity of:
     * - Fetching data from Spotify API
     * - Parsing JSON response
     * - Creating/updating music entities
     * - Saving to database
     * 
     * @param account User's account with valid tokens
     */
    public void syncUserListeningHistory(Account account) {
        spotifySyncService.syncRecentlyPlayed(account);
    }

    /**
     * Get current user's Spotify profile information.
     * 
     * @param account User's account with valid tokens
     * @return User profile data (JSON)
     */
    public JsonNode getUserProfile(Account account) {
        return spotifyApiClient.getCurrentUserProfile(account);
    }

    /**
     * Get user's top artists over a time period.
     * 
     * @param account User's account with valid tokens
     * @param timeRange "short_term" (4 weeks), "medium_term" (6 months), or "long_term" (all time)
     * @param limit Number of artists to return (max 50)
     * @return Top artists data (JSON)
     */
    public JsonNode getTopArtists(Account account, String timeRange, int limit) {
        return spotifyApiClient.getTopArtists(account, timeRange, limit);
    }

    /**
     * Get user's top tracks over a time period.
     * 
     * @param account User's account with valid tokens
     * @param timeRange "short_term" (4 weeks), "medium_term" (6 months), or "long_term" (all time)
     * @param limit Number of tracks to return (max 50)
     * @return Top tracks data (JSON)
     */
    public JsonNode getTopTracks(Account account, String timeRange, int limit) {
        return spotifyApiClient.getTopTracks(account, timeRange, limit);
    }

    /**
     * Get audio features for a specific track.
     * Features include: danceability, energy, tempo, acousticness, etc.
     * 
     * @param account User's account with valid tokens
     * @param trackSpotifyId Spotify ID of the track
     * @return Audio features data (JSON)
     */
    public JsonNode getTrackAudioFeatures(Account account, String trackSpotifyId) {
        return spotifyApiClient.getAudioFeatures(account, trackSpotifyId);
    }

    /**
     * Get information about a specific artist.
     * 
     * @param account User's account with valid tokens
     * @param artistSpotifyId Spotify ID of the artist
     * @return Artist information (JSON)
     */
    public JsonNode getArtistInfo(Account account, String artistSpotifyId) {
        return spotifyApiClient.getArtist(account, artistSpotifyId);
    }

    /**
     * Refresh user's Spotify access token (handles expiration automatically).
     * Called automatically by the system before API requests, but exposed
     * here for explicit control if needed.
     * 
     * @param account User's account with refresh token
     */
    public void refreshSpotifyToken(Account account) {
        spotifyApiClient.refreshAccessToken(account);
    }

    /**
     * Check if a user has an active Spotify connection.
     * 
     * @param account User's account
     * @return true if has access token and refresh token
     */
    public boolean hasValidSpotifyConnection(Account account) {
        return account.getAccessToken() != null && 
               account.getRefreshToken() != null &&
               !account.getAccessToken().isEmpty() &&
               !account.getRefreshToken().isEmpty();
    }
}

