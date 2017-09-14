package com.deco2800.potatoes.entities.projectiles;

import java.util.Map;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.effects.AOEEffect;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.Box3D;

public class BallisticProjectile extends Projectile {


	public BallisticProjectile() {

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
	 * @param fPosX
	 *            target x position
	 * @param fPosY
	 *            target y position
	 * @param fPosZ
	 *            target z position
	 * @param RANGE
	 *            Projectile range
	 * @param DAMAGE
	 *            Projectile hit damage
	 * @param aoeDAMAGE
	 *            AOE damage
	 */

	public BallisticProjectile(Class<?> targetClass, float posX, float posY, float posZ, float targetPosX,
			float targetPosY, float targetPosZ, float range, float damage, float aoeDAMAGE, Effect endEffect) {
		super(targetClass, posX, posY, posZ, targetPosX, targetPosY, targetPosZ, range, damage, xRenderLength, yRenderLength, endEffect);

	}

	@Override
	public void onTick(long time) {
		super.onTick(time);
	}
}
