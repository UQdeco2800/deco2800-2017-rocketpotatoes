package com.deco2800.potatoes.worlds;

import java.util.List;
import java.util.function.Supplier;

import com.deco2800.potatoes.worlds.terrain.Terrain;
import com.deco2800.potatoes.worlds.terrain.TerrainType;
import com.deco2800.potatoes.entities.AbstractEntity;

public class VolcanoWorld extends WorldType {
	private static final TerrainType volcanoTerrain = new TerrainType(null, new Terrain(GRASS, 1, true), new Terrain(GROUND, 0.5f, false), new Terrain(WATER, 0, false));
	private static final List<Supplier<AbstractEntity>> volcanoEntities = defaultEntities("volcano");

	private static final VolcanoWorld instance = new VolcanoWorld(volcanoTerrain, volcanoEntities);
	
	private VolcanoWorld(TerrainType terrain, List<Supplier<AbstractEntity>> entities) {
		super(volcanoTerrain, volcanoEntities);
	}

	/**
	 * Returns the singleton instance for this world type
	 */
	public static VolcanoWorld get() {
		return instance;
	}
}
