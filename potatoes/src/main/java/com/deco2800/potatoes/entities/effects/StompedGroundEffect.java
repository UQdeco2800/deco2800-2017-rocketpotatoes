package com.deco2800.potatoes.entities.effects;

import java.util.Map;
import java.util.Optional;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.ResourceEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.SoundManager;
import com.deco2800.potatoes.util.Box3D;
import com.deco2800.potatoes.util.WorldUtil;

/**
 * A StompedGroundEffect, essentially terrain that has been "damaged" by an entity in the game.
 * Originally created for the TankEnemy, as the bear moves he "stomps" the ground and damages it.
 * This effect can be either temporary or permanent.
 * When entities walk through this effect, they will be slowed down TODO Actually make them slow down.
 */
public class StompedGroundEffect extends Effect {
    //TODO Texture is a placeholder. Need to design proper artwork for stomped ground.
    private final static transient String TEXTURE = "DamagedGroundTemp1";

    private boolean isTemporary;
    private boolean resourceStomped = false;
    private Box3D effectPosition;
    private int currentTextureIndexCount = 0;
    private String[] currentTextureArray = { "DamagedGroundTemp1", "DamagedGroundTemp2", "DamagedGroundTemp3" };
    private int timer = 0;

    private static final SoundManager soundManager = new SoundManager();

    /**
     * Empty constructor. Used for serialisation purposes
     */
    public StompedGroundEffect() {
    }

    /**
     * Creates a new stomped ground effect.
     * Effect is either temporary and will disappear, or is permanent and will affect game-play indefinitely.
     *
     * @param posX
     *          x start position
     * @param posY
     *          y start position
     * @param posZ
     *          z start position
     * @param isTemporary
     *          boolean for whether this effect is temporary or permanent
     */
    public StompedGroundEffect(float posX, float posY, float posZ, boolean isTemporary) {
        super(posX, posY, posZ, 1f, 1f, 1f, 1.2f, 1.2f, TEXTURE);
        this.isTemporary = isTemporary;
        effectPosition = getBox3D();
    }

    @Override
    public void onTick(long time) {
        if (isTemporary) {
            timer++;
            if (!resourceStomped) {
                Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();
                for (AbstractEntity entity : entities.values()) {
                    if (!this.equals(entity) && entity instanceof ResourceEntity  &&
                            effectPosition.overlaps(entity.getBox3D()) ) {
                        String resourceType = ((ResourceEntity) entity).getType().getTypeName();
                        GameManager.get().getWorld().removeEntity(entity);
                        if (resourceType.equals("seed")) {
                            soundManager.playSound("seedResourceDestroyed.wav");
                        } else if (resourceType.equals("food")) {
                            soundManager.playSound("foodResourceDestroyed.wav");
                        }
                    }
                }
                resourceStomped = true;
            }
            if (timer % 150 == 0) {
                if (currentTextureIndexCount < 3) {
                    setTexture(currentTextureArray[currentTextureIndexCount]);
                    currentTextureIndexCount++;
                } else {
                    GameManager.get().getWorld().removeEntity(this);
                }
            }
        }
    }

    /**
     * Returns damage value, value is zero. This effect does not damage entities.
     */
    @Override
    public float getDamage() {
        return 0;
    }

    /**
     * String representation of the damaged ground at its set position.
     *
     * @return String representation of the stomped ground
     */
    @Override
    public String toString() {
        return String.format("Stomped Ground at (%d, %d)", (int) getPosX(), (int) getPosY());
    }
}
