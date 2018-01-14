package com.example.ishant.olaplaystudios.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.ishant.olaplaystudios.R;
import com.example.ishant.olaplaystudios.model.SongModel;
import com.example.ishant.olaplaystudios.views.viewholder.SongListItemViewholder;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SongListItemAdapter extends RecyclerView.Adapter<SongListItemViewholder> implements Filterable {

    private ArrayList<SongModel> songsArraylist;
    private Context context ;
    private int mSelectedItem = -1;

    public SongListItemAdapter(ArrayList<SongModel> songsArraylist, Context context) {
        this.songsArraylist = songsArraylist;
        this.context = context;
    }

    @Override
    public SongListItemViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout,parent,false);
        return (new SongListItemViewholder(v));
    }

    @Override
    public void onBindViewHolder(SongListItemViewholder holder, int position) {
        SongModel data = songsArraylist.get(position);
        holder.songTitle.setText(data.getSong());
        holder.songArtist.setText("Artist : "+data.getArtists());
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttpDownloader(context));
        builder.build()
                .load(data.getCoverImage())
                .placeholder(R.drawable.ic_ola_music)
                .into(holder.songThumbnail);


    }

    @Override
    public int getItemCount() {
        return songsArraylist.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
