package com.deco2800.potatoes.entities.effects;

import com.deco2800.potatoes.collisions.CollisionMask;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Tickable;

public abstract class Effect extends AbstractEntity implements Tickable {

	public Effect() {

	}

    public Effect(CollisionMask mask, float xRenderLength, float yRenderLength, String texture) {
        super(mask, xRenderLength, yRenderLength, texture);
    }

	public void drawEffect(SpriteBatch batch) {

	}

	@Override
	public void onTick(long time) {

	}

	public abstract float getDamage();

}
