package barrettbubalo.spotifytracker.repository;

import barrettbubalo.spotifytracker.model.Account;
import barrettbubalo.spotifytracker.model.Notification;
import barrettbubalo.spotifytracker.model.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByAccountAndSeenFalse(Account account);
    List<Notification> findByAccountOrderByCreatedAtDesc(Account account);
    List<Notification> findByAccountAndType(Account account, NotificationType type);
}