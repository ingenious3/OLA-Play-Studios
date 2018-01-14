package com.example.ishant.olaplaystudios.utils;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.ishant.olaplaystudios.model.SongModel;

import java.util.ArrayList;

public class SongsLoader extends AsyncTaskLoader<ArrayList<SongModel>> {

    private static final String LOG_TAG = SongsLoader.class.getSimpleName();

    private String mUrl;

    public SongsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<SongModel> loadInBackground() {
        if (mUrl == null){
            return null;
        }
        ArrayList<SongModel> Earthquakes = HttpQueryUtils.fetchSongList(mUrl);

        return Earthquakes;
    }
}
