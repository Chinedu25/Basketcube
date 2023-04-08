package com.bskcoobe.game23gg;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Spike extends GameObject{
    public float speed;


    public Spike (float posX, float posY, int width, int height){
        super(posX,posY,width,height);
    }

    public void draw(Canvas canvas){

//        if (GameView.worldMove){
//            speed = -10;
//        }
//        else{
//            speed = -GameView.playerSpeed/2  * 0.95f;;
//        }
//
//
//        this.posX += speed;

        canvas.drawBitmap(this.sprite, this.posX, this.posY, null);
    }

    public void randomY(){
        Random r = new Random();

        this.posY =r.nextInt((0+this.height/3)+1)-this.height/3;

    }

    public void setSprite(Bitmap spp){
        this.sprite = Bitmap.createScaledBitmap(spp, width, height, true);
    }
}
