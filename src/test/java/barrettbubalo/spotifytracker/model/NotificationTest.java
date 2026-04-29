package barrettbubalo.spotifytracker.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class NotificationTest {
    @Test
    void testNotificationLogic() {
        Notification n = new Notification(null, NotificationType.MILESTONE, "msg");
        n.onCreate();

        assertThat(n.getMessage()).isEqualTo("msg");
        assertThat(n.getType()).isEqualTo(NotificationType.MILESTONE);
        assertThat(n.getCreatedAt()).isNotNull();
        assertThat(n.isSeen()).isFalse();

        n.setSeen(true);
        n.setId(1L);
        assertThat(n.getId()).isEqualTo(1L);
        assertThat(n.isSeen()).isTrue();
    }
}