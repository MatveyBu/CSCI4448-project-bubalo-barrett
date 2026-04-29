package barrettbubalo.spotifytracker.patterns.observer;
 
import barrettbubalo.spotifytracker.model.*;
import barrettbubalo.spotifytracker.repository.ListeningRecordRepository;
import barrettbubalo.spotifytracker.repository.MilestoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
 
import java.util.HashSet;
import java.util.List;
import java.util.Set;
 
@Component
public class MilestoneChecker implements SyncEventListener, MilestoneEventPublisher {
 
    @Autowired
    private ListeningRecordRepository listeningRecordRepository;
 
    @Autowired
    private MilestoneRepository milestoneRepository;

    private List<MilestoneEventListener> milestoneListeners;

    @Autowired
    public MilestoneChecker(List<MilestoneEventListener> listeners) {
        this.milestoneListeners = listeners;
    } 
 
    private static final int[] PLAY_MILESTONES = {10, 25, 50, 100, 250, 500, 1000};
    private static final int[] TOTAL_MILESTONES = {25, 100, 250, 500, 1000, 5000, 10000};
 
    @Override
    public void onSyncComplete(SyncEvent event) {
        Account account = event.getAccount();
        List<ListeningRecord> newRecords = event.getNewListeningRecords();
 
        if (newRecords.isEmpty()) {
            return;
        }
 
        checkTotalPlaysMilestones(account);
        checkArtistMilestones(account, newRecords);
        checkTrackMilestones(account, newRecords);
    }
 
    private void checkTotalPlaysMilestones(Account account) {
        int totalPlays = listeningRecordRepository.countByAccount(account);
 
        for (int threshold : TOTAL_MILESTONES) {
            if (totalPlays >= threshold && !alreadyAchieved(account, MilestoneType.TOTAL_PLAYS, threshold)) {
                Milestone milestone = new Milestone(
                    account,
                    MilestoneType.TOTAL_PLAYS,
                    threshold,
                    "Reached " + threshold + " total plays!"
                );
                milestoneRepository.save(milestone);
                notifyListeners(account, milestone);
            }
        }
    }
 
    private void checkArtistMilestones(Account account, List<ListeningRecord> newRecords) {
        Set<Artist> artists = new HashSet<>();
        for (ListeningRecord record : newRecords) {
            if (record.getTrack().getMainArtist() != null) {
                artists.add(record.getTrack().getMainArtist());
            }
        }
 
        for (Artist artist : artists) {
            int artistPlays = listeningRecordRepository.countByAccountAndTrackMainArtist(
                account, artist);
 
            for (int threshold : PLAY_MILESTONES) {
                if (artistPlays >= threshold && !alreadyAchievedForArtist(account, threshold, artist)) {
                    Milestone milestone = new Milestone(
                        account,
                        MilestoneType.ARTIST_PLAYS,
                        threshold,
                        "Reached " + threshold + " plays on " + artist.getName() + "!"
                    );
                    milestone.setArtist(artist);
                    milestoneRepository.save(milestone);
                    notifyListeners(account, milestone);
                }
            }
        }
    }
 
    private void checkTrackMilestones(Account account, List<ListeningRecord> newRecords) {
        Set<Track> tracks = new HashSet<>();
        for (ListeningRecord record : newRecords) {
            tracks.add(record.getTrack());
        }
 
        for (Track track : tracks) {
            int trackPlays = listeningRecordRepository.countByAccountAndTrack(
                account, track);
 
            for (int threshold : PLAY_MILESTONES) {
                if (trackPlays >= threshold && !alreadyAchievedForTrack(account, threshold, track)) {
                    Milestone milestone = new Milestone(
                        account,
                        MilestoneType.TRACK_PLAYS,
                        threshold,
                        "Reached " + threshold + " plays on " + track.getName() + "!"
                    );
                    milestone.setTrack(track);
                    milestoneRepository.save(milestone);
                    notifyListeners(account, milestone);
                }
            }
        }
    }
 
    private boolean alreadyAchieved(Account account, MilestoneType type, int threshold) {
        return milestoneRepository.existsByAccountAndMilestoneTypeAndThreshold(account, type, threshold);
    }
 
    private boolean alreadyAchievedForArtist(Account account, int threshold, Artist artist) {
        return milestoneRepository.existsByAccountAndMilestoneTypeAndThresholdAndArtist(
            account, MilestoneType.ARTIST_PLAYS, threshold, artist);
    }
 
    private boolean alreadyAchievedForTrack(Account account, int threshold, Track track) {
        return milestoneRepository.existsByAccountAndMilestoneTypeAndThresholdAndTrack(
            account, MilestoneType.TRACK_PLAYS, threshold, track);
    }



    public void addListener(MilestoneEventListener listener) {
        milestoneListeners.add(listener);
    }

    public void removeListener(MilestoneEventListener listener) {
        milestoneListeners.remove(listener);
    }

    public void notifyListeners(Account account, Milestone milestone) {
        for (MilestoneEventListener listener : milestoneListeners) {
            listener.onMilestoneAchieved(account, milestone);
        }
    }
}