package com.deco2800.potatoes.entities.effects;

import com.deco2800.potatoes.collisions.Circle2D;
import com.badlogic.gdx.math.Vector3;

public class AOEEffect extends Effect {

	private static float aoeWidth = 5f;
	private static float aoeHeight = 2f;
	private Vector3 pos = new Vector3(0, 0, 0);
	public AOEEffect() {
		// empty for serialization
	}

	public AOEEffect(Class<?> targetClass, Vector3 position, float damage, float range) {
        super(targetClass, new Circle2D(position.x+3, position.y, 4), aoeWidth, aoeHeight, damage, range, EffectTexture.AOE);

		loopAnimation = false;
		this.pos = position;
	}

	@Override
	public float getPosX() {
		return pos.x;
	}

	@Override
	public float getPosY() {
		return pos.y;
	}

	@Override
	public void onTick(long time) {
		super.onTick(time);
	}
}
