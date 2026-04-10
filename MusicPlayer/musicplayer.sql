DROP TABLE Song;
DROP TABLE Playlist;
DROP TABLE PlaylistSong;

CREATE SEQUENCE playlist_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE Song (
    song_id NUMBER PRIMARY KEY,
    title VARCHAR2(100) NOT NULL UNIQUE,
    artist VARCHAR2(100) NOT NULL,
    duration NUMBER NOT NULL
);

CREATE TABLE Playlist (
    playlist_id NUMBER PRIMARY KEY,
    playlist_name VARCHAR2(100) NOT NULL
);

SELECT * FROM Playlist;

CREATE TABLE PlaylistSong (
    playlist_id NUMBER NOT NULL,
    song_title VARCHAR2(100) NOT NULL,
    CONSTRAINT pk_playlistsong PRIMARY KEY (playlist_id, song_title),
    CONSTRAINT fk_playlistsong_playlist
        FOREIGN KEY (playlist_id) REFERENCES Playlist(playlist_id) ON DELETE CASCADE,
    CONSTRAINT fk_playlistsong_song
        FOREIGN KEY (song_title) REFERENCES Song(title) ON DELETE CASCADE
);

INSERT INTO Song (song_id, title, artist, duration) VALUES (1, 'Blinding Lights', 'The Weeknd', 200);
INSERT INTO Song (song_id, title, artist, duration) VALUES (2, 'Shape of You', 'Ed Sheeran', 233);
INSERT INTO Song (song_id, title, artist, duration) VALUES (3, 'Believer', 'Imagine Dragons', 204);
INSERT INTO Song (song_id, title, artist, duration) VALUES (4, 'Levitating', 'Dua Lipa', 203);
INSERT INTO Song (song_id, title, artist, duration) VALUES (5, 'Stay', 'The Kid LAROI', 141);
INSERT INTO Song (song_id, title, artist, duration) VALUES (6, 'Bad Guy', 'Billie Eilish', 194);
INSERT INTO Song (song_id, title, artist, duration) VALUES (7, 'Sunflower', 'Post Malone', 158);
INSERT INTO Song (song_id, title, artist, duration) VALUES (8, 'Someone You Loved', 'Lewis Capaldi', 182);
INSERT INTO Song (song_id, title, artist, duration) VALUES (9, 'As It Was', 'Harry Styles', 167);
INSERT INTO Song (song_id, title, artist, duration) VALUES (10, 'Heat Waves', 'Glass Animals', 238);

COMMIT;