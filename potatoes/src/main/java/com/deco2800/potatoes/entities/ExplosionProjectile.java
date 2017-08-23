package com.deco2800.potatoes.entities;

import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.Box3D;

import java.util.Collection;
import java.util.Optional;

public class ExplosionProjectile extends Projectile{

	private final static transient String TEXTURE = "aoe";
	private float DAMAGE = 1;

	public ExplosionProjectile() {
		//empty for serialization
	}


	public ExplosionProjectile(float posX, float posY, float posZ,float xLength, float yLength, float zLength, float xRenderLength, float yRenderLength, String TEXTURE, float DAMAGE) {
		super(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, TEXTURE);
		this.DAMAGE = DAMAGE;
	}

	int currentSpriteIndex=1;
	int timer=0;
	int dmgTimer = 0;
	@Override
	public void onTick(long time) {
		timer++;
		if(timer%10==0) {
			if (currentSpriteIndex < 3) {
				currentSpriteIndex++;
			} else {
				//GameManager.get().getWorld().removeEntity(this);
			}
		}

		Collection<AbstractEntity> entities = GameManager.get().getWorld().getEntities().values();
		for (AbstractEntity entity : entities) {
			if (entity instanceof Player && this.collidesWith(entity)) {
				System.out.println("player col");
			}

				if (entity instanceof EnemyEntity && this.collidesWith(entity)) {
					dmgTimer++;
					if(dmgTimer%16 == 0){
						((EnemyEntity)entity).getShot(this);

					}

					return;
				}


		}
	}

	//TODO: Change to amount
	@Override
	public float getDamage() {
		return DAMAGE;
	}

}
