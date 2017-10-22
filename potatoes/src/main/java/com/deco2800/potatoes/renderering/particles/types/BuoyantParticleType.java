package com.deco2800.potatoes.renderering.particles.types;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.potatoes.renderering.particles.Particle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class BuoyantParticleType extends ParticleType {

    private Random random = new Random();
    /**
     * Creates a square particle of the given color.
     * @param number maximum number of particles of this type to be produced.
     * @param lifeTime lifetime of a single particle (in ms)
     * @param cycleDelta how long till a cycle (i.e. when we emit particles). (in ms)
     * @param rate how many particle should be produced on a cycle
     * @param color color of this particle
     * @param sizeX size of the particle in pixels
     * @param sizeY size of the particle in pixels
     */
    public BuoyantParticleType(int number, float lifeTime, float cycleDelta, int rate, Color color, int sizeX, int sizeY) {
        this.number = number;
        this.setLifeTime(lifeTime);
        this.cycleDelta = cycleDelta;
        this.rate = rate;
        this.setSizeX(sizeX);
        this.setSizeY(sizeY);
        this.setColor(color);
        particles = new ArrayList<>();
    }

    /**
     * Creates a particle with the given texture
     * @param number maximum number of particles to produce
     * @param lifeTime lifetime of a single particle (in ms)
     * @param cycleDelta how long till a cycle (i.e. when we emit particles). (in ms)
     * @param rate the rate particles should be produced per cycle
     * @param texture texture to use for the particle
     */
    public BuoyantParticleType(int number, float lifeTime, float cycleDelta, int rate, Texture texture) {
        this.number = number;
        this.setLifeTime(lifeTime);
        this.cycleDelta = cycleDelta;
        this.rate = rate;
        particles = new ArrayList<>();
        this.texture = texture;
    }


    /**
     * Tick's the particles associated with this type
     *
     * @param deltaTime    delta to use
     * @param particlePool pool to fetch new particles from
     * @param originX      origin point for new particles
     * @param originY      origin point for new particles
     * @param active       should this spawn new particles?
     */
    @Override
    public void onTick(double deltaTime, List<Particle> particlePool, float originX, float originY, boolean active) {
        // Tick particles
        Iterator<Particle> iter = this.particles.iterator();
        while (iter.hasNext()) {
            Particle p = iter.next();
            // Tick particles
            p.x += p.vector.x * deltaTime;
            p.y += p.vector.y * deltaTime;
            p.lifeTime -= deltaTime;
            p.rotation += this.getRotationSpeed() - this.getRotationSpeed()  * 2.0f * (p.hashCode() % 2);

            // Buoyant logic (adjust direction of vector to be up)
            p.vector.setAngle(90.0f);

            // Delete expired
            if (p.lifeTime <= 0.0f) {
                p.alive = false;
                iter.remove();
            }
        }


        // Check if new particles are needed
        if (active) {
            this.currentCycleTime += deltaTime;
            while (this.currentCycleTime >= this.cycleDelta) {
                this.currentCycleTime -= this.cycleDelta;
                // How many produced this cycle
                int count = 0;
                while (this.particles.size() < this.number) {
                    if (count == this.rate) {
                        break;
                    }
                    Particle newP = null;

                    // Find particle
                    for (Particle p : particlePool) {
                        if (!p.alive) {
                            newP = p;
                            break;
                        }
                    }

                    // Add it
                    if (newP == null) {
                        throw new IllegalStateException("Ayyyyyy too many particles");
                    } else {
                        newP.alive = true;
                        newP.x = originX;
                        newP.y = originY;

                        float min = this.getSpeedVarianceMin();
                        float max = this.getSpeedVarianceMax();
                        float factor = (random.nextFloat() * (max - min) + min) * this.getSpeed();
                        float direction = random.nextFloat() * 360;

                        // Gen normalized vec and scale it by factor
                        newP.vector = new Vector2(
                                (float) Math.sin(Math.toRadians(direction)),
                                (float) Math.cos(Math.toRadians(direction))).nor().scl(factor);

                        newP.lifeTime = this.getLifeTime();
                        newP.rotation = random.nextFloat();

                        this.particles.add(newP);
                        count++;
                    }
                }
            }
        }
    }
}
