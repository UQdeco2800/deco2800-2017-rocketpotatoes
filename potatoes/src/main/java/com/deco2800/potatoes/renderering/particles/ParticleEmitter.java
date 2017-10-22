package com.deco2800.potatoes.renderering.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.potatoes.renderering.particles.types.ParticleType;

import java.util.ArrayList;
import java.util.List;

public class ParticleEmitter {
    // Emitter parameters
    private float originX, originY;

    // Particle types (defines the settings of the particles we create)
    private List<ParticleType> particleTypes;

    // Is this emitter should produce particles
    private boolean active;

    // If this emitter has any existing particles
    private boolean hasParticles;

    /**
     * Create a particle emitter with the given particleTypes 
     * @param x position of the emitter
     * @param y position of the emitter
     * @param particleTypes the particleTypes to be created
     */
    public ParticleEmitter(float x, float y, ParticleType... particleTypes) {
        this.particleTypes = new ArrayList<>();
        this.originX = x;
        this.originY = y;
        this.active = true;

        for (ParticleType particleType : particleTypes) {

            // If our type has no texture. Assume we want a square
            if (particleType.texture == null) {
                Pixmap p = new Pixmap(particleType.getSizeX(), particleType.getSizeY(), Pixmap.Format.RGB888);
                p.setColor(particleType.getColor());
                // Fill box with pixels
                for (int pX = 0; pX < particleType.getSizeX(); ++pX) {
                    for (int pY = 0; pY < particleType.getSizeY(); ++pY) {
                        p.drawPixel(pX, pY);
                    }
                }
                Texture t = new Texture(p);

                particleType.texture = t;
            }

            this.particleTypes.add(particleType);
        }
    }

    /**
     * Tick method for this particle emitter. Updates existing particles, creates new ones, deletes expired ones.
     *
     * Creates particles as neccessary. Particles are pooled, so we need the pool from the ParticleManager
     * @param deltaTime the tick time
     * @param particlePool the pool of particles to use 
     */
    public void onTick(double deltaTime, List<Particle> particlePool) {
        // Tick particles
        for (ParticleType particleType : particleTypes) {
            particleType.onTick(deltaTime, particlePool, originX, originY, active);
        }

        // Check if any particles exist
        hasParticles = false;
        for (ParticleType particleType : particleTypes) {
            if (particleType.particles.size() != 0) {
                hasParticles = true;
                break;
            }

        }

    }

    /**
     * Draw's all the particles for this emitter. Include fadeout and other transition effects
     * @param batch batch to draw with (must be activated!)
     */
    public void draw(SpriteBatch batch) {
        Color prev = batch.getColor();
        for (ParticleType particleType : particleTypes) {
            particleType.draw(batch);
            batch.setColor(prev);
        }

    }

    public List<ParticleType> getParticleTypes() {
        return particleTypes;
    }

    /**
     * Set's the origin of particles for this emitter
     * @param x x coord
     * @param y y coord
     */
    public void setOrigin(float x, float y) {
        originX = x;
        originY = y;
    }

    /**
     * Gracefully stops this emitter (it stops producing more particles)
     * When no particles exist the hasParticles flag will be set to false
     */
    public void stop() {
        active = false;
    }

    /**
     * Start's the emitter if it hasn't started (emitters default to started)
     */
    public void start() {
        active = true;
    }

    /**
     * @return if this emitter is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @return if this emitter has particles
     */
    public boolean hasParticles() {
        return hasParticles;
    }

    public void forceStop() {
        for (ParticleType particleType : particleTypes) {
            particleType.cleanup();
        }
        active = false;
        hasParticles = false;
    }
}
