package com.deco2800.potatoes.entities.enemies;

import com.deco2800.potatoes.collisions.Circle2D;
import com.deco2800.potatoes.entities.health.HasProgressBar;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.entities.health.ProgressBar;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.worlds.terrain.Terrain;

/**
 * A gate where enemies come out.
 *
 */
public class EnemyGate extends MortalEntity implements HasProgressBar {

	private static final String GRASS = "grass_tile_1";

	/**
	 * Empty constructor for serialization
	 */
	public EnemyGate() {
		//Empty for serialization purposes
	}

	/**
	 * Create an enemy gate at coordinates on the map
	 *
	 * @param posX x coordinate to place gate
	 * @param posY y coordinate to place gate
	 */
	public EnemyGate(float posX, float posY, String texture) {
        super(new Circle2D(posX, posY, 1.3f), 1.6f, 1.6f, texture, 1000f);
        this.setSolid( true );
        this.setStatic( true );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProgressBar getProgressBar() {
		return null;
	}

	/**
	 * @return a string representation of the enemy gate
	 */
	@Override
	public String toString() {
		return "The Enemy Gate";
	}

}
