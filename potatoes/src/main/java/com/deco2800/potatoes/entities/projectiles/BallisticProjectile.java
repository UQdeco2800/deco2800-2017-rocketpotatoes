package com.deco2800.potatoes.entities.projectiles;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.effects.Effect;

public class BallisticProjectile extends Projectile {

	public BallisticProjectile() {
		// Blank comment to please the lord Sonar
	}
	
	/**
	 * Creates a new Ballistic Projectile. Ballistic Projectiles do not change
	 * direction once fired. The initial direction is based on the direction to the
	 * closest entity
	 * 
	 * @param targetClass the target entity's class i.e. EnemyEntity, MortalEntity (for all) etc.
	 * @param posX initial
	 * @param posY
	 * @param posZ
	 * @param targetPosX
	 * @param targetPosY
	 * @param targetPosZ
	 * @param range
	 * @param damage
	 * @param projectileTexture
	 * @param startEffect
	 * @param endEffect
	 */
	public BallisticProjectile(Class<?> targetClass, Vector3 startPos, Vector3 targetPos, float range, float damage, ProjectileTexture projectileTexture,
			Effect startEffect, Effect endEffect, String Directions, ShootingStyles shootingStyle) {
		super(targetClass,startPos, targetPos, range, damage, projectileTexture, startEffect, endEffect, Directions, shootingStyle);
	}



	@Override
	public void onTick(long time) {
		super.onTick(time);

	}
}
