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

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public List<Album> getAlbums() {
        return albums;
    }
}
