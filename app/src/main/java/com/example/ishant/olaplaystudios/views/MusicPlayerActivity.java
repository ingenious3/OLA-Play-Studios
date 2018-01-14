package com.example.ishant.olaplaystudios.views;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ishant.olaplaystudios.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MusicPlayerActivity extends AppCompatActivity {

    private static final String TAG = MusicPlayerActivity.class.getSimpleName();

    private MediaPlayer mediaPlayer;
    private Boolean initialStage = true;
    private Boolean mPlayPause = false;

    private String songUrl = "";
    private String coverImageUrl = "";
    private Intent intent = null;
    private ImageView coverImageView;
    private ProgressDialog progressDialog;
    private ImageView imageViewPlayPause;
    private TextView songTitle;
    private ImageView imageViewDownloadSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        songTitle = (TextView)findViewById(R.id.song_name_textview);
        coverImageView = (ImageView)findViewById(R.id.album_cover_image);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        progressDialog = new ProgressDialog(MusicPlayerActivity.this);
        imageViewPlayPause = (ImageView)findViewById(R.id.play_pause_imageview);
        imageViewDownloadSong = (ImageView)findViewById(R.id.download_imageview);

        intent = getIntent();

        new UrlExpanderTask().execute((intent.getStringExtra("song_url")));

        songTitle.setText(intent.getStringExtra("song_title"));
        coverImageUrl = intent.getStringExtra("cover_image");

        Glide.with(MusicPlayerActivity.this).load(coverImageUrl).into(coverImageView);

        imageViewPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configureMusicPlayer();
            }
        });
        imageViewDownloadSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadSong(songUrl,intent.getStringExtra("song_title"));
            }
        });
    }


    private class UrlExpanderTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String finalUrl = "";
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setInstanceFollowRedirects(false);
                finalUrl = urlConnection.getHeaderField("Location");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(finalUrl != null){
                return finalUrl;
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            songUrl = s;
            configureMusicPlayer();
        }
    }

    public void downloadSong(String song_url, String song_title) {

        Uri download_uri = Uri.parse(song_url);

        DownloadManager.Request downloadRequest = new DownloadManager.Request(download_uri);
        downloadRequest.setTitle("Downloading " + song_title + "...");
        downloadRequest.allowScanningByMediaScanner();
        downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        downloadRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, song_title+".mp3");
        DownloadManager manager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        if( manager != null) {
            manager.enqueue(downloadRequest);
            Toast.makeText(MusicPlayerActivity.this,"Downloading started ...",Toast.LENGTH_SHORT).show();
        }
    }

    public void configureMusicPlayer(){
        if(!mPlayPause){
            imageViewPlayPause.setImageResource(R.drawable.ic_pause_button);
            if(initialStage){
                new Player().execute(songUrl.toString());
            }else{
                if(!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                }
            }
            mPlayPause = true;
        }else{
            imageViewPlayPause.setImageResource(R.drawable.ic_play_button);
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
            }
            mPlayPause = false;
        }
    }

    private class Player extends AsyncTask<String, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Buffering...");
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean prepared = false;
            try{
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        initialStage = true;
                        mPlayPause = false;

                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });
                mediaPlayer.prepare();
                prepared = true;
            } catch (IOException e) {
                prepared = false;
                e.printStackTrace();
            }

            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(progressDialog.isShowing()){
                progressDialog.cancel();
            }
            mediaPlayer.start();
            initialStage = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer!=null){
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

