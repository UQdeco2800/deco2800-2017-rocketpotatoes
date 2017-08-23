package com.deco2800.potatoes.entities;

import com.deco2800.potatoes.managers.GameManager;

import java.util.Collection;
import java.util.Optional;

public class HomingProjectile extends Projectile {

	private final static transient String TEXTURE = "projectile";
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

	public HomingProjectile() {
		// empty for serialization
	}

	public HomingProjectile(float posX, float posY, float posZ, Optional<AbstractEntity> target, float range,
			float DAMAGE) {
		super(posX, posY, posZ, TEXTURE);
		this.DAMAGE = DAMAGE;
		this.mainTarget = target;
		this.goalX = target.get().getPosX();
		this.goalY = target.get().getPosY();
		this.goalZ = target.get().getPosZ();

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

		this.goalX = mainTarget.get().getPosX();
		this.goalY = mainTarget.get().getPosY();

		float deltaX = getPosX() - this.goalX;
		float deltaY = getPosY() - this.goalY;

		float angle = (float) (Math.atan2(deltaY, deltaX)) + (float) (Math.PI);

		changeX = (float) (speed * Math.cos(angle));
		changeY = (float) (speed * Math.sin(angle));

		boolean maxRange = false;
		if (range < speed) {
			setPosX(goalX);
			setPosY(goalY);
			maxRange = true;
		} else {
			setPosX(getPosX() + changeX);
			setPosY(getPosY() + changeY);
		}

		range -= speed;

		rotateAngle = (int) ((angle * 180 / Math.PI) + 45 + 90);

		Collection<AbstractEntity> entities = GameManager.get().getWorld().getEntities().values();
		for (AbstractEntity entity : entities) {

			if (entity instanceof EnemyEntity && this.collidesWith(entity)) {
				((EnemyEntity) entity).getShot(this);
				GameManager.get().getWorld().removeEntity(this);
				float AOE_width = 5f;
				float AOE_height = 2f;
				ExplosionProjectile exp = new ExplosionProjectile(goalX - (AOE_width / 2), goalY - (AOE_height / 2), 0,
						AOE_width, AOE_height, 0, AOE_width, AOE_height, "aoe1");
				// GameManager.get().getWorld().addEntity(exp);

				return;
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
