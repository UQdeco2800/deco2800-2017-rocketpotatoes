package com.deco2800.potatoes.entities.enemies;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.entities.projectiles.Projectile;

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
		super(targetClass, startPos, 5f, 5f, 0, 1f, 1f, damage, range, EffectType.SWIPE);
	}

	@Override
	public void onTick(long time) {
		super.onTick(time);
		/*
		 * endEffect = new SwipeEffect(goalX - (effect_width / 2), goalY -
		 * (effect_height / 2) + 1, 0, effect_width, effect_height, 0, effect_width,
		 * effect_height);
		 */
	}

	/**
	 * Remove projectile after waiting a certain amount of ticks
	 */
	private static void removeProjectileLater(int ticks, Projectile projectile) {
	}
}

/*
 * for (AbstractEntity entity : entities.values()) { if
 * (targetClass.isInstance(entity)) { if (newPos.overlaps(entity.getBox3D())) {
 * if (ticksWaited == 0) { ((MortalEntity) entity).damage(DAMAGE);
 * ExplosionEffect expEffect = new ExplosionEffect(goalX, goalY, goalZ, 5f, 5f,
 * 0, 1f, 1f); GameManager.get().getWorld().addEntity(expEffect); ticksWaited++;
 * } else if (ticksWaited < 3) { ticksWaited++; } else {
 * GameManager.get().getWorld().removeEntity(this); ticksWaited = 0; } } } }
 */