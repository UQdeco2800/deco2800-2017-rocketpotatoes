package com.deco2800.potatoes.entities.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Tickable;

public abstract class Effect extends AbstractEntity implements Tickable {

	public Effect() {

	}

	public Effect(float posX, float posY, float posZ, float xLength, float yLength, float zLength, float xRenderLength,
			float yRenderLength, String texture) {
		super(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, true, texture);
	}

	public void drawEffect(SpriteBatch batch) {

	}

	@Override
	public void onTick(long time) {

	}

	public abstract float getDamage();

}
