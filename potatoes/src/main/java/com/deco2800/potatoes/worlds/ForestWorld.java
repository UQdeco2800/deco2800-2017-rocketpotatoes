package com.deco2800.potatoes.worlds;

import java.util.List;
import java.util.function.Supplier;

import com.deco2800.potatoes.worlds.terrain.Terrain;
import com.deco2800.potatoes.worlds.terrain.TerrainType;
import com.deco2800.potatoes.entities.AbstractEntity;

public class ForestWorld extends WorldType {
	
	private static final TerrainType forestTerrain = new TerrainType(null, new Terrain(GRASS, 1, true), new Terrain(GROUND, 1, false), new Terrain(WATER, 0, false));
	private List<Supplier<AbstractEntity>> forestEntities = defaultEntities("forest");
	
	public ForestWorld(TerrainType terrain, List<Supplier<AbstractEntity>> entities) {
		terrain = forestTerrain;
		entities = forestEntities;
		clearSpots = clearPoints();
	}
}
