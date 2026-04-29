package barrettbubalo.spotifytracker.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class RankedItemDTOTest {
    @Test
    void testDTO() {
        RankedItemDTO dto = new RankedItemDTO("n", "id", "url", 1, 100, "TRACK");

        assertThat(dto.getName()).isEqualTo("n");
        assertThat(dto.getSpotifyId()).isEqualTo("id");
        assertThat(dto.getImageUrl()).isEqualTo("url");
        assertThat(dto.getRank()).isEqualTo(1);
        assertThat(dto.getMetricScore()).isEqualTo(100);
        assertThat(dto.getMusicEntityType()).isEqualTo("TRACK");
    }
}