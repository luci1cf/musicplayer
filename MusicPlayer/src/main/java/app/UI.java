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

    public void start() {
        boolean running = true;

        while (running) {
            showMenu();
            String choice = input.nextLine();

            switch (choice) {
                case "1":
                    // addSongMenu();
                    break;
                case "2":
                    // removeSongMenu();
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

    private void addSongMenu() {
        System.out.println("Available songs:");
        System.out.println("1 - Blinding Lights");
        System.out.println("2 - Shape of You");
        System.out.println("3 - Believer");
        System.out.print("Enter song number: ");

        String addChoice = input.nextLine();

        switch (addChoice) {
            case "1":
                player.addSongToQueue(song1);
                break;
            case "2":
                player.addSongToQueue(song2);
                break;
            case "3":
                player.addSongToQueue(song3);
                break;
            default:
                System.out.println("Invalid song number.");
        }
    }

    private void removeSongMenu() {
        System.out.println("Which song do you want to remove?");
        System.out.println("1 - Blinding Lights");
        System.out.println("2 - Shape of You");
        System.out.println("3 - Believer");
        System.out.print("Enter song number: ");

        String removeChoice = input.nextLine();

        switch (removeChoice) {
            case "1":
                player.removeSongFromQueue(song1);
                break;
            case "2":
                player.removeSongFromQueue(song2);
                break;
            case "3":
                player.removeSongFromQueue(song3);
                break;
            default:
                System.out.println("Invalid song number.");
        }
    }
}
