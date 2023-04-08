package com.bskcoobe.game23gg;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Pipe extends GameObject{

    public float speed;


    public Pipe (float posX, float posY, int width, int height){
        super(posX,posY,width,height);
    }

    public void draw(Canvas canvas){


           // speed = -GameView.playerSpeed/6  * 0.95f;;



       // this.posX += speed;

        canvas.drawBitmap(this.sprite, this.posX, this.posY, null);
    }

    public void randomY(){
        Random r = new Random();

        this.posY =r.nextInt((0+this.height/4)+1)-this.height/4;

    }

    public void setSprite(Bitmap spp){
        this.sprite = Constants.getRoundedCornerBitmap(Bitmap.createScaledBitmap(spp, width, height, true), 20);
    }
}
