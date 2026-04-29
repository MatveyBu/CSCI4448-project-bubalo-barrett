package barrettbubalo.spotifytracker.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class AlbumTest {

    @Test
    void testGettersAndSetters() {
        Album album = new Album();
        Artist artist = new Artist();
        List<Artist> artists = Collections.singletonList(artist);
        LocalDateTime now = LocalDateTime.now();

        album.setSpotifyId("abc");
        album.setName("Test Album");
        album.setMainArtist(artist);
        album.setArtists(artists);
        album.setImageUrl("http://image.com");
        album.setSpotifyUri("spotify:album:123");
        album.setFirstSeenAt(now);

        assertThat(album.getSpotifyId()).isEqualTo("abc");
        assertThat(album.getName()).isEqualTo("Test Album");
        assertThat(album.getMainArtist()).isEqualTo(artist);
        assertThat(album.getArtists()).hasSize(1);
        assertThat(album.getImageUrl()).isEqualTo("http://image.com");
        assertThat(album.getSpotifyUri()).isEqualTo("spotify:album:123");
        assertThat(album.getFirstSeenAt()).isEqualTo(now);
        assertThat(album.getType()).isEqualTo(MusicEntityType.ALBUM);
    }

    @Test
    void testParameterizedConstructor() {
        Artist artist = new Artist();
        List<Artist> artists = Collections.singletonList(artist);
        Album album = new Album("id", "name", artist, artists);

        assertThat(album.getSpotifyId()).isEqualTo("id");
        assertThat(album.getName()).isEqualTo("name");
    }

    @Test
    void testEqualsAndHashCode() {
        Album a1 = new Album();
        Album a2 = new Album();

        // Test null IDs
        assertThat(a1).isNotEqualTo(a2);
        assertThat(a1.hashCode()).isZero();

        // Test matching IDs
        // Reflection or a setter for ID would be needed here if ID is not accessible
        // Assuming ID is null-checked in your implementation:
        assertThat(a1.equals(null)).isFalse();
        assertThat(a1.equals(new Object())).isFalse();
    }
}