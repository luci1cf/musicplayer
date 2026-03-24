package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Album {
    private int albumId;
    private String albumName;
    private Artist artist;
    private LocalDate releaseDate;
    private List<Song> albumSongs = new ArrayList<>();
    private int albumDuration;  // duration of the playlist in second; later on converted to mins and # of playlists

    public Album(int albumId, String albumName, Artist artist, LocalDate releaseDate) {
        this.albumId = albumId;
        this.albumName = albumName;
        this.artist = artist;
        this.releaseDate = releaseDate;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Song> getAlbumSongs() {
        return albumSongs;
    }

    public int getAlbumDuration() {
        return albumDuration;
    }

    public String getFormattedAlbumDuration() {
        int hours = albumDuration / 3600;
        int minutes = (albumDuration % 3600) / 60;
        return String.format("%d:%02d", hours, minutes);
    }

    public int getAlbumSongCount() {
        return albumSongs.size();
    }

    @Override
    public String toString() {
        return "Album [" +
                "albumId:" + albumId +
                ", albumName: '" + albumName + '\'' +
                ", artist: '" + artist + '\'' +
                ", releaseDate: " + releaseDate +
                ", albumSongs: " + albumSongs +
                ", albumDuration: " + getFormattedAlbumDuration() +
                ", albumSongCount" + getAlbumSongCount() +
                '}';
    }
}
