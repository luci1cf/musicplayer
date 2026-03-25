package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MusicPlayer {
    private List<Song> queue;
    private Song currentSong;
    private int currentIndex;       // position of the currentSong (the song which is playing)
    private boolean isPlaying;

    public MusicPlayer() {
        this.queue = new ArrayList<>();
        this.currentSong = null;
        this.currentIndex = -1;
        this.isPlaying = false;
    }

    public List<Song> getQueue() {
        return queue;
    }

    public Song getCurrentSong() {
        return currentSong;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

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
            int removedIndex = queue.indexOf(song);
            queue.remove(song);

            if (removedIndex == currentIndex) {
                currentSong = null;
                isPlaying = false;
                currentIndex = -1;
            } else if (removedIndex < currentIndex) {
                currentIndex--;
            }

            System.out.println("Song removed from queue.");
        } else {
            System.out.printf("The song %s by %s is not in the queue.%n", song.getTitle(), song.getArtist());
        }
    }

    public void play() {
        if (queue.isEmpty()) {
            System.out.println("The queue is empty.");
            return;
        }

        if (currentSong == null) {
            currentIndex = 0;
            currentSong = queue.get(currentIndex);
        }

        isPlaying = true;
        System.out.printf("Now playing: %s by %s%n", currentSong.getTitle(), currentSong.getArtist());
    }

    public void pause() {
        if (!isPlaying) {
            System.out.println("No song is currently playing.");
            return;
        }

        isPlaying = false;
        System.out.println("Playback paused.");
    }

    public void playNext() {
        if (queue.isEmpty()) {
            System.out.println("The queue is empty.");
            return;
        }

        if (currentIndex < queue.size() - 1) {
            currentIndex++;
            currentSong = queue.get(currentIndex);
            isPlaying = true;

            System.out.printf("Now playing: %s by %s%n", currentSong.getTitle(), currentSong.getArtist());
        } else {
            System.out.println("You are already at the last song in the queue.");
        }
    }

    public void playLast() {
        if (queue.isEmpty()) {
            System.out.println("The queue is empty.");
            return;
        }

        if (currentIndex > 0) {
            currentIndex--;
            currentSong = queue.get(currentIndex);
            isPlaying = true;

            System.out.printf("Now playing: %s by %s%n", currentSong.getTitle(), currentSong.getArtist());
        } else {
            System.out.println("You are already at the first song in the queue.");
        }
    }

    public void shuffle() {
        if (queue.isEmpty()) {
            System.out.println("The queue is empty.");
            return;
        }

        Collections.shuffle(queue);
        currentIndex = 0;
        currentSong = queue.get(currentIndex);

        System.out.println("Queue shuffled.");
    }

    public void showQueue() {
        if (queue.isEmpty()) {
            System.out.println("The queue is empty.");
            return;
        }

        System.out.println("Current queue:");
        for (int i = 0; i < queue.size(); i++) {
            Song song = queue.get(i);
            System.out.printf("%d. %s by %s%n", i + 1, song.getTitle(), song.getArtist());
        }
    }
}
