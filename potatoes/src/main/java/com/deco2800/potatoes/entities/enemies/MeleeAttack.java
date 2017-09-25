package com.deco2800.potatoes.entities.enemies;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.collisions.Circle2D;

/**
 *  A melee attack that can be performed by an entity to damage another.
 */
public class MeleeAttack extends Effect {

	private final static float effect_width = 1f;
	private final static float effect_height = 1f;

	/**
	 * Empty constructor for serialization
	 */
	public MeleeAttack() {
	}

	/**
	 * Creates a melee attack. Homing Projectiles changes direction once fired. The
	 * initial direction is based on the direction to the closest entity and follows
	 * it.
	 *
	 * @param posX
	 *            x start position
	 * @param posY
	 *            y start position
	 * @param posZ
	 *            z start position
	 * @param target
	 *            Entity target object
	 * @param range
	 *            Projectile range
	 * @param DAMAGE
	 *            Projectile damage
	 */

	public MeleeAttack(Class<?> targetClass, Vector3 startPos, Vector3 targetPos, float damage, float range) {
        super(targetClass, new Circle2D(startPos.x, startPos.y, 7.07f), 1f, 1f, damage, range, EffectTexture.SWIPE);

	}

	@Override
	public void onTick(long time) {
		super.onTick(time);
	}
}
