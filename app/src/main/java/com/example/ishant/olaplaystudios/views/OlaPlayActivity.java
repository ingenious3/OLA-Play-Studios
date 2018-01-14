package com.example.ishant.olaplaystudios.views;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ishant.olaplaystudios.R;
import com.example.ishant.olaplaystudios.eventlistener.EndlessRecyclerViewScrollListener;
import com.example.ishant.olaplaystudios.eventlistener.RecyclerItemClickListener;
import com.example.ishant.olaplaystudios.model.SongModel;
import com.example.ishant.olaplaystudios.utils.SongsLoader;
import com.example.ishant.olaplaystudios.views.adapter.SongListItemAdapter;

import java.util.ArrayList;

public class OlaPlayActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<ArrayList<SongModel>>,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String REQUEST_URL = "http://starlord.hackerearth.com/studio";
    private static int SONG_LOADER_ID = 1;
    private SongListItemAdapter songListItemAdapter;
    private TextView emptyTextView;
    private ProgressBar mProgressBar;
    private RecyclerView songRecyclerView;
    private Toolbar toolbar = null;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private EndlessRecyclerViewScrollListener scrollListener;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<SongModel> songModelArrayList = new ArrayList<>();
    private ArrayList<SongModel> searchList = new ArrayList<>();
    private SearchView mSearchView;
    private SwipeRefreshLayout mSwipeLayout;
    private android.app.LoaderManager loaderManager = getLoaderManager();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ola_play_actvity);
        initializeViews();

        downloadData(SONG_LOADER_ID);
    }

    public void initializeViews(){

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        emptyTextView = (TextView)findViewById(R.id.Empty_txtview);
        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(R.color.blue,R.color.green,R.color.yellow,R.color.red);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("OLA PLAY");
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        songRecyclerView = (RecyclerView)findViewById(R.id.song_list);
        linearLayoutManager = new LinearLayoutManager(this);
        songRecyclerView.setLayoutManager(linearLayoutManager);
        songRecyclerView.setHasFixedSize(true);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

            }
        };

        songRecyclerView.addOnScrollListener(scrollListener);

        songRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, songRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        SongModel data = songModelArrayList.get(position);

                        Intent intent = new Intent(OlaPlayActivity.this,MusicPlayerActivity.class);
                        intent.putExtra("cover_image",data.getCoverImage());
                        intent.putExtra("song_url", (data.getUrl()));
                        intent.putExtra("song_title",data.getSong());
                        startActivity(intent);

                    }
                    @Override public void onLongItemClick(View view, int position) {
                        SongModel data = songModelArrayList.get(position);
                        Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse(data.getUrl()));
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }
                })
        );

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        mSearchView=(SearchView)findViewById(R.id.search);
        mSearchView.setIconified(false);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setQueryHint("Enter song title or artist");
        mSearchView.clearFocus();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchList.clear();
                for(SongModel songModel : songModelArrayList){
                    if(songModel.getSong().toString().toUpperCase().contains(query.toUpperCase())||songModel.getArtists().toString().toUpperCase().contains(query.toUpperCase())){
                        searchList.add(songModel);
                    }
                }
                songListItemAdapter = new SongListItemAdapter(searchList,OlaPlayActivity.this);
                songListItemAdapter.notifyDataSetChanged();
                songRecyclerView.setAdapter(songListItemAdapter);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals(""))
                {
                    songListItemAdapter = new SongListItemAdapter(songModelArrayList,OlaPlayActivity.this);
                    songListItemAdapter.notifyDataSetChanged();
                    songRecyclerView.setAdapter(songListItemAdapter);
                }
                return true;
            }
        });
    }

    public void downloadData(int loader_id){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo !=null && networkInfo.isConnected()){
            if(mSwipeLayout!=null){
                mSwipeLayout.setRefreshing(false);
            }
            loaderManager = getLoaderManager();
            loaderManager.initLoader(loader_id, null, this);
        } else {
            if(songModelArrayList.size()==0) {
                mProgressBar.setVisibility(View.GONE);
                emptyTextView.setText(R.string.no_internet);
            }
        }
    }

    @Override
    public android.content.Loader<ArrayList<SongModel>> onCreateLoader(int id, Bundle args) {

        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        return new SongsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(android.content.Loader<ArrayList<SongModel>> loader, ArrayList<SongModel> songModelArrayList) {

        mProgressBar.setVisibility(View.GONE);
        if (songModelArrayList != null && !songModelArrayList.isEmpty()) {
            if(mSwipeLayout!=null){
                mSwipeLayout.setRefreshing(false);
            }
            this.songModelArrayList = songModelArrayList;
            songListItemAdapter = new SongListItemAdapter(songModelArrayList,OlaPlayActivity.this);
            songListItemAdapter.notifyDataSetChanged();
            songRecyclerView.setAdapter(songListItemAdapter);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<ArrayList<SongModel>> loader) {
        searchList.clear();
        songModelArrayList.clear();
        songListItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        searchList.clear();
        songModelArrayList.clear();
        songListItemAdapter.notifyDataSetChanged();
        SONG_LOADER_ID++;
        downloadData(SONG_LOADER_ID);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mSearchView.clearFocus();
    }
}
