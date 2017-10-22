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
	protected Vector3 startPos;
	protected  Vector3 targetPos;
	protected String directions;
	protected Class<?> shootObjectClass;
	protected Class<?> targetClass;
	protected PlayerShootMethod shootingStyle;
	public Projectile projectile;
	public Vector2 mousePos;
	private float damage;
	private Effect startEffect;
	private Effect endEffect;
	private float range;
	private ProjectileTexture projectileTexture;
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
		this.shootObjectClass = shootObjectClass;
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
		this.startPos = startPos;
		this.targetPos = targetPos;
		this.damage = damage;
		this.directions = directions;
		this.shootingStyle = shootingStyle;
		this.startEffect = startEffect;
		this.endEffect = endEffect;
		this.projectileTexture = projectileTexture;
		this.range = range;
		this.targetClass = targetClass;
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
		// System.out.println("second");

		mousePos = Render3D.screenToTile(GameManager.get().getManager(InputManager.class).getMouseX(),
				GameManager.get().getManager(InputManager.class).getMouseY());
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
					projectile.setTargetPosition(startPos.x - 5, startPos.y - 5, 0);
					break;
				case "east":
					projectile.setTargetPosition(startPos.x + 5, startPos.y + 5, 0);
					break;
				case "north":
					projectile.setTargetPosition(startPos.x + 15, startPos.y - 15, 0);
					break;
				case "south":
					projectile.setTargetPosition(startPos.x - 15, startPos.y + 15, 0);
					break;
				case "north-east":
					projectile.setTargetPosition(startPos.x + 15, startPos.y + 1, 0);
					break;
				case "north-west":
					projectile.setTargetPosition(startPos.x - 15, startPos.y - 200, 0);
					break;
				case "south-east":
					projectile.setTargetPosition(startPos.x + 20, startPos.y + 200, 0);
					break;
				case "south-west":
					projectile.setTargetPosition(startPos.x - 200, startPos.y - 20, 0);
					break;
			}
		}
		if (shootingStyle == PlayerShootMethod.MOUSE) {

			mousePos = Render3D.screenToTile(GameManager.get().getManager(InputManager.class).getMouseX(),
					GameManager.get().getManager(InputManager.class).getMouseY());
			if (HomingProjectile.class.isAssignableFrom(shootObjectClass)) {
				((HomingProjectile) projectile).setHomingDelay(10);
			}
			projectile.setTargetPosition(mousePos.x, mousePos.y, 0);
		}
	}


	public float getDamage(){
		return damage;
	}

	@Override
	public Effect getStartEffect() {
		return startEffect;
	}

	@Override
	public Effect getEndEffect() {
		return endEffect;
	}

	public ProjectileTexture getProjectileTexture() {
		return projectileTexture;
	}

	public float getPosX(){
		return startPos.x;
	}

	public float getPosY(){
		return startPos.y;
	}

	public float getRange(){
		return range;
	}

	public float getTargetPosX(){
		return targetPos.x;
	}

	public float getTargetPosY(){
		return targetPos.y;
	}

	public Class<?> getTargetClass(){
		return targetClass;
	}
}