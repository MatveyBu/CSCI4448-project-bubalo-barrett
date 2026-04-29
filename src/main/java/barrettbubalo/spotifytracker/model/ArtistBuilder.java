package barrettbubalo.spotifytracker.model;

/**
 * Builder for Artist entity
 * Provides fluent interface for creating Artist objects
 */
public class ArtistBuilder {
    
    private String spotifyId;
    private String name;
    private String imageUrl;
    
    public ArtistBuilder withSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
        return this;
    }
    
    public ArtistBuilder withName(String name) {
        this.name = name;
        return this;
    }
    
    public ArtistBuilder withImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
    
    public Artist build() {
        Artist artist = new Artist();
        artist.setSpotifyId(spotifyId);
        artist.setName(name);
        if (imageUrl != null) {
            artist.setImageUrl(imageUrl);
        }
        return artist;
    }
}

