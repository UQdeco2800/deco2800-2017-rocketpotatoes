package com.deco2800.potatoes.entities.enemies;

import com.deco2800.potatoes.collisions.Box2D;
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

	/**
	 * Empty constructor for serialization
	 */
	public EnemyGate() {
		// empty for serialization
	}

	/**
	 * Create an enemy gate at coordinates on the map
	 *
	 * @param posX x coordinate to place gate
	 * @param posY y coordinate to place gate
	 */
	public EnemyGate(float posX, float posY) {
        super(new Box2D(posX, posY, 1f, 1f), 1f, 1f, TEXTURE, 1000f);
        this.setSolid( false );
        this.setStatic( true );
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
