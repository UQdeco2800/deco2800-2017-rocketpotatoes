package com.deco2800.potatoes.entities;

import com.deco2800.potatoes.entities.AbstractEntity;

/**
 * Class representing the "goal/nexus" thingy that the player needs to defend.
 * Very likely this will be refactored and replaced.
 * 
 * @author leggy
 *
 */
public class GoalPotate extends AbstractEntity{
	
	private final static transient String TEXTURE = "potate";

	public GoalPotate() {
		// empty for serialization
	}


	public GoalPotate(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 1f, 1f, 1f, TEXTURE);
	}

}
