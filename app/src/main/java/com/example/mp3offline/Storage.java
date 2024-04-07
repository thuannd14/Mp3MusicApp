package com.example.mp3offline;

import com.example.mp3offline.model.Song;

import java.util.List;

public class Storage {
    public List<Song> songList;

    private int curSong;

    public int getCurSong() {
        return curSong;
    }

    public void setCurSong(int curSong) {
        this.curSong = curSong;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    public List<Song> getSongList() {
        return songList;
    }
}
