package com.deco2800.potatoes.entities.effects;

public class AOEEffect extends Effect {

	private static float aoeWidth = 5f;
	private static float aoeHeight = 2f;
	private static String effectType = "aoe";

	public AOEEffect() {
		// empty for serialization
	}

	public AOEEffect(Class<?> targetClass, float posX, float posY, float posZ, float damage, float range) {
		super(targetClass, posX - (aoeWidth / 2), posY - (aoeHeight / 2), 0, aoeWidth + 3, aoeHeight + 3, 0, aoeWidth,
				aoeHeight, damage, range, effectType);
		textureLoop = false;
	}

	@Override
	public void onTick(long time) {
		super.onTick(time);
	}
}
