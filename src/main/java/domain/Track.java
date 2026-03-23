package domain;

public class Track {
    private final String id;
    private final String title;
    private final Artist artist;
    private final int durationMs;

    public Track(String id, String title, Artist artist, int durationMs){
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.durationMs = durationMs;
    }

    public String getId() {return id;}
    public String getTitle() {return title;}
    public Artist getArtist() {return artist;}
    public int getDurationMs() {return durationMs;}
}
