package com.deco2800.potatoes.entities.effects;

import com.badlogic.gdx.math.Vector3;

public class AOEEffect extends Effect {

	private static float aoeWidth = 5f;
	private static float aoeHeight = 2f;

	public AOEEffect() {
		// empty for serialization
	}

	public AOEEffect(Class<?> targetClass, Vector3 position, float damage, float range) {
		super(targetClass, position, aoeWidth + 3, aoeHeight + 3, 0, aoeWidth, aoeHeight,
				damage, range, EffectType.AOE);
		loopAnimation = false;
	}



	@Override
	public void onTick(long time) {
		super.onTick(time);
	}
}
