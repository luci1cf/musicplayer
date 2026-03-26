package app;

import model.MusicPlayer;
import model.Playlist;
import model.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
            System.out.println();

            switch (choice) {
                case "1":
                    showQueueMenu();
                    break;
                case "2":
                    showPlaylistMenu();
                    break;
                case "3":
                    showPlaybackMenu();
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
        System.out.print("1 - Queue management");
        System.out.print("\t2 - Playlist management\n");
        System.out.print("3 - Playback management");
        System.out.println("\t0 - Exit");
        System.out.print("Choose an option: ");
    }

    private void showLibrary() {
        System.out.println("\n--- SONG LIBRARY---");
        for (int i = 0; i < library.size(); i++) {
            Song song = library.get(i);
            System.out.printf("%d - %s by %s \n", i+1, song.getTitle(), song.getArtist());
        }
    }

    private void showPlaylists() {
        System.out.println("=== PLAYLISTS ===");

        for (Playlist playlist : player.getPlaylists().values()) {
            System.out.printf("%d: %s\n", playlist.getPlaylistId(), playlist.getPlaylistName());
        }
        System.out.println();
    }

    private void showPlaylistMenu() {
        boolean running = true;

        while (running) {
            System.out.println("Do you want to see all playlists, create a new one, manage a already existing one, delete a playlist or exit?");
            System.out.println("(see/create/manage/delete/exit)");
            String choice = input.nextLine();
            System.out.println();

            switch (choice) {
                case "see":
                    System.out.println(player.getPlaylists());
                    System.out.println();
                    break;
                case "create":
                    System.out.println("Playlist name:");
                    String playlistName = input.nextLine();

                    while (player.getPlaylists().containsKey(playlistName)) {
                        System.out.println("This name is already taken. Please give another name:");
                        playlistName = input.nextLine();
                    }

                    int newId = player.getPlaylists().size() + 1;
                    Playlist newPlaylist = new Playlist(newId, playlistName);
                    player.getPlaylists().put(playlistName, newPlaylist);
                    System.out.printf("Playlist %s successfully created.%n", playlistName);
                    System.out.println();
                    break;
                case "manage":
                    System.out.println("Which playlist do you want to manage?");
                    showPlaylists();
                    System.out.println("Playlist: ");
                    String chosenPlaylist = input.nextLine();
                    System.out.println();

                    if (player.getPlaylists().containsKey(chosenPlaylist)) {
                        System.out.println("Do you want to add songs, remove songs or change the playlist name?");
                        System.out.println("(add/remove/change)");
                        String choice2 = input.nextLine();
                        System.out.println();

                        switch (choice2) {
                            case "add":
                                System.out.println("Which song do you want to add to the playlist?");
                                showLibrary();
                                int addSong = Integer.parseInt(input.nextLine());
                                System.out.println();

                                if (addSong >= 1 && addSong <= library.size()) {
                                    Song selectedSong = library.get(addSong-1);
                                    player.getPlaylists().get(chosenPlaylist).addSong(selectedSong);
                                } else {
                                    System.out.println("Invalid song number.");
                                    System.out.println();
                                }
                                break;
                            case "remove":
                                System.out.println("Which song do you want to remove from the playlist?");
                                Map<String, Song> playlistSongs = player.getPlaylists().get(chosenPlaylist).getPlaylistSongs();
                                String songToRemove = input.nextLine();
                                System.out.println();

                                if (playlistSongs.containsKey(songToRemove)) {
                                    player.getPlaylists().get(chosenPlaylist).removeSong(songToRemove);
                                } else {
                                    System.out.println("Invalid song number.");
                                    System.out.println();
                                }
                                break;
                            case "change":
                                System.out.print("New name for the playlist: ");
                                String newPlaylistName = input.nextLine();
                                System.out.println();

                                Map<String, Playlist> playlists = player.getPlaylists();

                                if (playlists.containsKey(newPlaylistName)) {
                                    System.out.println("A playlist with this name already exists.");
                                    System.out.println();
                                    break;
                                }

                                Playlist playlist = playlists.remove(chosenPlaylist);

                                if (playlist == null) {
                                    System.out.println("Playlist not found.");
                                    System.out.println();
                                    break;
                                }

                                playlist.setPlaylistName(newPlaylistName);
                                playlists.put(newPlaylistName, playlist);

                                System.out.println("Playlist name changed successfully.");
                                System.out.println();
                                break;
                            default:
                                System.out.println("Invalid input.");
                                System.out.println();
                                break;
                        }
                    } else {
                        System.out.println("Playlist you are searching for does not exist.");
                        System.out.println();
                    }
                    break;
                case "delete":
                    System.out.println("Which playlist do you want to delete?");
                    showPlaylists();
                    System.out.println("Playlist: ");
                    String playlistToDelete = input.nextLine();
                    System.out.println();

                    if (player.getPlaylists().containsKey(playlistToDelete)) {
                        player.getPlaylists().remove(playlistToDelete);
                        System.out.printf("Playlist %s successfully deleted.\n", playlistToDelete);
                    }
                    break;
                case "exit":
                    running = false;
                    System.out.println("Playlist menu closed");
                    System.out.println();
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
                    System.out.println();
                    break;
            }
        }
    }

    private void showQueueMenu() {
        boolean running = true;

        while (running) {
            System.out.println("Do you want to add a song to the queue, remove a song from the queue, show the queue or exit the menu?");
            System.out.println("(add/remove/show/exit)");
            String choice = input.nextLine();

            switch (choice) {
                case "add":
                    addSongMenu();
                    break;
                case "remove":
                    removeSongMenu();
                    break;
                case "show":
                    player.showQueue();
                    break;
                case "exit":
                    running = false;
                    System.out.println("Queue menu closed.");
                default:
                    System.out.println("Invalid input. Please try again.");
                    break;
            }
        }
    }

    private void showPlaybackOptions() {
        System.out.print("1 - Play");
        System.out.print("\t2 - Pause\n");
        System.out.print("3 - Play next song");
        System.out.print("\t4 - Play previous song");
        System.out.print("\t\t\t5 - Shuffle queue\n");
        System.out.println("0 - Exit");
        System.out.print("Choose an option: ");
    }

    private void showPlaybackMenu() {
        boolean running = true;

        while (running) {
            System.out.println("=== PLAYBACK MENU ===");
            showPlaybackOptions();
            String choice = input.nextLine();

            switch (choice) {
                case "1":
                    player.play();
                    break;
                case "2":
                    player.pause();
                    break;
                case "3":
                    player.playNext();
                    break;
                case "4":
                    player.playLast();
                    break;
                case "5":
                    player.shuffle();
                    break;
                case "0":
                    running = false;
                    System.out.println("Playback menu closed.");
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
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
