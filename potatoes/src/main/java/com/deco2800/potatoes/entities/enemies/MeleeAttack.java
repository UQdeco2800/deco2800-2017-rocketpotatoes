package com.deco2800.potatoes.entities.enemies;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.collisions.Circle2D;

/**
 *  A melee attack that can be performed by an entity to damage another.
 */
public class MeleeAttack extends Effect {


	/**
	 * Empty constructor for serialization
	 */
	public MeleeAttack() {
		//Empty for serialization purposes
	}

	/**
	 * Constructor for a melee attack
	 *
	 * @param targetClass the class of entities that will be attacked
	 * @param startPos vector describing the starting position of the attack
	 * @param targetPos vector describing the end position of the attack
	 * @param damage the amount of damage this attack inflicts on targetClass entities
	 * @param range the maximum range between start and target positions for attacks to occur
	 */
	public MeleeAttack(Class<?> targetClass, Vector3 startPos, Vector3 targetPos, float damage, float range) {
        super(targetClass, new Circle2D(startPos.x, startPos.y, 7.07f), 1f, 1f, damage, range, EffectTexture.SWIPE);

	}
}
