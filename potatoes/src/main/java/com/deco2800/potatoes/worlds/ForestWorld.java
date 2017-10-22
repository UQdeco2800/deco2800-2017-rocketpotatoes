package com.deco2800.potatoes.worlds;

import java.util.List;
import java.util.function.Supplier;

import com.deco2800.potatoes.worlds.terrain.Terrain;
import com.deco2800.potatoes.worlds.terrain.TerrainType;
import com.deco2800.potatoes.entities.AbstractEntity;

public class ForestWorld extends WorldType {
	private static final TerrainType forestTerrain = new TerrainType(null, new Terrain(GRASS, 1, true), new Terrain(GROUND, 1, false), new Terrain(WATER, 0, false));
	private static final List<Supplier<AbstractEntity>> forestEntities = defaultEntities("forest");
	private static final ForestWorld instance = new ForestWorld(forestTerrain, forestEntities);
	
	private ForestWorld(TerrainType terrain, List<Supplier<AbstractEntity>> entities) {
		super(forestTerrain, forestEntities);
	}

	/**
	 * Returns the singleton instance for this world type
	 */
	public static ForestWorld get() {
		return instance;
	}
}
