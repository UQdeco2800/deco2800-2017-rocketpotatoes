package com.deco2800.potatoes.worlds;

import java.util.List;
import java.util.function.Supplier;

import com.deco2800.potatoes.worlds.terrain.Terrain;
import com.deco2800.potatoes.worlds.terrain.TerrainType;
import com.deco2800.potatoes.entities.AbstractEntity;

public class OceanWorld extends WorldType {
	private static final TerrainType oceanTerrain = new TerrainType(null, new Terrain(WATER, 1, true), new Terrain(GROUND, 1, false), new Terrain(GRASS, 0, false));
	private static final List<Supplier<AbstractEntity>> oceanEntities = defaultEntities("sea");
	private static final OceanWorld instance = new OceanWorld(oceanTerrain, oceanEntities);
	
	/**
	 * Creates the ocean world type using the super constructor.
	 * 
	 * @param terrain The terrain type
	 * @param entities The entities for that terrain
	 */
	private OceanWorld(TerrainType terrain, List<Supplier<AbstractEntity>> entities) {
		super(oceanTerrain, oceanEntities);
	}

	/**
	 * Returns the singleton instance for this world type
	 */
	public static OceanWorld get() {
		return instance;
	}
}
