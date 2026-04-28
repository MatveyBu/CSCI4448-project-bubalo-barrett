package barrettbubalo.spotifytracker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import java.util.*;

@Entity
@Table(name = "artists")
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String spotifyId;

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
}