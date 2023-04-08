package com.bskcoobe.game23gg;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;

public class Player extends GameObject{

    private Bitmap _sprite;
    private float playerVelocityY = 0;
    private int JumpForce = 3;

    private float playerVelocityX = 0f;
    private final float FRICTION = 0.95f; // friction coefficient to slow down player in x axis
    private final float DRAG = 0.99f;

    private ParticleSystem particleSystem;
    private ArrayList<ParticleSystem> destroyPlayerParticleSystems;
    private Bitmap particleSprite;

    private Boolean exhaustedFuel;

    private float playerSpeed = 1.5f;
    private float lastScoreDistance;

    private Camera camera;


    public CollisionDetection collisionDetection;

    public void setCamera(Camera camera){
        this.camera = camera;
    }

    public Pipe closetObstacle;

    private String TAG ="Player";

    private Handler mHandler = new Handler();
    private Runnable mUpdatePowerRunnable = new Runnable() {
        @Override
        public void run() {
            UpdatePower();
            mHandler.postDelayed(this, 7000); // post the Runnable again after 7 seconds
        }
    };
    private void UpdatePower(){
        if(exhaustedFuel)
            return;
        MainActivity.modifyPowerBy(40f);
    }

    public Player() {

        collisionDetection = new CollisionDetection();
        initializeDestroyPlayerParticles();
        particleSystem = new ParticleSystem(35);
        exhaustedFuel = false;
        mHandler.postDelayed(mUpdatePowerRunnable, 3000);
    }

    private  void initializeDestroyPlayerParticles(){
        destroyPlayerParticleSystems = new ArrayList<>();

        destroyPlayerParticleSystems.add(new ParticleSystem(20));
        destroyPlayerParticleSystems.add(new ParticleSystem(20));
        destroyPlayerParticleSystems.add(new ParticleSystem(20));
        destroyPlayerParticleSystems.add(new ParticleSystem(20));

    }

    private  void renderDestroyPlayerParticles(Canvas canvas){

        for (int i = 0; i < destroyPlayerParticleSystems.size(); i++){
            destroyPlayerParticleSystems.get(i).setParticleBitmap(this.particleSprite);
            destroyPlayerParticleSystems.get(i).draw(canvas);
        }
    }
    private Bitmap getParticleSystemSprite(){
        return this.particleSprite;
    }

    public float getVelocityX(){
        return this.playerVelocityX;
    }
    public float getVelocityY(){
        return playerVelocityY;
    }

    public void setVelocityX(float value){
        playerVelocityX = value;
    }
    public void setVelocityY(float value){
        playerVelocityY = value;
    }

    public void draw(Canvas canvas){
        Update();

        particleSystem.setParticleBitmap(this.particleSprite);
       // particleSystem.emitParticles((int)this.getPosX() - (this.getWidth()/2) +2, (int)this.getPosY() + this.getHeight()/2);
        particleSystem.draw(canvas);
        renderDestroyPlayerParticles(canvas);

        if (!MainActivity.isDead)
            canvas.drawBitmap(this.getSprite(), this.posX, this.posY ,null);

    }



    private void UpdateScore(){

        if (lastScoreDistance < getPosX()){
            lastScoreDistance = getPosX();
            MainActivity.modifyScoreBy(.05f);
        }

    }

    public void explode(){
        destroyPlayerParticleSystems.get(0).emitParticles((int)this.getPosX() - (this.getWidth()/2), (int)this.getPosY() + this.getHeight()/2, 10);
        destroyPlayerParticleSystems.get(1).emitParticles((int)this.getPosX() + this.getWidth(), (int)this.getPosY() - this.getHeight()/2, -10);
        destroyPlayerParticleSystems.get(2).emitParticles((int)this.getPosX() - (this.getWidth()/2), (int)this.getPosY() - this.getHeight()/2, 10);
        destroyPlayerParticleSystems.get(3).emitParticles((int)this.getPosX() + this.getWidth(), (int)this.getPosY() + this.getHeight()/2, -10);


    }

