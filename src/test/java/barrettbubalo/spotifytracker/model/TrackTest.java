package barrettbubalo.spotifytracker.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.assertj.core.api.Assertions.assertThat;

class TrackTest {

    @Test
    void testDurationFormatting() {
        Track track = new Track();
        track.setDurationMs(185000); // 3:05
        assertThat(track.getFormattedDuration()).isEqualTo("3:05");
    }

    @Test
    void testGetImageUrlFromAlbum() {
        Album album = new Album();
        album.setImageUrl("test-url");
        Track track = new Track();
        track.setAlbum(album);
        assertThat(track.getImageUrl()).isEqualTo("test-url");
    }

    @Test
    void testGettersAndSetters() {
        Track track = new Track("id", "name", new Artist(), new Album(), 100);
        track.setAllArtist(new ArrayList<>());
        track.setAlbumImageUrl("img");
        track.setSpotifyUri("uri");
        track.setPreviewUrl("preview");
        track.setExplicit(true);
        track.setId(1L);

        assertThat(track.getId()).isEqualTo(1L);
        assertThat(track.getAllArtist()).isEmpty();
        assertThat(track.getAlbumImageUrl()).isEqualTo("img");
        assertThat(track.getSpotifyUri()).isEqualTo("uri");
        assertThat(track.getPreviewUrl()).isEqualTo("preview");
        assertThat(track.isExplicit()).isTrue();
        assertThat(track.getType()).isEqualTo(MusicEntityType.TRACK);
    }

    @Test
    void testLifecycleHooks() {
        Track track = new Track();
        track.onCreate();
        assertThat(track.getFirstSeenAt()).isNotNull();
    }
}