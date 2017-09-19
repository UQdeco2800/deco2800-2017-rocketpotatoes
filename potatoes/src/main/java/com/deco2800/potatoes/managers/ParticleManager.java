package com.deco2800.potatoes.managers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.potatoes.renderering.particles.Particle;
import com.deco2800.potatoes.renderering.particles.ParticleEmitter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParticleManager extends Manager {

    private class EmitterContainer {
        public ParticleEmitter emitter;
        public float maxLifeTime;
        public float currentLifeTime;
    }

    // Emitters currently active
    List<EmitterContainer> emitters;

    // Emitters waiting to stop
    List<EmitterContainer> toDestroyEmitters;

    List<Particle> particlePool;

    public ParticleManager() {
        emitters = new ArrayList<>();
        toDestroyEmitters = new ArrayList<>();

        // Initialize our pool of particles
        particlePool = new ArrayList<>();
        for (int i = 0; i < 1000000; ++i) {
            particlePool.add(new Particle());
        }
    }

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
    public void onTick(double deltaTime) {
        Iterator<EmitterContainer> emitterIterator = emitters.iterator();

        while (emitterIterator.hasNext()) {
            EmitterContainer e = emitterIterator.next();

            e.emitter.onTick(deltaTime, particlePool);
            e.currentLifeTime += deltaTime;

            if (e.currentLifeTime >= e.maxLifeTime) {
                toDestroyEmitters.add(e);
                emitterIterator.remove();
            }
        }
        toDestroyEmitters.removeIf(emitter -> !emitter.emitter.hasParticles());
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
     * destroying the emitter.
     * @param emitter the emitter to be removed
     */
    public void stopEmitter(ParticleEmitter emitter) {
        Iterator<EmitterContainer> emitterIterator = emitters.iterator();

        while (emitterIterator.hasNext()) {
            EmitterContainer e = emitterIterator.next();

            if (e.emitter == emitter) {
                e.emitter.stop();
                toDestroyEmitters.add(e);
                emitterIterator.remove();
                return;
            }
        }
    }

    /**
     * Instantly stops an emitter from producing particles, and destroys any existing particles.
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
    }
}
