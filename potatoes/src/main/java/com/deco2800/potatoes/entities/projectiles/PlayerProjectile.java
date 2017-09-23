package com.deco2800.potatoes.entities.projectiles;

import com.deco2800.potatoes.entities.effects.Effect;

public class PlayerProjectile extends Projectile {

	public PlayerProjectile() {
		//Blank comment to please the lord Sonar
	}

	/**
	 * Creates a new Ballistic Projectile. Ballistic Projectiles do not change
	 * direction once fired. The initial direction is based on the direction to the
	 * closest entity
	 *
	 * @param posX
	 *            x start position
	 * @param posY
	 *            y start position
	 * @param posZ
	 *            z start position

	 */

	public PlayerProjectile(Class<?> targetClass, float posX, float posY, float posZ, float range, float damage, ProjectileType projectileType, Effect startEffect,
                            Effect endEffect, String Directions, float TargetPosX, float TargetPosY, ShootingStyles shootingStyle) {
		super(targetClass,posX, posY, posZ, range, damage, projectileType, startEffect, endEffect, Directions, TargetPosX, TargetPosY, shootingStyle);
	}



	@Override
	public void onTick(long time) {
		super.onTick(time);
	}
}
