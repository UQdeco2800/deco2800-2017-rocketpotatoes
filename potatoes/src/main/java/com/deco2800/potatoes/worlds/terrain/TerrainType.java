package com.deco2800.potatoes.worlds.terrain;

import java.util.List;

/**
 * Class representing all the types of terrain for a world type
 */
public class TerrainType {
	private final Terrain grass;
	private final Terrain rock;
	private final Terrain water;

	/**
	 * Creates a terrain type with the specified terrains
	 *  @param grass
	 *            the grass terrain
	 * @param rock
	 *            the rock terrain
	 * @param water
	 */
	public TerrainType(Terrain grass, Terrain rock, Terrain water) {
		this.grass = grass;
		this.rock = rock;
		this.water = water;
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

	/*
	 * Auto generated, no need to manually test. Created from fields: slopes, grass,
	 * rock, water
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (grass == null ? 0 : grass.hashCode());
		result = prime * result + (rock == null ? 0 : rock.hashCode());
		result = prime * result + (water == null ? 0 : water.hashCode());
		return result;
	}

	/*
	 * Auto generated, no need to manually test. Created from fields: slopes, grass,
	 * rock, water
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TerrainType other = (TerrainType) obj;
		if (grass == null) {
			if (other.grass != null)
				return false;
		} else if (!grass.equals(other.grass))
			return false;
		if (rock == null) {
			if (other.rock != null)
				return false;
		} else if (!rock.equals(other.rock))
			return false;
		if (water == null) {
			if (other.water != null)
				return false;
		} else if (!water.equals(other.water))
			return false;
		return true;
	}
}
