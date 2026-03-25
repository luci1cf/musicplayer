package model;

import java.util.List;
import java.util.Scanner;

public class MusicPlayer {
    private List<Song> queue;
    private Song currentSong;
    private int currentIndex;
    private boolean isPlaying;

    public void addSongToQueue(Song song) {
        if (queue.contains(song)) {
            Scanner input = new Scanner(System.in);
            System.out.print("This song is already in your queue. Add again? (y/n): ");

            String choice = input.nextLine();

            if (!choice.equalsIgnoreCase("y")) {
                System.out.println("Song was not added.");
                return;
            }
        }

        queue.add(song);
        System.out.println("Song added to queue.");
    }

    public void removeSongFromQueue(Song song) {
        if (queue.contains(song)) {
            queue.remove(song);
        } else {
            System.out.printf("The song %s by %s is not in the queue.", song.getTitle(), song.getArtist());
        }
    }
}
