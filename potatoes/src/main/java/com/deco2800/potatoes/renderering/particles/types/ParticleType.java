package com.deco2800.potatoes.renderering.particles.types;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.potatoes.renderering.particles.Particle;

import java.util.List;

public abstract class ParticleType {
    public Color color;
    public int sizeX, sizeY;
    public int number, rate;
    public float lifeTime;
    public float cycleDelta;
    public float currentCycleTime;
    public float rotationSpeed = 1.0f;
    public float alphaCeil = 0.8f;
    public float fadeOutPercent = 1.0f;
    public float speed = 1.0f;

    // Random spread of speed (if this are the same the speed is constant)
    public float speedVarianceMin = 0.0f;
    public float speedVarianceMax = 1.0f;

    // Not functional yet. Will implement when/if needed
    public float fadeInPercent = 0.1f;
    public Texture texture;

    // List of active particles of this type
    public List<Particle> particles;

    /**
     * Removes all particles from being tracked
     */
    public void cleanup() {
        for (Particle particle : particles) {
            particle.alive = false;
        }
        particles.clear();
    }

    /**
     * Tick's the particles associated with this type
     *
     * @param deltaTime delta to use
     * @param particlePool pool to fetch new particles from
     * @param originX origin point for new particles
     * @param originY origin point for new particles
     * @param active should this spawn new particles?
     */
    public abstract void onTick(double deltaTime, List<Particle> particlePool, float originX, float originY,
                                boolean active);

    /**
     * Draws the particles associated with this type. The batch should have begun before this is called.
     * @param batch batch to draw withh
     */
    public abstract void draw(SpriteBatch batch);


}
