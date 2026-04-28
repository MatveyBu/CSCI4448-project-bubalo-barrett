package barrettbubalo.spotifytracker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
 
@Entity
@Table(name = "accounts")
public class Account {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String displayName;
 
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    private String profileImageUrl;

    // -- Spotify Specific Information

    // Spotify account identifiers
    private String spotifyId;

    // OAuth tokens for Spotify API access
    @Column(length = 512)
    private String accessToken;
 
    @Column(length = 512)
    private String refreshToken;
 
    // When the current access token expires
    // Used to know when to refresh without making a failed API call first
    private LocalDateTime tokenExpiresAt;
 
    // Account timestamps
    private LocalDateTime createdAt;
 
    private LocalDateTime lastSyncAt;
 
    // Automatically set createdAt before first save
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
 
    // Default constructor required by JPA
    public Account() {}
 
    // Convenience constructor for creating an account
    public Account(String displayName, String email, String passwordHash) {
        this.displayName = displayName;
        this.email = email;
        this.passwordHash = passwordHash;
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
 
    public String getDisplayName() {
        return displayName;
    }
 
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
 
    public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return this.passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
 
    public String getProfileImageUrl() {
        return profileImageUrl;
    }
 
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
 
    public String getAccessToken() {
        return accessToken;
    }
 
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
 
    public String getRefreshToken() {
        return refreshToken;
    }
 
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
 
    public LocalDateTime getTokenExpiresAt() {
        return tokenExpiresAt;
    }
 
    public void setTokenExpiresAt(LocalDateTime tokenExpiresAt) {
        this.tokenExpiresAt = tokenExpiresAt;
    }
 
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
 
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
 
    public LocalDateTime getLastSyncAt() {
        return lastSyncAt;
    }
 
    public void setLastSyncAt(LocalDateTime lastSyncAt) {
        this.lastSyncAt = lastSyncAt;
    }
}