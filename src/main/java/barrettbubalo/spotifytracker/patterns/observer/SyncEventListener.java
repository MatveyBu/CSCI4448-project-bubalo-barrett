package barrettbubalo.spotifytracker.patterns.observer;

public interface SyncEventListener {
    void onSyncComplete(SyncEvent event);
}