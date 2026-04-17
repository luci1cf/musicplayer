import app.UI;
import model.*;

public class Main {
    public static void main(String[] args) {
//      UI ui = new UI();
//      ui.start();
    }

    public static void runTests() {
        int success = 0;
        int failed = 0;

        if (testCreatePlaylist()) success++; else failed++;
        if (testChangePlaylistName()) success++; else failed++;
        if (testAddSongToPlaylist()) success++; else failed++;
        if (testRemoveSongFromPlaylist()) success++; else failed++;
        if (testDeletePlaylist()) success++; else failed++;


        System.out.println("---- RESULTS ----");
        System.out.println("SUCCESS: ", success);
        System.out.println("FAIL: ", failed);
    }

    public static boolean testCreatePlaylist() {
        try {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean testChangePlaylistName() {
        try {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean testAddSongToPlaylist() {
        try {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean testRemoveSongFromPlaylist() {
        try {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean testDeletePlaylist() {
        try {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
