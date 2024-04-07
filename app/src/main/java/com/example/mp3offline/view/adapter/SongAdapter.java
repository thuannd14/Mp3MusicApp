package com.example.mp3offline.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mp3offline.App;
import com.example.mp3offline.Mp3Player;
import com.example.mp3offline.R;
import com.example.mp3offline.Ui;
import com.example.mp3offline.model.Song;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {

    private static final String TAG = SongAdapter.class.getName();
    private Context context;
    private List<Song> songList;

    private View.OnClickListener event;
    private int curSong;

    private Ui callBack;

    public void setCallBack(Ui callBack) {
        this.callBack = callBack;
    }

    public SongAdapter(Context context, List<Song> songList, View.OnClickListener event ){
        this.context = context;
        this.songList = songList;
        this.event = event;
    }
    public SongAdapter(Context context, List<Song> songList){
        this.context = context;
        this.songList = songList;
    }

    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_song, null);
        return new SongHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongHolder holder, int position) {
        Song song = songList.get(position);
        holder.tvNameSong.setText(song.title);
        Glide.with(context).load(song.path).into(holder.ivNameSong);

        if(position == curSong){
            holder.lnSong.setBackgroundResource(R.color.blue);
        }else{
            holder.lnSong.setBackgroundResource(R.color.white);
        }

        holder.tvNameSong.setTag(song);

    }

    @Override
    public int getItemCount() {
        return songList.size()-1;
    }

    public void updateUI(int curSong) {
        this.curSong = App.getInstance().getStorage().getCurSong();
        notifyItemRangeChanged(0, songList.size());
    }

    public class SongHolder extends RecyclerView.ViewHolder {
        ImageView ivNameSong;
        TextView tvNameSong;
        View lnSong;
        public SongHolder(@NonNull View view) {
            super(view);
            ivNameSong = view.findViewById(R.id.iv_song);
            tvNameSong = view.findViewById(R.id.tv_song);
            lnSong = view.findViewById(R.id.ln_item_song);

            lnSong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in));
                    clickItemSong((Song)tvNameSong.getTag());
                }
            });
        }
    }

    private void clickItemSong(Song tag) {
        Log.i(TAG, "clickItemSong" + tag.title);

        // Bam vao bai nao thi phat bai do.
        Mp3Player.getInstance().findSong(tag.title);

        // khi bam vao 1 song bat ki.
        // chuyen den mainact de thuc hien viec cap nhat.
        callBack.updateUI();
    }
}
