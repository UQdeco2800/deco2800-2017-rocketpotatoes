package com.deco2800.potatoes.worlds;

import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.worlds.terrain.Terrain;
import com.deco2800.potatoes.worlds.terrain.TerrainType;

/**
 * Represents a type of world with a set of terrain types and world generation.
 */
public class WorldType {
	private final TerrainType terrain;

	/**
	 * @param terrain
	 */
	public WorldType(TerrainType terrain) {
		this.terrain = terrain;
	}

	/**
	 * @return the terrain
	 */
	public TerrainType getTerrain() {
		return terrain;
	}

	/**
	 * Generates a grid of terrain based on the given world size. The terrain types
	 * and world generation is based on the details of this world type
	 */
	public Terrain[][] generateWorld(int worldSize, float[][] height) {
		Terrain[][] terrain = new Terrain[worldSize][worldSize];
		float[][] grass = GameManager.get().getManager(WorldManager.class).getRandomGrid();
		for (int x = 0; x < worldSize; x++) {
			for (int y = 0; y < worldSize; y++) {
				if (height[x][y] < 0.3) {
					terrain[x][y] = getTerrain().getWater();
				} else if (height[x][y] < 0.35) {
					terrain[x][y] = getTerrain().getRock();
				} else {
					terrain[x][y] = grass[x][y] < 0.5 ? getTerrain().getGrass() : getTerrain().getRock();
				}
			}
		}
		return terrain;
	}
}
