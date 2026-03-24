package model;

import java.util.ArrayList;
import java.util.List;

public class Artist {
    private int artistId;
    private String artistName;
    private List<Song> songs = new ArrayList<>();
    private List<Album> albums = new ArrayList<>();

    public Artist(int artistId, String artistName) {
        this.artistId = artistId;
        this.artistName = artistName;
    }

    public void addSong(Song song) {
        if (!songs.contains(song)){
            songs.add(song);
            System.out.printf("Song %s added successfully.", song);
        } else {
            System.out.printf("Song %s by %s already exists.", song, artistName);
        }
    }

    public void addAlbum(Album album) {
        if (!albums.contains(album)){
            albums.add(album);
            System.out.printf("Album %s added successfully.", album);
        } else {
            System.out.printf("Album %s by %s already exists.", album, artistName);
        }
    }

    public void removeSong(Song song) {
        if (!songs.contains(song)) {
            songs.remove(song);
            System.out.printf("Song %s successfully removed.", song);
        } else {
            System.out.printf("Song %s by %s does not exist.", song, artistName);
        }
    }

    public void removeAlbum(Album album) {
        if (!albums.contains(album)) {
            albums.remove(album);
            System.out.printf("Album %s successfully removed.", album);
        } else {
            System.out.printf("Album %s by %s does not exist.", album, artistName);
        }
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    @Override
    public String toString() {
        return "Artist [" +
                "artistId: " + artistId +
                ", artistName: '" + artistName + '\'' +
                ", songs: " + songs +
                ", albums: " + albums +
                '}';
    }
}
