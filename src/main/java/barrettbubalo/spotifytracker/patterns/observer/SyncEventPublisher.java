package barrettbubalo.spotifytracker.patterns.observer;

public interface SyncEventPublisher {
    void addListener(SyncEventListener listener);
    void removeListener(SyncEventListener listener);
    void notifyListeners(SyncEvent event);
}