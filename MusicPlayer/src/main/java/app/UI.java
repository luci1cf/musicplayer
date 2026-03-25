package app;

import model.MusicPlayer;
import model.Song;

import java.util.Scanner;

public class UI {
    private final Scanner input;
    private final MusicPlayer player;

    private final Song song1;
    private final Song song2;
    private final Song song3;

    public UI() {
        input = new Scanner(System.in);
        player = new MusicPlayer();

        song1 = new Song(1, "Blinding Lights", "The Weeknd", 200);
        song2 = new Song(2, "Shape of You", "Ed Sheeran", 233);
        song3 = new Song(3, "Believer", "Imagine Dragons", 204);
    }
}
