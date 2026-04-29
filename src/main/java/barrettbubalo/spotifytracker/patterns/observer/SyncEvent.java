package barrettbubalo.spotifytracker.patterns.observer;

import barrettbubalo.spotifytracker.model.*;

import java.time.LocalDateTime;
import java.util.List;

public class SyncEvent {
    private Account account;
    private List<ListeningRecord> newListeningRecords;
    private LocalDateTime syncTimestamp;

    public SyncEvent(Account account, List<ListeningRecord> newListeningRecords, LocalDateTime syncTimestamp) {
        this.account = account;
        this.newListeningRecords = newListeningRecords;
        this.syncTimestamp = syncTimestamp;
    }
    
    public Account getAccount() {
        return this.account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<ListeningRecord> getNewListeningRecords() {
        return this.newListeningRecords;
    }
    
    public void setNewListeningRecords(List<ListeningRecord> newListeningRecords) {
        this.newListeningRecords = newListeningRecords;
    }

    public LocalDateTime getSyncTimestasmp() {
        return this.syncTimestamp;
    }

    public void setSyncTimestamp(LocalDateTime syncTimestamp) {
        this.syncTimestamp = syncTimestamp;
    }
}