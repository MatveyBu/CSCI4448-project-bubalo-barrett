package spotifyanalytics.integration;

import spotifyanalytics.domain.Artist;
import spotifyanalytics.domain.PlayedTrack;
import spotifyanalytics.domain.Track;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class FakeSpotifyClient implements SpotifyClient {
	private final List<PlayedTrack> recentlyPlayed;
	private final List<Track> topTracks;

	public FakeSpotifyClient() {
		Artist radiohead = new Artist("1", "Radiohead");
		Artist arcadeFire = new Artist("2", "Arcade Fire");

		Track creep = new Track("t1", "Creep", radiohead, 238000);
		Track karmaPolice = new Track("t2", "Karma Police", radiohead, 264000);
		Track wakeUp = new Track("t3", "Wake Up", arcadeFire, 337000);

		this.recentlyPlayed = List.of(
				new PlayedTrack(creep, Instant.parse("2026-01-01T10:00:00Z")),
				new PlayedTrack(karmaPolice, Instant.parse("2026-01-01T10:05:00Z")),
				new PlayedTrack(wakeUp, Instant.parse("2026-01-01T10:11:00Z")),
				new PlayedTrack(creep, Instant.parse("2026-01-01T10:16:00Z"))
		);

		this.topTracks = List.of(creep, wakeUp, karmaPolice);
	}

	@Override
	public List<PlayedTrack> getRecentlyPlayed(int limit) {
		return cappedCopy(recentlyPlayed, limit);
	}

	@Override
	public List<Track> getTopTracks(int limit) {
		return cappedCopy(topTracks, limit);
	}

	private static <T> List<T> cappedCopy(List<T> source, int limit) {
		if (limit <= 0) {
			return List.of();
		}
		int capped = Math.min(limit, source.size());
		return new ArrayList<>(source.subList(0, capped));
	}
}

