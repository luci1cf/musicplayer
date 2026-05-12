import app.UI;
import ctrl.DAO;
import model.MusicPlayer;
import model.Playlist;
import model.Song;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Scanner;

import static org.junit.Assert.*;


public class Tests {

    private static final String SONG_BLINDING_LIGHTS = "Blinding Lights";  // id=1, 200s
    private static final String SONG_SHAPE_OF_YOU    = "Shape of You";     // id=2, 233s

    private static final String TEST_PLAYLIST        = "JUnit_TestPlaylist";
    private static final String TEST_PLAYLIST_RENAME = "JUnit_TestPlaylist_Renamed";

    private Playlist testPlaylist;
    private Song     testSong;
    private Song     testSong2;

    @Before
    public void setUp() {
        DAO.deletePlaylist(TEST_PLAYLIST);
        DAO.deletePlaylist(TEST_PLAYLIST_RENAME);

        testPlaylist = new Playlist(0, TEST_PLAYLIST);
        DAO.savePlaylist(testPlaylist);

        Map<String, Song> songs = DAO.getSongs();
        testPlaylist.setPlaylistId(DAO.getPlaylists(songs).get(TEST_PLAYLIST).getPlaylistId());

        testSong  = songs.get(SONG_BLINDING_LIGHTS);
        testSong2 = songs.get(SONG_SHAPE_OF_YOU);
    }

    @After
    public void tearDown() {
        DAO.deletePlaylist(TEST_PLAYLIST);
        DAO.deletePlaylist(TEST_PLAYLIST_RENAME);
    }

    private UI buildUI(String simulatedInput) {
        UI ui = new UI();
        try {
            Field inputField = UI.class.getDeclaredField("input");
            inputField.setAccessible(true);
            inputField.set(ui, new Scanner(new ByteArrayInputStream(simulatedInput.getBytes())));
        } catch (Exception e) {
            fail("buildUI() konnte Scanner nicht setzen: " + e.getMessage());
        }
        return ui;
    }

    @Test
    public void testGetSongsNotNull() {
        assertNotNull("getSongs() soll nicht null zurückgeben",
                DAO.getSongs());
    }

    @Test
    public void testGetSongsNotEmpty() {
        assertFalse("Songs-Map soll nicht leer sein",
                DAO.getSongs().isEmpty());
    }

    @Test
    public void testGetSongsContainsBlindingLights() {
        assertTrue("Songs soll 'Blinding Lights' enthalten",
                DAO.getSongs().containsKey(SONG_BLINDING_LIGHTS));
    }

    @Test
    public void testGetSongsDurationCorrect() {
        Song song = DAO.getSongs().get(SONG_BLINDING_LIGHTS);
        assertNotNull("'Blinding Lights' soll in der DB existieren", song);
        assertEquals("Dauer von 'Blinding Lights' soll 200s sein", 200, song.getDuration());
    }

    @Test
    public void testGetSongsArtistCorrect() {
        Song song = DAO.getSongs().get(SONG_BLINDING_LIGHTS);
        assertNotNull("'Blinding Lights' soll in der DB existieren", song);
        assertEquals("Artist soll 'The Weeknd' sein", "The Weeknd", song.getArtist());
    }

    @Test
    public void testGetPlaylistsNotNull() {
        assertNotNull("getPlaylists() soll nicht null zurückgeben",
                DAO.getPlaylists(DAO.getSongs()));
    }

    @Test
    public void testGetPlaylistsContainsTestPlaylist() {
        assertTrue("Playlists sollen die Test-Playlist enthalten",
                DAO.getPlaylists(DAO.getSongs()).containsKey(TEST_PLAYLIST));
    }

    @Test
    public void testGetPlaylistsIdGreaterZero() {
        Playlist loaded = DAO.getPlaylists(DAO.getSongs()).get(TEST_PLAYLIST);
        assertNotNull("Test-Playlist soll geladen werden", loaded);
        assertTrue("Playlist ID soll > 0 sein", loaded.getPlaylistId() > 0);
    }

    @Test
    public void testCheckPlaylistNameExisting() {
        assertTrue("checkPlaylistName() soll true bei existierendem Namen zurückgeben",
                DAO.checkPlaylistName(TEST_PLAYLIST));
    }

