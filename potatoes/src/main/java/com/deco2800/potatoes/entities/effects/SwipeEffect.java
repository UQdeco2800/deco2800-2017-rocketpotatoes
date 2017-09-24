package com.deco2800.potatoes.entities.effects;

import com.deco2800.potatoes.collisions.CollisionMask;
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
     * Creates a new swipe effect on impact. Swipe effect does not change direction,
     * it should be stationary and shown at the location ballistic projectile hit.
     *
     * @param mask
     *            The collision mask of the effect.
     * @param xRenderLength
     *            Projectile x length
     * @param yRenderLength
     *            Projectile y length
     */
    public SwipeEffect(CollisionMask mask, float xRenderLength, float yRenderLength) {
        super(mask, xRenderLength, yRenderLength, TEXTURE);
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
