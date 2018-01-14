package com.example.ishant.olaplaystudios.views.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ishant.olaplaystudios.R;

public class SongListItemViewholder extends RecyclerView.ViewHolder {

    public ImageView songThumbnail = null;
    public ImageView playButton = null;
    public ImageView downloadButton = null;
    public TextView songTitle = null;
    public TextView songArtist = null;
//    public CheckBox ratingStar = null;

    public SongListItemViewholder(View itemView) {
        super(itemView);
        songThumbnail = (ImageView)itemView.findViewById(R.id.song_icon);
        playButton = (ImageView)itemView.findViewById(R.id.play_button);
        downloadButton = (ImageView)itemView.findViewById(R.id.download_button);
        songTitle = (TextView)itemView.findViewById(R.id.song_title);
        songArtist = (TextView)itemView.findViewById(R.id.artist);
//        ratingStar = (CheckBox) itemView.findViewById(R.id.rating);
    }
}
