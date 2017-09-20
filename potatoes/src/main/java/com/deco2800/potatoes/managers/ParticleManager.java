package com.deco2800.potatoes.managers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.potatoes.renderering.Render3D;
import com.deco2800.potatoes.renderering.particles.Particle;
import com.deco2800.potatoes.renderering.particles.ParticleEmitter;
import com.deco2800.potatoes.renderering.particles.types.BasicParticleType;
import com.deco2800.potatoes.renderering.particles.types.BuoyantParticleType;
import com.deco2800.potatoes.renderering.particles.types.ParticleType;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParticleManager extends Manager implements TickableManager {

    private class EmitterContainer {
        public ParticleEmitter emitter;
        public float maxLifeTime;
        public float currentLifeTime;

        public boolean toRemove;
    }

    // Emitters currently active
    List<EmitterContainer> emitters;

    List<Particle> particlePool;

    public ParticleManager() {
        emitters = new ArrayList<>();

        // Initialize our pool of particles
        particlePool = new ArrayList<>();
        for (int i = 0; i < 1000000; ++i) {
            particlePool.add(new Particle());
        }

        ParticleType typeOne = new BasicParticleType(100000, 5000.0f, 100.0f, 128,
                GameManager.get().getManager(TextureManager.class).getTexture("snowflake"));
        typeOne.speed = 0.4f;

        //addParticleEmitter(0.0f, new ParticleEmitter(50, 50, typeOne));

        //addParticleEmitter(5000.0f, new ParticleEmitter(50, 50, typeOne, typeTwo));
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
    @Override
    public void onTick(long deltaTime) {
        Iterator<EmitterContainer> emitterIterator = emitters.iterator();

        while (emitterIterator.hasNext()) {
            EmitterContainer e = emitterIterator.next();

            /*
            float x = GameManager.get().getManager(PlayerManager.class).getPlayer().getPosX();
            float y = GameManager.get().getManager(PlayerManager.class).getPlayer().getPosY();

            int tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get("tilewidth");
            int tileHeight = (int) GameManager.get().getWorld().getMap().getProperties().get("tileheight");

            Vector2 p = Render3D.worldToScreenCoordinates(x, y, 0);
            e.emitter.setOrigin(p.x + tileWidth / 2, p.y + tileHeight / 2);
            */

            e.emitter.onTick(deltaTime, particlePool);

            if (e.maxLifeTime != 0.0f) {
                e.currentLifeTime += deltaTime;
            }

            // If exceeded max (don't do anything if lifetime is set to unlimited)
            if (e.toRemove || (e.currentLifeTime >= e.maxLifeTime && e.maxLifeTime != 0)) {
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
     * destroying the emitter.
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
