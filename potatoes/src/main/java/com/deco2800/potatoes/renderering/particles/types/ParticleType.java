package com.deco2800.potatoes.renderering.particles.types;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.potatoes.renderering.particles.Particle;

import java.util.List;

public abstract class ParticleType {
    private Color color;
    private int sizeX, sizeY;
    public int number, rate;
    private float lifeTime;
    public float cycleDelta;
    public float currentCycleTime;
    private float rotationSpeed = 1.0f;
    private float alphaCeil = 1.0f;
    private float fadeOutPercent = 1.0f;
    private float speed = 1.0f;

    // Random angle spread
    private float lowerAngleBound = 0.0f;
    private float upperAngleBound = 360.0f;

    // Random spread of SPEED (if this are the same the SPEED is constant)
    private float speedVarianceMin = 0.0f;
    private float speedVarianceMax = 1.0f;

    // Not functional yet. Will implement when/if needed
    private float fadeInPercent = 0.1f;
    public Texture texture;

    // List of active particles of this type
    public List<Particle> particles;

    /**
     * Removes all particles from being tracked
     */
    public void cleanup() {
        for (Particle particle : particles) {
            particle.alive = false;
        }
        particles.clear();
    }

    /**
     * Tick's the particles associated with this type
     *
     * @param deltaTime delta to use
     * @param particlePool pool to fetch new particles from
     * @param originX origin point for new particles
     * @param originY origin point for new particles
     * @param active should this spawn new particles?
     */
    public abstract void onTick(double deltaTime, List<Particle> particlePool, float originX, float originY,
                                boolean active);

    /**
     * Draws the particles associated with this type. The batch should have begun before this is called.
     * @param batch batch to draw withh
     */
    public void draw(SpriteBatch batch) {
        for (Particle p : this.particles) {
            Color col = batch.getColor();
            float alpha = 1.0f;

            float fadeOutThreshold = this.lifeTime * this.fadeOutPercent;
            if (p.lifeTime < fadeOutThreshold) {
                alpha = p.lifeTime / fadeOutThreshold;
            }

            if (alpha > this.alphaCeil) { 
                alpha = this.alphaCeil; 
            }

            batch.setColor(col.r, col.g, col.b, alpha);
            batch.draw(this.texture, p.x, p.y, this.texture.getWidth() / 2, this.texture.getHeight() / 2,
                    this.texture.getWidth(), this.texture.getHeight(),
                    1.0f, 1.0f, p.rotation,
                    0, 0, this.texture.getWidth(), this.texture.getHeight(),
                    false, false);
        }
    }
    //return speed
    public float getSpeed(){
        return this.speed;
    }
    //set speed
    public void setSpeed(float speed){
        this.speed = speed;
    }
    //set speedVarianceMin
    public float getSpeedVarianceMin(){
        return this.speedVarianceMin;
    }
    //set speedVarianceMin
    public void setSpeedVarianceMin(float speedVarianceMin){
        this.speedVarianceMin = speedVarianceMin;
    }
    //set speedVarianceMax
    public float getSpeedVarianceMax(){
        return this.speedVarianceMax;
    }
    //set speedVarianceMin
    public void setSpeedVarianceMax(float speedVarianceMax){
        this.speedVarianceMax = speedVarianceMax;
    }
    //return size X
    public int getSizeX(){
        return this.sizeX;
    }
    //set size X
    public void setSizeX(int sizeX){
        this.sizeX = sizeX;
    }
    //return size Y
    public int getSizeY(){
        return this.sizeX;
    }
    //set size Y
    public void setSizeY(int sizeY){
        this.sizeY = sizeY;
    }
    //get lowerAngleBound
    public float getLowerAngleBound(){
        return this.lowerAngleBound;
    }
    //set lowerAngleBound
    public void setlowerAngleBound(float lowerAngleBound){
        this.lowerAngleBound = lowerAngleBound;
    }
    //set upperAngleBound
    public float getUpperAngleBound(){
        return this.upperAngleBound;
    }
    //set lowerAngleBound
    public void setUpperAngleBound(float upperAngleBound){
        this.upperAngleBound = upperAngleBound;
    }
    //set colour
    public Color getColor(){
        return this.color;
    }
    //set colour
    public void setColor(Color color){
        this.color = color;
    }
    //return LifeTime
    public float getLifeTime(){
        return this.lifeTime;
    }
    //set LifeTime
    public void setLifeTime(float lifeTime){
        this.lifeTime = lifeTime;
    }
    //return rotationSpeed
    public float getRotationSpeed(){
        return this.rotationSpeed;
    }
    //set rotationSpeed
    public void setRotationSpeed(float rotationSpeed){
        this.rotationSpeed = rotationSpeed;
    }
}
