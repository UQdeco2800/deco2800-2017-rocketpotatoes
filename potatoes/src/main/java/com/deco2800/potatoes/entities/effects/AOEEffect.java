package com.deco2800.potatoes.entities.effects;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.util.Line;

public class AOEEffect extends Effect {

	private static float aoeWidth = 5f;
	private static float aoeHeight = 2f;
	private  Vector3 pos;
	public AOEEffect() {
		// empty for serialization
	}

	public AOEEffect(Class<?> targetClass, Vector3 position, float damage, float range) {
		super(targetClass, position, aoeWidth + 3f, aoeHeight + 3f, 0, aoeWidth, aoeHeight,
				damage, range, EffectTexture.AOE);
		loopAnimation = false;
		this.pos = position;
	}

	public float getPosX(){
		return pos.x;
	}

	public float getPosY(){
		return pos.y;
	}



	@Override
	public void onTick(long time) {
		super.onTick(time);
	}
}
