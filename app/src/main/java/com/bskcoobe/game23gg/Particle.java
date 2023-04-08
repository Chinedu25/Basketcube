package com.bskcoobe.game23gg;

public class Particle {
    private int posX;
    private int posY;
    private int velocityX;
    private int velocityY;
    private int life;
    private boolean alive;

    public Particle() {
        alive = false;
    }

    public void activate(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.velocityX = (int) (Math.random() * 10 + 5);
        this.velocityY = (int) (Math.random() * -20 - 5);
        this.life = (int) (Math.random() * 20 + 30);
        this.alive = true;
    }

    public void activate(int posX, int posY, int xForce) {
        this.posX = posX;
        this.posY = posY;
        this.velocityX = (int) (Math.random() * 10 + xForce);
        this.velocityY = (int) (Math.random() * -20 - 5);
        this.life = (int) (Math.random() * 20 + 30);
        this.alive = true;
    }

    public void update() {

        posX -= velocityX * MainActivity.GameTime;
        posY -= velocityY * MainActivity.GameTime;

        if (MainActivity.GameTime == 0 )
            return;

        life--;
        if (life <= 0) {
            alive = false;
        }
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public boolean isAlive() {
        return alive;
    }
}
