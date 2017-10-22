package com.deco2800.potatoes.entities.projectiles;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.entities.player.Player.ShootStage;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;

public class OrbProjectile extends Projectile {

	private float chargeTime = 5f;
	private float chargeAmount = 0.01f;
	private float maxCharge = 5f;
	private int currentCharge = 0;
	private int numTexPerCharge = 3;
	private boolean canFire = false;
	private Vector3 playerPos;
	public boolean hasBeenReleased=false;

	private Class<?> targetClass;
	private Vector3 targetPos;
	private float pPosX;
	private float pPosY;
	private float tPosX;
	private float tPosY;
	private float range;
	private float damage;
	private ProjectileTexture projectileTexture;
	private Effect startEffect;
	private Effect endEffect;

	public OrbProjectile() {

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

	public OrbProjectile(Class<?> targetClass, Vector3 startPos, Vector3 targetPos, float range, float damage,
						 ProjectileTexture projectileTexture, Effect startEffect, Effect endEffect) {
		super(targetClass, startPos, targetPos, range, damage, projectileTexture, startEffect, endEffect);
		playerPos = startPos;
		this.pPosX = startPos.x;
		this.pPosY = startPos.y;
		this.targetClass = targetClass;
		this.targetPos = targetPos;
		this.range = range;
		this.damage = damage;
		this.projectileTexture = projectileTexture;
		this.startEffect = startEffect;
		this.endEffect = endEffect;
	}

	@Override
	public void onTick(long time) {

		Player p = GameManager.get().getManager(PlayerManager.class).getPlayer();
		if (p.currentShootStage==ShootStage.HOLDING&&!hasBeenReleased) {
			charge(new Vector3(p.getPosX(), p.getPosY(), p.getPosZ()));
			setTargetPosition(p.mousePos.x, p.mousePos.y, 0);
		} else if(p.currentShootStage==ShootStage.LOOSE) {
			fire();
		}
		if (canFire) {
			// dont remove until range reached
			if (range < SPEED) {
				canRemove = true;
			} else {
				canRemove = false;
			}
			super.onTick(time);
		}
	}

	@Override
	protected void animate() {
		projectileEffectTimer++;
		if (projectileEffectTimer % 4 == 0) {
			setTexture(projectileTexture.textures()[(int) (Math.floor((double) currentCharge)) * numTexPerCharge
					+ projectileCurrentSpriteIndexCount]);
			if (projectileCurrentSpriteIndexCount == projectileTexture.textures().length / numTexPerCharge - 1)
				projectileCurrentSpriteIndexCount = 0;
			else {
				projectileCurrentSpriteIndexCount++;
			}
		}
	}

	public void charge(Vector3 playerPos) {
		this.playerPos = playerPos;
		setPosX(playerPos.x);
		setPosY(playerPos.y);
		if (currentCharge < maxCharge)
			currentCharge += chargeAmount;
	}

	public void fire() {
		canFire = true;
	}

	public static float getShadowRadius() {
		return shadowRadius;
	}

	public void hasBeenReleased(boolean tf) {
		hasBeenReleased=tf;
	}

	public float getRange(){
		return range;
	}

	public Class<?> getTargetClass(){
		return targetClass;
	}

	public float getDamage(){
		return damage;
	}

	public Vector3 getPos(){
		return playerPos;
	}

	public ProjectileTexture getProjectileTexture() {
		return projectileTexture;
	}

	@Override
	public Effect getStartEffect() {
		return startEffect;
	}

	@Override
	public Effect getEndEffect() {
		return endEffect;
	}



}
