package barrettbubalo.spotifytracker.repository;

import barrettbubalo.spotifytracker.model.Account;
import barrettbubalo.spotifytracker.model.Artist;
import barrettbubalo.spotifytracker.patterns.observer.Milestone;
import barrettbubalo.spotifytracker.patterns.observer.MilestoneType;
import barrettbubalo.spotifytracker.model.Track;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
    List<Milestone> findByAccount(Account account);
    boolean existsByAccountAndMilestoneTypeAndThreshold(Account account, MilestoneType type, int threshold);
    boolean existsByAccountAndMilestoneTypeAndThresholdAndArtist(Account account, MilestoneType milestoneType, int threshold, Artist artist);
    boolean existsByAccountAndMilestoneTypeAndThresholdAndTrack(Account account, MilestoneType milestoneType, int threshold, Track track);
}