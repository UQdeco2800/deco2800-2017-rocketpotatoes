package com.deco2800.potatoes.entities.effects;

import com.deco2800.potatoes.collisions.Circle2D;
import com.badlogic.gdx.math.Vector3;

public class AOEEffect extends Effect {

	private static float aoeWidth = 5f;
	private static float aoeHeight = 2f;

	public AOEEffect() {
		// empty for serialization
	}

	public AOEEffect(Class<?> targetClass, Vector3 position, float damage, float range) {
        super(targetClass, new Circle2D(position.x, position.y, 4), aoeWidth, aoeHeight, damage, range, EffectType.AOE);
		loopAnimation = false;
	}

	@Override
	public void onTick(long time) {
		super.onTick(time);
	}
}
