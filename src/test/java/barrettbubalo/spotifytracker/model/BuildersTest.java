package barrettbubalo.spotifytracker.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class BuildersTest {

    @Test
    void testAlbumBuilder() {
        Artist artist = new Artist();
        Album album = new AlbumBuilder()
                .withSpotifyId("sid")
                .withName("name")
                .withMainArtist(artist)
                .withImageUrl("img")
                .build();

        assertThat(album.getSpotifyId()).isEqualTo("sid");
        assertThat(album.getName()).isEqualTo("name");
        assertThat(album.getMainArtist()).isEqualTo(artist);
        assertThat(album.getImageUrl()).isEqualTo("img");
    }

    @Test
    void testArtistBuilder() {
        Artist artist = new ArtistBuilder()
                .withSpotifyId("sid")
                .withName("name")
                .withImageUrl("img")
                .build();
        assertThat(artist.getImageUrl()).isEqualTo("img");

        Artist artistNoImg = new ArtistBuilder().withName("n").build();
        assertThat(artistNoImg.getImageUrl()).isNull();
    }

    @Test
    void testTrackBuilder() {
        Track track = new TrackBuilder()
                .withSpotifyId("sid")
                .withName("name")
                .withDurationMs(100)
                .withMainArtist(new Artist())
                .withAlbum(new Album())
                .build();

        assertThat(track.getSpotifyId()).isEqualTo("sid");
        assertThat(track.getDurationMs()).isEqualTo(100);
    }
}