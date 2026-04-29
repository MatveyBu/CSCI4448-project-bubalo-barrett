package barrettbubalo.spotifytracker.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ArtistTest {

    @Test
    void testArtistProperties() {
        Artist artist = new Artist("name", "id");
        artist.setImageUrl("url");

        assertThat(artist.getName()).isEqualTo("name");
        assertThat(artist.getSpotifyId()).isEqualTo("id");
        assertThat(artist.getImageUrl()).isEqualTo("url");
        assertThat(artist.getType()).isEqualTo(MusicEntityType.ARTIST);
    }

    @Test
    void testEqualsAndHashCode() {
        Artist a1 = new Artist();
        assertThat(a1.hashCode()).isZero();
        assertThat(a1.equals(a1)).isTrue();
        assertThat(a1.equals(null)).isFalse();
    }
}