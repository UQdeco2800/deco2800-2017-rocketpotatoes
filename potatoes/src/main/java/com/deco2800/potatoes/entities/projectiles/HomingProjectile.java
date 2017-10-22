package com.deco2800.potatoes.entities.projectiles;

import java.util.Optional;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.WorldUtil;

public class HomingProjectile extends Projectile {

	int homingDelay = 0;

	public HomingProjectile() {

	}

	/**
	 * Creates a new projectile. A projectile is the vehicle used to deliver damage
	 * to a target over a distance
	 *
	 * @param targetClass
	 *            the targets class
	 * @param startPos
	 * @param targetPos
	 * 
	 * @param range
	 * @param damage
	 *            damage of projectile
	 * @param projectileTexture
	 *            the texture set to use for animations. Use ProjectileTexture._
	 * @param startEffect
	 *            the effect to play at the start of the projectile being fired
	 * @param endEffect
	 *            the effect to be played if a collision occurs
	 */

	public HomingProjectile(Class<?> targetClass, Vector3 startPos, Vector3 targetPos, float range, float damage,
			ProjectileTexture projectileTexture, Effect startEffect, Effect endEffect) {
		super(targetClass, startPos, targetPos, range, damage, projectileTexture, startEffect, endEffect);

	}

	int i = 0;

	@Override
	public void onTick(long time) {
		if (i < homingDelay) {
			i++;
		} else {
			Optional<AbstractEntity> targetEntity = WorldUtil.getClosestEntityOfClass(targetClass, targetPos.x,
					targetPos.y);
			if (targetEntity.isPresent()) {
				targetPos.lerp(new Vector3(targetEntity.get().getPosX(), targetEntity.get().getPosY(),
						targetEntity.get().getPosZ()), 0.05f);
				setTargetPosition(targetPos.x,targetPos.y,targetPos.z);
			} 
		}
		super.onTick(time);

	}

	public void setHomingDelay(int numOfFrames) {
		this.homingDelay = numOfFrames;
	}

}
