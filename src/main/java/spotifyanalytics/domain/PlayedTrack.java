package spotifyanalytics.domain;

import java.time.Instant;

public class PlayedTrack
{
    private final Track track;
    private final Instant playedAt;

    public PlayedTrack(spotifyanalytics.domain.Track track, Instant playedAt){
        this.track = track;
        this.playedAt = playedAt;
    }

    public spotifyanalytics.domain.Track getTrack() {return track;}
    public Instant getPlayedAt() {return playedAt;}
}
