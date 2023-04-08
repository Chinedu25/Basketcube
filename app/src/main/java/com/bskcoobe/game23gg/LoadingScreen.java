package com.bskcoobe.game23gg;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class LoadingScreen extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

// Hide the action bar
        if (getActionBar() != null) {
            getActionBar().hide();
        }
        DisplayMetrics dm = new DisplayMetrics();

        this.getWindowManager().getDefaultDisplay().getMetrics(dm);

        Constants.SCREEN_HEIGHT = dm.heightPixels;
        Constants.SCREEN_WIDTH = dm.widthPixels;

        Constants.Ref_Height = 360;
        Constants.Ref_Width = 640;

        setContentView(R.layout.activity_loading_screen);
        ImageView iconImageView = findViewById(R.id.iconImageView);
        iconImageView.setClipToOutline(true);

// Create the rotation animation
        ObjectAnimator rotation = ObjectAnimator.ofFloat(iconImageView, "rotation", 0f, 360f);
        rotation.setDuration(3000); // 3 seconds per full rotation
        rotation.setRepeatCount(ValueAnimator.INFINITE); // Repeat indefinitely
        rotation.setInterpolator(new LinearInterpolator()); // Linear interpolation for smooth rotation

// Start the animation
        rotation.start();

        Glide.with(this)
                .load(R.drawable.background)
                .apply(bitmapTransform(new BlurTransformation(50)))
                .into((ImageView) this.findViewById(R.id.blur));


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Write whatever to want to do after delay specified (1 sec)
                Intent intent = new Intent(LoadingScreen.this, MainMenu.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

}