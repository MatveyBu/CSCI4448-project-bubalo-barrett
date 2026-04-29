package barrettbubalo.spotifytracker.service;

import barrettbubalo.spotifytracker.model.Account;
import barrettbubalo.spotifytracker.model.Notification;
import barrettbubalo.spotifytracker.model.NotificationType;
import barrettbubalo.spotifytracker.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import barrettbubalo.spotifytracker.patterns.observer.*;

/**
 * Centralized notification service that receives events from multiple
 * sources and creates user-facing notifications.
 * 
 * Currently observes:
 * - MilestoneChecker (new milestones achieved)
 * - StreakTracker (streak updates)
 * 
 * Could be extended to handle:
 * - Friend activity
 * - New releases from favorite artists
 * - Listening goal progress
 */
@Service
public class NotificationService implements MilestoneEventListener {

    @Autowired
    private NotificationRepository notificationRepository;

    /**
     * Creates a notification for a milestone achievement.
     * Called by MilestoneChecker when a new milestone is created.
     */
    public void notifyMilestone(Account account, String description) {
        Notification notification = new Notification(
            account,
            NotificationType.MILESTONE,
            description
        );
        notificationRepository.save(notification);
    }

    /**
     * Creates a notification for a streak update.
     * Called by StreakTracker when a streak record is broken or a streak ends.
     */
    /*
    public void notifyStreak(Account account, String description) {
        Notification notification = new Notification(
            account,
            NotificationType.STREAK,
            description
        );
        notificationRepository.save(notification);
    }
    */

    /**
     * Creates a general notification.
     * Can be used by any service that needs to notify the user.
     */
    public void notify(Account account, NotificationType type, String description) {
        Notification notification = new Notification(
            account,
            type,
            description
        );
        notificationRepository.save(notification);
    }

    /**
     * Marks all unseen notifications as seen for an account.
     */
    public void markAllSeen(Account account) {
        var unseen = notificationRepository.findByAccountAndSeenFalse(account);
        for (Notification n : unseen) {
            n.setSeen(true);
            notificationRepository.save(n);
        }
    }

    @Override
    public void onMilestoneAchieved(Account account, Milestone milestone) {
        notifyMilestone(account, milestone.getDescription());
    }
}