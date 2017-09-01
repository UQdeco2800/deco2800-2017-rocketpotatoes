package com.deco2800.potatoes.managers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.potatoes.renderering.Render3D;
import com.deco2800.potatoes.renderering.particles.Particle;
import com.deco2800.potatoes.renderering.particles.ParticleType;
import com.deco2800.potatoes.renderering.particles.ParticleEmitter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParticleManager extends Manager {

    // Emitters currently active
    List<ParticleEmitter> emitters;

    // Emitters waiting to stop
    List<ParticleEmitter> toDestroyEmitters;

    List<Particle> particlePool;

    public ParticleManager() {
        emitters = new ArrayList<>();
        toDestroyEmitters = new ArrayList<>();

        // Initialize our pool of particles
        particlePool = new ArrayList<>();
        for (int i = 0; i < 1000000; ++i) {
            particlePool.add(new Particle());
        }

        ParticleEmitter e = new ParticleEmitter(0, 0,
                new ParticleType(2000, 1000.0f, 10, GameManager.get().getManager(TextureManager.class).getTexture("snowflake")));

        addParticleEmitter(e);
    }

    public void addParticleEmitter(ParticleEmitter e) {
        emitters.add(e);
    }

    /**
     * Ticks all active particle emitters and their particles
     * @param deltaTime tick delta
     */
    public void onTick(double deltaTime) {
        for (ParticleEmitter emitter : emitters) {
            float x = GameManager.get().getManager(PlayerManager.class).getPlayer().getPosX();
            float y = GameManager.get().getManager(PlayerManager.class).getPlayer().getPosY();

            Vector2 p = Render3D.worldToScreenCoordinates(x, y);
            emitter.setOrigin(p.x, p.y);
            emitter.onTick(deltaTime, particlePool);
        }

        toDestroyEmitters.removeIf(emitter -> !emitter.hasParticles());
    }

    /**
     * Draw's all particles
     * @param batch batch to draw with (will ensure the state of the batch is returned to normal after)
     */
    public void draw(SpriteBatch batch) {
        for (ParticleEmitter emitter : emitters) {
            emitter.draw(batch);
        }
    }

    /**
     * Gracefully stops an emitter, stops producing particles and waits until all particles have timed out before
     * destroying the emitter.
     * @param emitter the emitter to be removed
     */
    public void stopEmitter(ParticleEmitter emitter) {
        Iterator<ParticleEmitter> emitterIterator = emitters.iterator();

        while (emitterIterator.hasNext()) {
            ParticleEmitter e = emitterIterator.next();

            // If we ma
            if (e == emitter) {
                e.stop();
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
        Iterator<ParticleEmitter> emitterIterator = emitters.iterator();

        while (emitterIterator.hasNext()) {
            ParticleEmitter e = emitterIterator.next();

            // If we ma
            if (e == emitter) {
                e.forceStop();
                emitterIterator.remove();
                return;
            }
        }
    }
}
