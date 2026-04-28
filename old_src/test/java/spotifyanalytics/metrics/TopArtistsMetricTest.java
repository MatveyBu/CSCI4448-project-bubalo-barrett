package spotifyanalytics.metrics;

import org.junit.jupiter.api.Test;
import spotifyanalytics.domain.Artist;
import spotifyanalytics.domain.PlayedTrack;
import spotifyanalytics.domain.Track;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TopArtistsMetricTest {

    private List<PlayedTrack> sampleHistory() {
        Artist radiohead = new Artist("1", "Radiohead");
        Artist arcade = new Artist("2", "Arcade Fire");

        Track t1 = new Track("t1", "Creep", radiohead, 238000);
        Track t2 = new Track("t2", "Wake Up", arcade, 337000);

        return List.of(
                new PlayedTrack(t1, Instant.now()),
                new PlayedTrack(t1, Instant.now()),
                new PlayedTrack(t1, Instant.now()),
                new PlayedTrack(t2, Instant.now())
        );
    }

    @Test
    public void testTopArtistsRankedByPlayCount(){
        TopArtistsMetric metric = new TopArtistsMetric(2);
        MetricResult<List<String>> result = metric.compute(sampleHistory());
        assertEquals("Radiohead", result.getValue().get(0));
        assertEquals("Arcade Fire", result.getValue().get(1));
    }
}
