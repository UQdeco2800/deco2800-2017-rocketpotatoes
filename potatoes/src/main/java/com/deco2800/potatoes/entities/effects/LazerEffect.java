package com.deco2800.potatoes.entities.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.Box3D;

public class LazerEffect extends Effect {

	private float lifetime = 0.4f;

	float xPos;
	float yPos;
	float fxPos;
	float fyPos;

	public LazerEffect(Class<?> targetClass, Vector3 startPos, Vector3 targetPos, float damage,
			float range) {
		super(targetClass, targetPos, 5f, 5f, 0, 1f, 1f, damage, range, EffectType.LAZER);

		// TODO: figure out why inverses
		this.xPos = yPos;
		this.yPos = xPos;
		this.fxPos = fyPos;
		this.fyPos = fxPos;
	}

	public void drawEffect(SpriteBatch batch) {

		drawTextureBetween(batch, getTexture(), xPos, yPos, fxPos, fyPos);

		Box3D newPos = getBox3D();
		newPos.setX(this.getPosX());
		newPos.setY(this.getPosY());

//		ExplosionEffect expEffect = new ExplosionEffect(targetClass, newPos.getX(), newPos.getY(), 0, damage, range);
//		GameManager.get().getWorld().addEntity(expEffect);

	}

	@Override
	public void onTick(long time) {
		super.onTick(time);
		lifetime -= 0.05;
		if (lifetime <= 0) {
			GameManager.get().getWorld().removeEntity(this);
		}
	}
}
