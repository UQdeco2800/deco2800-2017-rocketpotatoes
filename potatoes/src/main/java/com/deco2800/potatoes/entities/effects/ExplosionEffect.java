package com.deco2800.potatoes.entities.effects;

import com.badlogic.gdx.math.Vector3;

public class ExplosionEffect extends Effect {

	private static float effectWidth = 5f;
	private static float effectHeight = 2f;

	public ExplosionEffect() {
		// empty for serialization
	}

	public ExplosionEffect(Class<?> targetClass, Vector3 targetPos, float damage, float range) {
		super(targetClass, new Vector3(targetPos.x - 4, targetPos.y - 1, targetPos.z), effectWidth, effectHeight, 0,
				effectWidth, effectHeight, damage, range, EffectTexture.EXPLOSION);
		animate = true;
		loopAnimation = false;
	}

	@Override
	public void onTick(long time) {
		super.onTick(time);
	}

}
