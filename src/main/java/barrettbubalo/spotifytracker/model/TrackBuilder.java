package barrettbubalo.spotifytracker.model;

/**
 * Builder for Track entity
 * Provides fluent interface for creating Track objects
 */
public class TrackBuilder {
    
    private String spotifyId;
    private String name;
    private int durationMs;
    private Artist mainArtist;
    private Album album;
    
    public TrackBuilder withSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
        return this;
    }
    
    public TrackBuilder withName(String name) {
        this.name = name;
        return this;
    }
    
    public TrackBuilder withDurationMs(int durationMs) {
        this.durationMs = durationMs;
        return this;
    }
    
    public TrackBuilder withMainArtist(Artist mainArtist) {
        this.mainArtist = mainArtist;
        return this;
    }
    
    public TrackBuilder withAlbum(Album album) {
        this.album = album;
        return this;
    }
    
    public Track build() {
        Track track = new Track();
        track.setSpotifyId(spotifyId);
        track.setName(name);
        track.setDurationMs(durationMs);
        track.setMainArtist(mainArtist);
        track.setAlbum(album);
        return track;
    }
}

