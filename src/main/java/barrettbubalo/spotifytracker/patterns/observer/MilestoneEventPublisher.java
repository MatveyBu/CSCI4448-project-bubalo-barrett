package barrettbubalo.spotifytracker.patterns.observer;

import barrettbubalo.spotifytracker.model.Account;

public interface MilestoneEventPublisher {
    void addListener(MilestoneEventListener listener);
    void removeListener(MilestoneEventListener listener);
    void notifyListeners(Account account, Milestone milestone);
}