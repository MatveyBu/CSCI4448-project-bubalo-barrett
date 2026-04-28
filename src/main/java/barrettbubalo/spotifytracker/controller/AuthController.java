package barrettbubalo.spotifytracker.controller;

import barrettbubalo.spotifytracker.model.Account;
import barrettbubalo.spotifytracker.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String displayName = body.get("displayName");
        String password = body.get("password");

        if (accountRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body("Email already in use");
        }

        Account account = new Account();
        account.setEmail(email);
        account.setDisplayName(displayName);
        account.setPasswordHash(passwordEncoder.encode(password));

        accountRepository.save(account);

        return ResponseEntity.ok("Account created");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body, HttpServletRequest request) {
        String email = body.get("email");
        String password = body.get("password");

        Optional<Account> account = accountRepository.findByEmail(email);

        if (account.isEmpty()) {
            return ResponseEntity.badRequest().body("No account associated with the provided email");
        }

        if (!passwordEncoder.matches(password, account.get().getPasswordHash())) {
            return ResponseEntity.badRequest().body("Invalid password");
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("accountId", account.get().getId());
        session.setAttribute("email", account.get().getEmail());

        return ResponseEntity.ok("Login Successful");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // false means get the session but dont create one if the session doesnt exist
        if (session == null || session.getAttribute("accountId") == null) {
            return ResponseEntity.status(401).body("Not logged in");
        }

        Long accountId = (Long) session.getAttribute("accountId");
        Account account = accountRepository.findById(accountId).orElseThrow();

        return ResponseEntity.ok(Map.of(
            "id", account.getId(),
            "email", account.getEmail(),
            "displayName", account.getDisplayName()
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok("Logged out");
    }
}