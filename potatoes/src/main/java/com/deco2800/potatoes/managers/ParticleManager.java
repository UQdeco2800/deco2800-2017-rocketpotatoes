package com.deco2800.potatoes.managers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.potatoes.renderering.particles.ParticleEmitter;

import java.util.ArrayList;
import java.util.List;

public class ParticleManager extends Manager {
    List<ParticleEmitter> emitters;

    public ParticleManager() {
        // Initialize pool?

        emitters = new ArrayList<>();
    }

    public void addParticleEmitter(ParticleEmitter e) {
    }

    /**
     * Ticks all active particle emitters and their particles
     * @param deltaTime tick delta
     */
    public void onTick(double deltaTime) {
        // Tick particles
    }

    /**
     * Draw's all particles
     * @param batch batch to draw with (will ensure the state of the batch is returned to normal after)
     */
    public void draw(SpriteBatch batch) {

    }
}
