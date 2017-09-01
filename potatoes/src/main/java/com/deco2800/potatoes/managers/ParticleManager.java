package com.deco2800.potatoes.managers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.potatoes.renderering.particles.Particle;
import com.deco2800.potatoes.renderering.particles.ParticleType;
import com.deco2800.potatoes.renderering.particles.ParticleEmitter;

import java.util.ArrayList;
import java.util.List;

public class ParticleManager extends Manager {
    List<ParticleEmitter> emitters;

    List<Particle> particlePool;

    public ParticleManager() {
        emitters = new ArrayList<>();

        // Initialize our pool of particles (let's start with 1024)
        particlePool = new ArrayList<>();
        for (int i = 0; i < 2000000; ++i) {
            particlePool.add(new Particle());
        }

        ParticleEmitter e = new ParticleEmitter(0, 0,
                new ParticleType(200, 5, Color.RED),
                new ParticleType(100, 5, Color.BLUE),
                new ParticleType(100, 5, Color.GREEN));

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
            emitter.onTick(deltaTime, particlePool);
        }
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
}
