import ctrl.ConnectionFactory;
import ctrl.DAO;
import model.MusicPlayer;
import model.Playlist;
import model.Song;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class Main {
    static int passCount = 0;
    static int failCount = 0;

    static void pass(String testName) {
        passCount++;
        System.out.println("\033[32m [PASS] \033[0m" + testName);
    }

    static void fail(String testName, String reason) {
        failCount++;
        System.out.println("\033[31m [FAIL] \033[0m" + testName + " --> " + reason);
    }

    static void section(String title) {
        System.out.println("\n========== " + title + " ==========");
    }

    static void printSummary() {
        int total = passCount + failCount;
        System.out.printf( "Ergebnis: %2d / %2d Tests", passCount, total);
        System.out.println();
        System.out.printf( "✔ PASS: %-3d  ✘ FAIL: %-3d", passCount, failCount);
    }

    static Song getSongFromDb(String title) {
        Connection con = ConnectionFactory.getInstance();
        try {
            PreparedStatement stmt = con.prepareStatement(
                    "SELECT song_id, title, artist, duration FROM Song WHERE title = ?");
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Song(rs.getInt("song_id"), rs.getString("title"),
                        rs.getString("artist"), rs.getInt("duration"));
            }
        } catch (SQLException e) {
            System.out.println("  [DB-ERROR] getSongFromDb: " + e.getMessage());
        }
        return null;
    }

    static boolean playlistExistsInDb(String playlistName) {
        Connection con = ConnectionFactory.getInstance();
        try {
            PreparedStatement stmt = con.prepareStatement(
                    "SELECT 1 FROM Playlist WHERE playlist_name = ?");
            stmt.setString(1, playlistName);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("  [DB-ERROR] playlistExistsInDb: " + e.getMessage());
        }
        return false;
    }

    static boolean songInPlaylistInDb(int playlistId, String songTitle) {
        Connection con = ConnectionFactory.getInstance();
        try {
            PreparedStatement stmt = con.prepareStatement(
                    "SELECT 1 FROM PlaylistSong WHERE playlist_id = ? AND song_title = ?");
            stmt.setInt(1, playlistId);
            stmt.setString(2, songTitle);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("  [DB-ERROR] songInPlaylistInDb: " + e.getMessage());
        }
        return false;
    }

    static int getPlaylistIdFromDb(String playlistName) {
        Connection con = ConnectionFactory.getInstance();
        try {
            PreparedStatement stmt = con.prepareStatement(
                    "SELECT playlist_id FROM Playlist WHERE playlist_name = ?");
            stmt.setString(1, playlistName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("playlist_id");
        } catch (SQLException e) {
            System.out.println("  [DB-ERROR] getPlaylistIdFromDb: " + e.getMessage());
        }
        return -1;
    }

    static void cleanupPlaylist(String playlistName) {
        Connection con = ConnectionFactory.getInstance();
        try {
            PreparedStatement stmt = con.prepareStatement(
                    "DELETE FROM Playlist WHERE playlist_name = ?");
            stmt.setString(1, playlistName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("  [DB-ERROR] cleanupPlaylist: " + e.getMessage());
        }
    }

    static void testPlaylistAddSong() {
        Map<String, Song> songs = DAO.getSongs();
        Map<String, Playlist> playlists = DAO.getPlaylists(songs);

        if (songs.isEmpty() || playlists.isEmpty()) {
            fail("testPlaylistAddSong", "DB liefert keine Songs oder Playlists");
            return;
        }
        Song song = new Song(9, "As It Was", "Harry Styles", -1);
        Playlist playlist = new Playlist(999, "TestAddSong_tmp");
        playlist.addSong(song);

        if (playlist.getPlaylistSongs().containsKey(song.getTitle())) pass("testPlaylistAddSong");
        else fail("testPlaylistAddSong", "Song wurde nicht hinzugefügt");
    }

    static void testPlaylistAddDuplicateSong() {
        Map<String, Song> songs = DAO.getSongs();
        if (songs.isEmpty()) { fail("testPlaylistAddDuplicateSong", "Keine Songs in DB"); return; }

        Song song = songs.values().iterator().next();
        Playlist playlist = new Playlist(1, "Favorites");
        playlist.addSong(song);
        playlist.addSong(song); // Duplikat

        if (playlist.getSongCount() == 1) pass("testPlaylistAddDuplicateSong");
        else fail("testPlaylistAddDuplicateSong", "Duplikat wurde eingefügt; Count: " + playlist.getSongCount());
    }

    static void testPlaylistRemoveSong() {
        Map<String, Song> songs = DAO.getSongs();
        if (songs.isEmpty()) { fail("testPlaylistRemoveSong", "Keine Songs in DB"); return; }

        Song song = songs.values().iterator().next();
        Playlist playlist = new Playlist(1, "Favorites");
        playlist.addSong(song);
        playlist.removeSong(song.getTitle());

        if (!playlist.getPlaylistSongs().containsKey(song.getTitle())) pass("testPlaylistRemoveSong");
        else fail("testPlaylistRemoveSong", "Song wurde nicht entfernt");
    }

    static void testPlaylistRemoveNonExistentSong() {
        Playlist playlist = new Playlist(1, "Favorites");
        try {
            playlist.removeSong("Ghost Song");
            pass("testPlaylistRemoveNonExistentSong");
        } catch (Exception e) {
            fail("testPlaylistRemoveNonExistentSong", "Exception geworfen: " + e.getMessage());
        }
    }

    static void testPlaylistDurationAfterAdd() {
        Song s1 = getSongFromDb("Blinding Lights"); // 200s
        Song s2 = getSongFromDb("Shape of You");    // 233s
        if (s1 == null || s2 == null) { fail("testPlaylistDurationAfterAdd", "Songs nicht in DB gefunden"); return; }

        Playlist playlist = new Playlist(1, "Favorites");
        playlist.addSong(s1);
        playlist.addSong(s2);
        int expected = s1.getDuration() + s2.getDuration();

        if (playlist.getPlaylistDuration() == expected) pass("testPlaylistDurationAfterAdd");
        else fail("testPlaylistDurationAfterAdd", "Erwartet " + expected + ", aber: " + playlist.getPlaylistDuration());
    }

    static void testPlaylistDurationAfterRemove() {
        Song s1 = getSongFromDb("Blinding Lights");
        Song s2 = getSongFromDb("Shape of You");
        if (s1 == null || s2 == null) { fail("testPlaylistDurationAfterRemove", "Songs nicht in DB gefunden"); return; }

        Playlist playlist = new Playlist(1, "Favorites");
        playlist.addSong(s1);
        playlist.addSong(s2);
        playlist.removeSong(s1.getTitle());

        if (playlist.getPlaylistDuration() == s2.getDuration()) pass("testPlaylistDurationAfterRemove");
        else fail("testPlaylistDurationAfterRemove", "Erwartet " + s2.getDuration() + ", aber: " + playlist.getPlaylistDuration());
    }

    static void testPlaylistGetSongCount() {
        Map<String, Song> songs = DAO.getSongs();
        if (songs.size() < 2) { fail("testPlaylistGetSongCount", "Weniger als 2 Songs in DB"); return; }

        Playlist playlist = new Playlist(1, "Favorites");
        java.util.Iterator<Song> it = songs.values().iterator();
        playlist.addSong(it.next());
        playlist.addSong(it.next());

        if (playlist.getSongCount() == 2) pass("testPlaylistGetSongCount");
        else fail("testPlaylistGetSongCount", "Erwartet 2, aber: " + playlist.getSongCount());
    }

    static void testPlaylistGetFormattedDuration() {
        Song s = getSongFromDb("Believer"); // 204s → 3:24
        if (s == null) { fail("testPlaylistGetFormattedDuration", "Song nicht in DB gefunden"); return; }

        Playlist playlist = new Playlist(1, "Test");
        playlist.addSong(s);

        int mins = s.getDuration() / 60;
        int secs = s.getDuration() % 60;
        String expected = String.format("%d:%02d", mins, secs);

        if (expected.equals(playlist.getFormattedPlaylistDuration()))
            pass("testPlaylistGetFormattedDuration");
        else fail("testPlaylistGetFormattedDuration", "Erwartet '" + expected + "', aber: " + playlist.getFormattedPlaylistDuration());
    }

    static void testPlaylistSetName() {
        Playlist playlist = new Playlist(1, "Old Name");
        playlist.setPlaylistName("New Name");
        if ("New Name".equals(playlist.getPlaylistName())) pass("testPlaylistSetName");
        else fail("testPlaylistSetName", "Erwartet 'New Name', aber: " + playlist.getPlaylistName());
    }

    static void testPlaylistDeleteFromDb() {
        String testName_DB = "TestPlaylist_Delete";
        cleanupPlaylist(testName_DB);

        Playlist playlist = new Playlist(0, testName_DB);
        try {
            DAO.savePlaylist(playlist);
            DAO.deletePlaylist(testName_DB);
            if (!playlistExistsInDb(testName_DB)) pass("testPlaylistDeleteFromDb");
            else fail("testPlaylistDeleteFromDb", "Playlist wurde nicht aus DB gelöscht");
        } catch (Exception e) {
            fail("testPlaylistDeleteFromDb", "Exception: " + e.getMessage());
        }
    }

    static void testPlaylistUpdateNameInDb() {
        String oldName = "TestPlaylist_OldName";
        String newName = "TestPlaylist_NewName";
        cleanupPlaylist(oldName);
        cleanupPlaylist(newName);

        Playlist playlist = new Playlist(0, oldName);
        try {
            DAO.savePlaylist(playlist);
            DAO.updatePlaylistName(oldName, newName);
            if (!playlistExistsInDb(oldName) && playlistExistsInDb(newName))
                pass("testPlaylistUpdateNameInDb");
            else fail("testPlaylistUpdateNameInDb",
                    "Name nicht aktualisiert – oldExists=" + playlistExistsInDb(oldName)
                            + ", newExists=" + playlistExistsInDb(newName));
        } catch (Exception e) {
            fail("testPlaylistUpdateNameInDb", "Exception: " + e.getMessage());
        } finally {
            cleanupPlaylist(newName);
        }
    }

    static void testAddSongToPlaylistInDb() {
        String testName_DB = "TestPlaylist_AddSong";
        cleanupPlaylist(testName_DB);

        Playlist playlist = new Playlist(0, testName_DB);
        Song s = getSongFromDb("Bad Guy");
        if (s == null) { fail("testAddSongToPlaylistInDb", "Song nicht in DB"); return; }

        try {
            DAO.savePlaylist(playlist);
            int id = getPlaylistIdFromDb(testName_DB);
            playlist.setPlaylistId(id);
            DAO.addSongToPlaylist(playlist, s);

            if (songInPlaylistInDb(id, s.getTitle())) pass("testAddSongToPlaylistInDb");
            else fail("testAddSongToPlaylistInDb", "Song nicht in PlaylistSong-Tabelle");
        } catch (Exception e) {
            fail("testAddSongToPlaylistInDb", "Exception: " + e.getMessage());
        } finally {
            cleanupPlaylist(testName_DB);
        }
    }

    static void testRemoveSongFromPlaylistInDb() {
        String testName_DB = "TestPlaylist_RemoveSong";
        cleanupPlaylist(testName_DB);

        Playlist playlist = new Playlist(0, testName_DB);
        Song s = getSongFromDb("Sunflower");
        if (s == null) { fail("testRemoveSongFromPlaylistInDb", "Song nicht in DB"); return; }

        try {
            DAO.savePlaylist(playlist);
            int id = getPlaylistIdFromDb(testName_DB);
            playlist.setPlaylistId(id);
            DAO.addSongToPlaylist(playlist, s);
            DAO.removeSongFromPlaylist(playlist, s.getTitle());

            if (!songInPlaylistInDb(id, s.getTitle())) pass("testRemoveSongFromPlaylistInDb");
            else fail("testRemoveSongFromPlaylistInDb", "Song noch in PlaylistSong-Tabelle");
        } catch (Exception e) {
            fail("testRemoveSongFromPlaylistInDb", "Exception: " + e.getMessage());
        } finally {
            cleanupPlaylist(testName_DB);
        }
    }

    static void testMusicPlayerAddSongToQueue() {
        MusicPlayer player = new MusicPlayer();
        Song song = getSongFromDb("Lose Yourself");
        if (song == null) song = getSongFromDb("Blinding Lights");
        if (song == null) { fail("testMusicPlayerAddSongToQueue", "Kein Song in DB gefunden"); return; }

        player.addSongToQueue(song);
        if (player.getQueue().contains(song)) pass("testMusicPlayerAddSongToQueue");
        else fail("testMusicPlayerAddSongToQueue", "Song nicht in der Queue");
    }

    static void testMusicPlayerRemoveSongFromQueue() {
        MusicPlayer player = new MusicPlayer();
        Song song = getSongFromDb("Blinding Lights");
        if (song == null) { fail("testMusicPlayerRemoveSongFromQueue", "Song nicht in DB"); return; }

        player.addSongToQueue(song);
        player.removeSongFromQueue(song);
        if (!player.getQueue().contains(song)) pass("testMusicPlayerRemoveSongFromQueue");
        else fail("testMusicPlayerRemoveSongFromQueue", "Song noch in der Queue");
    }

    static void testMusicPlayerPlay() {
        MusicPlayer player = new MusicPlayer();
        Song song = getSongFromDb("Shape of You");
        if (song == null) { fail("testMusicPlayerPlay", "Song nicht in DB"); return; }

        player.addSongToQueue(song);
        player.play();
        if (player.isPlaying() && player.getCurrentSong() != null) pass("testMusicPlayerPlay");
        else fail("testMusicPlayerPlay", "isPlaying=" + player.isPlaying() + ", currentSong=" + player.getCurrentSong());
    }

    static void testMusicPlayerPlayEmptyQueue() {
        MusicPlayer player = new MusicPlayer();
        player.play();
        if (!player.isPlaying()) pass("testMusicPlayerPlayEmptyQueue");
        else fail("testMusicPlayerPlayEmptyQueue", "isPlaying sollte false sein");
    }

    static void testMusicPlayerPause() {
        MusicPlayer player = new MusicPlayer();
        Song song = getSongFromDb("Believer");
        if (song == null) { fail("testMusicPlayerPause", "Song nicht in DB"); return; }

        player.addSongToQueue(song);
        player.play();
        player.pause();
        if (!player.isPlaying()) pass("testMusicPlayerPause");
        else fail("testMusicPlayerPause", "isPlaying sollte false sein nach pause()");
    }

    static void testMusicPlayerPlayNext() {
        MusicPlayer player = new MusicPlayer();
        Song s1 = getSongFromDb("Levitating");
        Song s2 = getSongFromDb("Stay");
        if (s1 == null || s2 == null) { fail("testMusicPlayerPlayNext", "Songs nicht in DB"); return; }

        player.addSongToQueue(s1);
        player.addSongToQueue(s2);
        player.play();
        player.playNext();
        if (player.getCurrentSong().equals(s2) && player.getCurrentIndex() == 1)
            pass("testMusicPlayerPlayNext");
        else fail("testMusicPlayerPlayNext",
                "currentSong=" + player.getCurrentSong() + ", index=" + player.getCurrentIndex());
    }

    static void testMusicPlayerPlayLast() {
        MusicPlayer player = new MusicPlayer();
        Song s1 = getSongFromDb("Bad Guy");
        Song s2 = getSongFromDb("Sunflower");
        if (s1 == null || s2 == null) { fail("testMusicPlayerPlayLast", "Songs nicht in DB"); return; }

        player.addSongToQueue(s1);
        player.addSongToQueue(s2);
        player.play();
        player.playNext();
        player.playLast();
        if (player.getCurrentSong().equals(s1) && player.getCurrentIndex() == 0)
            pass("testMusicPlayerPlayLast");
        else fail("testMusicPlayerPlayLast",
                "currentSong=" + player.getCurrentSong() + ", index=" + player.getCurrentIndex());
    }

    static void testMusicPlayerShuffle() {
        MusicPlayer player = new MusicPlayer();
        Map<String, Song> songs = DAO.getSongs();
        if (songs.size() < 3) { fail("testMusicPlayerShuffle", "Zu wenige Songs in DB"); return; }

        for (Song s : songs.values()) player.addSongToQueue(s);
        int sizeBefore = player.getQueue().size();

        player.shuffle();
        if (player.getCurrentIndex() == 0 && player.getCurrentSong() != null
                && player.getQueue().size() == sizeBefore)
            pass("testMusicPlayerShuffle");
        else fail("testMusicPlayerShuffle",
                "index=" + player.getCurrentIndex() + ", size=" + player.getQueue().size());
    }

    public static void main(String[] args) {
        section("Playlist");
        testPlaylistAddSong();
        testPlaylistAddDuplicateSong();
        testPlaylistRemoveSong();
        testPlaylistRemoveNonExistentSong();
        testPlaylistDurationAfterAdd();
        testPlaylistDurationAfterRemove();
        testPlaylistGetSongCount();
        testPlaylistGetFormattedDuration();
        testPlaylistSetName();

        section("Playlist – Datenbank");
        testPlaylistDeleteFromDb();
        testPlaylistUpdateNameInDb();
        testAddSongToPlaylistInDb();
        testRemoveSongFromPlaylistInDb();

        section("MusicPlayer");
        testMusicPlayerAddSongToQueue();
        testMusicPlayerRemoveSongFromQueue();
        testMusicPlayerPlay();
        testMusicPlayerPlayEmptyQueue();
        testMusicPlayerPause();
        testMusicPlayerPlayNext();
        testMusicPlayerPlayLast();
        testMusicPlayerShuffle();

        System.out.println("\n========== FERTIG ==========");
        printSummary();
    }
}