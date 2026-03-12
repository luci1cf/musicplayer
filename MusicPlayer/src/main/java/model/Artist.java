package model;

import java.util.ArrayList;
import java.util.List;

public class Artist {
    private int artistId;
    private String name;
    private List<Song> songs = new ArrayList<>();
    private List<Album> albums = new ArrayList<>();

    public Artist(int artistId, String name) {
        this.artistId = artistId;
        this.name = name;
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public void addAlbum(Album album) {
        albums.add(album);
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
