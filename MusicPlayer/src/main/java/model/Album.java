package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Album {
    private int albumId;
    private String albumName;
    private String artist;
    private LocalDate releaseDate;
    private List<Song> albumSongs = new ArrayList<>();
    private int albumDuration;  // duration of the playlist in second; later on converted to mins and # of playlists

    public Album(int albumId, String albumName, String artist, LocalDate releaseDate) {
        this.albumId = albumId;
        this.albumName = albumName;
        this.artist = artist;
        this.releaseDate = releaseDate;
    }
}
