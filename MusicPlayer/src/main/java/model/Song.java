package model;

public class Song {
    private int songId;
    private String title;
    private String artist;
    private int duration;   // duration of song in seconds

    public Song(int songId, String title, String artist, int duration) {
        this.songId = songId;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public int getDuration() {
        return duration;
    }

    public String getFormattedDuration() {
        int minutes = duration / 60;
        int seconds = duration % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    @Override
    public String toString() {
        return "Song [" +
                "songId: '" + songId +
                "', title: '" + title + '\'' +
                ", artist: '" + artist + '\'' +
                ", duration: " + getFormattedDuration() +
                ']';
    }
}
