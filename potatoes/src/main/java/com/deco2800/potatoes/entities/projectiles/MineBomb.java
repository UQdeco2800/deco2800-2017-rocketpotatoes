package com.deco2800.potatoes.entities.projectiles;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.InputManager;
import com.deco2800.potatoes.renderering.Render3D;

public class MineBomb extends Projectile {
	protected float pPosX;
	protected float pPosY;
	protected float tPosX;
	protected float tPosY;
	protected String directions;
	protected Class<?> shootObjectClass;
	public Projectile projectile;

	public MineBomb() {
		// Blank comment to please the lord Sonar
	}

	/**
	 * Creates a new projectile. A projectile is the vehicle used to deliver damage
	 * to a target over a distance
	 *
	 * @param targetClass
	 *            the targets class
	 * @param startPos
	 * @param targetPos
	 * @param range
	 * @param damage
	 *            damage of projectile
	 * @param projectileTexture
	 *            the texture set to use for animations. Use ProjectileTexture._
	 * @param startEffect
	 *            the effect to play at the start of the projectile being fired
	 * @param endEffect
	 *            the effect to be played if a collision occurs
	 * @param directions
	 *            Player Directions
	 * @param shootingStyle
	 *            Directional, Homing or Ballistic?
	 */

	public MineBomb(Class<?> targetClass, Vector3 startPos, Vector3 targetPos, float range, float damage,
                    ProjectileTexture projectileTexture, Effect startEffect, Effect endEffect, String directions,
                    Class<?> shootObjectClass) {

	}

	@Override
	public void onTick(long time) {

	}



	/**
	 * Returns Target Pos X
	 */
	public float getTargetPosX() {
		return tPosX;
	}

	/**
	 * Returns Target Pos Y
	 */
	public float getTargetPosY() {
		return tPosY;
	}

}
