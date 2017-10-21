package com.deco2800.potatoes.entities.projectiles;

import java.util.Optional;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.InputManager;
import com.deco2800.potatoes.renderering.Render3D;
import com.deco2800.potatoes.util.WorldUtil;

public class PlayerProjectile extends Projectile {
	protected float pPosX;
	protected float pPosY;
	protected float tPosX;
	protected float tPosY;
	protected String directions;
	protected PlayerShootMethod shootingStyle;
	public Projectile projectile;

	public enum PlayerShootMethod {
		DIRECTIONAL, CLOSEST, MOUSE
	}

	public PlayerProjectile() {
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

	public PlayerProjectile(Class<?> targetClass, Vector3 startPos, Vector3 targetPos, float range, float damage,
			ProjectileTexture projectileTexture, Effect startEffect, Effect endEffect, String directions,
			PlayerShootMethod shootingStyle, Class<?> shootObjectClass) {
		if (BallisticProjectile.class.isAssignableFrom(shootObjectClass)) {
			projectile = new BallisticProjectile(targetClass, startPos, targetPos, range, damage, projectileTexture,
					startEffect, endEffect);
		} else if (OrbProjectile.class.isAssignableFrom(shootObjectClass)) {
			projectile = new OrbProjectile(targetClass, startPos, targetPos, range, damage, projectileTexture,
					startEffect, endEffect);
		} // else if (BombProjectile.class.isAssignableFrom(shootObjectClass)) {
			// projectile = new BombProjectile(targetClass, startPos, targetPos, range,
			// damage, projectileTexture,
			// startEffect, endEffect);
			// }
		else if (HomingProjectile.class.isAssignableFrom(shootObjectClass)) {
			projectile = new HomingProjectile(targetClass, startPos, targetPos, range, damage, projectileTexture,
					startEffect, endEffect);
		} else {
			projectile = new BallisticProjectile(targetClass, startPos, targetPos, range, damage, projectileTexture,
					startEffect, endEffect);
		}
		GameManager.get().getWorld().addEntity(projectile);
		this.pPosX = startPos.x;
		this.pPosY = startPos.y;
		this.tPosX = targetPos.x;
		this.tPosY = targetPos.y;
		this.directions = directions;
		this.shootingStyle = shootingStyle;
		ShootingStyle(shootingStyle);
	}

	@Override
	public void onTick(long time) {
		// if ("HOMING".equalsIgnoreCase(shootingStyle.toString())) {
		// Optional<AbstractEntity> targetEntity =
		// WorldUtil.getClosestEntityOfClass(targetClass, targetPos.x,
		// targetPos.y);
		// if (targetEntity.isPresent()) {
		// projectile.setTargetPosition(targetEntity.get().getPosX(),
		// targetEntity.get().getPosY(),
		// targetEntity.get().getPosZ());
		// } else {
		// GameManager.get().getWorld().removeEntity(projectile);
		// GameManager.get().getWorld().removeEntity(this);
		// }
		// projectile.updatePosition();
		// }
		// if (shootingStyle == PlayerShootMethod.ORB) {
		// ((OrbProjectile)projectile).
		//// }
	}

	/**
	 * Returns selected shooting styles
	 */
	public PlayerProjectile.PlayerShootMethod getPlayerShootMethod() {
		return shootingStyle;
	}

	public void ShootingStyle(PlayerShootMethod shootingStyle) {

		/**
		 * Shoots enemies base on the player direction
		 */
		if (shootingStyle == PlayerShootMethod.DIRECTIONAL) {
			switch (directions.toLowerCase()) {
			case "west":
				projectile.setTargetPosition(pPosX - 5, pPosY - 5, 0);
				//projectile.updatePosition();
				//projectile.setPosition();
				break;
			case "east":
				projectile.setTargetPosition(pPosX + 5, pPosY + 5, 0);
				//projectile.updatePosition();
				//projectile.setPosition();
				break;
			case "north":
				projectile.setTargetPosition(pPosX + 15, pPosY - 15, 0);
				//projectile.updatePosition();
				//projectile.setPosition();
				break;
			case "south":
				projectile.setTargetPosition(pPosX - 15, pPosY + 15, 0);
				//projectile.updatePosition();
				//projectile.setPosition();
				break;
			case "north-east":
				projectile.setTargetPosition(pPosX + 15, pPosY + 1, 0);
				//projectile.updatePosition();
				//projectile.setPosition();
				break;
			case "north-west":
				projectile.setTargetPosition(pPosX - 15, pPosY - 200, 0);
				//projectile.updatePosition();
				//projectile.setPosition();
				break;
			case "south-east":
				projectile.setTargetPosition(pPosX + 20, pPosY + 200, 0);
				//projectile.updatePosition();
				//projectile.setPosition();
				break;
			case "south-west":
				projectile.setTargetPosition(pPosX - 200, pPosY - 20, 0);
				//projectile.updatePosition();
				//projectile.setPosition();
				break;
			}
		}
		if (shootingStyle == PlayerShootMethod.MOUSE) {
			Vector2 mousePos = Render3D.screenToTile(GameManager.get().getManager(InputManager.class).getMouseX(),
					GameManager.get().getManager(InputManager.class).getMouseY());
			projectile.setTargetPosition(mousePos.x, mousePos.y, 0);
		}

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