    @Test
    public void testCheckPlaylistNameNonExisting() {
        assertFalse("checkPlaylistName() soll false bei nicht existierendem Namen zurückgeben",
                DAO.checkPlaylistName("Playlist_xyz_existiert_nicht_999"));
    }

    @Test
    public void testUICreatePlaylistExistsInDb() {
        String name = "JUnit_UI_Create";
        DAO.deletePlaylist(name);

        buildUI(name + "\n").createPlaylist();

        assertTrue("createPlaylist() soll Playlist in DB anlegen",
                DAO.checkPlaylistName(name));

        DAO.deletePlaylist(name);
    }

    @Test
    public void testUICreatePlaylistVisibleViaGetPlaylists() {
        String name = "JUnit_UI_Create2";
        DAO.deletePlaylist(name);

        buildUI(name + "\n").createPlaylist();

        assertTrue("createPlaylist() soll Playlist über getPlaylists() sichtbar machen",
                DAO.getPlaylists(DAO.getSongs()).containsKey(name));

        DAO.deletePlaylist(name);
    }

    @Test
    public void testSavePlaylistExistsInDb() {
        assertTrue("Test-Playlist soll nach savePlaylist() in DB existieren",
                DAO.checkPlaylistName(TEST_PLAYLIST));
    }

    @Test
    public void testSavePlaylistIdIsSet() {
        assertTrue("Playlist ID soll nach setUp() > 0 sein",
                testPlaylist.getPlaylistId() > 0);
    }

    @Test
    public void testSavePlaylistNameCorrect() {
        Playlist loaded = DAO.getPlaylists(DAO.getSongs()).get(TEST_PLAYLIST);
        assertNotNull("Test-Playlist soll aus DB ladbar sein", loaded);
        assertEquals("Playlist-Name soll korrekt gespeichert sein",
                TEST_PLAYLIST, loaded.getPlaylistName());
    }

    @Test
    public void testAddSongToPlaylistVisibleViaGetPlaylists() {
        assertNotNull("Voraussetzung: testSong darf nicht null sein", testSong);

        DAO.addSongToPlaylist(testPlaylist, testSong);

        Playlist loaded = DAO.getPlaylists(DAO.getSongs()).get(TEST_PLAYLIST);
        assertNotNull("Test-Playlist soll aus DB ladbar sein", loaded);
        assertTrue("Song soll in der geladenen Playlist enthalten sein",
                loaded.getPlaylistSongs().containsKey(testSong.getTitle()));
    }

    @Test
    public void testAddMultipleSongsToPlaylist() {
        assertNotNull("Voraussetzung: testSong darf nicht null sein",  testSong);
        assertNotNull("Voraussetzung: testSong2 darf nicht null sein", testSong2);

        DAO.addSongToPlaylist(testPlaylist, testSong);
        DAO.addSongToPlaylist(testPlaylist, testSong2);

        Playlist loaded = DAO.getPlaylists(DAO.getSongs()).get(TEST_PLAYLIST);
        assertNotNull("Test-Playlist soll aus DB ladbar sein", loaded);
        assertTrue("Erster Song soll in Playlist stehen",
                loaded.getPlaylistSongs().containsKey(testSong.getTitle()));
        assertTrue("Zweiter Song soll in Playlist stehen",
                loaded.getPlaylistSongs().containsKey(testSong2.getTitle()));
    }

    @Test
    public void testAddSongToPlaylistSongCountIncreases() {
        assertNotNull("Voraussetzung: testSong darf nicht null sein", testSong);

        DAO.addSongToPlaylist(testPlaylist, testSong);

        Playlist loaded = DAO.getPlaylists(DAO.getSongs()).get(TEST_PLAYLIST);
        assertEquals("getSongCount() soll 1 sein nach addSongToPlaylist()",
                1, loaded.getSongCount());
    }


    @Test
    public void testUIAddSongToPlaylistVisibleViaGetPlaylists() {
        assertNotNull("Voraussetzung: testSong darf nicht null sein", testSong);

        try {
            buildUI("1\n").addSongToPlaylist(TEST_PLAYLIST);
        } catch (Exception e) {
            fail("addSongToPlaylist() warf Exception: " + e.getMessage());
        }

        Playlist loaded = DAO.getPlaylists(DAO.getSongs()).get(TEST_PLAYLIST);
        assertNotNull("Test-Playlist soll aus DB ladbar sein", loaded);
        assertTrue("Song soll nach UI.addSongToPlaylist() in Playlist stehen",
                loaded.getPlaylistSongs().containsKey(SONG_BLINDING_LIGHTS));
    }

