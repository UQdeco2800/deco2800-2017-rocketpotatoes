package com.deco2800.potatoes.renderering.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.List;

public class ParticleType {
    public Color color;
    public int sizeX, sizeY;
    public int number, rate;
    public Texture texture;

    // List of active particles of this type
    List<Particle> particles;

    /**
     * Creates a square particle of the given color.
     * @param number maximum number of particles of this type to be produced.
     * @param rate rate that particles should be produced per tick
     * @param color color of this particle
     * @param sizeX size of the particle in pixels
     * @param sizeY size of the particle in pixels
     */
    public ParticleType(int number, int rate, Color color, int sizeX, int sizeY) {
        this.number = number;
        this.rate = rate;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.color = color;
        particles = new ArrayList<>();
    }

    /**
     * Creates a particle with the given texture
     * @param number maximum number of particles to produce
     * @param rate the rate particles should be produced per tick
     * @param texture texture to use for the particle
     */
    public ParticleType(int number, int rate, Texture texture) {
        this.number = number;
        this.rate = rate;
        particles = new ArrayList<>();
        this.texture = texture;
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
