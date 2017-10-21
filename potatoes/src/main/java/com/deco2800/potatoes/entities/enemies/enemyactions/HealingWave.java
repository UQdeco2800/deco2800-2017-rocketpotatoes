package com.deco2800.potatoes.entities.enemies.enemyactions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.entities.enemies.EnemyEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.ParticleManager;
import com.deco2800.potatoes.renderering.Render3D;
import com.deco2800.potatoes.renderering.particles.ParticleEmitter;
import com.deco2800.potatoes.renderering.particles.types.BasicParticleType;
import com.deco2800.potatoes.renderering.particles.types.ParticleType;
import com.deco2800.potatoes.util.WorldUtil;

import java.util.Map;

/**
 * A healing wave TimeEvent which, when belonging to an enemy, causes the enemy to heal other enemies around it for.
 *
 * @author: craig
 **/
public class HealingWave extends TimeEvent<EnemyEntity> {

    private float healAmount;
    private float waveRadius;

    /**
     * Default constructor for serialization
     */
    public HealingWave() {
        // Blank comment for sonar
    }

    /**
     * Constructor for a HealingWave TimeEvent.
     *
     * @param eventRate
     *          The rate at which this TimeEvent occurs in terms of number of ticks.
     * @param waveRadius
     *          The size of the radius of the created healing wave that other enemies need to be within to be healed
     * @param healAmount
     *          The amount of healing the wave provides affected enemies
     */
    public HealingWave(int eventRate, float waveRadius, float healAmount) {
        setDoReset(true);
        setResetAmount(eventRate);
        this.waveRadius = waveRadius;
        this.healAmount = healAmount;
        reset();
    }

    /**
     * The healing wave event - enemies within the radius of healing wave will be healed for a particular amount.
     *
     * @param enemy
     *          The enemy that this healing wave belongs to.
     */
    public void action(EnemyEntity enemy) {
        enemy.setMoving(true);
        float distanceToEnemy;
        //System.err.println("I'm a moose an i want to heal");
        Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();
        for (AbstractEntity entity : entities.values()) {
            if (entity instanceof EnemyEntity) {
                distanceToEnemy = WorldUtil.distance(enemy.getPosX(), enemy.getPosY(), entity.getPosX(), entity.getPosY());
                if (distanceToEnemy < waveRadius) {
                    EnemyEntity enemyTarget = (EnemyEntity) entity;
                    enemyTarget.heal(healAmount);
                }
            }
        }

        //Particle effect for visual cue that healing wave has occurred.
        ParticleManager p = GameManager.get().getManager(ParticleManager.class);

        ParticleType particle =  new BasicParticleType(100000, 500.0f,
                0.0f, 1024, Color.GREEN, 10, 10);
        particle.speed = 0.9f;

        Vector2 pos = Render3D.worldToScreenCoordinates(enemy.getPosX(), enemy.getPosY(), 0);
        int tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get("tilewidth");
        int tileHeight = (int) GameManager.get().getWorld().getMap().getProperties().get("tileheight");
        p.addParticleEmitter(1.0f, new ParticleEmitter(pos.x + tileWidth / 2, pos.y + tileHeight / 2,
                particle));
    }

    /**
     * @return a copy of this MeleeAttackEvent
     */
    @Override
    public TimeEvent<EnemyEntity> copy() {
        return new HealingWave(getResetAmount(), this.waveRadius, this.healAmount);
    }

    /**
     * @return string representation of melee attack
     */
    @Override
    public String toString() {
        return String.format("Healing wave with %d rate, %f radius, %f heal amount", this.getResetAmount(), waveRadius, healAmount);
    }
}
