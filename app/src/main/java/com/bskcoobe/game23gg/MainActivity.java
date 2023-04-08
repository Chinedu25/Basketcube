package com.bskcoobe.game23gg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;

import android.util.Log;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import android.view.WindowManager;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {

    private AppManager appManager;

    private static OnDeadListener onDeadListener;

    public interface OnDeadListener {
        void onDead(boolean isDead);
    }
    public static float power = 0;
    public static float score = 0;

    public static CustomTextView powerText;
    public static CustomTextView scoreText;
    public static CustomTextView gameOverScoreText;
    public static CustomTextView gameOverHighScoreText;

    public static Boolean isDead;

    public static int GameTime  = 1;

    ConstraintLayout pauseLayout;
    ConstraintLayout gameOverLayout;
    View realTimeBlur;

    SharedPreferences sharedPreferences;

    private MediaPlayer mediaPlayer;


    public void setOnDeadListener(OnDeadListener listener) {
        this.onDeadListener = listener;
    }

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
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        powerText = findViewById(R.id.Fuel);
        scoreText = findViewById(R.id.Score);
        gameOverScoreText = findViewById(R.id.GameOverScore);
        gameOverHighScoreText = findViewById(R.id.HighScore);

       pauseLayout = findViewById(R.id.pauseLayout);
        pauseLayout.setVisibility(View.GONE);

        gameOverLayout = findViewById(R.id.gameOverLayout);
        gameOverLayout.setVisibility(View.GONE);

       realTimeBlur = findViewById(R.id.gameOverBlurImage1);
        realTimeBlur.setVisibility(View.GONE);

        setPower(100);
        setScore(0);
        GameTime = 1;
        isDead = false;

        appManager = (AppManager) getApplicationContext();
        appManager.startMusic();

        setOnDeadListener(new OnDeadListener() {
            @Override
            public void onDead(boolean isDead) {
                if (isDead) {


                    int highScore = sharedPreferences.getInt("highScore", 0);

// Compare the current score with the stored high score
                    if (score > highScore) {
                        // Update the high score in SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("highScore", (int)score);
                        editor.apply();

                        gameOverHighScoreText.setText("HIGH SCORE: " + (int)score);
                    }
                    else{
                        gameOverHighScoreText.setText("HIGH SCORE: " + highScore);

                    }

                    // Do something when isDead is true
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Call your function here

                            gameOverLayout.setVisibility(View.VISIBLE);
                            realTimeBlur.setVisibility(View.VISIBLE);
                        }
                    }, 1000);

                }
            }
        });

        UpdateUI();

        ImageView pauseButton = findViewById(R.id.pauseButton);

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Temporarily change opacity
                pauseButton.setColorFilter(Color.argb(150, 255, 255, 255));

//action here
                pause();

                realTimeBlur.setVisibility(View.VISIBLE);
                pauseLayout.setVisibility(View.VISIBLE);




                // Remove opacity filter
                pauseButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pauseButton.clearColorFilter();
                    }
                }, 200); // Delay in milliseconds to give the user visual feedback
            }
        });

       ImageView backButton = findViewById(R.id.backButton);

       backButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               GameTime  = 1;
               pauseLayout.setVisibility(View.GONE);
               realTimeBlur.setVisibility(View.GONE);
           }
       });


        ImageView restartButton = findViewById(R.id.restartButton);

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Temporarily change opacity
                restartButton.setColorFilter(Color.argb(150, 255, 255, 255));
                recreate();
                // Remove opacity filter
                restartButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       restartButton.clearColorFilter();
                    }
                }, 200);
            }
        });


        ImageView homeButton = findViewById(R.id.homeButton);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Temporarily change opacity
                homeButton.setColorFilter(Color.argb(150, 255, 255, 255));

                Intent intent = new Intent(MainActivity.this, MainMenu.class);
                startActivity(intent);
                finish();

                // Remove opacity filter
                homeButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        homeButton.clearColorFilter();
                    }
                }, 200); // Delay in milliseconds to give the user visual feedback
            }
        });

        ImageView gameOverRestartButton = findViewById(R.id.gameOverRestartButton);

        gameOverRestartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Temporarily change opacity
                gameOverRestartButton.setColorFilter(Color.argb(150, 255, 255, 255));
                recreate();
                // Remove opacity filter
                gameOverRestartButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gameOverRestartButton.clearColorFilter();
                    }
                }, 200);
            }
        });


        ImageView gameOverHomeButton = findViewById(R.id.gameOverHomeButton);

        gameOverHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Temporarily change opacity
                gameOverHomeButton.setColorFilter(Color.argb(150, 255, 255, 255));

                Intent intent = new Intent(MainActivity.this, MainMenu.class);
                startActivity(intent);
                finish();

                // Remove opacity filter
                gameOverHomeButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gameOverHomeButton.clearColorFilter();
                    }
                }, 200); // Delay in milliseconds to give the user visual feedback
            }
        });



    }



    public static void setPower(float value){
        power = value;
        power = Math.max(power, 0);
        power = Math.min(power, 100);
UpdateUI();
    }

    public static void pause() {
        MainActivity.GameTime = 0;
    }
    public static void setScore(float value){
        score = value;
        score = Math.max(score, 0);
        UpdateUI();
    }


    public static void modifyScoreBy(float value){
        float s = getScore() + value;
        setScore(s);
    }

    public static void modifyPowerBy(float value){
        if (MainActivity.GameTime == 0)
            return;
        float p = getPower() + value;
        setPower(p);
    }

    public static float getPower(){
        return power;
    }
    public static float getScore(){
        return score;
    }

    public static void UpdateUI(){

        powerText.setText("POWER: " + (int)getPower());
        scoreText.setText("SCORE: "+ (int)getScore());
        gameOverScoreText.setText("SCORE: " + (int)getScore());

    }

    public static void setIsDead(boolean value){
        isDead = value;
        if (onDeadListener != null) {
            onDeadListener.onDead(isDead);
        }
        UpdateUI();
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
        appManager.startMusic();
    }
}