package com.deco2800.potatoes.entities.effects;

import com.deco2800.potatoes.managers.GameManager;

public class SwipeEffect extends Effect {

    private final static transient String TEXTURE = "swipe1";

    private int currentSpriteIndexCount = 1;
    private String[] currentSpriteIndex = { "swipe2", "swipe3", "swipe4", "swipe5" };
    private int effectsTimer = 0;

    public SwipeEffect() {
        // empty for serialization

    }

    /**
     * Returns damage value, value will always be 0 because this is just showing
     * animation
     */
    @Override
    public float getDamage() {
        return 0;
    }

    /**
     * Creates a new Explosion Projectile on impact (AOE Effect). Explosion
     * Projectiles does not change direction, it should be stationary and shown at
     * the location ballistic projectile hit.
     *
     * @param posX
     *            x start position
     * @param posY
     *            y start position
     * @param posZ
     *            z start position
     * @param xLength
     *            target x position
     * @param yLength
     *            target y position
     * @param zLength
     *            target z position
     * @param xRenderLength
     *            Projectile x length
     * @param yRenderLength
     *            Projectile y length
     */

    public SwipeEffect(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
                           float xRenderLength, float yRenderLength) {
        super(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, TEXTURE);
    }

    @Override
    public void onTick(long time) {

        effectsTimer++;
        if (effectsTimer % 10 == 0) {
            if (currentSpriteIndexCount <= 1) {
                setTexture(currentSpriteIndex[currentSpriteIndexCount]);
                if (currentSpriteIndexCount < 2) {
                    currentSpriteIndexCount++;
                }
            } else {
                GameManager.get().getWorld().removeEntity(this);
            }
        }

    }

}