    @Test
    public void testRemoveSongFromPlaylistNotVisibleViaGetPlaylists() {
        assertNotNull("Voraussetzung: testSong darf nicht null sein", testSong);

        DAO.addSongToPlaylist(testPlaylist, testSong);
        DAO.removeSongFromPlaylist(testPlaylist, testSong.getTitle());

        Playlist loaded = DAO.getPlaylists(DAO.getSongs()).get(TEST_PLAYLIST);
        assertNotNull("Test-Playlist soll aus DB ladbar sein", loaded);
        assertFalse("Song soll nach removeSongFromPlaylist() nicht mehr in Playlist stehen",
                loaded.getPlaylistSongs().containsKey(testSong.getTitle()));
    }

    @Test
    public void testRemoveSongFromPlaylistOnlyRemovesTargetSong() {
        assertNotNull("Voraussetzung: testSong darf nicht null sein",  testSong);
        assertNotNull("Voraussetzung: testSong2 darf nicht null sein", testSong2);

        DAO.addSongToPlaylist(testPlaylist, testSong);
        DAO.addSongToPlaylist(testPlaylist, testSong2);
        DAO.removeSongFromPlaylist(testPlaylist, testSong.getTitle());

        Playlist loaded = DAO.getPlaylists(DAO.getSongs()).get(TEST_PLAYLIST);
        assertFalse("Erster Song soll entfernt sein",
                loaded.getPlaylistSongs().containsKey(testSong.getTitle()));
        assertTrue("Zweiter Song soll noch vorhanden sein",
                loaded.getPlaylistSongs().containsKey(testSong2.getTitle()));
    }

    @Test
    public void testUIRemoveSongFromPlaylistNotVisibleViaGetPlaylists() {
        assertNotNull("Voraussetzung: testSong darf nicht null sein", testSong);

        DAO.addSongToPlaylist(testPlaylist, testSong);

        // Erst testSong in Playlist-Map des UI laden, dann entfernen
        try {
            buildUI(testSong.getTitle() + "\n").removeSongFromPlaylist(TEST_PLAYLIST);
        } catch (Exception e) {
            fail("UI.removeSongFromPlaylist() warf Exception: " + e.getMessage());
        }

        Playlist loaded = DAO.getPlaylists(DAO.getSongs()).get(TEST_PLAYLIST);
        assertFalse("Song soll nach UI.removeSongFromPlaylist() nicht mehr in Playlist stehen",
                loaded.getPlaylistSongs().containsKey(testSong.getTitle()));
    }

    @Test
    public void testUpdatePlaylistNameOldNameGone() {
        DAO.updatePlaylistName(TEST_PLAYLIST, TEST_PLAYLIST_RENAME);

        assertFalse("Alter Playlist-Name soll nicht mehr in DB existieren",
                DAO.checkPlaylistName(TEST_PLAYLIST));
    }

    @Test
    public void testUpdatePlaylistNameNewNameExists() {
        DAO.updatePlaylistName(TEST_PLAYLIST, TEST_PLAYLIST_RENAME);

        assertTrue("Neuer Playlist-Name soll in DB existieren",
                DAO.checkPlaylistName(TEST_PLAYLIST_RENAME));
    }

    @Test
    public void testUpdatePlaylistNameNewNameVisibleViaGetPlaylists() {
        DAO.updatePlaylistName(TEST_PLAYLIST, TEST_PLAYLIST_RENAME);

        Map<String, Playlist> playlists = DAO.getPlaylists(DAO.getSongs());
        assertFalse("Alter Name soll nicht mehr über getPlaylists() sichtbar sein",
                playlists.containsKey(TEST_PLAYLIST));
        assertTrue("Neuer Name soll über getPlaylists() sichtbar sein",
                playlists.containsKey(TEST_PLAYLIST_RENAME));
    }

    @Test
    public void testUpdatePlaylistNameSongsStillLinked() {
        assertNotNull("Voraussetzung: testSong darf nicht null sein", testSong);

        DAO.addSongToPlaylist(testPlaylist, testSong);
        DAO.updatePlaylistName(TEST_PLAYLIST, TEST_PLAYLIST_RENAME);

        Playlist loaded = DAO.getPlaylists(DAO.getSongs()).get(TEST_PLAYLIST_RENAME);
        assertNotNull("Umbenannte Playlist soll ladbar sein", loaded);
        assertTrue("Song soll nach Umbenennung noch mit der Playlist verknüpft sein",
                loaded.getPlaylistSongs().containsKey(testSong.getTitle()));
    }

