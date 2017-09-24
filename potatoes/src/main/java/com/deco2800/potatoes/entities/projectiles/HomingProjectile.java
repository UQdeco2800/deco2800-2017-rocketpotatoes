package com.deco2800.potatoes.entities.projectiles;

import java.util.Optional;

import com.badlogic.gdx.math.Vector3;
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
	public HomingProjectile(Class<?> targetClass, Vector3 startPos, Vector3 targetPos, float range, float damage, ProjectileType projectileType, Effect startEffect,
			Effect endEffect) {
		super(targetClass, startPos, targetPos, range, damage, projectileType,
				startEffect, endEffect);
	}

	@Override
	public void onTick(long time) {
		Optional<AbstractEntity> targetEntity = WorldUtil.getClosestEntityOfClass(targetClass, targetPos.x, targetPos.y);
		if (targetEntity.isPresent()) {
			setTargetPosition(targetEntity.get().getPosX(), targetEntity.get().getPosY(), targetEntity.get().getPosZ());
		} else {
			GameManager.get().getWorld().removeEntity(this);
		}
		updatePosition();
		super.onTick(time);

	}
}
