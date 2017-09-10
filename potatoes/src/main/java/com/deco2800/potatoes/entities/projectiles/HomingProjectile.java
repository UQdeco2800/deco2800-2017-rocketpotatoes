package com.deco2800.potatoes.entities.projectiles;

import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.effects.ExplosionEffect;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.Box3D;

import java.util.Map;
import java.util.Optional;

public class HomingProjectile extends Projectile {

	public HomingProjectile() {
		DAMAGE = 1;
		rotationAngle = 0;
		maxRange = false;
	}

	public HomingProjectile(Class<?> targetClass, float posX, float posY, float posZ, float targetPosX,
			float targetPosY, float targetPosZ, float range, float DAMAGE, String projectileType) {
		super(posX, posY, posZ, 1.5f, 1.5f, TEXTURE);
		for (int t = 0; t < 3; t++) {
			textureArray[t] = projectileType + Integer.toString(t + 1);
		}
		this.DAMAGE = DAMAGE;
		this.goalX = targetPosX;
		this.goalY = targetPosY;
		this.goalZ = targetPosZ;
		this.projectileType = projectileType;

		this.RANGE = range;
		this.targetClass = targetClass;

		float deltaX = getPosX() - goalX;
		float deltaY = getPosY() - goalY;

		float angle = (float) (Math.atan2(deltaY, deltaX)) + (float) (Math.PI);

		changeX = (float) (speed * Math.cos(angle));
		changeY = (float) (speed * Math.sin(angle));

		rotationAngle = (int) ((angle * 180 / Math.PI) + 45 + 90);
	}

	/**
	 * ****************************************************************************
	 * ****************************************************************************
	 * DO NOT USE THIS CONSTRUCTOR METHOD, USED FOR TESTING ONLY.
	 * ****************************************************************************
	 * ****************************************************************************
	 * <p>
	 * Creates a new Homing Projectile. Homing Projectiles changes direction once
	 * fired. The initial direction is based on the direction to the closest entity
	 * and follows it.
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
	 * @param DAMAGE
	 *            Projectile damage
	 */

	public HomingProjectile(float posX, float posY, float posZ, float targetPosX, float targetPosY, float targetPosZ,
			float range, float DAMAGE, String projectileType) {
		super(posX, posY, posZ, 1, 2, TEXTURE);
		this.DAMAGE = DAMAGE;
		this.goalX = targetPosX;
		this.goalY = targetPosY;
		this.goalZ = targetPosZ;
		this.projectileType = projectileType;
		this.RANGE = range;

		float deltaX = getPosX() - goalX;
		float deltaY = getPosY() - goalY;

		float angle = (float) (Math.atan2(deltaY, deltaX)) + (float) (Math.PI);

		changeX = (float) (speed * Math.cos(angle));
		changeY = (float) (speed * Math.sin(angle));

		rotationAngle = (int) ((angle * 180 / Math.PI) + 45 + 90);
	}

	@Override
	public int rotateAngle() {
		return rotationAngle;
	}

	// must be called for homing to take place
	public void updateTargetPosition(float xPos, float yPos, float zPos) {
		this.goalX = xPos;
		this.goalY = yPos;
		this.goalZ = zPos;
	}

	@Override
	public void onTick(long time) {

		animate();

		float deltaX = getPosX() - this.goalX;
		float deltaY = getPosY() - this.goalY;

		float angle = (float) (Math.atan2(deltaY, deltaX)) + (float) (Math.PI);

		changeX = (float) (speed * Math.cos(angle));
		changeY = (float) (speed * Math.sin(angle));

		setPosX(getPosX() + changeX);
		setPosY(getPosY() + changeY);

		if (RANGE < speed) {
			maxRange = true;
		}

		RANGE -= speed;

		rotationAngle = (int) ((angle * 180 / Math.PI) + 45 + 90);

		Box3D newPos = getBox3D();
		newPos.setX(this.getPosX());
		newPos.setY(this.getPosY());

		Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();

		for (AbstractEntity entity : entities.values()) {
			if (targetClass.isInstance(entity)) {
				if (newPos.overlaps(entity.getBox3D())) {
					((MortalEntity) entity).damage(DAMAGE);
					ExplosionEffect expEffect = new ExplosionEffect(goalX, goalY, goalZ, 5f, 5f, 0, 1f, 1f);
					GameManager.get().getWorld().removeEntity(this);
					GameManager.get().getWorld().addEntity(expEffect);

				}

			}
		}
		if (maxRange) {
			GameManager.get().getWorld().removeEntity(this);
		}
	}

	@Override
	public float getDamage() {
		return DAMAGE;
	}

	private int projectileEffectTimer;
	private int projectileCurrentSpriteIndexCount;

	@Override
	public void animate() {
		projectileEffectTimer++;
		if (projectileEffectTimer % 4 == 0) {
			if (projectileType.equalsIgnoreCase("rocket")) {
				setTexture(textureArray[projectileCurrentSpriteIndexCount]);
				if (projectileCurrentSpriteIndexCount == textureArray.length - 1)
					projectileCurrentSpriteIndexCount = 0;
				else {
					projectileCurrentSpriteIndexCount++;
				}
			} else if (projectileType.equalsIgnoreCase("chilli")) {

				setTexture(textureArray[projectileCurrentSpriteIndexCount]);
				if (projectileCurrentSpriteIndexCount == textureArray.length - 1)
					projectileCurrentSpriteIndexCount = 0;
				else {
					projectileCurrentSpriteIndexCount++;
				}
			}

		}

	}

}
