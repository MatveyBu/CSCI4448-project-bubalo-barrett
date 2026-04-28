package spotifyanalytics.metrics;

import org.junit.jupiter.api.Test;
import spotifyanalytics.domain.Artist;
import spotifyanalytics.domain.PlayedTrack;
import spotifyanalytics.domain.Track;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeListenedMetricTest {

    @Test
    public void testTotalTimeListnedeSumsCorrectly(){
        Artist artist = new Artist("1", "Radiohead");
        Track track1 = new Track("t1", "Creep", artist, 238000);
        Track track2 = new Track("t2", "Karma Police", artist, 264000);

        List<PlayedTrack> history = List.of(
                new PlayedTrack(track1, Instant.now()),
                new PlayedTrack(track2, Instant.now())
        );

        TimeListenedMetric metric = new TimeListenedMetric();
        MetricResult<Long> result = metric.compute(history);
        assertEquals(502000L, result.getValue());
    }
}
