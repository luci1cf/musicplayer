package model;

public class Song {
    private int songId;
    private String title;
    private String artist;
    private int duration;   // duration of song in seconds

    public Song(String title, String artist, int duration) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
    }
}
