package model;

import java.util.ArrayList;
import java.util.List;

public class Artist {
    private int artistId;
    private String name;
    private List<Song> songs;
    private List<Album> albums;

    public Artist(int artistId, String name, List<Song> songs, List<Album> albums) {
        this.artistId = artistId;
        this.name = name;
        this.songs = new ArrayList<>();
        this.albums = new ArrayList<>();
    }
}
