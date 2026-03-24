package spotifyanalytics.integration;

import org.junit.jupiter.api.Test;
import spotifyanalytics.domain.PlayedTrack;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpotifyClientFacadeTest {

    @Test
    public void getRecentlyPlayedRespectsLimitAndOrder() {
        SpotifyClient client = new FakeSpotifyClient();

        List<PlayedTrack> recent = client.getRecentlyPlayed(2);

        assertEquals(2, recent.size());
        assertEquals("Creep", recent.get(0).getTrack().getTitle());
        assertEquals("Karma Police", recent.get(1).getTrack().getTitle());
    }
}

