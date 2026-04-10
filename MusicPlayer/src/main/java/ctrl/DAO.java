package ctrl;

import model.Playlist;
import model.Song;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DAO {

    public static HashMap<String, Song> getSongs() {
        HashMap<String, Song> songs = new HashMap<>();

        Connection con = ConnectionFactory.getInstance();

        try {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM Song");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int songId = rs.getInt("song_id");
                String title = rs.getString("title");
                String artist = rs.getString("artist");
                int duration = rs.getInt("duration");

                Song song = new Song(songId, title, artist, duration);
                songs.put(title, song);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return songs;
    }

    public static HashMap<String, Playlist> getPlaylists(Map<String, Song> songs) {
        HashMap<String, Playlist> playlists = new HashMap<>();

        Connection con = ConnectionFactory.getInstance();

        try {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM Playlist");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int playlistId = rs.getInt("playlist_id");
                String playlistName = rs.getString("playlist_name");

                Playlist playlist = new Playlist(playlistId, playlistName);
                playlists.put(playlistName, playlist);
            }

            loadPlaylistSongs(playlists, songs);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return playlists;
    }

    public static boolean checkPlaylistName(String playlistName) {
        boolean check = false;
        Connection con = ConnectionFactory.getInstance();

        try {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM Playlist WHERE playlist_name = ?");
            stmt.setString(1, playlistName);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("This playlist name is already in use.");
                check = true;
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1) {
                System.out.println("Playlist name already exists!");
            }
            throw new RuntimeException(e);
        }

        return check;
    }

    private static void loadPlaylistSongs(Map<String, Playlist> playlists, Map<String, Song> songs) {
        Connection con = ConnectionFactory.getInstance();

        try {
            PreparedStatement stmt = con.prepareStatement("""
                    SELECT playlist_id, song_title
                    FROM PlaylistSong
                    """);

            ResultSet rs = stmt.executeQuery();

            HashMap<Integer, Playlist> playlistById = getPlaylistIdMap(playlists);

            while (rs.next()) {
                int playlistId = rs.getInt("playlist_id");
                String songTitle = rs.getString("song_title");

                Playlist playlist = playlistById.get(playlistId);
                Song song = songs.get(songTitle);

                if (playlist != null && song != null) {
                    playlist.addSong(song);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static HashMap<Integer, Playlist> getPlaylistIdMap(Map<String, Playlist> playlists) {
        HashMap<Integer, Playlist> playlistMap = new HashMap<>();

        for (Playlist playlist : playlists.values()) {
            playlistMap.put(playlist.getPlaylistId(), playlist);
        }

        return playlistMap;
    }

    public static void saveSong(Song song) {
        Connection con = ConnectionFactory.getInstance();

        try {
            PreparedStatement stmt = con.prepareStatement("""
                    INSERT INTO Song (song_id, title, artist, duration)
                    VALUES (?, ?, ?, ?)
                    """);

            stmt.setInt(1, song.getSongId());
            stmt.setString(2, song.getTitle());
            stmt.setString(3, song.getArtist());
            stmt.setInt(4, song.getDuration());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void savePlaylist(Playlist playlist) {
        Connection con = ConnectionFactory.getInstance();

        try {
            PreparedStatement stmt = con.prepareStatement("""
                INSERT INTO Playlist (playlist_id, playlist_name)
                VALUES (playlist_seq.NEXTVAL, ?)
            """);

            stmt.setString(1, playlist.getPlaylistName());

            stmt.executeUpdate();

            savePlaylistSongs(playlist);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void savePlaylistSongs(Playlist playlist) {
        Connection con = ConnectionFactory.getInstance();

        try {
            for (Song song : playlist.getPlaylistSongs().values()) {
                PreparedStatement stmt = con.prepareStatement("""
                        INSERT INTO PlaylistSong (playlist_id, song_title)
                        VALUES (?, ?)
                        """);

                stmt.setInt(1, playlist.getPlaylistId());
                stmt.setString(2, song.getTitle());

                stmt.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addSongToPlaylist(Playlist playlist, Song song) {
        Connection con = ConnectionFactory.getInstance();

        try {
            PreparedStatement stmt = con.prepareStatement("""
                    INSERT INTO PlaylistSong (playlist_id, song_title)
                    VALUES (?, ?)
                    """);

            stmt.setInt(1, playlist.getPlaylistId());
            stmt.setString(2, song.getTitle());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeSongFromPlaylist(Playlist playlist, String songTitle) {
        Connection con = ConnectionFactory.getInstance();

        try {
            PreparedStatement stmt = con.prepareStatement("""
                    DELETE FROM PlaylistSong
                    WHERE playlist_id = ? AND song_title = ?
                    """);

            stmt.setInt(1, playlist.getPlaylistId());
            stmt.setString(2, songTitle);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deletePlaylist(String playlistName) {
        Connection con = ConnectionFactory.getInstance();

        try {
            PreparedStatement stmt = con.prepareStatement("""
                    DELETE FROM Playlist
                    WHERE playlist_name = ?
                    """);

            stmt.setString(1, playlistName);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updatePlaylistName(String oldName, String newName) {
        Connection con = ConnectionFactory.getInstance();

        try {
            PreparedStatement stmt = con.prepareStatement("""
                    UPDATE Playlist
                    SET playlist_name = ?
                    WHERE playlist_name = ?
                    """);

            stmt.setString(1, newName);
            stmt.setString(2, oldName);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}