package barrettbubalo.spotifytracker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import java.util.*;

@Entity
@Table(name = "artists")
public class Artist implements MusicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String spotifyId;

    private String imageUrl;

    public Artist() {}

    public Artist(String name, String spotifyId) {
        this.name = name;
        this.spotifyId = spotifyId;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpotifyId() {
        return this.spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public MusicEntityType getType() {
        return MusicEntityType.ARTIST;
    }

    // For preventing incorrect comparisions when pbjects are fetched from database by JPA
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return id != null && id.equals(artist.getId());
    }

    @Override
    public int hashCode() {
        return spotifyId != null ? spotifyId.hashCode() : 0;
    }
}