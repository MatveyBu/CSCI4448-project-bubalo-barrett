package barrettbubalo.spotifytracker.controller;

import barrettbubalo.spotifytracker.model.Account;
import barrettbubalo.spotifytracker.repository.AccountRepository;
import barrettbubalo.spotifytracker.service.SpotifyApiClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/spotify")
public class SpotifyAuthController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SpotifyApiClient spotifyApiClient;
    
    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.redirect-uri}")
    private String redirectUri;

    @GetMapping("/connect")
    public void connect(HttpServletResponse response) throws IOException {
        String scopes = "user-read-recently-played user-top-read";

        String authUrl = "https://accounts.spotify.com/authorize"
            + "?client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8)
            + "&response_type=code"
            + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
            + "&scope=" + URLEncoder.encode(scopes, StandardCharsets.UTF_8);

        response.sendRedirect(authUrl);
    }

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam String code) {
        Account account = accountRepository.findByEmail("test@test.com").orElseThrow();
        spotifyApiClient.exchangeCodeForTokens(account, code);
        return ResponseEntity.ok("Spotify connected!");
    }

    /* real /callback
    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam String code, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("accountId") == null) {
            return ResponseEntity.status(401).body("Not logged In");
        }

        Long accountId = (Long) session.getAttribute("accountId");
        Account account = accountRepository.findById(accountId).orElseThrow();

        spotifyApiClient.exchangeCodeForTokens(account, code);

        return ResponseEntity.ok("Spotify connected");
        
    }
    */
}