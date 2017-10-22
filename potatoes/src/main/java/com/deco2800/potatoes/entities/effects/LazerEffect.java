package com.deco2800.potatoes.entities.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.collisions.Shape2D;
import com.deco2800.potatoes.collisions.Circle2D;
import com.deco2800.potatoes.managers.GameManager;

public class LazerEffect extends Effect {

	private float lifetime = 0.4f;

	float xPos;
	float yPos;
	float fxPos;
	float fyPos;


	public LazerEffect(Class<?> targetClass, Vector3 startPos, Vector3 targetPos, float damage, float range) {
        super(targetClass, new Circle2D(startPos.x, startPos.y, 7.07f), 1f, 1f, damage, range, EffectTexture.LAZER);
		
		this.xPos = yPos;
		this.yPos = xPos;
		this.fxPos = fyPos;
		this.fyPos = fxPos;
	}

	@Override
	public void drawEffect(SpriteBatch batch) {

		drawTextureBetween(batch, getTexture(), xPos, yPos, fxPos, fyPos);

		Shape2D newPos = getMask();
		newPos.setX(this.getPosX());
		newPos.setY(this.getPosY());


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
