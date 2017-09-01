package com.deco2800.potatoes.renderering.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;

public class ParticleType {
    public Color color;
    public int size;
    public int number;
    public Texture texture;

    // List of active particles of this type
    List<Particle> particles;

    public ParticleType(int number, int size, Color color) {
        this.number = number;
        this.size = size;
        this.color = color;
        particles = new ArrayList<>();
    }

    /**
     * Removes all particles from being tracked
     */
    public void cleanup() {
        for (Particle particle : particles) {
            particle.alive = false;
        }
        particles.clear();
    }
}
