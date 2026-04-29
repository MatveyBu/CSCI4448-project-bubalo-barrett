package barrettbubalo.spotifytracker.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

class ListeningRecordTest {
    @Test
    void testRecord() {
        Track track = new Track();
        LocalDateTime now = LocalDateTime.now();
        ListeningRecord record = new ListeningRecord(null, track, now);

        record.setId(10L);
        assertThat(record.getId()).isEqualTo(10L);
        assertThat(record.getTrack()).isEqualTo(track);
        assertThat(record.getPlayedAt()).isEqualTo(now);

        record.setAccount(null);
        assertThat(record.getAccount()).isNull();
    }
}