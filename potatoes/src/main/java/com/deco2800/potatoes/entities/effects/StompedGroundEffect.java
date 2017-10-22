package com.deco2800.potatoes.entities.effects;

import com.deco2800.potatoes.collisions.Shape2D;
import com.deco2800.potatoes.collisions.Circle2D;

import com.deco2800.potatoes.managers.GameManager;

/**
 * A StompedGroundEffect, essentially terrain that has been "damaged" by an
 * entity in the game. Originally created for the TankEnemy, as the bear moves
 * he "stomps" the ground and damages it. This effect can be either temporary or
 * permanent. When entities walk through this effect, they will be slowed down
 *
 * @author ryanjphelan
 */
public class StompedGroundEffect extends Effect {

	private boolean isTemporary;
	private Shape2D effectPosition;
	private int indexCount = 0;
	private String[] currentTextureArray = { "DamagedGroundTemp1", "DamagedGroundTemp2", "DamagedGroundTemp3" };
	private int timer = 0;


	/**
	 * Empty constructor. Used for serialisation purposes
	 */
	public StompedGroundEffect() {
		// Empty for serialization
	}

	/**
	 * Creates a new stomped ground effect. Effect is either temporary and will
	 * disappear, or is permanent and will affect game-play indefinitely.
	 *
	 * @param posX
	 *            x start position
	 * @param posY
	 *            y start position
	 * @param isTemporary
	 *            boolean for whether this effect is temporary or permanent
	 */

	public StompedGroundEffect(Class<?> targetClass, float posX, float posY, boolean isTemporary, float damage, 
            float range) {
        super(targetClass, new Circle2D(posX, posY, 1.414f), 1f, 1f, damage, range, EffectTexture.DAMAGED_GROUND);

		this.isTemporary = isTemporary;
		effectPosition = getMask();
	}

	@Override
	public void onTick(long time) {
		if (isTemporary) {
			timer++;
			if (timer % 200 == 0) {
				if (indexCount < 3) {
					setTexture(currentTextureArray[indexCount]);
					indexCount++;
				} else {
					GameManager.get().getWorld().removeEntity(this);
				}
			}
		}
	}

	/**
	 * Get the current index for the texture being used
	 *
	 * @return Int current index
	 */
	public int getCurrentTextureIndex() {
		return indexCount;
	}

	/**
	 * Return the Shape2D position of the stomp
	 *
	 * @return Shape2D position of footstep
	 */
	public Shape2D getStompedGroundPosition() {
		return effectPosition;
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