    @Test
    public void testUIChangePlaylistNameOldNameGone() {
        buildUI(TEST_PLAYLIST_RENAME + "\n").changePlaylistName(TEST_PLAYLIST);

        assertFalse("Alter Playlist-Name soll nach changePlaylistName() nicht mehr in DB existieren",
                DAO.checkPlaylistName(TEST_PLAYLIST));
    }

    @Test
    public void testUIChangePlaylistNameNewNameExists() {
        buildUI(TEST_PLAYLIST_RENAME + "\n").changePlaylistName(TEST_PLAYLIST);

        assertTrue("Neuer Playlist-Name soll nach changePlaylistName() in DB existieren",
                DAO.checkPlaylistName(TEST_PLAYLIST_RENAME));
    }

    @Test
    public void testDeletePlaylistNotVisibleViaCheckPlaylistName() {
        DAO.deletePlaylist(TEST_PLAYLIST);

        assertFalse("Playlist soll nach deletePlaylist() nicht mehr in DB existieren",
                DAO.checkPlaylistName(TEST_PLAYLIST));
    }

    @Test
    public void testDeletePlaylistNotVisibleViaGetPlaylists() {
        DAO.deletePlaylist(TEST_PLAYLIST);

        assertFalse("Gelöschte Playlist soll nicht mehr über getPlaylists() sichtbar sein",
                DAO.getPlaylists(DAO.getSongs()).containsKey(TEST_PLAYLIST));
    }

    @Test
    public void testDeletePlaylistCascadesSongs() {
        assertNotNull("Voraussetzung: testSong darf nicht null sein", testSong);

        DAO.addSongToPlaylist(testPlaylist, testSong);
        DAO.deletePlaylist(TEST_PLAYLIST);

        testPlaylist = new Playlist(0, TEST_PLAYLIST);
        DAO.savePlaylist(testPlaylist);
        testPlaylist.setPlaylistId(DAO.getPlaylists(DAO.getSongs()).get(TEST_PLAYLIST).getPlaylistId());

        assertTrue("Neu angelegte Playlist soll leer sein – Cascade hat Songs entfernt",
                DAO.getPlaylists(DAO.getSongs()).get(TEST_PLAYLIST).getPlaylistSongs().isEmpty());
    }


    @Test
    public void testUIDeletePlaylistNotVisibleViaCheckPlaylistName() {
        buildUI(TEST_PLAYLIST + "\n").deletePlaylist();

        assertFalse("Playlist soll nach UI.deletePlaylist() nicht mehr in DB existieren",
                DAO.checkPlaylistName(TEST_PLAYLIST));
    }

    @Test
    public void testUIDeletePlaylistNotVisibleViaGetPlaylists() {
        buildUI(TEST_PLAYLIST + "\n").deletePlaylist();

        assertFalse("Gelöschte Playlist soll nicht mehr über getPlaylists() sichtbar sein",
                DAO.getPlaylists(DAO.getSongs()).containsKey(TEST_PLAYLIST));
    }


    @Test
    public void testPlaylistAddSongModel() {
        assertNotNull("Voraussetzung: testSong darf nicht null sein", testSong);

        testPlaylist.addSong(testSong);

        assertTrue("addSong() soll Song in playlistSongs-Map ablegen",
                testPlaylist.getPlaylistSongs().containsKey(testSong.getTitle()));
    }

    @Test
    public void testPlaylistAddSongDuplicateIgnored() {
        assertNotNull("Voraussetzung: testSong darf nicht null sein", testSong);

        testPlaylist.addSong(testSong);
        testPlaylist.addSong(testSong);

        assertEquals("Duplikat soll ignoriert werden – Song darf nur einmal enthalten sein",
                1, testPlaylist.getSongCount());
    }

    @Test
    public void testPlaylistRemoveSongModel() {
        assertNotNull("Voraussetzung: testSong darf nicht null sein", testSong);

        testPlaylist.addSong(testSong);
        testPlaylist.removeSong(testSong.getTitle());

        assertFalse("removeSong() soll Song aus playlistSongs entfernen",
                testPlaylist.getPlaylistSongs().containsKey(testSong.getTitle()));
    }

