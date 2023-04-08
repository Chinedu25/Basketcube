package com.bskcoobe.game23gg;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class GameObject {
    protected float posX, posY;
    protected int width, height;
    protected Rect rect; //for collision detection
    protected Bitmap sprite;
    public float minX, minY, maxX, maxY;

    public  GameObject (){

    }
    public GameObject(float posX, float posY, int width, int height, Bitmap sprite) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
    }

    public GameObject(float posX, float posY, int width, int height) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;

        this.minX = posX;
        this.minY = posY;
        this.maxX = posX + width;
        this.maxY = posY + height;
    }

    public void updateMinMax(){

        this.minX = posX;
        this.minY = posY;
        this.maxX = posX + width;
        this.maxY = posY + height;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Bitmap getSprite() {
        return sprite;
    }

    public void setSprite(Bitmap sprite) {
        this.sprite = sprite;
    }

    public Rect getRect() {
        return new Rect((int)this.posX,(int)this.posY, (int)this.posX+this.width, (int)this.posY+this.height);
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }
}
