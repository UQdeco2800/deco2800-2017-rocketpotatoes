package com.deco2800.potatoes.entities.projectiles;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.MortalEntity;
import com.deco2800.potatoes.entities.effects.ExplosionEffect;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.Box3D;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class HomingProjectile extends Projectile {

	private final static transient String TEXTURE = "rocket1";
	private float DAMAGE = 1;

	private float goalX;
	private float goalY;
	private float goalZ;

	private int rotateAngle = 0;

	private float range;

	private final float speed = 0.2f;

	private float changeX;
	private float changeY;
	private Optional<AbstractEntity> mainTarget;
	private boolean maxRange = false;

	private int rocketEffectTimer;
	private int rocketCurrentSpriteIndexCount;
	private String[] rocketSpriteArray = { "rocket1", "rocket2", "rocket3" };

	private Class<?> targetClass;

	public HomingProjectile() {
		// empty for serialization
		DAMAGE = 1;
		rotateAngle = 0;
		maxRange = false;
	}

	/**
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
	 * @param target
	 *            Entity target object
	 * @param range
	 *            Projectile range
	 * @param DAMAGE
	 *            Projectile damage
	 */

	public HomingProjectile(Class<?> targetClass, float posX, float posY, float posZ, Optional<AbstractEntity> target,
			float range, float DAMAGE) {
		super(posX, posY, posZ, 1, 1, TEXTURE);
		this.DAMAGE = DAMAGE;
		this.mainTarget = target;
		this.goalX = target.get().getPosX();
		this.goalY = target.get().getPosY();
		this.goalZ = target.get().getPosZ();

		this.range = range;
		this.targetClass = targetClass;

		float deltaX = getPosX() - goalX;
		float deltaY = getPosY() - goalY;

		float angle = (float) (Math.atan2(deltaY, deltaX)) + (float) (Math.PI);

		changeX = (float) (speed * Math.cos(angle));
		changeY = (float) (speed * Math.sin(angle));

		rotateAngle = (int) ((angle * 180 / Math.PI) + 45 + 90);
	}

	/**
	 * ****************************************************************************
	 * ****************************************************************************
	 * DO NOT USE THIS CONSTRUCTOR METHOD, USED FOR TESTING ONLY.
	 * ****************************************************************************
	 * ****************************************************************************
	 *
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
	 * @param fposX
	 *            target x position
	 * @param fposY
	 *            target y position
	 * @param fposZ
	 *            target z position
	 * @param range
	 *            Projectile range
	 * @param DAMAGE
	 *            Projectile damage
	 */

	public HomingProjectile(float posX, float posY, float posZ, float fposX, float fposY, float fposZ, float range,
			float DAMAGE) {
		super(posX, posY, posZ, 1, 2, TEXTURE);
		this.DAMAGE = DAMAGE;
		this.goalX = fposX;
		this.goalY = fposY;
		this.goalZ = fposZ;

		this.range = range;

		float deltaX = getPosX() - goalX;
		float deltaY = getPosY() - goalY;

		float angle = (float) (Math.atan2(deltaY, deltaX)) + (float) (Math.PI);

		changeX = (float) (speed * Math.cos(angle));
		changeY = (float) (speed * Math.sin(angle));

		rotateAngle = (int) ((angle * 180 / Math.PI) + 45 + 90);
	}

	public int rotateAngle() {
		return rotateAngle;
	}

	@Override
	public void onTick(long time) {

		if (mainTarget != null) {
			this.goalX = mainTarget.get().getPosX();
			this.goalY = mainTarget.get().getPosY();
		}

		float deltaX = getPosX() - this.goalX;
		float deltaY = getPosY() - this.goalY;

		float angle = (float) (Math.atan2(deltaY, deltaX)) + (float) (Math.PI);

		changeX = (float) (speed * Math.cos(angle));
		changeY = (float) (speed * Math.sin(angle));

		setPosX(getPosX() + changeX);
		setPosY(getPosY() + changeY);

		if (range < speed) {
			maxRange = true;
		}

		range -= speed;

		rocketEffectTimer++;
		if (rocketEffectTimer % 4 == 0) {
			setTexture(rocketSpriteArray[rocketCurrentSpriteIndexCount]);
			if (rocketCurrentSpriteIndexCount == rocketSpriteArray.length - 1)
				rocketCurrentSpriteIndexCount = 0;
			else
				rocketCurrentSpriteIndexCount++;
		}

		rotateAngle = (int) ((angle * 180 / Math.PI) + 45 + 90);

		Box3D newPos = getBox3D();
		newPos.setX(this.getPosX());
		newPos.setY(this.getPosY());

		Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();
		// Check surroundings
		for (AbstractEntity entity : entities.values()) {
			if (targetClass.isInstance(entity)) {
					if (newPos.overlaps(entity.getBox3D())) {
						((MortalEntity) entity).damage(DAMAGE);
						ExplosionEffect expEffect = new ExplosionEffect(goalX, goalY, goalZ, 5f, 5f, 0, 1f, 1f);
						GameManager.get().getWorld().addEntity(expEffect);
						GameManager.get().getWorld().removeEntity(this);
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


}
