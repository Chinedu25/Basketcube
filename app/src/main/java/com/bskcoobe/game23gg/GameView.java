package com.bskcoobe.game23gg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class GameView extends View {

    private Handler handler;
    private Runnable r;
    private Player player;

    private static float lastTouchX = -1;
    public static boolean isTouching;

    public static boolean worldMove = false;
    public static float playerSpeed;

    public ArrayList<Pipe> pipeArrayList;
    public ArrayList<HotLava> hotLavaArrayList;

    public ArrayList<Spike> threeSpikeArrayList;

    private ArrayList<GameBackground> background;
    private int sumPipes, distance;

    private int pipeWidth = 50, pipeHeight= 180;
    private int playerWidth = 58, playerHeight= 58;

    private String TAG = "GameView";

    public static int soundExplode;
    public static int soundFuel;
    private float volume;
    private boolean loadedSound;
    public static SoundPool soundPool;

    Camera camera;

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initializeBackground();
        initializePlayer();
        initializeSpike();
        initializePipe();
        initializeHotLava();
        initializeCamera();

        player.setCamera(camera);

        Update();

        initializeSound();
    }

    private void initializeCamera(){
        camera = new Camera(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        camera.follow(player);
    }

    public static  void PlayDeadSound(){
        int streamID = soundPool.play(soundExplode, (float) 0.5, (float) 0.5, 1, 0, 1f );
    }

    public static void PlayFuelBurnSound(){
        int streamID = soundPool.play(soundFuel, (float) 0.25, (float) 0.25, 1, 0, 0.5f );
    }

    private void initializeSpike(){
        threeSpikeArrayList = new ArrayList<>();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.spike1);

        for (int i = 0; i < 6; i++){
            threeSpikeArrayList.add(new Spike(0, 0,
                    44 * Constants.GetWidthRatio(), 38 * Constants.GetHeightRatio()
            ));
            threeSpikeArrayList.get(threeSpikeArrayList.size()-1).setSprite(
                    bitmap
            );
        }
    }

    private void initializeSound(){

        if (Build.VERSION.SDK_INT >= 21){
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setAudioAttributes(audioAttributes).setMaxStreams(5);
            this.soundPool = builder.build();
        }
        else{
            this.soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loadedSound = true;
            }
        });

        soundExplode = this.soundPool.load(this.getContext(), R.raw.dead_sound, 1);
        soundFuel = this.soundPool.load(this.getContext(), R.raw.fuel_burn, 1);
    }

    private void initializeBackground(){
        background = new ArrayList<>();
        for (int i =0; i < 2; i++){
            this.background.add(new GameBackground(
                     (i * Constants.SCREEN_WIDTH - 50 * Constants.GetWidthRatio()),
                    0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));

        if (i % 2 != 0){
            this.background.get(this.background.size()-1).setSprite(Constants.createFlippedBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.background), true, false));
        }
        else{
            this.background.get(this.background.size()-1).setSprite(BitmapFactory.decodeResource(this.getResources(), R.drawable.background));
        }
        }


    }

    private void GenerateUpPipe(){

    }

    private void initializeHotLava(){

        float hotlavaHeight = 20 * Constants.GetHeightRatio();
        hotLavaArrayList = new ArrayList<>();

        this.hotLavaArrayList.add(new HotLava(0, 0, Constants.SCREEN_WIDTH + Constants.SCREEN_WIDTH/2, (int)hotlavaHeight));
        this.hotLavaArrayList.add(new HotLava(0, Constants.SCREEN_HEIGHT - hotlavaHeight, Constants.SCREEN_WIDTH + Constants.SCREEN_WIDTH/2, (int)hotlavaHeight));

        // Load the bitmap from a resource
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hot_lava);

