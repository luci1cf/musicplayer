package model;

import java.util.HashMap;
import java.util.Map;

public class Playlist {
    private int playlistId;
    private String playlistName;
    private Map<String, Song> playlistSongs = new HashMap<>();
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

    public Map<String,Song> getPlaylistSongs() {
        return playlistSongs;
    }

    public int getPlaylistDuration() {
        return playlistDuration;
    }

    public String getFormattedPlaylistDuration() {
        int minutes = playlistDuration / 60;
        int seconds = playlistDuration % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    public int getSongCount() {
        return playlistSongs.size();
    }

    public void addSong(Song song) {
        if (!playlistSongs.containsKey(song.getTitle())) {
            playlistSongs.put(song.getTitle(), song);
            playlistDuration += song.getDuration();
        } else {
            System.out.printf("%s is already in your playlist.", song);
        }
    }

    public void removeSong(String songTitle) {
        if (playlistSongs.containsKey(songTitle)) {
            playlistDuration -= playlistSongs.get(songTitle).getDuration();
            playlistSongs.remove(songTitle);
        } else {
            System.out.printf("Could not find %s in this playlist.", songTitle);
        }
    }

    @Override
    public String toString() {
        return "Playlist [" +
                "playlistId: " + playlistId +
                ", playlistName: '" + playlistName + '\'' +
                ", playlistSongs: " + playlistSongs +
                ", playlistDuration: " + getFormattedPlaylistDuration() +
                ", numberOfSongs: " + getSongCount() +
                ']';
    }
}
