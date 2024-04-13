package com.example.mp3offline.view.act;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


import com.example.mp3offline.App;
import com.example.mp3offline.Mp3Player;
import com.example.mp3offline.R;
import com.example.mp3offline.SeekBarChange;
import com.example.mp3offline.Ui;
import com.example.mp3offline.databinding.ActivityMainBinding;
import com.example.mp3offline.model.Song;
import com.example.mp3offline.view.adapter.SongAdapter;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, Ui {
    // Nho khai bao quyen de doc duoc cai file audio luu trong may.
    // WRITE_EXTERNAL_STORAGE
    // READ_EXTERNAL_STORAGE
    private static final String TAG = MainActivity.class.getName();

    private List<Song> songList;
    private ActivityMainBinding binding;


    private boolean appRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        Mp3Player.getInstance().loadOffline();
        songList = new ArrayList<>();
        Mp3Player.getInstance().setOnCompletionListener(mp -> updateUI());
        initViews();
    }

    private void initViews() {
        songList = App.getInstance().getStorage().getSongList();
        Log.i(TAG, "initViews Main" + songList);
        SongAdapter adapter = new SongAdapter(this, songList);

        binding.vpListMusic.setAdapter(adapter);

        // set su kien choi nhac.
        binding.includedItemNavi.ivPlay.setOnClickListener(this);
        binding.includedItemNavi.ivNext.setOnClickListener(this);
        binding.includedItemNavi.ivBack.setOnClickListener(this);


        // callback tu SongAdapter cap nhat ui.
        // khi click vao 1 song baats ki treen recyclerView.
        adapter.setCallBack(MainActivity.this);

        // xu li thanh seekbar.
        appRunning = true; // bai hat chay
        new Thread(this::updateSeekBar).start();

        // xu li su kien tua nhac.
        binding.includedItemNavi.sbTimeSong.setOnSeekBarChangeListener((SeekBarChange) seekBar -> Mp3Player.getInstance().seekTo(seekBar.getProgress()));
    }

    private void updateSeekBar() {
        while (appRunning) {
            try {
                Thread.sleep(500);
                runOnUiThread(() -> {
                    String curTimeText = Mp3Player.getInstance().getCurTimeText();
                    String totalTimeText = Mp3Player.getInstance().getTotalTimeText();
                    int curTime = Mp3Player.getInstance().getCurTime();
                    int totalTime = Mp3Player.getInstance().getTotalTime();
                    binding.includedItemNavi.sbTimeSong.setMax(totalTime);
                    binding.includedItemNavi.sbTimeSong.setProgress(curTime);
                    binding.includedItemNavi.tvTimeSong.setText(String.format("%s/%s", curTimeText, totalTimeText));
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_play) {
            Mp3Player.getInstance().play();
        } else if (v.getId() == R.id.iv_back) {
            Mp3Player.getInstance().back();
        } else {
            Mp3Player.getInstance().next();
        }
        updateUI();
    }

    public void updateUI() {
        if (Mp3Player.getState() == Mp3Player.STATE_PLAYING) {
            binding.includedItemNavi.ivPlay.setImageResource(R.drawable.ic_pause);
        } else if (Mp3Player.getState() == Mp3Player.STATE_PAUSED) {
            binding.includedItemNavi.ivPlay.setImageResource(R.drawable.ic_play);
        }

        // cập nhật bg khi song được chọn.
        Song song = Mp3Player.getInstance().getCurIndex();

        binding.includedItemNavi.tvNameSong.setText(song.title);

        SongAdapter adapter = (SongAdapter) binding.vpListMusic.getAdapter();
        adapter.updateUI(Mp3Player.getInstance().getCurSong());
    }

    @Override
    protected void onDestroy() {
        appRunning = false;
        super.onDestroy();
    }
}