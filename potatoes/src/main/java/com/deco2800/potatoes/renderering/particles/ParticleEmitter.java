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
    // Particle types (defines the settings of the particles we create)
    List<ParticleType> particleTypes;

    // List of active particles (from the pool)
    List<Particle> particles;

    // TEST todo
    Texture texture;

    // Random gen
    Random random = new Random();

    /**
     * Create a particle emitter with the given particleTypes TODO emitter settings
     * @param x position of the emitter
     * @param y position of the emitter
     * @param particleTypes the particleTypes to be created
     */
    public ParticleEmitter(float x, float y, ParticleType... particleTypes) {
        this.particleTypes = new ArrayList<>();
        this.particles = new ArrayList<>();

        for (ParticleType particleType : particleTypes) {
            this.particleTypes.add(particleType);
        }
        Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGB888);
        p.setColor(Color.RED);
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
            p.lifeTime += deltaTime;

            // Delete expired
            if (p.lifeTime > 1.0f) {
                p.alive = false;
                iter.remove();
            }
        }

        // Create new
        while (particles.size() < 10) {
            for (Particle p : particlePool) {
                if (!p.alive) {
                    p.alive = true;
                    p.vectorX = (random.nextFloat() * 2.0f) - 1.0f; // -1.0 < x < 1.0
                    p.vectorY = (random.nextFloat() * 2.0f) - 1.0f; // -1.0 < x < 1.0
                    p.lifeTime = 0.0f;
                    particles.add(p);
                }
            }
        }
    }

    public void draw(SpriteBatch batch) {
        for (Particle p : particles) {
            batch.draw(texture, p.x, p.y);
        }
    }
}
