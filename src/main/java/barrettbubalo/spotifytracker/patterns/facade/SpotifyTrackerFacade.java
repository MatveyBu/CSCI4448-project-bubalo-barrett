package barrettbubalo.spotifytracker.patterns.facade;

import barrettbubalo.spotifytracker.model.Account;
import barrettbubalo.spotifytracker.service.SpotifyApiClient;
import barrettbubalo.spotifytracker.service.SpotifySyncService;
import barrettbubalo.spotifytracker.repository.AccountRepository;
import barrettbubalo.spotifytracker.repository.ListeningRecordRepository;
import barrettbubalo.spotifytracker.model.ListeningRecord;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * LEAN FACADE PATTERN IMPLEMENTATION
 * 
 * This Facade provides a FOCUSED interface to the Spotify integration subsystem.
 * It only handles:
 * 1. ORCHESTRATION - Complex multi-step operations
 * 2. DATA ABSTRACTION - Repository layer hiding
 * 
 * For simple API queries, controllers call SpotifyApiClient directly.
 * This avoids unnecessary abstraction layers while maintaining clean separation.
 * 
 * Benefits:
 * 1. Orchestration - Combines multiple steps into one operation
 * 2. Repository Abstraction - Controllers don't know about database layer
 * 3. No Bloat - Pass-through methods removed
 * 4. Clean - Only genuine value-add methods
 */
@Service
public class SpotifyTrackerFacade {

    @Autowired
    private SpotifyApiClient spotifyApiClient;

    @Autowired
    private SpotifySyncService spotifySyncService;

    @Autowired
    private ListeningRecordRepository listeningRecordRepository;

    // ========================================================================
    // ORCHESTRATION METHODS - Multi-step operations that justify the Facade
    // ========================================================================

    /**
     * Complete Spotify OAuth flow with automatic sync.
     * 
     * ORCHESTRATES multiple steps:
     * 1. Exchange authorization code for tokens
     * 2. Sync user's recently played tracks
     * 3. Create/update music entities in database
     * 
     * This is the CORE reason for the Facade - combining OAuth + data sync.
     * 
     * @param account User's account
     * @param authorizationCode Code from Spotify callback
     * @return Success message
     */
    public String completeSpotifyConnection(Account account, String authorizationCode) {
        spotifyApiClient.exchangeCodeForTokens(account, authorizationCode);
        spotifySyncService.syncRecentlyPlayed(account);
        return "Spotify account fully connected and synced";
    }

    // ========================================================================
    // REPOSITORY ABSTRACTION METHODS - Hide database layer from controllers
    // ========================================================================

    /**
     * Get all of a user's listening history stored locally.
     * 
     * ABSTRACTS: Repository layer - Controllers don't need to know about
     * ListeningRecordRepository or query methods.
     * 
     * @param accountId User's database ID
     * @return List of listening records
     */
    public List<ListeningRecord> getUserListeningHistory(Long accountId) {
        return listeningRecordRepository.findByAccountId(accountId);
    }

    /**
     * Get listening history after a specific timestamp.
     * Useful for incremental syncs and filtering recent activity.
     * 
     * ABSTRACTS: Complex repository queries - Controllers call simple method
     * instead of knowing about "findByAccountIdAndPlayedAtAfter".
     * 
     * @param accountId User's database ID
     * @param after Only return records played after this time
     * @return Filtered listening records
     */
    public List<ListeningRecord> getUserListeningHistorySince(Long accountId, LocalDateTime after) {
        return listeningRecordRepository.findByAccountIdAndPlayedAtAfter(accountId, after);
    }

    // ========================================================================
    // NOTE: Simple API queries should call SpotifyApiClient directly
    // ========================================================================
    // 
    // Controllers can use:
    //
    //   spotifyApiClient.getTopArtists(account, timeRange, limit);
    //   spotifyApiClient.getTopTracks(account, timeRange, limit);
    //   spotifyApiClient.getCurrentUserProfile(account);
    //   spotifyApiClient.getAudioFeatures(account, trackId);
    //   spotifyApiClient.getArtist(account, artistId);
    //
    // These don't need a Facade layer - they're simple, straightforward API calls.
    // Adding a Facade would be unnecessary abstraction.
    //
}

