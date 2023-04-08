package com.bskcoobe.game23gg;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class HotLava extends GameObject{

    public HotLava (float posX, float posY, int width, int height){
        super(posX,posY,width,height);
    }

    public void draw(Canvas canvas){

        canvas.drawBitmap(this.sprite, this.posX, this.posY, null);
    }


    public void setSprite(Bitmap spp){
        this.sprite = Bitmap.createScaledBitmap(spp, width, height, true);
    }
}
