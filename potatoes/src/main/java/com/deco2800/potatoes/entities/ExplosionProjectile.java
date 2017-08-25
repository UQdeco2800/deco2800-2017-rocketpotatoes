package com.deco2800.potatoes.entities;

import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.Box3D;

import java.util.Collection;
import java.util.Optional;

public class ExplosionProjectile extends Projectile {

	private final static transient String TEXTURE = "aoe1";
	private float DAMAGE = 10;
	private int currentSpriteIndexCount = 1;
	private String[] currentSpriteIndex = { "aoe1", "aoe2", "aoe3" };
	private int effectsTimer = 0;
	private int dmgTimer = 0;

	public ExplosionProjectile() {
		// empty for serialization
	}

	public ExplosionProjectile(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
			float xRenderLength, float yRenderLength, float DAMAGE) {
		super(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, TEXTURE);
		this.DAMAGE = DAMAGE;
	}

	@Override
	public void onTick(long time) {

		effectsTimer++;
		if (effectsTimer % 10 == 0) {
			if (currentSpriteIndexCount <= 2) {
				setTexture(currentSpriteIndex[currentSpriteIndexCount]);
				if (currentSpriteIndexCount < 3) {
					currentSpriteIndexCount++;
				}
			} else {
				GameManager.get().getWorld().removeEntity(this);

			}
		}

		dmgTimer++;
		if (dmgTimer % 6 == 0) {
			Collection<AbstractEntity> entities = GameManager.get().getWorld().getEntities().values();
			for (AbstractEntity entity : entities) {
				if (entity instanceof Player && this.collidesWith(entity)) {

				}

				if (entity instanceof EnemyEntity && this.collidesWith(entity)) {
					((EnemyEntity) entity).getShot(this);
				}
			}

		}
	}

	@Override
	public float getDamage() {
		return DAMAGE;
	}

}
