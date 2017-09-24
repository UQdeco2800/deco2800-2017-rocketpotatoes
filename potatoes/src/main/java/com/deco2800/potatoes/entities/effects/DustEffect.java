package com.deco2800.potatoes.entities.effects;

import com.badlogic.gdx.math.Vector3;

public class DustEffect extends Effect {

	private static float effectWidth = 3f;
	private static float effectHeight = 1f;

	public DustEffect() {
		// empty for serialization
	}

	public DustEffect(Class<?> targetClass, Vector3 targetPos, float damage, float range) {
		super(targetClass, new Vector3(targetPos.x, targetPos.y + 1, targetPos.z), effectWidth, effectHeight, 0,
				effectWidth, effectHeight, damage, range, EffectTexture.DUST);
		animate = true;
		loopAnimation = false;
	}

	@Override
	public void onTick(long time) {
		super.onTick(time);
	}

}
