package spotifyanalytics.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class PlayedTrackTest {

    @Test
    public void testPlayedTrackStoresCorrectly(){
        Artist artist = new Artist("1", "Radiohead");
        Track track = new Track("t1", "Creep", artist, 238000);
        Instant now = Instant.now();

        PlayedTrack playedTrack = new PlayedTrack(track, now);

        assertEquals("Creep", playedTrack.getTrack().getTitle());
        assertEquals(now, playedTrack.getPlayedAt());
    }
}
