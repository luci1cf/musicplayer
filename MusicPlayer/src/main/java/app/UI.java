package app;

import model.MusicPlayer;
import model.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UI {
    private final Scanner input;
    private final MusicPlayer player;
    private final List<Song> library;

    public UI() {
        input = new Scanner(System.in);
        player = new MusicPlayer();
        library = new ArrayList<>();

        library.add(new Song(1, "Blinding Lights", "The Weeknd", 200));
        library.add(new Song(2, "Shape of You", "Ed Sheeran", 233));
        library.add(new Song(3, "Believer", "Imagine Dragons", 204));
        library.add(new Song(4, "Levitating", "Dua Lipa", 203));
        library.add(new Song(5, "Stay", "The Kid LAROI", 141));
        library.add(new Song(6, "Bad Guy", "Billie Eilish", 194));
        library.add(new Song(7, "Sunflower", "Post Malone", 158));
        library.add(new Song(8, "Someone You Loved", "Lewis Capaldi", 182));
        library.add(new Song(9, "As It Was", "Harry Styles", 167));
        library.add(new Song(10, "Heat Waves", "Glass Animals", 238));
    }

    public void start() {
        boolean running = true;

        while (running) {
            showMenu();
            String choice = input.nextLine();

            switch (choice) {
                case "1":
                    addSongMenu();
                    break;
                case "2":
                    removeSongMenu();
                    break;
                case "3":
                    player.showQueue();
                    break;
                case "4":
                    player.play();
                    break;
                case "5":
                    player.pause();
                    break;
                case "6":
                    player.playNext();
                    break;
                case "7":
                    player.playLast();
                    break;
                case "8":
                    player.shuffle();
                    break;
                case "0":
                    running = false;
                    System.out.println("Music Player closed.");
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
        }
        input.close();
    }

    private void showMenu() {
        System.out.println("\n=== MUSIC PLAYER MENU ===");
        System.out.println("1 - Add song to queue");
        System.out.println("2 - Remove song from queue");
        System.out.println("3 - Show queue");
        System.out.println("4 - Play");
        System.out.println("5 - Pause");
        System.out.println("6 - Next song");
        System.out.println("7 - Previous song");
        System.out.println("8 - Shuffle queue");
        System.out.println("0 - Exit");
        System.out.print("Choose an option: ");
    }

    private void showLibrary() {
        System.out.println("\n--- SONG LIBRARY---");
        for (int i = 0; i < library.size(); i++) {
            Song song = library.get(i);
            System.out.printf("%d - %s by %s \n", i+1, song.getTitle(), song.getArtist());
        }
    }

    private void addSongMenu() {
        System.out.println("Which song do you want to add?");
        showLibrary();
        System.out.print("Enter song number: ");

        try {
            int songNumber = Integer.parseInt(input.nextLine());

            if (songNumber >= 1 && songNumber <= library.size()) {
                Song selectedSong = library.get(songNumber-1);
                player.addSongToQueue(selectedSong);
            } else {
                System.out.println("Invalid song number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Enter a valid number.");
        }
    }

    private void removeSongMenu() {
        System.out.println("Which song do you want to remove?");
        player.showQueue();
        System.out.print("Enter song number: ");

        try {
            int songNumber = Integer.parseInt(input.nextLine());

            if (songNumber >= 1 && songNumber <= library.size()) {
                Song selectedSong = library.get(songNumber-1);
                player.removeSongFromQueue(selectedSong);
            } else {
                System.out.println("Invalid song number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }
}
