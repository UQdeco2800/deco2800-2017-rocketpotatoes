package com.deco2800.potatoes.entities;

import java.util.List;

import com.deco2800.potatoes.managers.GameManager;

public class BallisticProjectile extends Projectile{
	
	private final static String TEXTURE = "projectile";
	private final static float DAMAGE = 40;
	
	private float goalX;
	private float goalY;
	private float goalZ;
	
	private float range;
	
	private final float speed = 0.2f;
	
	private float changeX;
	private float changeY;


	public BallisticProjectile(float posX, float posY, float posZ, float goalX, float goalY, float goalZ, float range) {
		super(posX, posY, posZ, TEXTURE);
		this.goalX = goalX;
		this.goalY = goalY;
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
		
		List<AbstractEntity> entities = GameManager.get().getWorld().getEntities();
		for (AbstractEntity entity : entities) {
			if (entity instanceof EnemyEntity && this.collidesWith(entity)) {
				((EnemyEntity)entity).getShot(this);
				GameManager.get().getWorld().removeEntity(this);
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
