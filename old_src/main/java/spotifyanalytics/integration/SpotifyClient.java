package spotifyanalytics.integration;

import spotifyanalytics.domain.PlayedTrack;
import spotifyanalytics.domain.Track;

import java.util.List;

public interface SpotifyClient {
	List<PlayedTrack> getRecentlyPlayed(int limit);

	List<Track> getTopTracks(int limit);
}

