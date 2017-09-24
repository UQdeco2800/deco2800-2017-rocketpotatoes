package com.deco2800.potatoes.entities.enemies;

import com.deco2800.potatoes.entities.health.HasProgressBar;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.entities.health.ProgressBar;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;

/**
 * A gate where enemies come out.
 *
 */
public class EnemyGate extends MortalEntity implements HasProgressBar {
	
	//the current texture is just a placeholder, designers needed 
	private static final transient String TEXTURE = "enemyGate";

	private static final ProgressBarEntity PROGRESS_BAR = new ProgressBarEntity("healthbar", 2);

	public EnemyGate() {
		// empty for serialization
	}

	public EnemyGate(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 1f, 1f, 1f, TEXTURE, 1000f);
		this.setStaticCollideable(true);
	}

	@Override
	public ProgressBar getProgressBar() {
		return PROGRESS_BAR;
	}

	@Override
	public String toString() {
		return "The Enemy Gate";
	}

}