    @Test
    public void testPlaylistRemoveNonExistentSongNoException() {
        testPlaylist.removeSong("Song der nicht existiert xyz");
        // Kein Exception = Test bestanden
    }

    @Test
    public void testPlaylistDurationAfterAddFromDb() {
        assertNotNull("Voraussetzung: testSong darf nicht null sein",  testSong);
        assertNotNull("Voraussetzung: testSong2 darf nicht null sein", testSong2);

        testPlaylist.addSong(testSong);
        testPlaylist.addSong(testSong2);

        assertEquals("Playlist-Dauer soll Summe der Song-Dauern sein",
                testSong.getDuration() + testSong2.getDuration(),
                testPlaylist.getPlaylistDuration());
    }

    @Test
    public void testPlaylistDurationAfterRemoveFromDb() {
        assertNotNull("Voraussetzung: testSong darf nicht null sein",  testSong);
        assertNotNull("Voraussetzung: testSong2 darf nicht null sein", testSong2);

        testPlaylist.addSong(testSong);
        testPlaylist.addSong(testSong2);
        testPlaylist.removeSong(testSong.getTitle());

        assertEquals("Dauer soll nach removeSong() korrekt verringert sein",
                testSong2.getDuration(), testPlaylist.getPlaylistDuration());
    }

    @Test
    public void testPlaylistFormattedDurationFromDb() {
        assertNotNull("Voraussetzung: testSong darf nicht null sein", testSong);

        testPlaylist.addSong(testSong); // 200s = 3:20
        String expected = String.format("%d:%02d",
                testSong.getDuration() / 60, testSong.getDuration() % 60);

        assertEquals("getFormattedPlaylistDuration() soll korrekt formatieren",
                expected, testPlaylist.getFormattedPlaylistDuration());
    }

    @Test
    public void testPlaylistSongCountFromDb() {
        assertNotNull("Voraussetzung: testSong darf nicht null sein",  testSong);
        assertNotNull("Voraussetzung: testSong2 darf nicht null sein", testSong2);

        testPlaylist.addSong(testSong);
        testPlaylist.addSong(testSong2);

        assertEquals("getSongCount() soll 2 zurückgeben", 2, testPlaylist.getSongCount());
    }

    @Test
    public void testMusicPlayerInitialState() {
        MusicPlayer player = new MusicPlayer();

        assertTrue("Neue Queue soll leer sein",        player.getQueue().isEmpty());
        assertNull("currentSong soll null sein",       player.getCurrentSong());
        assertEquals("currentIndex soll -1 sein", -1, player.getCurrentIndex());
        assertFalse("isPlaying soll false sein",       player.isPlaying());
    }

    @Test
    public void testAddSongToQueue() {
        assertNotNull("Voraussetzung: testSong darf nicht null sein", testSong);

        MusicPlayer player = new MusicPlayer();
        player.addSongToQueue(testSong);

        assertTrue("Queue soll den hinzugefügten Song enthalten",
                player.getQueue().contains(testSong));
    }

    @Test
    public void testRemoveSongFromQueue() {
        assertNotNull("Voraussetzung: testSong darf nicht null sein", testSong);

        MusicPlayer player = new MusicPlayer();
        player.addSongToQueue(testSong);
        player.removeSongFromQueue(testSong);

        assertFalse("Queue soll Song nach removeSongFromQueue() nicht mehr enthalten",
                player.getQueue().contains(testSong));
    }

    @Test
    public void testRemoveCurrentSongResetsPlayerState() {
        assertNotNull("Voraussetzung: testSong darf nicht null sein", testSong);

        MusicPlayer player = new MusicPlayer();
        player.addSongToQueue(testSong);
        player.play();
        player.removeSongFromQueue(testSong);

        assertNull("currentSong soll null sein",   player.getCurrentSong());
        assertFalse("isPlaying soll false sein",   player.isPlaying());
        assertEquals("currentIndex soll -1 sein", -1, player.getCurrentIndex());
    }

