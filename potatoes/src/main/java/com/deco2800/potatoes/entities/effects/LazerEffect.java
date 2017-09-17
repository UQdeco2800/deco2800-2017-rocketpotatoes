package com.deco2800.potatoes.entities.effects;

import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.Box3D;

public class LazerEffect extends Effect {

	private float lifetime = 0.4f;

	float xPos;
	float yPos;
	float fxPos;
	float fyPos;

	public LazerEffect(float xPos, float yPos, float fxPos, float fyPos) {
		super(fyPos, fxPos, 0, 5f, 5f, 0, 1f, 1f, "lightning");
		damage = 0.1f;

		// TODO: figure out why inverses
		this.xPos = yPos;
		this.yPos = xPos;
		this.fxPos = fyPos;
		this.fyPos = fxPos;
	}

	public void drawEffect(SpriteBatch batch) {

		drawTextureBetween(batch, getTexture(), xPos, yPos, fxPos, fyPos);

		Box3D newPos = getBox3D();
		newPos.setX(fyPos);
		newPos.setY(fxPos);

		Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();

		float radiusOfImpact = 0.5f;
		for (AbstractEntity entity : entities.values()) {
			if (entity.distance(newPos.getX(), newPos.getY(), 0) <= radiusOfImpact) {
				try {
					((MortalEntity) entity).damage(damage);
				} catch (Exception e) {

				}
				break;
			}
		}

		ExplosionEffect expEffect = new ExplosionEffect(newPos.getX(), newPos.getY(), 0, 5f, 5f, 0, 1f, 1f);
		GameManager.get().getWorld().addEntity(expEffect);

	}

	@Override
	public void onTick(long time) {
		lifetime -= 0.05;
		if (lifetime <= 0) {
			GameManager.get().getWorld().removeEntity(this);
		}
	}

	@Override
	public float getDamage() {
		return damage;
	}

}
