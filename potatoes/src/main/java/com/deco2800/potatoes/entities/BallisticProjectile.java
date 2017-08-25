package com.deco2800.potatoes.entities;

import java.util.Collection;
import java.util.Optional;

import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.deco2800.potatoes.managers.GameManager;

public class BallisticProjectile extends Projectile{
	
	private final static transient String TEXTURE = "projectile";
	private float DAMAGE = 1;
	
	private float goalX;
	private float goalY;
	private float goalZ;
	
	private float range;
	
	private final float speed = 0.2f;
	private Optional<AbstractEntity> mainTarget;
	private float changeX;
	private float changeY;

	public BallisticProjectile() {
		// empty for serialization
	}


	public BallisticProjectile(float posX, float posY, float posZ, Optional<AbstractEntity> target, float goalZ, float range, float DAMAGE) {
		super(posX, posY, posZ, TEXTURE);
		this.DAMAGE = DAMAGE;
		this.mainTarget = target;
		this.goalX = target.get().getPosX();
		this.goalY = target.get().getPosY();
		this.goalZ = goalZ;
		
		this.range = range;
		
		float deltaX = getPosX() - goalX;
		float deltaY = getPosY() - goalY;

		float angle = (float)(Math.atan2(deltaY, deltaX)) + (float)(Math.PI);

		changeX = (float)(speed * Math.cos(angle));
		changeY = (float)(speed * Math.sin(angle));
	}

	@Override
	public void onTick(long time) {
		boolean maxRange = false;
		if(range < speed) {
			setPosX(goalX);
			setPosY(goalY);
			maxRange = true;
		} else {
			setPosX(getPosX() + changeX);
			setPosY(getPosY() + changeY);
		}
		range -= speed;
		
		Collection<AbstractEntity> entities = GameManager.get().getWorld().getEntities().values();
		for (AbstractEntity entity : entities) {

			if (entity instanceof EnemyEntity && this.collidesWith(entity)) {
				((EnemyEntity)entity).getShot(this);
				GameManager.get().getWorld().removeEntity(this);
				float AOE_width = 5f;
				float AOE_height = 2f;
				ExplosionProjectile exp = new ExplosionProjectile(goalX - (AOE_width / 2), goalY - (AOE_height / 2), 0,
						AOE_width, AOE_height, 0, AOE_width, AOE_height, 50);
				 GameManager.get().getWorld().addEntity(exp);

				return;
			}
		}
		
		if(maxRange) {
			GameManager.get().getWorld().removeEntity(this);
		}
	}

	@Override
	public float getDamage() {
		return DAMAGE;
	}

}
