package spotifyanalytics.service;

import org.junit.jupiter.api.Test;
import spotifyanalytics.domain.*;
import spotifyanalytics.metrics.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DashboardServiceTest {

    private List<PlayedTrack> sampleHistory() {
        Artist artist = new Artist("1", "Radiohead");
        Track track = new Track("t1", "Creep", artist, 238000);
        return List.of(
                new PlayedTrack(track, Instant.now()),
                new PlayedTrack(track, Instant.now())
        );
    }

    @Test
    public void testDashboardReturnsResultForEachMetric(){
        List<ListeningMetric<?>> metrics = MetricFactory.createDefaultMetrics();
        DashboardService dashboardService = new DashboardService(metrics);
        Map<String, MetricResult<?>> results = dashboardService.compute(sampleHistory());
        assertEquals(3, results.size());
    }

    @Test
    public void testDashboardHandlesEmptyHistory(){
        List<ListeningMetric<?>> metrics = MetricFactory.createDefaultMetrics();
        DashboardService dashboardService = new DashboardService(metrics);
        assertDoesNotThrow(() -> dashboardService.compute(List.of()));
    }

}
