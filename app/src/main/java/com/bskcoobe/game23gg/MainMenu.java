package com.bskcoobe.game23gg;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class MainMenu extends AppCompatActivity {

    private Boolean playMusic = true;
    private AppManager appManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main_menu);

        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

// Hide the action bar
        if (getActionBar() != null) {
            getActionBar().hide();
        }
        Glide.with(this)
                .load(R.drawable.background)
                .apply(bitmapTransform(new BlurTransformation(50)))
                .into((ImageView) this.findViewById(R.id.blur));


        appManager = (AppManager) getApplicationContext();
        appManager.startMusic();

        ImageView playButton = findViewById(R.id.playButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Temporarily change opacity
                playButton.setColorFilter(Color.argb(150, 255, 255, 255));

                Intent intent = new Intent(MainMenu.this, MainActivity.class);
                startActivity(intent);
                finish();

                // Remove opacity filter
                playButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playButton.clearColorFilter();
                    }
                }, 200); // Delay in milliseconds to give the user visual feedback
            }
        });

        ImageView soundButton = findViewById(R.id.soundButton);

        if (!playMusic){
            soundButton.setColorFilter(Color.argb(150, 0, 0, 0));
        }

        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Temporarily change opacity


                if (playMusic){
                    appManager.stopMusic();
                    soundButton.setColorFilter(Color.argb(150, 0, 0, 0));
                    playMusic = false;
                }
                else{
                    appManager.startMusic();
                    soundButton.clearColorFilter();
                    playMusic = true;
                }

            }
        });

    }
    private boolean isMinimized = false;

    @Override
    protected void onPause() {
        super.onPause();
        if (!isMinimized) {
            // Pause the music only if the app is not minimized
            appManager.pauseMusic();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isFinishing() && !isChangingConfigurations()) {
            // Another activity is starting, so the app is not minimized
            isMinimized = false;
        } else {
            // The activity is finishing, so the app is being minimized
            isMinimized = true;
//            appManager.stopMusic();
//            appManager.releaseMusic();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!appManager.isPlayingMusic() && !isMinimized) {
            // Resume playing the music only if the app is not minimized
            appManager.startMusic();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //appManager.stopMusic();
    }

}