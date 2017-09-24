package com.deco2800.potatoes.entities.effects;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.Box3D;



/*
   A HealingEffect, an virtually terrain that has been "heal" in the game.
   HealingEffect created for Moose's healing spell to enemyentity,Whenever Moose heal
   other enemy, the effect will appear to show it is healing the enemy.
   TODO heal enemy.
 */
public class HealingEffect extends Effect {
    private static final transient String TEXTURE = "Healing1";

    private boolean isTemporary;
    private Box3D effectPosition;
    private int currentTextureIndexCount = 0;
    private String[] currentTextureArray = { "Healing1", "Healing2", "Healing3" };
    private int timer = 0;

    /**
     * Empty constructor. Used for serialisation purposes
     */
    public HealingEffect() {
        // empty for serialization
    }

    /**
     * Creates a healing effect.
     *
     * @param posX
     *            x start position
     * @param posY
     *            y start position
     * @param posZ
     *            z start position
     * @param isTemporary
     *            boolean for whether this effect is temporary or permanent
     */
    public HealingEffect(Class<?> targetClass, float posX, float posY, float posZ, boolean isTemporary,
                               float healing, float range) {
        super(targetClass, new Vector3(posX, posY, posZ), 3f, 2f, 2f, 1.2f, 1.2f,healing, range, EffectTexture.HEALING);
        this.isTemporary = isTemporary;
        effectPosition = getBox3D();
        animate = false;
    }

    @Override
    public void onTick(long time) {
        if (isTemporary) {
            timer++;
            if (timer % 100 == 0) {
                if (currentTextureIndexCount < 3) {
                    setTexture(currentTextureArray[currentTextureIndexCount]);
                    currentTextureIndexCount++;
                } else {
                    GameManager.get().getWorld().removeEntity(this);
                }
            }
        }
    }

    /*
     *
     * @return String representation of the healing
     */
    @Override
    public String toString() {
        return String.format("Healiung at (%d, %d)", (int) getPosX(), (int) getPosY());
    }
}
