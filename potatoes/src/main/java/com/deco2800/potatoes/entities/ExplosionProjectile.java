package com.deco2800.potatoes.entities;

import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.Box3D;

import java.util.Collection;
import java.util.Optional;

public class ExplosionProjectile extends Projectile{

	private final static transient String TEXTURE = "aoe";

	public ExplosionProjectile() {
		//empty for serialization
	}


	public ExplosionProjectile(float posX, float posY, float posZ,float xLength, float yLength, float zLength, float xRenderLength, float yRenderLength, String TEXTURE) {
		super(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, TEXTURE);
//		super(posX, posY, posZ,3f, 3f, 3f, 3f,3f, "aoe1");

	}

	int dud=1;
	int timer=0;
	@Override
	public void onTick(long time) {
		timer++;
		if(timer%10==0) {
			setTexture("aoe" + dud);
			if (dud < 3) {
				dud++;
			} else {
				GameManager.get().getWorld().removeEntity(this);
			}
		}

		Collection<AbstractEntity> entities = GameManager.get().getWorld().getEntities().values();
		for (AbstractEntity entity : entities) {

			if (entity instanceof EnemyEntity && this.collidesWith(entity)) {
				((EnemyEntity)entity).getShot(this);
				//end
//				GameManager.get().getWorld().removeEntity(this);
//				ExplosionProjectile exp=new ExplosionProjectile(goalX, goalY, goalZ,0.4f,0.4f,0.4f,0.4f,0.4f, "aoe1");
//				GameManager.get().getWorld().addEntity(exp);

				//GameManager.get().getWorld().addEntity(exp);


				return;
			}
		}

	}

	@Override
	public float getDamage() {
		return 0;
	}

}