// Create a new flipped bitmap
        Bitmap flippedSprite = Constants.createFlippedBitmap(bitmap, false, true);

        hotLavaArrayList.get(0).setSprite(flippedSprite);
        hotLavaArrayList.get(1).setSprite(bitmap);

    }



    private void initializePipe() {
        sumPipes = 6;
        distance = 10 * Constants.GetHeightRatio();
    pipeArrayList = new ArrayList<>();


for (int i = 0; i < sumPipes; i++){
    int pipeX = Constants.SCREEN_WIDTH + (i * 200 * Constants.SCREEN_WIDTH / 1080) * 3;
    Random random = new Random();
    int yRandPos = random.nextInt(Constants.SCREEN_HEIGHT/2);
    this.pipeArrayList.add(new Pipe(pipeX, 0
            , pipeWidth * Constants.GetWidthRatio(),pipeHeight * Constants.GetHeightRatio() ));
    this.pipeArrayList.get(this.pipeArrayList.size()-1).setSprite(BitmapFactory.decodeResource(this.getResources(), R.drawable.pipe_spp));
    this.pipeArrayList.get(this.pipeArrayList.size()-1).randomY();
    this.pipeArrayList.get(this.pipeArrayList.size()-1).setPosY(this.pipeArrayList.get(this.pipeArrayList.size()-1).getPosY() + yRandPos);
    }


}


    private void initializePlayer(){
        player = new Player();

        player.setWidth(playerWidth * Constants.GetHeightRatio());
        player.setHeight(playerHeight * Constants.GetHeightRatio());
        player.setPosX(115 * Constants.SCREEN_WIDTH / 1080);
        Log.d("Consts", "GameView: " + Constants.SCREEN_HEIGHT);
        player.setPosY(Constants.SCREEN_HEIGHT / 2 + player.getHeight() / 2);

        player.setSpr(BitmapFactory.decodeResource(this.getResources(), R.drawable.squarebasball));
        player.setParticleSystemSprite(BitmapFactory.decodeResource(this.getResources(), R.drawable.particle_spp));
    }


    private void Update() {
        handler = new Handler();

        r = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };


    }

    public void draw(Canvas canvas) {
        super.draw(canvas);


        // Translate the canvas to center the player
//        canvas.translate(-player.getPosX() + Constants.SCREEN_WIDTH / 4 , 0);

        if (player.getVelocityX() >= 0)
            camera.update();
        else{
            camera.updateMinX();
        }
        camera.draw(canvas);
        renderBackground(canvas);
        player.draw(canvas);


        renderPipes(canvas);

        renderSpikes(canvas);

        if (MainActivity.getScore() > 20)
            renderLava(canvas);



        // Translate canvas to camera position




        handler.postDelayed(r, 10);
    }




    private void renderLava(Canvas canvas) {
        for (int i = 0; i < hotLavaArrayList.size(); i++) {
            Rect lavaRect = hotLavaArrayList.get(i).getRect();
            if (player.getRect().intersect(lavaRect)) {
                killPlayer();
            }

            hotLavaArrayList.get(i).setPosX(camera.posX);
            hotLavaArrayList.get(i).draw(canvas);
        }
    }

    private void killPlayer(){
        if (!MainActivity.isDead) {
            player.explode();
            PlayDeadSound();
            MainActivity.setIsDead(true);
        }
    }

    private int getLastBackgroundPos(){
        float backgroundPos = 0;
        for (int i =0; i< background.size(); i++){
            if (backgroundPos < background.get(i).getPosX()){
                backgroundPos = background.get(i).getPosX();
            }
        }

        return (int) backgroundPos;
    }

    private  void renderBackground(Canvas canvas){

        for (int i =0; i < background.size(); i++){
            if (this.background.get(i).getPosX() < camera.getPosX() - this.background.get(i).getWidth()){
                this.background.get(i).setPosX(getLastBackgroundPos() +this.background.get(i).getWidth() );
            }
            this.background.get(i).draw(canvas);
        }

    }

    private int getLastPipePos(){
        float lastPipePos = 0;
        for (int i =0; i< pipeArrayList.size(); i++){
            if (lastPipePos < pipeArrayList.get(i).getPosX()){
                lastPipePos = pipeArrayList.get(i).getPosX();
            }
        }

        return (int) lastPipePos;
    }

    private void renderPipes(Canvas canvas){

        if (this.pipeArrayList.size() < 0)
            return;

        float closet = Float.POSITIVE_INFINITY;

        for (int i =0; i < sumPipes; i++){

            Random random = new Random();
            int randomNum = random.nextInt(51) + 200;

            int pipeX = getLastPipePos() + (randomNum * Constants.GetWidthRatio());

            if (this.pipeArrayList.get(i).getPosX() < camera.getPosX()-pipeArrayList.get(i).getWidth()){
                this.pipeArrayList.get(i).setPosX( pipeX);
                //this.pipeArrayList.get(i).setPosX(this.pipeArrayList.get(i).getPosX() + 100);

                Random randY = new Random();
                int randomValueY = randY.nextInt(4);

                int randYPos = randY.nextInt(1) + Constants.SCREEN_HEIGHT;

                if (randomValueY == 0){
                    pipeArrayList.get(i).randomY();
                }
                else if (randomValueY == 1){
                    pipeArrayList.get(i).randomY();
                    pipeArrayList.get(i).setPosY(pipeArrayList.get(i).getHeight() + this.distance);
                }
                else if (randomValueY == 2){

                    pipeArrayList.get(i).setPosY(pipeArrayList.get(i).getHeight()/2);
                }

                else if (randomValueY == 3){

                    pipeArrayList.get(i).setPosY(randYPos);
                }
            }

            float checkCloset =  Math.abs(pipeArrayList.get(i).getPosX()- (player.getPosX()+player.getVelocityX()));
            if ( checkCloset < closet){
                closet = checkCloset;
                player.closetObstacle = pipeArrayList.get(i);
            }
            this.pipeArrayList.get(i).draw(canvas);
        }
    }
    Pipe pipeToSpawn;
    Pipe pipeToSpawn2;

    int spikePosition1;
    int spikePosition2;


    int spikePosY1;
    int spikePosY2;

    private void generateFirstThreeSpikes(){
        //spawn first 3 spikes
        Pipe temp = null;
        if (  pipeToSpawn == null || this.threeSpikeArrayList.get(0).getPosX() <  camera.getPosX()) {

            temp = pipeToSpawn;
            pipeToSpawn = null;
            for (int i = 0; i < this.pipeArrayList.size(); i++) {

                if (this.pipeArrayList.get(i).getPosX() > camera.getPosX() + Constants.SCREEN_WIDTH && pipeToSpawn == null) {
                    if (pipeToSpawn2 == null){
                        pipeToSpawn = pipeArrayList.get(i);
                    }
                    else{
                        if (pipeToSpawn2 != pipeArrayList.get(i)){
                            pipeToSpawn = pipeArrayList.get(i);
                        }
                    }

                }
            }

            if (pipeToSpawn ==null){
                pipeToSpawn = temp;
            }

            Random random = new Random();
            spikePosition1 = random.nextInt(2);
            spikePosY1 = random.nextInt(2);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.spike1);


            if (spikePosition1 == 0){

                for (int i = 0; i < threeSpikeArrayList.size()/2; i++){
                    this.threeSpikeArrayList.get(i).setSprite(bitmap);
                }
            }
            else {
                for (int i = 0; i < threeSpikeArrayList.size()/2; i++){
                    this.threeSpikeArrayList.get(i).setSprite(Constants.createFlippedBitmap(bitmap, true, false));
                }
            }
        }

        if (spikePosY1 == 0 ){
            if (spikePosition1 == 0 ){
                threeSpikeArrayList.get(0).setPosX(pipeToSpawn.getPosX() +
                        pipeToSpawn.getWidth() - 5);
                threeSpikeArrayList.get(0).setPosY(pipeToSpawn.getPosY() + pipeToSpawn.getHeight()
                        - threeSpikeArrayList.get(0).getHeight());

                for (int i = 1; i < threeSpikeArrayList.size()/2; i++) {

                    threeSpikeArrayList.get(i).setPosX(pipeToSpawn.getPosX() +
                            pipeToSpawn.getWidth() - 5);
                    threeSpikeArrayList.get(i).setPosY(threeSpikeArrayList.get(i - 1).posY
                            - threeSpikeArrayList.get(i).getHeight());
                }
            }
            else{

                threeSpikeArrayList.get(0).setPosX(pipeToSpawn.getPosX() -
                        threeSpikeArrayList.get(0).getWidth() + 5);
                threeSpikeArrayList.get(0).setPosY(pipeToSpawn.getPosY() + pipeToSpawn.getHeight()
                        - threeSpikeArrayList.get(0).getHeight());

                for (int i = 1; i < threeSpikeArrayList.size()/2; i++) {

                    threeSpikeArrayList.get(i).setPosX(pipeToSpawn.getPosX() -
                            threeSpikeArrayList.get(0).getWidth() + 5);
                    threeSpikeArrayList.get(i).setPosY(threeSpikeArrayList.get(i - 1).posY
                            - threeSpikeArrayList.get(i).getHeight());
                }
            }

        }
        else{
            if (spikePosition1 == 0 ){
                threeSpikeArrayList.get(0).setPosX(pipeToSpawn.getPosX() +
                        pipeToSpawn.getWidth() - 5);
                threeSpikeArrayList.get(0).setPosY(pipeToSpawn.getPosY() + threeSpikeArrayList.get(0).getHeight()/2);

                for (int i = 1; i < threeSpikeArrayList.size()/2; i++) {

                    threeSpikeArrayList.get(i).setPosX(pipeToSpawn.getPosX() +
                            pipeToSpawn.getWidth() - 5);
                    threeSpikeArrayList.get(i).setPosY(threeSpikeArrayList.get(i - 1).posY
                            + threeSpikeArrayList.get(i).getHeight());
                }
            }
            else{

                threeSpikeArrayList.get(0).setPosX(pipeToSpawn.getPosX() -
                        threeSpikeArrayList.get(0).getWidth() + 5);
                threeSpikeArrayList.get(0).setPosY(pipeToSpawn.getPosY() + threeSpikeArrayList.get(0).getHeight()/2);

                for (int i = 1; i < threeSpikeArrayList.size()/2; i++) {

                    threeSpikeArrayList.get(i).setPosX(pipeToSpawn.getPosX() -
                            threeSpikeArrayList.get(0).getWidth() + 5);
                    threeSpikeArrayList.get(i).setPosY(threeSpikeArrayList.get(i - 1).posY
                            + threeSpikeArrayList.get(i).getHeight());
                }
            }

        }



    }
    private void generateSecondThreeSpikes(){
        //spawn second 3spikes
        Pipe temp2 = null;

        Random random = new Random();
        int randPipeSpike = random.nextInt(3);
        if (  pipeToSpawn2 == null || this.threeSpikeArrayList.get(3).getPosX() <  camera.getPosX()) {

            temp2 = pipeToSpawn2;
            pipeToSpawn2 = null;
            for (int i = 0; i < this.pipeArrayList.size(); i++) {

                if (this.pipeArrayList.get(i).getPosX() > Constants.SCREEN_WIDTH + camera.getPosX() && pipeToSpawn2 == null) {

                    if (pipeToSpawn == null){
                        if (i + randPipeSpike < this.pipeArrayList.size()){
                            pipeToSpawn2 = pipeArrayList.get(i + randPipeSpike);
                        }
                        else{
                            pipeToSpawn2 = pipeArrayList.get(i);
                        }
                    }
                    else{
                        if (pipeToSpawn != pipeArrayList.get(i)){
                            if (i + randPipeSpike < this.pipeArrayList.size()){
                                pipeToSpawn2 = pipeArrayList.get(i + randPipeSpike);
                            }
                            else{
                                pipeToSpawn2 = pipeArrayList.get(i);
                            }
                        }
                    }

                }
            }

            if (pipeToSpawn2 ==null){
                pipeToSpawn2 = temp2;
            }

            spikePosition2 = random.nextInt(2);
            spikePosY2 = random.nextInt(2);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.spike1);


            if (spikePosition2 == 0){

                for (int i = 3; i < threeSpikeArrayList.size(); i++){
                    this.threeSpikeArrayList.get(i).setSprite(bitmap);
                }
            }
            else {
                for (int i = 3; i < threeSpikeArrayList.size(); i++){
                    this.threeSpikeArrayList.get(i).setSprite(Constants.createFlippedBitmap(bitmap, true, false));
                }
            }
        }

        if (spikePosY2 == 0 ){
            if (spikePosition2 == 0 ){
                threeSpikeArrayList.get(3).setPosX(pipeToSpawn2.getPosX() +
                        pipeToSpawn2.getWidth() - 5);
                threeSpikeArrayList.get(3).setPosY(pipeToSpawn2.getPosY() + pipeToSpawn2.getHeight()
                        - threeSpikeArrayList.get(3).getHeight());

                for (int i = 4; i < threeSpikeArrayList.size(); i++) {

                    threeSpikeArrayList.get(i).setPosX(pipeToSpawn2.getPosX() +
                            pipeToSpawn2.getWidth() - 5);
                    threeSpikeArrayList.get(i).setPosY(threeSpikeArrayList.get(i - 1).posY
                            - threeSpikeArrayList.get(i).getHeight());
                }
            }
            else{

                threeSpikeArrayList.get(3).setPosX(pipeToSpawn2.getPosX() -
                        threeSpikeArrayList.get(3).getWidth() + 5);
                threeSpikeArrayList.get(3).setPosY(pipeToSpawn2.getPosY() + pipeToSpawn2.getHeight()
                        - threeSpikeArrayList.get(3).getHeight());

                for (int i = 4; i < threeSpikeArrayList.size(); i++) {

                    threeSpikeArrayList.get(i).setPosX(pipeToSpawn2.getPosX() -
                            threeSpikeArrayList.get(3).getWidth() + 5);
                    threeSpikeArrayList.get(i).setPosY(threeSpikeArrayList.get(i - 1).posY
                            - threeSpikeArrayList.get(i).getHeight());
                }
            }

        }
        else{
            if (spikePosition2 == 0 ){
                threeSpikeArrayList.get(3).setPosX(pipeToSpawn2.getPosX() +
                        pipeToSpawn2.getWidth() - 5);
                threeSpikeArrayList.get(3).setPosY(pipeToSpawn2.getPosY() + threeSpikeArrayList.get(3).getHeight()/2);

                for (int i = 4; i < threeSpikeArrayList.size(); i++) {

                    threeSpikeArrayList.get(i).setPosX(pipeToSpawn2.getPosX() +
                            pipeToSpawn2.getWidth() - 5);
                    threeSpikeArrayList.get(i).setPosY(threeSpikeArrayList.get(i - 1).posY
                            + threeSpikeArrayList.get(i).getHeight());
                }
            }
            else{

                threeSpikeArrayList.get(3).setPosX(pipeToSpawn2.getPosX() -
                        threeSpikeArrayList.get(3).getWidth() + 5);
                threeSpikeArrayList.get(3).setPosY(pipeToSpawn2.getPosY() + threeSpikeArrayList.get(3).getHeight()/2);

                for (int i = 4; i < threeSpikeArrayList.size(); i++) {

                    threeSpikeArrayList.get(i).setPosX(pipeToSpawn2.getPosX() -
                            threeSpikeArrayList.get(3).getWidth() + 5);
                    threeSpikeArrayList.get(i).setPosY(threeSpikeArrayList.get(i - 1).posY
                            + threeSpikeArrayList.get(i).getHeight());
                }
            }

        }

    }

    private void renderSpikes(Canvas canvas){


        generateFirstThreeSpikes();
        generateSecondThreeSpikes();


        //render spikes
        for (int i = 0; i < threeSpikeArrayList.size(); i++){

            if (player.getRect().intersect(threeSpikeArrayList.get(i).getRect())){
                killPlayer();
            }
            this.threeSpikeArrayList.get(i).draw(canvas);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isTouching = true;
            case MotionEvent.ACTION_POINTER_DOWN:
                int pointerIndex = event.getActionIndex();
                float touchX = event.getX(pointerIndex);

                // Store the position of the last touch
                lastTouchX = touchX;
                break;
            case MotionEvent.ACTION_UP:
                isTouching = false;
        }

        return true;
    }

    // Getter for the last touch position
    public static float getLastTouchX() {
        return lastTouchX;
    }
}