    private void Update(){

        if (MainActivity.isDead)
            return;

        if (exhaustedFuel){

                explode();
                GameView.PlayDeadSound();
                MainActivity.setIsDead(true);

            return;
        }

        playerVelocityY += (9.8 / 8) * MainActivity.GameTime;

        if (collisionDetection.getCollisionData().collisionTime < 1.0f){

            playerVelocityY = (int)collisionDetection.getSlideY(playerVelocityX, playerVelocityY,
                    collisionDetection.getCollisionData().normalX,
                    collisionDetection.getCollisionData().normalY,
                    collisionDetection.getRemainingTime(collisionDetection.getCollisionData().collisionTime));

            playerVelocityX = collisionDetection.getSlideX(playerVelocityX, playerVelocityY,
                    collisionDetection.getCollisionData().normalX,
                    collisionDetection.getCollisionData().normalY,
                    collisionDetection.getRemainingTime(collisionDetection.getCollisionData().collisionTime));
        }

        if (GameView.isTouching){
            // Check touch input to determine horizontal movement

            if (MainActivity.GameTime == 0)
                return;

            playerVelocityY -= JumpForce * MainActivity.GameTime;


            GameView.PlayFuelBurnSound();
            if (GameView.getLastTouchX() < Constants.SCREEN_WIDTH / 2) {
                // Left half of screen touched, move left
                playerVelocityX += playerSpeed;
                UpdateScore();
                MainActivity.modifyPowerBy(-.2f);

                particleSystem.emitParticles((int)this.getPosX() - (this.getWidth()/2) +2, (int)this.getPosY() + this.getHeight()/2, 10);


            } else {


                    // Right half of screen touched, move right
                    //setPosX(getPosX() + 10);
                    playerVelocityX -= playerSpeed;;
                    MainActivity.modifyPowerBy(-.2f);
//                    GameView.playerSpeed = 0;
                    particleSystem.emitParticles((int)this.getPosX() + this.getWidth(), (int)this.getPosY() + this.getHeight()/2, -10);


            }

        }

        // limit player's horizontal velocity to a maximum of 10 units per frame

        playerVelocityX = Math.min(playerVelocityX, 10);
        playerVelocityX = Math.max(playerVelocityX, -10);



        if (closetObstacle != null){


                collisionDetection.setCollisionData(collisionDetection.MySweptAABB(this, closetObstacle,
                        getVelocityX(), getVelocityY(), closetObstacle.speed , 0));



//            if (collisionDetection.isColliding(this, closetObstacle)){
//                Log.d(TAG, "Collision happening");
//                if (getVelocityX() > 0 && collisionDetection.getCollisionData().normalY == 0){
//                    if (playerVelocityY != 0){
//                        if (getPosX() < closetObstacle.getPosX() + closetObstacle.getWidth() && getPosX() + this.getWidth() < closetObstacle.getPosX() + closetObstacle.getWidth()/2)
//                       // setPosX(closetObstacle.getPosX() - this.getWidth());
//                    }
//
//
//                    totalVelocity = 0;
//                   setVelocityX(0);
//                }
//            }
        }
//
        if (collisionDetection.getCollisionData().collisionTime < 1.0f){

            playerVelocityY = (int)collisionDetection.getSlideY(playerVelocityX, playerVelocityY,
                    collisionDetection.getCollisionData().normalX,
                    collisionDetection.getCollisionData().normalY,
                    collisionDetection.getRemainingTime(collisionDetection.getCollisionData().collisionTime));

            playerVelocityX = collisionDetection.getSlideX(playerVelocityX, playerVelocityY,
                    collisionDetection.getCollisionData().normalX,
                    collisionDetection.getCollisionData().normalY,
                    collisionDetection.getRemainingTime(collisionDetection.getCollisionData().collisionTime));
        }

            this.setPosX(getPosX() + playerVelocityX * collisionDetection.getCollisionData().collisionTime * MainActivity.GameTime);
            this.setPosY(getPosY() + playerVelocityY * collisionDetection.getCollisionData().collisionTime * MainActivity.GameTime);



        // Make sure player stays within the screen bounds
        if (getPosX() < camera.getPosX()) {
            setPosX(camera.getPosX());
            playerVelocityX = 0;
            playerVelocityX *= FRICTION;
        }



        // Make sure player stays within the screen bounds
        if (getPosY() < 0) {
            setPosY(0);
            playerVelocityY = 0;
            playerVelocityX *= FRICTION * MainActivity.GameTime;
        } else if (getPosY() > Constants.SCREEN_HEIGHT - getHeight()) {
            setPosY(Constants.SCREEN_HEIGHT - getHeight());
            playerVelocityY = 0;
                playerVelocityX *= FRICTION * MainActivity.GameTime;
        }
    }

    public void setSpr(Bitmap spp){
        if (spp == null) {
            throw new IllegalArgumentException("Sprite cannot be null");
        }
        if (spp.getWidth() <= 0 || spp.getHeight() <= 0) {
            throw new IllegalArgumentException("Invalid sprite dimensions");
        }
        this._sprite = Bitmap.createScaledBitmap(spp, this.width, this.height, true);
    }

    public void setParticleSystemSprite(Bitmap spp){
        if (spp == null) {
            throw new IllegalArgumentException("Sprite cannot be null");
        }
        if (spp.getWidth() <= 0 || spp.getHeight() <= 0) {
            throw new IllegalArgumentException("Invalid sprite dimensions");
        }
        this.particleSprite = Bitmap.createScaledBitmap(spp, this.width/3, this.height/3, true);
        particleSystem.setParticleBitmap(this.particleSprite);
    }

    @Override
    public Bitmap getSprite() {
        return Constants.getRoundedCornerBitmap(this._sprite, 20);
    }
}
