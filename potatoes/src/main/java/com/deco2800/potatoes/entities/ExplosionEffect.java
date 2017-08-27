package com.deco2800.potatoes.entities;

import com.deco2800.potatoes.managers.GameManager;

import java.util.Collection;

public class ExplosionEffect extends Projectile {

    private final static transient String TEXTURE = "exp1";

    private int currentSpriteIndexCount = 1;
    private String[] currentSpriteIndex = {"exp1","exp2","exp3"};
    private int effectsTimer = 0;
    private int dmgTimer = 0;

    public ExplosionEffect() {
        // empty for serialization

    }

    @Override
    public float getDamage() {
        return 0;
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

    public ExplosionEffect(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
                           float xRenderLength, float yRenderLength) {
        super(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, TEXTURE);

    }

    @Override
    public void onTick(long time) {

        effectsTimer++;
        if (effectsTimer % 6 == 0) {
            if (currentSpriteIndexCount <= 2) {
                setTexture(currentSpriteIndex[currentSpriteIndexCount]);
                if (currentSpriteIndexCount < 3) {
                    currentSpriteIndexCount++;
                }
            } else {
                GameManager.get().getWorld().removeEntity(this);

            }
        }


    }



}
