package com.deco2800.potatoes.entities.effects;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.collisions.Box2D;
import com.deco2800.potatoes.collisions.Circle2D;

public class ExplosionEffect extends Effect {

	private static float effectWidth = 5f;
	private static float effectHeight = 2f;

	public ExplosionEffect() {
		// empty for serialization
	}

	public ExplosionEffect(Class<?> targetClass, Vector3 targetPos, float damage, float range) {

		super(targetClass, new Box2D(targetPos.x,targetPos.y,effectWidth,effectHeight),effectWidth,effectHeight, damage, range, EffectTexture.AOE);
		animate = true;
		loopAnimation = false;
	}

	@Override
	public void onTick(long time) {
		super.onTick(time);
	}

}
