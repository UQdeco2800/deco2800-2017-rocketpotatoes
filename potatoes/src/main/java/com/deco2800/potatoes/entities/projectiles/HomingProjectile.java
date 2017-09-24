package com.deco2800.potatoes.entities.projectiles;

import java.util.Optional;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.WorldUtil;

public class HomingProjectile extends Projectile {
	protected float pPosX;
	protected float pPosY;
	protected float tPosX;
	protected float tPosY;
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
	public HomingProjectile(Class<?> targetClass, Vector3 startPos, Vector3 targetPos, float range, float damage, ProjectileTexture projectileTexture, Effect startEffect,
							Effect endEffect, String Directions, PlayerProjectile.PlayerShootMethod shootingStyle) {
		super(targetClass,startPos, targetPos, range, damage, projectileTexture, startEffect, endEffect, Directions, shootingStyle);

		this.pPosX = startPos.x;
		this.pPosY = startPos.y;
		this.tPosX = targetPos.x;
		this.tPosY = targetPos.y;
	}

	public PlayerProjectile.PlayerShootMethod getPlayerShootMethod() {
		return playerShootMethod;
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

	public float getTargetPosX() {
		return tPosX;
	}

	public float getTargetPosY() {
		return tPosY;
	}

}
