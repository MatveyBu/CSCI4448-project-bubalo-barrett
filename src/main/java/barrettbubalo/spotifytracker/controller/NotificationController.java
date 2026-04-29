package barrettbubalo.spotifytracker.controller;

import barrettbubalo.spotifytracker.model.Account;
import barrettbubalo.spotifytracker.model.NotificationType;
import barrettbubalo.spotifytracker.repository.AccountRepository;
import barrettbubalo.spotifytracker.repository.NotificationRepository;
import barrettbubalo.spotifytracker.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/unseen")
    public ResponseEntity<?> getUnseenNotifications() {
        // TODO: restore session-based auth later
        Account account = accountRepository.findById(1L).orElseThrow();
        return ResponseEntity.ok(notificationRepository.findByAccountAndSeenFalse(account));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllNotifications() {
        Account account = accountRepository.findById(1L).orElseThrow();
        return ResponseEntity.ok(notificationRepository.findByAccountOrderByCreatedAtDesc(account));
    }

    @GetMapping("/by-type")
    public ResponseEntity<?> getNotificationsByType(@RequestParam String type) {
        Account account = accountRepository.findById(1L).orElseThrow();
        NotificationType notificationType = NotificationType.valueOf(type.toUpperCase());
        return ResponseEntity.ok(notificationRepository.findByAccountAndType(account, notificationType));
    }

    @PostMapping("/mark-seen")
    public ResponseEntity<?> markAllSeen() {
        Account account = accountRepository.findById(1L).orElseThrow();
        notificationService.markAllSeen(account);
        return ResponseEntity.ok("Notifications marked as seen");
    }
}