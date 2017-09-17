package com.deco2800.potatoes.entities.projectiles;

import java.util.Optional;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.WorldUtil;

public class HomingProjectile extends Projectile {

	public HomingProjectile() {

	}

	/**
	 * Creates a new Homing Projectile. Homing Projectiles changes direction once
	 * fired. The initial direction is based on the direction to the closest entity
	 * and follows it.
	 *
	 * @param posX
	 *            x start position
	 * @param posY
	 *            y start position
	 * @param posZ
	 *            z start position
	 * @param targetPosX
	 *            target x position
	 * @param targetPosY
	 *            target y position
	 * @param targetPosZ
	 *            target z position
	 * @param range
	 *            Projectile range
	 * @param DAMAGE
	 *            Projectile damage
	 */
	public HomingProjectile(Class<?> targetClass, float posX, float posY, float posZ, float targetPosX,
			float targetPosY, float targetPosZ, float range, float damage, String projectileType, Effect startEffect,
			Effect endEffect) {
		super(targetClass, posX, posY, posZ, targetPosX, targetPosY, targetPosZ, range, damage, projectileType,startEffect, endEffect);
	}

	@Override
	public void onTick(long time) {
		Optional<AbstractEntity> targetEntity = WorldUtil.getClosestEntityOfClass(targetClass, this.goalX, this.goalY);
		if (targetEntity != null) {
			setTargetPosition(targetEntity.get().getPosX(), targetEntity.get().getPosY(), targetEntity.get().getPosZ());
		} else {
			GameManager.get().getWorld().removeEntity(this);
		}

		super.onTick(time);

	}
}
