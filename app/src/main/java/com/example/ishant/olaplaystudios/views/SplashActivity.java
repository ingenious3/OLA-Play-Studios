package com.example.ishant.olaplaystudios.views;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.ishant.olaplaystudios.R;

public class SplashActivity extends AppCompatActivity {

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        image = (ImageView)findViewById(R.id.app_logo);
        Thread background = new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void run() {
                try {
                    // Thread will sleep for 2 seconds
                    sleep(1 * 1200);
                    Intent intent = new Intent(SplashActivity.this,OlaPlayActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {

                }
            }
        };
        background.start();

    }
}
