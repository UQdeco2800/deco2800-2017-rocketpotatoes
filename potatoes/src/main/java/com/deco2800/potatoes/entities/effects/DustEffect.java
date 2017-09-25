package com.deco2800.potatoes.entities.effects;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.collisions.Box2D;

public class DustEffect extends Effect {

	private static float effectWidth = 3f;
	private static float effectHeight = 1f;

	public DustEffect() {
		// empty for serialization
	}

	public DustEffect(Class<?> targetClass, Vector3 targetPos, float damage, float range) {

		super(targetClass,new Box2D(targetPos.x,targetPos.y,effectWidth,effectHeight),effectWidth,effectHeight,damage,range,EffectTexture.DUST);
		animate = true;
		loopAnimation = false;
	}

	@Override
	public void onTick(long time) {
		super.onTick(time);
	}

}
