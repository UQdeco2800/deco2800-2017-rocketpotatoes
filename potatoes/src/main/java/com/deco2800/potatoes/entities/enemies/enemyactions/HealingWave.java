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

public class HealingWave extends TimeEvent<EnemyEntity> {

    private Class target;
    private float healAmount;
    private int rate;
    private float waveRadius;

    public HealingWave() {
        // Blank comment for sonar
    }

    public HealingWave(int rate, Class target, float waveRadius, float healAmount) {
        setDoReset(true);
        setResetAmount(rate);
        this.target = target;
        this.rate = rate;
        this.waveRadius = waveRadius;
        this.healAmount = healAmount;
        reset();
    }

    /*Should we turn this into an effect?*/
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

        //Particle effect for visual cue that healing is occurring.
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
        return new HealingWave(getResetAmount(), this.target, this.waveRadius, this.healAmount);
    }

    /**
     * @return string representation of melee attack
     */
    @Override
    public String toString() {
        return String.format("Healing wave with %d rate, %f radius, %f heal amount", this.getResetAmount(), waveRadius, healAmount);
    }
}
