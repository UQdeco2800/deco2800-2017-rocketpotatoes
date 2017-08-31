package com.deco2800.potatoes.renderering.particles;

import java.util.ArrayList;
import java.util.List;

public class ParticleEmitter {
    List<Particle> particles;

    /**
     * Create a particle emitter with the given particles TODO emitter settings
     * @param x position of the emitter
     * @param y position of the emitter
     * @param particles the particles to be created
     */
    public ParticleEmitter(float x, float y, Particle... particles) {
        this.particles = new ArrayList<>();

        for (Particle particle : particles) {
            this.particles.add(particle);
        }
    }
}
