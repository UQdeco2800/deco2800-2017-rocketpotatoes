package com.deco2800.potatoes.entities;

import com.deco2800.potatoes.collisions.Circle2D;
import com.deco2800.potatoes.entities.health.HasProgressBar;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.entities.health.ProgressBar;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;

/**
 * Class representing the "goal/nexus" thingy that the player needs to defend.
 * Very likely this will be refactored and replaced.
 * 
 * @author leggy
 *
 */
public class GoalPotate extends MortalEntity implements HasProgressBar {
	
	private static final transient String TEXTURE = "potate";

	private static final ProgressBarEntity PROGRESS_BAR = new ProgressBarEntity("healthBarGreen", 2);

	public GoalPotate() {
		// empty for serialization
	}

	/*lots of health so squirrels don't kill it so quickly*/
	public GoalPotate(float posX, float posY) {
		super(new Circle2D(posX, posY, 0.707f), 1, 1, TEXTURE, 1000f);
		this.setSolid( true );
		this.setStatic( true );
	}

	@Override
	public ProgressBar getProgressBar() {
		return PROGRESS_BAR;
	}

	@Override
	public String toString() {
		return "The goal potate";
	}

}
