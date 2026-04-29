package barrettbubalo.spotifytracker.patterns.observer;

import barrettbubalo.spotifytracker.model.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "milestones")
public class Milestone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime achievedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MilestoneType milestoneType;

    @Column(nullable = false)
    private int threshold;

    @ManyToOne
    @JoinColumn(name = "track_id")
    private Track track;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    // Default constructor required by JPA
    public Milestone() {}

    // Convenience constructor for general milestones (no specific entity)
    public Milestone(Account account, MilestoneType milestoneType, int threshold, String description) {
        this.account = account;
        this.milestoneType = milestoneType;
        this.threshold = threshold;
        this.description = description;
        this.achievedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (this.achievedAt == null) {
            this.achievedAt = LocalDateTime.now();
        }
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getAchievedAt() {
        return achievedAt;
    }

    public void setAchievedAt(LocalDateTime achievedAt) {
        this.achievedAt = achievedAt;
    }

    public MilestoneType getMilestoneType() {
        return milestoneType;
    }

    public void setMilestoneType(MilestoneType milestoneType) {
        this.milestoneType = milestoneType;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}