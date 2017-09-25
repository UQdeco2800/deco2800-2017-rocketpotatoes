package com.deco2800.potatoes.entities.effects;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.collisions.Box2D;

public class ExplosionEffect extends Effect {

	private static float effectWidth = 5f;
	private static float effectHeight = 2f;

	public ExplosionEffect() {
		// empty for serialization
	}

	public ExplosionEffect(Class<?> targetClass, Vector3 targetPos, float damage, float range) {

		super(targetClass, new Box2D(targetPos.x-3,targetPos.y,effectWidth,effectHeight),effectWidth,effectHeight, damage, range, EffectTexture.EXPLOSION);
		animate = true;
		loopAnimation = false;
	}

	@Override
	public void onTick(long time) {
		super.onTick(time);
	}

}
