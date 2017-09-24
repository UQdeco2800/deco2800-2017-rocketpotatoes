package com.deco2800.potatoes.entities.projectiles;

import com.badlogic.gdx.math.Vector3;
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
	 */

    public PlayerProjectile(Class<?> targetClass, Vector3 startPos, float range, float damage,
            ProjectileType projectileType, Effect startEffect, Effect endEffect, String directions) {
        super(targetClass, startPos, null, range, damage, projectileType, startEffect, endEffect, directions);
	}


	@Override
	public void onTick(long time) {
		super.onTick(time);
	}
}
