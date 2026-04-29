package barrettbubalo.spotifytracker.repository;

import barrettbubalo.spotifytracker.model.Album;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    Optional<Album> findBySpotifyId(String spotifyId);
}