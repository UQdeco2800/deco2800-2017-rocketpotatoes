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

    // Is this emitter should produce particles
    private boolean active;

    // If this emitter has any existing particles
    private boolean hasParticles;

    /**
     * Create a particle emitter with the given particleTypes TODO emitter settings
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
                Pixmap p = new Pixmap(particleType.sizeX, particleType.sizeY, Pixmap.Format.RGB888);
                p.setColor(particleType.color);
                // Fill box with pixels
                for (int pX = 0; pX < particleType.sizeX; ++pX) {
                    for (int pY = 0; pY < particleType.sizeY; ++pY) {
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
                p.rotation += 1.0f - 2.0f * (p.hashCode() % 2);

                // Delete expired
                if (p.lifeTime <= 0.0f) {
                    p.alive = false;
                    iter.remove();
                }
            }
        }

        // Create new if active
        if (active) {
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
                    } else {
                        newP.alive = true;
                        newP.x = originX;
                        newP.y = originY;

                        float factor = (random.nextFloat() * 2.0f - 1.0f);
                        float direction = random.nextFloat() * 360;
                        newP.vectorX = (float) Math.sin(Math.toRadians(direction)) * factor / 5.0f;
                        newP.vectorY = (float) Math.cos(Math.toRadians(direction)) * factor / 5.0f;
                        newP.lifeTime = 5.0f * 1000.0f;
                        newP.rotation = random.nextFloat();

                        particleType.particles.add(newP);
                        hasParticles = true;
                    }
                }
            }
        }
        else {
            // Check if any particles exist
            hasParticles = false;
            for (ParticleType particleType : particleTypes) {
                if (particleType.particles.size() != 0) {
                    hasParticles = true;
                    break;
                }

            }
        }

    }

    /**
     * Draw's all the particles for this emitter. Include fadeout and other transition effects
     * @param batch batch to draw with
     */
    public void draw(SpriteBatch batch) {
        Color prev = batch.getColor();

        batch.begin();
        for (ParticleType particleType : particleTypes) {
            for (Particle p : particleType.particles) {
                Color col = batch.getColor();
                float alpha = 1.0f;

                float fadeOutThreshold = (1.0f * 1000.0f) * 5.0f;
                if (p.lifeTime < fadeOutThreshold) {
                    alpha = p.lifeTime / fadeOutThreshold;
                }

                batch.setColor(col.r, col.g, col.b, alpha);
                batch.draw(particleType.texture, p.x, p.y, 0, 0,
                        particleType.texture.getWidth(), particleType.texture.getHeight(),
                        1.0f, 1.0f, p.rotation,
                        0, 0, particleType.texture.getWidth(), particleType.texture.getHeight(),
                        false, false);
            }


            batch.setColor(prev);
        }
        batch.end();
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
