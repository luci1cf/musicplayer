package model;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private int playlistId;
    private String playlistName;
    private List<Song> playlistSongs = new ArrayList<>();
    private String playlistDuration;    // duration of the playlist in second; later on converted to mins and # of playlists

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

    public String getPlaylistDuration() {
        return playlistDuration;
    }
}
