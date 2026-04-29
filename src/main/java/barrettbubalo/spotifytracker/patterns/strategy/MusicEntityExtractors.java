package barrettbubalo.spotifytracker.patterns.strategy;

public class MusicEntityExtractors {
    public static final MusicEntityExtractor BY_TRACK = record -> record.getTrack();
    public static final MusicEntityExtractor BY_ARTIST = record -> record.getTrack().getMainArtist();
    public static final MusicEntityExtractor BY_ALBUM = record -> record.getTrack().getAlbum();
}