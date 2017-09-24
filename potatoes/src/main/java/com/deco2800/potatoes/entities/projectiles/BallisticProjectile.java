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
	 * Creates a new projectile. A projectile is the vehicle used to deliver damage
	 * to a target over a distance
	 *
	 * @param targetClass       the targets class
	 * @param startPos
	 * @param targetPos
	 * @param range
	 * @param damage            damage of projectile
	 * @param projectileTexture the texture set to use for animations. Use ProjectileTexture._
	 * @param startEffect       the effect to play at the start of the projectile being fired
	 * @param endEffect         the effect to be played if a collision occurs
	 */
	public BallisticProjectile(Class<?> targetClass, Vector3 startPos, Vector3 targetPos, float range, float damage, ProjectileTexture projectileTexture,
			Effect startEffect, Effect endEffect) {
		super(targetClass,startPos, targetPos, range, damage, projectileTexture, startEffect, endEffect);

		this.pPosX = startPos.x;
		this.pPosY = startPos.y;
		this.tPosX = targetPos.x;
		this.tPosY = targetPos.y;
	}

	@Override
	public void onTick(long time) {
		super.onTick(time);

	}
	/**
	 * Returns Target Pos X
	 * */
	public float getTargetPosX() {
		return tPosX;
	}
	/**
	 * Returns Target Pos Y
	 * */
	public float getTargetPosY() {
		return tPosY;
	}


}
