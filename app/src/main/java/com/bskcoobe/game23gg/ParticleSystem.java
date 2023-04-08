package com.bskcoobe.game23gg;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ParticleSystem {
    private ArrayList<Particle> particles;

    public Bitmap getParticleBitmap() {
        return particleBitmap;
    }

    public void setParticleBitmap(Bitmap particleBitmap) {
        this.particleBitmap = particleBitmap;
    }

    private Bitmap particleBitmap;

    public ParticleSystem(int particleCount){
        particles = new ArrayList<>();
        // Create particles
        for (int i = 0; i < particleCount; i++) {
            particles.add(new Particle());
        }
    }
    public ParticleSystem(int particleCount, Bitmap particleBitmap) {
        particles = new ArrayList<>();
        this.particleBitmap = particleBitmap;

        // Create particles
        for (int i = 0; i < particleCount; i++) {
            particles.add(new Particle());
        }
    }

    public void emitParticles(int posX, int posY) {
        // Activate particles

        for (Particle particle : particles) {
            if (!particle.isAlive()) {
                //Log.d("Particle", "emitParticles: particle");
                particle.activate(posX, posY);
            }
        }
    }

    public void emitParticles(int posX, int posY, int xForce) {
        // Activate particles

        for (Particle particle : particles) {
            if (!particle.isAlive()) {
                //Log.d("Particle", "emitParticles: particle");
                particle.activate(posX, posY, xForce);
            }
        }
    }

    public void update() {
        // Update particles
        for (Particle particle : particles) {
            if (particle.isAlive()) {
                particle.update();
            }
        }
    }

    public void draw(Canvas canvas) {

        update();
        // Draw particles
        for (Particle particle : particles) {
            if (particle.isAlive()) {
                canvas.drawBitmap(particleBitmap, particle.getPosX(), particle.getPosY(), null);
            }
        }
    }
}