    @Test
    public void testPlay() {
        assertNotNull("Voraussetzung: testSong darf nicht null sein", testSong);

        MusicPlayer player = new MusicPlayer();
        player.addSongToQueue(testSong);
        player.play();

        assertTrue("isPlaying soll true sein nach play()",  player.isPlaying());
        assertEquals("currentSong soll testSong sein",      testSong, player.getCurrentSong());
        assertEquals("currentIndex soll 0 sein",            0, player.getCurrentIndex());
    }

    @Test
    public void testPlayEmptyQueueDoesNotPlay() {
        MusicPlayer player = new MusicPlayer();
        player.play();

        assertFalse("isPlaying soll false bleiben bei leerer Queue", player.isPlaying());
    }

    @Test
    public void testPause() {
        assertNotNull("Voraussetzung: testSong darf nicht null sein", testSong);

        MusicPlayer player = new MusicPlayer();
        player.addSongToQueue(testSong);
        player.play();
        player.pause();

        assertFalse("isPlaying soll false sein nach pause()", player.isPlaying());
    }

    @Test
    public void testPlayNext() {
        assertNotNull("Voraussetzung: testSong darf nicht null sein",  testSong);
        assertNotNull("Voraussetzung: testSong2 darf nicht null sein", testSong2);

        MusicPlayer player = new MusicPlayer();
        player.addSongToQueue(testSong);
        player.addSongToQueue(testSong2);
        player.play();
        player.playNext();

        assertEquals("currentSong soll testSong2 sein", testSong2, player.getCurrentSong());
        assertEquals("currentIndex soll 1 sein",        1,         player.getCurrentIndex());
    }

    @Test
    public void testPlayNextAtEndOfQueue() {
        assertNotNull("Voraussetzung: testSong darf nicht null sein", testSong);

        MusicPlayer player = new MusicPlayer();
        player.addSongToQueue(testSong);
        player.play();
        player.playNext(); // schon am Ende – kein Wechsel

        assertEquals("currentSong soll unverändert sein", testSong, player.getCurrentSong());
        assertEquals("currentIndex soll 0 bleiben",       0,        player.getCurrentIndex());
    }

    @Test
    public void testPlayLast() {
        assertNotNull("Voraussetzung: testSong darf nicht null sein",  testSong);
        assertNotNull("Voraussetzung: testSong2 darf nicht null sein", testSong2);

        MusicPlayer player = new MusicPlayer();
        player.addSongToQueue(testSong);
        player.addSongToQueue(testSong2);
        player.play();
        player.playNext();
        player.playLast();

        assertEquals("currentSong soll wieder testSong sein", testSong, player.getCurrentSong());
        assertEquals("currentIndex soll 0 sein",              0,        player.getCurrentIndex());
    }

    @Test
    public void testPlayLastAtBeginningOfQueue() {
        assertNotNull("Voraussetzung: testSong darf nicht null sein", testSong);

        MusicPlayer player = new MusicPlayer();
        player.addSongToQueue(testSong);
        player.play();
        player.playLast(); // schon am Anfang – kein Wechsel

        assertEquals("currentSong soll unverändert sein", testSong, player.getCurrentSong());
        assertEquals("currentIndex soll 0 bleiben",       0,        player.getCurrentIndex());
    }

    @Test
    public void testShuffleKeepsAllSongs() {
        Map<String, Song> songs = DAO.getSongs();
        assertFalse("Voraussetzung: Songs-Map soll nicht leer sein", songs.isEmpty());

        MusicPlayer player = new MusicPlayer();
        for (Song s : songs.values()) player.addSongToQueue(s);
        int sizeBefore = player.getQueue().size();

        player.shuffle();

        assertEquals("Queue soll nach shuffle() gleich viele Songs haben",
                sizeBefore, player.getQueue().size());
    }

    @Test
    public void testShuffleSetsCurrentSongAndIndex() {
        Map<String, Song> songs = DAO.getSongs();
        assertFalse("Voraussetzung: Songs-Map soll nicht leer sein", songs.isEmpty());

        MusicPlayer player = new MusicPlayer();
        for (Song s : songs.values()) player.addSongToQueue(s);
        player.shuffle();

        assertEquals("currentIndex soll 0 sein nach shuffle()",
                0, player.getCurrentIndex());
        assertEquals("currentSong soll Queue[0] entsprechen nach shuffle()",
                player.getQueue().get(0), player.getCurrentSong());
    }

    @Test
    public void testShuffleEmptyQueueNoException() {
        new MusicPlayer().shuffle(); // Kein Exception erlaubt
    }
}