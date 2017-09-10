package com.deco2800.potatoes.entities.projectiles;

import java.util.Map;
import java.util.Optional;

import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.effects.AOEEffect;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.Box3D;

public class BallisticProjectile extends Projectile {

	/**
	 * Width and height of AOE sprite. NOTE: (height < width) to give isometric
	 * illusion
	 *
	 * @param AOE_width
	 *            Width of AOE sprite
	 * @param AOE_height
	 *            Height of AOE sprite
	 */

	public BallisticProjectile() {
		rotationAngle = 0;
		DAMAGE = 1;
		// aoeDAMAGE = 1;
		maxRange = false;
	}

	public BallisticProjectile(Class<?> targetClass, float posX, float posY, float posZ, float targetPosX,
			float targetPosY, float targetPosZ, float RANGE, float DAMAGE, float aoeDAMAGE) {
		super(posX, posY, posZ, 1.4f, 1.4f, TEXTURE);
		// change to length
		for (int t= 0; t < 3; t++) {
			textureArray[t] = projectileType + Integer.toString(t + 1);
		}
		this.DAMAGE = DAMAGE;
		this.goalX = targetPosX;
		this.goalY = targetPosY;
		this.goalZ = targetPosZ;
		// this.aoeDAMAGE = aoeDAMAGE;
		this.RANGE = RANGE;
		this.targetClass = targetClass;

		float deltaX = getPosX() - goalX;
		float deltaY = getPosY() - goalY;

		float angle = (float) (Math.atan2(deltaY, deltaX)) + (float) (Math.PI);

		changeX = (float) (speed * Math.cos(angle));
		changeY = (float) (speed * Math.sin(angle));
		// TODO: add changeZ

		rotationAngle = (int) ((angle * 180 / Math.PI) + 45 + 90);

	}

	/**
	 *
	 * ****************************************************************************
	 * ****************************************************************************
	 * DO NOT USE THIS CONSTRUCTOR METHOD, USED FOR TESTING ONLY.
	 * ****************************************************************************
	 * ****************************************************************************
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
	public BallisticProjectile(float posX, float posY, float posZ, float fPosX, float fPosY, float fPosZ, float RANGE,
			float DAMAGE, float aoeDAMAGE) {
		super(posX, posY, posZ, TEXTURE);
		this.DAMAGE = DAMAGE;
		// this.aoeDAMAGE = aoeDAMAGE;
		this.goalX = fPosX;
		this.goalY = fPosY;
		this.goalZ = fPosZ;

		this.RANGE = RANGE;

		float deltaX = getPosX() - goalX;
		float deltaY = getPosY() - goalY;
		float deltaZ = getPosZ() - goalZ;

		float angle = (float) (Math.atan2(deltaY, deltaX)) + (float) (Math.PI);

		changeX = (float) (speed * Math.cos(angle));
		changeY = (float) (speed * Math.sin(angle));
		// TODO: add changeZ

		rotationAngle = (int) ((angle * 180 / Math.PI) + 45 + 90);
	}

	@Override
	public int rotateAngle() {
		return rotationAngle;
	}

	private int rocketEffectTimer;
	private int rocketCurrentSpriteIndexCount;

	@Override
	public void animate() {
		rocketEffectTimer++;
		if (rocketEffectTimer % 4 == 0) {
			setTexture(textureArray[rocketCurrentSpriteIndexCount]);
			if (rocketCurrentSpriteIndexCount == textureArray.length - 1)
				rocketCurrentSpriteIndexCount = 0;
			else
				rocketCurrentSpriteIndexCount++;
		}
	}

	@Override
	public void onTick(long time) {

		updatePos();
		animate();

		Box3D newPos = getBox3D();
		newPos.setX(this.getPosX());
		newPos.setY(this.getPosY());

		Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();
		// Check surroundings
		float AOE_width = 5f;
		float AOE_height = 2f;
		for (AbstractEntity entity : entities.values()) {
			if (targetClass.isInstance(entity)) {
				if (newPos.overlaps(entity.getBox3D())) {
					((MortalEntity) entity).damage(DAMAGE);
					AOEEffect exp = new AOEEffect(goalX - (AOE_width / 2), goalY - (AOE_height / 2), 0, AOE_width,
							AOE_height, 0, AOE_width, AOE_height, 1);
					GameManager.get().getWorld().addEntity(exp);
					GameManager.get().getWorld().removeEntity(this);
				}

			}
		}
		if (maxRange) {
			GameManager.get().getWorld().removeEntity(this);
		}
	}

	public void updatePos() {
		maxRange = false;
		if (RANGE < speed) {
			setPosX(goalX);
			setPosY(goalY);
			setPosZ(goalZ);
			maxRange = true;
		} else {
			setPosX(getPosX() + changeX);
			setPosY(getPosY() + changeY);
		}
		RANGE -= speed;
	}

	/**
	 * Returns Range value
	 */
	public float getRange() {
		return RANGE;
	}

	/**
	 * Returns Damage value
	 */
	@Override
	public float getDamage() {
		return DAMAGE;
	}

	/**
	 * Returns AOE Damage value
	 */
	// public float getAOEDamage() {
	// return aoeDAMAGE;
	// }

}
