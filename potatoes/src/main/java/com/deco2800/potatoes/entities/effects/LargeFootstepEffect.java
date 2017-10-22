package com.deco2800.potatoes.entities.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.potatoes.collisions.Circle2D;
import com.deco2800.potatoes.collisions.Shape2D;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.resources.ResourceEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.ParticleManager;
import com.deco2800.potatoes.managers.SoundManager;
import com.deco2800.potatoes.renderering.Render3D;
import com.deco2800.potatoes.renderering.particles.ParticleEmitter;
import com.deco2800.potatoes.renderering.particles.types.BasicParticleType;
import com.deco2800.potatoes.renderering.particles.types.ParticleType;

import java.util.Map;

/**
 * A large footstep effect. Created when a large enemy moves around the map.
 * Effect is meant to animate dirt rising up from the ground.
 * This effect destroys any resources it comes into contact with (i.e. the enemy "stomped" on the resource.
 *
 * @author ryanjphelan
 */
public class LargeFootstepEffect extends Effect {

    private boolean resourceStomped = false;
    private Shape2D effectPosition;
    private int currentTextureIndexCount = 0;
    private int timer = 0;

    /**
     * Empty constructor. Used for serialisation purposes
     */
    public LargeFootstepEffect() {
        // Empty for serialization purposes
    }

    /**
     * Creates a new footstep effect.
     * NOTE: Effect is currently being manually shifted for visual purposes.
     * i.e. posX and posY do not match with the given inputs (posX is decreased by 1 and posY is increased by 0.5)
     *
     * @param posX
     *            x start position
     * @param posY
     *            y start position
     */

    public LargeFootstepEffect(Class<?> targetClass, float posX, float posY, float damage, float range) {
        super(targetClass, new Circle2D(posX, posY, 0.75f), 1.4f, 1.4f, damage, range, EffectTexture.LARGE_FOOTSTEP);
        effectPosition = getMask();

    }

    @Override
    public void onTick(long time) {
        timer++;
        if (!resourceStomped) {
            Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();
            for (AbstractEntity entity : entities.values()) {
                if (this.equals(entity) || !(entity instanceof ResourceEntity)
                        || !effectPosition.overlaps(entity.getMask())) {
                    continue;
                }
                //Create a particle effect to represent a resource being destroyed
                ParticleType particle =  new BasicParticleType(1700, 1700.0f,
                        0.0f, 256, Color.OLIVE, 25, 4);
                particle.setSpeed(0.04f);
                Vector2 pos = Render3D.worldToScreenCoordinates(entity.getPosX(), entity.getPosY(), -0.5f);
                int tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get("tilewidth");
                int tileHeight = (int) GameManager.get().getWorld().getMap().getProperties().get("tileheight");
                GameManager.get().getManager(ParticleManager.class).addParticleEmitter(
                        1.5f, new ParticleEmitter(pos.x + tileWidth / 2, pos.y + tileHeight / 2, particle));
                //Play the appropriate sound effect
                String resourceType = ((ResourceEntity) entity).getType().getTypeName();
                GameManager.get().getWorld().removeEntity(entity);
                if ("seed".equals(resourceType)) {
                    GameManager.get().getManager(SoundManager.class).playSound("seedResourceDestroyed.wav");
                } else if ("food".equals(resourceType)) {
                    GameManager.get().getManager(SoundManager.class).playSound("foodResourceDestroyed.wav");
                } else {
                    GameManager.get().getManager(SoundManager.class).playSound("seedResourceDestroyed.wav");
                }
            }
            resourceStomped = true;
        }
        if (timer % 10 == 0) {
            if (currentTextureIndexCount < 3) {
                currentTextureIndexCount++;
            } else {
                GameManager.get().getWorld().removeEntity(this);
            }
        }
    }

    /**
     * Return the Shape2D position of the large footstep
     *
     * @return Shape2D position of footstep
     */
    public Shape2D getFootstepPosition() {
        return effectPosition;
    }

    /**
     * Return the current texture index count.
     *
     * @return the index
     */
    public int getCurrentTextureIndexCount() {
        return currentTextureIndexCount;
    }


    /**
     * String representation of the footstep at its set position.
     *
     * @return String representation of the stomped ground
     */
    @Override
    public String toString() {
        return String.format("Large Footstep at (%d, %d)", (int) getPosX(), (int) getPosY());
    }
}
