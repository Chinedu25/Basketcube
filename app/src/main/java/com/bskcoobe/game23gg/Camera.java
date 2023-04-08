package com.bskcoobe.game23gg;

import android.graphics.Canvas;

public class Camera extends GameObject {
    private float offsetX, offsetY;
    private GameObject target;
    private int screenWidth, screenHeight;

    private int minX;

    public Camera(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void follow(GameObject target) {
        this.target = target;
    }

    public void update() {
        if (target != null) {
            offsetX = target.getPosX() - screenWidth / 2;

            if (offsetX < minX)
                offsetX = minX;

            offsetY = target.getPosY() - screenHeight / 2;
        }
    }

    public void draw(Canvas canvas) {

        this.setPosX(offsetX);
        canvas.translate(-offsetX, 0);
    }

    public void updateMinX(){
        minX = (int)this.getPosX();
    }
}