package barrettbubalo.spotifytracker.patterns.observer;

import barrettbubalo.spotifytracker.model.Account;

public interface MilestoneEventListener {
    void onMilestoneAchieved(Account account, Milestone milestone);
}