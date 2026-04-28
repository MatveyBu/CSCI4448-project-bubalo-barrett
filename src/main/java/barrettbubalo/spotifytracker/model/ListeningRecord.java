package barrettbubalo.spotifytracker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "listening_records")
public class ListeningRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "track_id", nullable = false)
    private Track track;

    private LocalDateTime playedAt;

    // --- Constructors ---

    public ListeningRecord() {};

    public ListeningRecord(Account account, Track track, LocalDateTime playedAt) {
        this.account = account;
        this.track = track;
        this.playedAt = playedAt;
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Account getAccount() {
        return this.account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Track getTrack() {
        return this.track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public LocalDateTime getPlayedAt() {
        return this.playedAt;
    }

    public void setPlayedAt(LocalDateTime playedAt) {
        this.playedAt = playedAt;
    }
}