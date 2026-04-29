package barrettbubalo.spotifytracker.repository;

import barrettbubalo.spotifytracker.model.Artist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Optional<Artist> findBySpotifyId(String spotifyId);
}