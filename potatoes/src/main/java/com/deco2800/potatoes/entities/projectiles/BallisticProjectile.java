package com.deco2800.potatoes.entities.projectiles;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.effects.Effect;

public class BallisticProjectile extends Projectile {
	protected float pPosX;
	protected float pPosY;
	protected float tPosX;
	protected float tPosY;
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
			Effect startEffect, Effect endEffect, String Directions, PlayerProjectile.PlayerShootMethod shootingStyle) {
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
		super.onTick(time);

	}
	public float getTargetPosX() {
		return tPosX;
	}

	public float getTargetPosY() {
		return tPosY;
	}


}
