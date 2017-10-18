package com.deco2800.potatoes.worlds;

import java.util.List;
import java.util.function.Supplier;

import com.deco2800.potatoes.worlds.terrain.Terrain;
import com.deco2800.potatoes.worlds.terrain.TerrainType;
import com.deco2800.potatoes.entities.AbstractEntity;

public class IceWorld extends WorldType {
	private static final TerrainType iceTerrain = new TerrainType(null, new Terrain(GRASS, 1, true), new Terrain(GROUND, 1, false), new Terrain(WATER, 2f, false));
	private static final List<Supplier<AbstractEntity>> iceEntities = defaultEntities("iceland");

	private static final IceWorld instance = new IceWorld(iceTerrain, iceEntities);
	
	private IceWorld(TerrainType terrain, List<Supplier<AbstractEntity>> entities) {
		super(iceTerrain, iceEntities);
	}

	/**
	 * Returns the singleton instance for this world type
	 */
	public static IceWorld get() {
		return instance;
	}
}