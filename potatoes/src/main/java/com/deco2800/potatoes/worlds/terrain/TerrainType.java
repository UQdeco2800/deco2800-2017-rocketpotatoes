package com.deco2800.potatoes.worlds.terrain;

import java.util.List;

public class TerrainType {
	// Slopes need to be sorted out
	private final List<Terrain> slopes;
	private final Terrain grass;
	private final Terrain rock;
	private final Terrain water;
	
	/**
	 * @param slopes
	 * @param grass
	 * @param rock
	 * @param water
	 */
	public TerrainType(List<Terrain> slopes, Terrain grass, Terrain rock, Terrain water) {
		this.slopes = slopes;
		this.grass = grass;
		this.rock = rock;
		this.water = water;
	}

	/**
	 * @return the slopes
	 */
	public List<Terrain> getSlopes() {
		return slopes;
	}

	/**
	 * @return the grass
	 */
	public Terrain getGrass() {
		return grass;
	}

	/**
	 * @return the rock
	 */
	public Terrain getRock() {
		return rock;
	}

	/**
	 * @return the water
	 */
	public Terrain getWater() {
		return water;
	}
}
