package barrettbubalo.spotifytracker.controller;

import barrettbubalo.spotifytracker.model.Account;
import barrettbubalo.spotifytracker.repository.AccountRepository;
import barrettbubalo.spotifytracker.patterns.facade.SpotifyTrackerFacade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;

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
    private SpotifyTrackerFacade spotifyFacade;
    
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
        String message = spotifyFacade.completeSpotifyConnection(account, code);
        return ResponseEntity.ok(message);
    }

    /* real /callback (using Facade Pattern)
    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam String code, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("accountId") == null) {
            return ResponseEntity.status(401).body("Not logged In");
        }

        Long accountId = (Long) session.getAttribute("accountId");
        Account account = accountRepository.findById(accountId).orElseThrow();

        // ONE facade call instead of multiple service calls
        String message = spotifyFacade.completeSpotifyConnection(account, code);
        
        return ResponseEntity.ok(message);
    }
    */
}