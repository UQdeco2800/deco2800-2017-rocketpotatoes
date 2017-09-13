package com.deco2800.potatoes.worlds;

import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.worlds.terrain.Terrain;
import com.deco2800.potatoes.worlds.terrain.TerrainType;

/**
 * Represents a type of world with a set of terrain types and world generation.
 */
public class WorldType {
	public static final WorldType FOREST_WORLD = new WorldType(new TerrainType(null, new Terrain("grass", 1, true),
			new Terrain("ground_1", 1, false), new Terrain("w1", 0, false)));
	public static final WorldType DESERT_WORLD = new WorldType(new TerrainType(null, new Terrain("grass", 0.5f, true),
			new Terrain("ground_1", 1, false), new Terrain("w1", 0, false)));
	public static final WorldType ICE_WORLD = new WorldType(new TerrainType(null, new Terrain("grass", 1, true),
			new Terrain("ground_1", 1, false), new Terrain("w1", 2f, false)));
	public static final WorldType VOLCANO_WORLD = new WorldType(new TerrainType(null, new Terrain("grass", 1, true),
			new Terrain("ground_1", 0.5f, false), new Terrain("w1", 0, false)));
	public static final WorldType OCEAN_WORLD = new WorldType(new TerrainType(null, new Terrain("w1", 1, true),
			new Terrain("ground_1", 1, false), new Terrain("grass", 0, false)));

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((terrain == null) ? 0 : terrain.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorldType other = (WorldType) obj;
		if (terrain == null) {
			if (other.terrain != null)
				return false;
		} else if (!terrain.equals(other.terrain))
			return false;
		return true;
	}
}
