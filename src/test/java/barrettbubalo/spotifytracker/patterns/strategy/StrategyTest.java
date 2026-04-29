package barrettbubalo.spotifytracker.patterns.strategy;

import barrettbubalo.spotifytracker.model.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class StrategyPatternTest {

    @Test
    void testPlayCountStrategy() {
        Artist artist = new Artist("Artist A", "1");
        Track track = new Track("T1", "Track 1", artist, null, 100);
        ListeningRecord r1 = new ListeningRecord(null, track, null);
        ListeningRecord r2 = new ListeningRecord(null, track, null);

        PlayCountStrategy strategy = new PlayCountStrategy();
        List<RankedItem> results = strategy.rank(List.of(r1, r2), MusicEntityExtractors.BY_TRACK);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getMetricScore()).isEqualTo(2);
        assertThat(results.get(0).getRank()).isEqualTo(1);
    }

    @Test
    void testTotalListeningTimeStrategy() {
        Track track = new Track("T1", "Track 1", new Artist("A", "1"), null, 5000);
        ListeningRecord r1 = new ListeningRecord(null, track, null);

        TotalListeningTimeStrategy strategy = new TotalListeningTimeStrategy();
        List<RankedItem> results = strategy.rank(List.of(r1), MusicEntityExtractors.BY_TRACK);

        assertThat(results.get(0).getMetricScore()).isEqualTo(5000);
    }

    @Test
    void testRankingTieLogic() {
        // Create two different tracks with same play count
        Track t1 = new Track("T1", "Track 1", new Artist("A", "1"), null, 100);
        Track t2 = new Track("T2", "Track 2", new Artist("B", "2"), null, 100);

        List<ListeningRecord> records = List.of(
                new ListeningRecord(null, t1, null),
                new ListeningRecord(null, t2, null)
        );

        PlayCountStrategy strategy = new PlayCountStrategy();
        List<RankedItem> results = strategy.rank(records, MusicEntityExtractors.BY_TRACK);

        // Both should have rank 1
        assertThat(results.get(0).getRank()).isEqualTo(1);
        assertThat(results.get(1).getRank()).isEqualTo(1);
    }

    @Test
    void testRankedItemGettersSetters() {
        Artist artist = new Artist("Name", "ID");
        artist.setImageUrl("URL");
        RankedItem item = new RankedItem(artist, 1, 100, MetricType.PLAY_COUNT);

        item.setRank(2);
        item.setMetricScore(200);
        item.setMetricType(MetricType.LISTENING_TIME);

        assertThat(item.getName()).isEqualTo("Name");
        assertThat(item.getSpotifyId()).isEqualTo("ID");
        assertThat(item.getImageUrl()).isEqualTo("URL");
        assertThat(item.getMusicEntityType()).isEqualTo(MusicEntityType.ARTIST);
        assertThat(item.getRank()).isEqualTo(2);
        assertThat(item.getMetricScore()).isEqualTo(200);
        assertThat(item.getMetricType()).isEqualTo(MetricType.LISTENING_TIME);
    }

    @Test
    void testExtractors() {
        Artist artist = new Artist();
        Album album = new Album();
        Track track = new Track();
        track.setMainArtist(artist);
        track.setAlbum(album);
        ListeningRecord record = new ListeningRecord(null, track, null);

        assertThat(MusicEntityExtractors.BY_TRACK.extract(record)).isEqualTo(track);
        assertThat(MusicEntityExtractors.BY_ARTIST.extract(record)).isEqualTo(artist);
        assertThat(MusicEntityExtractors.BY_ALBUM.extract(record)).isEqualTo(album);
    }
}