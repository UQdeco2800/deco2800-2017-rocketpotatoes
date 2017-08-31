package com.deco2800.potatoes.entities.effects;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Enemies.EnemyEntity;
import com.deco2800.potatoes.managers.GameManager;

import java.util.Collection;

public class AOEEffect extends Effect {

    private final static transient String TEXTURE = "aoe1";
    private float DAMAGE = 1;
    private int currentSpriteIndexCount = 1;
    //    private String[] currentSpriteIndex = {"exp1","exp2","exp3","aoe1", "aoe2", "aoe3"};
    private String[] currentSpriteIndex = {"aoe1", "aoe2", "aoe3"};
    private int effectsTimer = 0;
    private int dmgTimer = 0;

    public AOEEffect() {
        // empty for serialization
        DAMAGE = 1;
    }

    /**
     * Creates a new Explosion Projectile on impact (AOE Effect). Explosion Projectiles does not change
     * direction, it should be stationary and shown at the location ballistic projectile hit.
     *
     * @param posX          x start position
     * @param posY          y start position
     * @param posZ          z start position
     * @param xLength       target x position
     * @param yLength       target y position
     * @param zLength       target z position
     * @param xRenderLength Projectile x length
     * @param yRenderLength Projectile y length
     * @param DAMAGE        Projectile damage
     */

    public AOEEffect(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
                               float xRenderLength, float yRenderLength, float DAMAGE) {
        super(posX, posY, posZ, xLength + 3, yLength + 3, zLength, xRenderLength, yRenderLength, TEXTURE);
        this.DAMAGE = DAMAGE;
    }

    @Override
    public void onTick(long time) {

        effectsTimer++;
        if (effectsTimer % 10 == 0) {
            if (currentSpriteIndexCount <= 2) {
                setTexture(currentSpriteIndex[currentSpriteIndexCount]);
                if (currentSpriteIndexCount < 3) {
                    currentSpriteIndexCount++;
                }
            } else {
                GameManager.get().getWorld().removeEntity(this);

            }
        }

        dmgTimer++;
        if (dmgTimer % 6 == 0) {
            Collection<AbstractEntity> entities = GameManager.get().getWorld().getEntities().values();
            for (AbstractEntity entity : entities) {
                if (entity instanceof EnemyEntity && this.collidesWith(entity)) {
                    ((EnemyEntity) entity).getShot(this);
                }
            }

        }
    }

    /**
     * Returns Damage value
     */
    @Override
    public float getDamage() {
        return DAMAGE;
    }

}
