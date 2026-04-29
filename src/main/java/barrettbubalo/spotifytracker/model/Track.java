package barrettbubalo.spotifytracker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import java.util.*;

@Entity
@Table(name = "tracks")
public class Track implements MusicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Spotify's unique identifier for this track
    @Column(unique = true)
    private String spotifyId;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "main_artist_id", nullable = false)
    //@JoinColumn(name = "main_artist_id")
    private Artist mainArtist;

    @ManyToMany
    @JoinTable(
        name = "track_artists",
        joinColumns = @JoinColumn(name = "track_id"),
        inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    private List<Artist> artists;

    @ManyToOne
    @JoinColumn(name = "album_id", nullable = true)
    private Album album;

    // Album art URL from Spotify
    private String albumImageUrl;

    // Duration in milliseconds (Spotify's format)
    private int durationMs;

    // Spotify URI for deep linking (e.g., "spotify:track:abc123")
    // Adds ability to "Open in Spotify" buttons in the UI
    private String spotifyUri;

    // Preview URL — 30-second audio clip if available
    private String previewUrl;

    // Whether the track contains explicit content
    private boolean explicit;

    // When this track was first seen/added to the database
    private LocalDateTime firstSeenAt;

    @PrePersist
    protected void onCreate() {
        this.firstSeenAt = LocalDateTime.now();
    }

    // Default constructor required by JPA
    public Track() {}

    // Convenience constructor for creating a track from Spotify API data
    public Track(String spotifyId, String name, Artist mainArtist, Album album, int durationMs) {
        this.spotifyId = spotifyId;
        this.name = name;
        this.mainArtist = mainArtist;
        this.album = album;
        this.durationMs = durationMs;
    }

    // Helper method to get duration in a readable format
    public String getFormattedDuration() {
        int seconds = durationMs / 1000;
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%d:%02d", minutes, remainingSeconds);
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Artist getMainArtist() {
        return mainArtist;
    }

    public void setMainArtist(Artist mainArtist) {
        this.mainArtist = mainArtist;
    }

    public List<Artist> getAllArtist() {
        return artists;
    }

    public void setAllArtist(List<Artist> artists) {
        this.artists = artists;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public String getAlbumImageUrl() {
        return albumImageUrl;
    }

    public void setAlbumImageUrl(String albumImageUrl) {
        this.albumImageUrl = albumImageUrl;
    }

    public int getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(int durationMs) {
        this.durationMs = durationMs;
    }

    public String getSpotifyUri() {
        return spotifyUri;
    }

    public void setSpotifyUri(String spotifyUri) {
        this.spotifyUri = spotifyUri;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public boolean isExplicit() {
        return explicit;
    }

    public void setExplicit(boolean explicit) {
        this.explicit = explicit;
    }

    public LocalDateTime getFirstSeenAt() {
        return firstSeenAt;
    }

    public void setFirstSeenAt(LocalDateTime firstSeenAt) {
        this.firstSeenAt = firstSeenAt;
    }

    public String getImageUrl() {
        return this.album.getImageUrl();
    }

    public MusicEntityType getType() {
        return MusicEntityType.TRACK;
    }
}