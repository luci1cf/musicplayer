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
}
