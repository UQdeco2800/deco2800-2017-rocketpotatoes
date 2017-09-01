package com.deco2800.potatoes.renderering.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.DoubleStream;

public class ParticleEmitter {
    // Emitter parameters
    private float originX, originY;

    // Particle types (defines the settings of the particles we create)
    private List<ParticleType> particleTypes;

    // Random gen
    private Random random = new Random();

    /**
     * Create a particle emitter with the given particleTypes TODO emitter settings
     * @param x position of the emitter
     * @param y position of the emitter
     * @param particleTypes the particleTypes to be created
     */
    public ParticleEmitter(float x, float y, ParticleType... particleTypes) {
        this.particleTypes = new ArrayList<>();
        this.originX = 300;
        this.originY = 50;

        for (ParticleType particleType : particleTypes) {

            // If our type has no texture. Assume we want a square
            if (particleType.texture == null) {
                Pixmap p = new Pixmap(5, 5, Pixmap.Format.RGB888);
                p.setColor(particleType.color);
                // Fill box with pixels
                for (int pX = 0; pX < 5; ++pX) {
                    for (int pY = 0; pY < 5; ++pY) {
                        p.drawPixel(pX, pY);
                    }
                }
                Texture t = new Texture(p);

                particleType.texture = t;
            }

            this.particleTypes.add(particleType);
        }

        // TODO hack

    }

    /**
     * Tick method for this particle emitter. Updates existing particles, creates new ones, deletes expired ones.
     *
     * Creates particles as neccessary. Particles are pooled, so we need the pool from the ParticleManager
     * @param deltaTime the tick time
     * @param particlePool the pool of particles to use (TODO expandable)
     */
    public void onTick(double deltaTime, List<Particle> particlePool) {
        for (ParticleType particleType : particleTypes) {

            Iterator<Particle> iter = particleType.particles.iterator();
            while (iter.hasNext()) {
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
        }

        // Create new
        for (ParticleType particleType : particleTypes) {
            while (particleType.particles.size() < particleType.number) {
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

                    float factor = (random.nextFloat() * 2.0f - 1.0f);
                    float direction = random.nextFloat() * 360;
                    newP.vectorX = (float)Math.sin(Math.toRadians(direction)) * factor;
                    newP.vectorY = (float)Math.cos(Math.toRadians(direction)) * factor;
                    newP.lifeTime = 1.0f * 1000.0f;

                    particleType.particles.add(newP);
                }
            }
        }

    }

    public void draw(SpriteBatch batch) {
        Color prev = batch.getColor();

        batch.begin();
        for (ParticleType particleType : particleTypes) {
            for (Particle p : particleType.particles) {
                Color col = batch.getColor();
                float alpha = 1.0f;

                float fadeOutThreshold = (1.0f * 1000.0f) * 1.0f;
                if (p.lifeTime < fadeOutThreshold) {
                    alpha = p.lifeTime / fadeOutThreshold;
                }

                batch.setColor(col.r, col.g, col.b, alpha);
                batch.draw(particleType.texture, p.x, p.y);
            }


            batch.setColor(prev);
        }
        batch.end();
    }
}
