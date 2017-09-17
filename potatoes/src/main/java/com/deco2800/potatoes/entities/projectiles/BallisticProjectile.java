package com.deco2800.potatoes.entities.projectiles;

import com.badlogic.gdx.Game;
import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;

public class BallisticProjectile extends Projectile {

	public BallisticProjectile() {
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
	 * @param targetPosX
	 *            target x position
	 * @param targetPosY
	 *            target y position
	 * @param targetPosZ
	 *            target z position
	 * @param range
	 *            Projectile range
	 * @param damage
	 *            Projectile hit damage
	 *
	 */

	public BallisticProjectile(Class<?> targetClass, float posX, float posY, float posZ, float targetPosX,
			float targetPosY, float targetPosZ, float range, float damage, String projectileType, Effect startEffect,
			Effect endEffect) {
		super(targetClass, posX, posY, posZ, targetPosX, targetPosY, targetPosZ, range, damage, projectileType,
				startEffect, endEffect);
	}

	@Override
	public void onTick(long time) {
		super.onTick(time);
//		System.out.println(GameManager.get().getManager(PlayerManager.class).getPlayer().getPosX());

	}
}
