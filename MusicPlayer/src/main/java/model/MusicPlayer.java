package model;

import java.util.List;

public class MusicPlayer {
    private List<Song> queue;

    public void addSongToQueue(Song song) {
        queue.add(song);
    }

    public void removeSongFromQueue(Song song) {
        if (queue.contains(song)) {
            queue.remove(song);
        } else {
            System.out.printf("The song %s by %s is not in the queue.", song.getTitle(), song.getArtist());
        }
    }
}
