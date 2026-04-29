package barrettbubalo.spotifytracker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import java.util.*;

@Entity
@Table(name = "albums")
public class Album implements MusicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String spotifyId;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "main_artist_id", nullable = false)
    private Artist mainArtist;

    @ManyToMany
    @JoinTable(
        name = "album_artists",
        joinColumns = @JoinColumn(name = "album_id"),
        inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    public List<Artist> artists;

    private String ImageUrl;

    // private int durationMs;

    private String spotifyUri;

    // private String previewUrl;

    // private boolean explicit;

    private LocalDateTime firstSeenAt;


    public Album() {}

    public Album(String spotifyId, String name, Artist mainArtist, List<Artist> artists) {
        this.spotifyId = spotifyId;
        this.name = name;
        this.mainArtist = mainArtist;
        this.artists = artists;
    }


    public Long getId() {
        return this.id;
    }

    public String getSpotifyId() {
        return this.spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Artist getMainArtist() {
        return this.mainArtist;
    }

    public void setMainArtist(Artist mainArtist) {
        this.mainArtist = mainArtist;
    }

    public List<Artist> getArtists() {
        return this.artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public String getImageUrl() {
        return this.ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.ImageUrl = imageUrl;
    }

    public String getSpotifyUri() {
        return this.spotifyUri;
    }

    public void setSpotifyUri(String spotifyUri) {
        this.spotifyUri = spotifyUri;
    }

    public LocalDateTime getFirstSeenAt() {
        return this.firstSeenAt;
    }

    public void setFirstSeenAt(LocalDateTime firstSeenAt) {
        this.firstSeenAt = firstSeenAt;
    }

    public MusicEntityType getType() {
        return MusicEntityType.ALBUM;
    }
}