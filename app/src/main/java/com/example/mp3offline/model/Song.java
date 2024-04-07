package com.example.mp3offline.model;

public class Song {
    public String title;
    public String path;
    public String album;
    public String artist;

    public Song(String title, String path, String album, String artist) {
        this.title = title;
        this.path = path;
        this.album = album;
        this.artist = artist;
    }
}
