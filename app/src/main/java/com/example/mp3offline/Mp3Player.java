package com.example.mp3offline;

import android.database.Cursor;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.util.Log;

import com.example.mp3offline.model.Song;

import java.util.ArrayList;
import java.util.List;

public class Mp3Player {
    public static final int STATE_IDLE = 1;
    public static final int STATE_PLAYING = 2;
    public static final int STATE_PAUSED = 3;
    private static int state = STATE_IDLE;
    private static final String TAG = Mp3Player.class.getName();
    private static Mp3Player instance;
    private List<Song> songList = new ArrayList<>();
    private final MediaPlayer mediaPlayer = new MediaPlayer();
    private int curSong;

    private Mp3Player() {

    }

    public static Mp3Player getInstance() {
        if (instance == null) {
            instance = new Mp3Player();
        }
        return instance;
    }

    // tai nhac tu thu muc download cua dien thoai.
    public void loadOffline() {
        songList = new ArrayList<>();
        // lenh query giong select.

        // đối tượng Cursor để truy cập dữ liệu từ cơ sở dữ liệu của hệ thống.
        //ts1: duong dan den noi luu cho nhac
        Cursor cursor = App.getInstance().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.TITLE + " ASC");
        cursor.moveToFirst(); // Di chuyển con trỏ của Cursor tới hàng đầu tiên của kết quả trả về từ truy vấn.

        Log.i(TAG, "CURSOR: " + cursor.getColumnCount());
        int cursorTitle = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);// Lấy chỉ mục của cột "TITLE" từ kết quả truy vấn
        int cursorPath = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
        int cursorAlbum = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
        int cursorArtist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        songList.clear();

        while (!cursor.isAfterLast()) {
            String tile = cursor.getString(cursorTitle);
            String path = cursor.getString(cursorPath);
            String album = cursor.getString(cursorAlbum);
            String artist = cursor.getString(cursorArtist);
            Song song = new Song(tile, path, album, artist);
            songList.add(song);
            cursor.moveToNext();
        }
        cursor.close();
        Log.i(TAG, "LIST: " + songList.size());
        App.getInstance().getStorage().setSongList(songList); // them vao moi truong chung.
        Log.i(TAG, "Song duoc luu vao APP: " + App.getInstance().getStorage().getSongList().size());
    }

    public void play() {
        if (state == STATE_IDLE) {
            mediaPlayer.reset();
            //  mediaPlayer.setDataSource va  mediaPlayer.prepare de het loi.
            try {
                mediaPlayer.setDataSource(songList.get(curSong).path);
                Log.i(TAG, "Path: " + songList.get(curSong).path);
                        mediaPlayer.prepare();
                mediaPlayer.start();
                state = STATE_PLAYING;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (state == STATE_PAUSED) {
            mediaPlayer.start();
            state = STATE_PLAYING;
        }else {
            mediaPlayer.pause();
            state = STATE_PAUSED;
        }
    }



    public void next(){
        curSong++;
        App.getInstance().getStorage().setCurSong(curSong);
        if(curSong == songList.size()){
            curSong = 0;
        }
        state = STATE_IDLE;
        play();
    }

    public void back(){
        curSong--;
        App.getInstance().getStorage().setCurSong(curSong);
        if(curSong == 0){
            curSong = songList.size()-1;
        }
        state = STATE_IDLE;
        play();
    }

    public static int getState() {
        return state;
    }


    // ấn vào bài này thì phát luôn bài đó.
    public void findSong(String title) {
        for(int i=0;i<songList.size();i++){
            if(songList.get(i).title.equals(title)){
                curSong = i;
                App.getInstance().getStorage().setCurSong(curSong);
                break;
            }
        }
        state = STATE_IDLE;
        play();
    }

    public int getCurSong() {
        return curSong;
    }

}
