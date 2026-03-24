package model;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private int playlistId;
    private String playlistName;
    private List<Song> playlistSongs = new ArrayList<>();
    private int playlistDuration;    // duration of the playlist in second; later on converted to mins and # of playlists

    public Playlist(int playlistId, String playlistName) {
        this.playlistId = playlistId;
        this.playlistName = playlistName;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public List<Song> getPlaylistSongs() {
        return playlistSongs;
    }

    public int getPlaylistDuration() {
        return playlistDuration;
    }

    public String getFormattedDuration() {
        int minutes = playlistDuration / 60;
        int seconds = playlistDuration % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    public void addSong(Song song) {
        if (!playlistSongs.contains(song)) {
            playlistSongs.add(song);
            playlistDuration += song.getDuration();
        } else {
            System.out.printf("%s is already in your playlist.", song);
        }
    }

    public void removeSong(Song song) {
        if (playlistSongs.contains(song)) {
            playlistSongs.remove(song);
            playlistDuration -= song.getDuration();
        } else {
            System.out.printf("Could not find %s in this playlist.", song);
        }
    }

    @Override
    public String toString() {
        return "Playlist [" +
                "playlistId: " + playlistId +
                ", playlistName: '" + playlistName + '\'' +
                ", playlistSongs: " + playlistSongs +
                ", playlistDuration: " + playlistDuration + '\'' +
                ']';
    }
}
