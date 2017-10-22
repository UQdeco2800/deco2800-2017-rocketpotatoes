package com.deco2800.potatoes.managers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.potatoes.renderering.particles.Particle;
import com.deco2800.potatoes.renderering.particles.ParticleEmitter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.deco2800.potatoes.util.MathUtil.compareFloat;

public class ParticleManager extends Manager implements TickableManager {

    public class EmitterContainer {
        public ParticleEmitter emitter;
        public float maxLifeTime;
        public float currentLifeTime;

        public boolean toRemove;
    }

    // Emitters currently active
    private List<EmitterContainer> emitters;

    private List<Particle> particlePool;

    /**
     * Initializes the particle manager.
     */
    public ParticleManager() {
        emitters = new ArrayList<>();

        // Initialize our pool of particles
        particlePool = new ArrayList<>();
        for (int i = 0; i < 1000000; ++i) {
            particlePool.add(new Particle());
        }
    }

    /**
     * Adds a particle emitter to be ticked, drawn and destroyed appropriately.
     * @param lifeTime how long this emitter lasts (if this value is zero this emitter will never expire unless
     *                 deleted with removeEmitter(...)).
     * @param e the emitter to be added.
     */
    public void addParticleEmitter(float lifeTime, ParticleEmitter e) {
        EmitterContainer con = new EmitterContainer();
        con.maxLifeTime = lifeTime;
        con.emitter = e;
        emitters.add(con);
    }

    /**
     * Ticks all active particle emitters and their particles
     * @param deltaTime tick delta
     */
    @Override
    public void onTick(long deltaTime) {
        Iterator<EmitterContainer> emitterIterator = emitters.iterator();

        while (emitterIterator.hasNext()) {
            EmitterContainer e = emitterIterator.next();

            e.emitter.onTick(deltaTime, particlePool);

            if (!compareFloat(e.maxLifeTime, 0.0f)) {
                e.currentLifeTime += deltaTime;
            }

            // If exceeded max (don't do anything if lifetime is set to unlimited)
            if (e.toRemove || (e.currentLifeTime >= e.maxLifeTime && !compareFloat(e.maxLifeTime, 0))) {
                e.toRemove = true;
                if (!e.emitter.hasParticles()) {
                    emitterIterator.remove();
                }

                e.emitter.stop();
            }
        }
    }

    /**
     * Draw's all particles
     * @param batch batch to draw with (will ensure the state of the batch is returned to normal after)
     */
    public void draw(SpriteBatch batch) {
        // batch begin here so we batch all emitters together for efficiency!
        batch.begin();
        for (EmitterContainer emitter : emitters) {
            emitter.emitter.draw(batch);
        }
        batch.end();
    }

    /**
     * Gracefully stops an emitter, stops producing particles and waits until all particles have timed out before
     * destroying the emitter. Throws IllegalArg exception if the emitter is not being tracked
     * @param emitter the emitter to be removed
     */
    public void stopEmitter(ParticleEmitter emitter) {
        Iterator<EmitterContainer> emitterIterator = emitters.iterator();

        while (emitterIterator.hasNext()) {
            EmitterContainer e = emitterIterator.next();

            if (e.emitter == emitter) {
                e.emitter.stop();
                e.toRemove = true;
                return;
            }
        }
        throw new IllegalArgumentException("Emitter is not being tracked");
    }

    /**
     * Instantly stops an emitter from producing particles, and destroys any existing particles.
     *  Throws IllegalArg exception if the emitter is not being tracked
     * @param emitter the emitter to be removed
     */
    public void forceStopEmitter(ParticleEmitter emitter) {
        Iterator<EmitterContainer> emitterIterator = emitters.iterator();

        while (emitterIterator.hasNext()) {
            ParticleEmitter e = emitterIterator.next().emitter;

            if (e == emitter) {
                e.forceStop();
                emitterIterator.remove();
                return;
            }
        }
        throw new IllegalArgumentException("Emitter is not being tracked");
    }
   /**
     * @return The current emitter list (in container form)
     */
    public List<EmitterContainer> getEmitters() {
        return emitters;
    }
}
