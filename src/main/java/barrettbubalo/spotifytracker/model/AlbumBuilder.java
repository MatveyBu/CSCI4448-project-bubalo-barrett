package barrettbubalo.spotifytracker.model;

/**
 * Builder for Album entity
 * Provides fluent interface for creating Album objects
 */
public class AlbumBuilder {
    
    private String spotifyId;
    private String name;
    private Artist mainArtist;
    private String imageUrl;
    
    public AlbumBuilder withSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
        return this;
    }
    
    public AlbumBuilder withName(String name) {
        this.name = name;
        return this;
    }
    
    public AlbumBuilder withMainArtist(Artist mainArtist) {
        this.mainArtist = mainArtist;
        return this;
    }
    
    public AlbumBuilder withImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
    
    public Album build() {
        Album album = new Album();
        album.setSpotifyId(spotifyId);
        album.setName(name);
        album.setMainArtist(mainArtist);
        album.setImageUrl(imageUrl);
        return album;
    }
}

