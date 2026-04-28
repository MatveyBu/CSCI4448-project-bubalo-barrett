package barrettbubalo.spotifytracker.repository;

import barrettbubalo.spotifytracker.model.ListeningRecord;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface ListeningRecordRepository extends JpaRepository<ListeningRecord, Long> {
    List<ListeningRecord> findByAccountId(Long accountId);
    List<ListeningRecord> findByAccountIdAndPlayedAtAfter(Long accountId, LocalDateTime after);
}