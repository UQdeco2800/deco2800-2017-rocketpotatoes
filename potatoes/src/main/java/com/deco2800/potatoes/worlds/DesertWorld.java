package com.deco2800.potatoes.worlds;

import java.util.List;
import java.util.function.Supplier;

import com.deco2800.potatoes.worlds.terrain.RandomTerrain;
import com.deco2800.potatoes.worlds.terrain.Terrain;
import com.deco2800.potatoes.worlds.terrain.TerrainType;
import com.deco2800.potatoes.entities.AbstractEntity;

public class DesertWorld extends WorldType {
	private static final TerrainType desertTerrain = new TerrainType(RandomTerrain.DIRT, new Terrain("sand_tile_1", 1, false), new Terrain(WATER, 0, false));
	private static final List<Supplier<AbstractEntity>> desertEntities = defaultEntities("desert");
	private static final DesertWorld instance = new DesertWorld(desertTerrain, desertEntities);
	
	/**
	 * Creates the desert world type using the super constructor.
	 * 
	 * @param terrain The terrain type
	 * @param entities The entities for that terrain
	 */
	private DesertWorld(TerrainType terrain, List<Supplier<AbstractEntity>> entities) {
		super(desertTerrain, desertEntities);
	}

	/**
	 * Returns the singleton instance for this world type
	 */
	public static DesertWorld get() {
		return instance;
	}
}
