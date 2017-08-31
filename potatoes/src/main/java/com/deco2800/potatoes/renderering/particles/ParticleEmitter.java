package com.deco2800.potatoes.renderering.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ParticleEmitter {
    // Emitter parameters
    float originX, originY;

    // Particle types (defines the settings of the particles we create)
    List<ParticleType> particleTypes;

    // List of active particles (from the pool)
    List<Particle> particles;

    // TEST todo
    Texture texture;

    // Random gen
    Random random = new Random();

    /**awawdZ
     * Create a particle emitter with the given particleTypes TODO emitter settings
     * @param x position of the emitter
     * @param y position of the emitter
     * @param particleTypes the particleTypes to be created
     */
    public ParticleEmitter(float x, float y, ParticleType... particleTypes) {
        this.particleTypes = new ArrayList<>();
        this.particles = new ArrayList<>();
        this.originX = 300;
        this.originY = 50;

        for (ParticleType particleType : particleTypes) {
            this.particleTypes.add(particleType);
        }
        Pixmap p = new Pixmap(5, 5, Pixmap.Format.RGB888);
        texture = new Texture(p);
    }

    /**
     * Tick method for this particle emitter. Updates existing particles, creates new ones, deletes expired ones.
     *
     * Creates particles as neccessary. Particles are pooled, so we need the pool from the ParticleManager
     * @param deltaTime the tick time
     * @param particlePool the pool of particles to use (TODO expandable)
     */
    public void onTick(double deltaTime, List<Particle> particlePool) {
        Iterator<Particle> iter = particles.iterator();
        while(iter.hasNext()) {
            Particle p = iter.next();
            // Tick particles
            p.x += p.vectorX * deltaTime;
            p.y += p.vectorY * deltaTime;
            p.lifeTime -= deltaTime;

            // Delete expired
            if (p.lifeTime <= 0.0f) {
                p.alive = false;
                iter.remove();
            }
        }

        // Create new
        while (particles.size() < 1024) {
            Particle newP = null;

            // Find particle
            for (Particle p : particlePool) {
                if (!p.alive) {
                    newP = p;
                    break;
                }
            }

            // Add it
            if (newP == null) {
                throw new IllegalStateException("Ayyyyyy too many particles");
            }
            else {
                newP.alive = true;
                newP.x = originX;
                newP.y = originY;
                float factor = 8.0f;
                newP.vectorX = (random.nextFloat() * (2.0f / factor)) - (1.0f / factor); // -1.0 < x < 1.0
                newP.vectorY = (random.nextFloat() * (2.0f / factor)) - (1.0f / factor); // -1.0 < x < 1.0
                newP.lifeTime = 5.0f * 1000.0f; // 5s
                particles.add(newP);
            }
        }
    }

    public void draw(SpriteBatch batch) {
        Color prev = batch.getColor();
        batch.setColor(1.0f, 0, 0, 1.0f);

        batch.begin();
        for (Particle p : particles) {
            Color col = batch.getColor();
            float alpha = 1.0f;

            float fadeOutThreshold = (5.0f * 1000.0f) * 1.0f;
            if (p.lifeTime < fadeOutThreshold) {
                alpha = p.lifeTime / fadeOutThreshold;
            }

            batch.setColor(col.r, col.g, col.b, alpha);
            batch.draw(texture, p.x, p.y);
        }
        batch.end();

        batch.setColor(prev);
    }
}
