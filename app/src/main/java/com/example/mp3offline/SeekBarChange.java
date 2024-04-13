package com.example.mp3offline;

import android.widget.SeekBar;

public interface SeekBarChange extends SeekBar.OnSeekBarChangeListener {
    @Override
    default void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // ko ghi de.
    }

    @Override
    default void onStartTrackingTouch(SeekBar seekBar) {
       // ko ghi de.
    }


}
