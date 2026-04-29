package barrettbubalo.spotifytracker.controller;

import barrettbubalo.spotifytracker.model.Account;
import barrettbubalo.spotifytracker.model.ListeningRecord;
import barrettbubalo.spotifytracker.repository.AccountRepository;
import barrettbubalo.spotifytracker.repository.ListeningRecordRepository;
import barrettbubalo.spotifytracker.service.SpotifyApiClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/spotify")
public class SpotifyAuthController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SpotifyApiClient spotifyApiClient;

    @Autowired
    private ListeningRecordRepository listeningRecordRepository;
    
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
    public void callback(@RequestParam String code, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("accountId") == null) {
            response.sendRedirect("/login.html?error=not_logged_in");
            return;
        }

        Long accountId = (Long) session.getAttribute("accountId");
        Account account = accountRepository.findById(accountId).orElseThrow();

        spotifyApiClient.exchangeCodeForTokens(account, code);

        // Redirect to dashboard after successful connection
        response.sendRedirect("/dashboard.html?spotify=connected");
    }

    @GetMapping("/history/{accountId}")
    public ResponseEntity<List<ListeningRecord>> getHistory(@PathVariable Long accountId, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("accountId") == null) {
            return ResponseEntity.status(401).build();
        }

        Long sessionAccountId = (Long) session.getAttribute("accountId");
        if (!sessionAccountId.equals(accountId)) {
            return ResponseEntity.status(403).build();
        }

        List<ListeningRecord> records = listeningRecordRepository.findByAccountIdOrderByPlayedAtDesc(accountId);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/disconnect")
    public void disconnect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("accountId") == null) {
            response.sendRedirect("/login.html");
            return;
        }

        Long accountId = (Long) session.getAttribute("accountId");
        Account account = accountRepository.findById(accountId).orElseThrow();

        // Clear Spotify tokens
        account.setAccessToken(null);
        account.setRefreshToken(null);
        accountRepository.save(account);

        response.sendRedirect("/dashboard.html?disconnected=true");
    }
}