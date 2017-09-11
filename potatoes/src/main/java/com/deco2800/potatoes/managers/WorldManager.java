package com.deco2800.potatoes.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.deco2800.potatoes.worlds.RandomWorldGeneration;
import com.deco2800.potatoes.worlds.World;
import com.deco2800.potatoes.worlds.terrain.BaseTerrain;
import com.deco2800.potatoes.worlds.terrain.Terrain;

/**
 * Manager for worlds. Stores and generates all the worlds.
 */
public class WorldManager extends Manager {
	private static final int WORLD_SIZE = 25;

	private HashMap<Integer, World> worlds;
	private Map<String, Cell> cells;
	private float[][][] randomGrids;
	private Terrain[][] terrain;

	public WorldManager() {
		worlds = new HashMap<>();
		cells = new HashMap<>();
		terrain = new Terrain[WORLD_SIZE][WORLD_SIZE];
		randomGrids = new float[20][][];
		for (int i = 0; i < randomGrids.length; i++) {
			randomGrids[i] = RandomWorldGeneration.smoothDiamondSquareAlgorithm(WORLD_SIZE, 0.4f, 2);
		}
	}

	/**
	 * Gets the given world where 0 is the main world. The world is generated if it
	 * hasn't already been.
	 * 
	 * @param key
	 *            the number of the world to get
	 * @return the world
	 */
	public World getWorld(int key) {
		if (worlds.containsKey(key)) {
			return worlds.get(key);
		} else {
			// Generate the world here
			worlds.put(key, generateWorld(new BaseTerrain(null, new Terrain("grass", 1, true),
					new Terrain("ground_1", 1, false), new Terrain("w1", 0, false))));
			return worlds.get(key);
		}
	}

	public void deleteWorld(int key) {
		worlds.remove(key);
	}

	public void clearWorlds() {
		worlds.clear();
	}

	/**
	 * Sets the world to the world with the given index. If it does not exist, it
	 * will be created.
	 */
	public void setWorld(int index) {
		// GameManager.setWorld will probably need to be updated. Some managers need to
		// be reloaded, etc.
		GameManager.get().setWorld(getWorld(index));
	}

	/**
	 * Returns a random grid that was the output from
	 * RandomWorldGeneration.smoothDiamondSquareAlgorithm
	 */
	public float[][] getRandomGrid() {
		return randomGrids[new Random().nextInt(randomGrids.length)];
	}

	/**
	 * Returns the cell associated with a given texture. A new cell is created with
	 * the given texture if one doesn't exist already
	 */
	private Cell getCell(String texture) {
		if (!cells.containsKey(texture)) {
			cells.put(texture, new Cell().setTile(new StaticTiledMapTile(
					new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture(texture)))));
		}
		return cells.get(texture);
	}

	private World generateWorld(BaseTerrain baseTerrain) {
		World world = new World();
		float[][] height = getRandomGrid();
		float[][] grass = getRandomGrid();
		terrain = new Terrain[WORLD_SIZE][WORLD_SIZE];
		Cell[][] terrainCells = new Cell[WORLD_SIZE][WORLD_SIZE];
		for (int x = 0; x < WORLD_SIZE; x++) {
			for (int y = 0; y < WORLD_SIZE; y++) {
				// TODO move this elsewhere and make these values not hard coded
				if (height[x][y] < 0.3) {
					terrain[x][y] = baseTerrain.getWater();
				} else if (height[x][y] < 0.35) {
					terrain[x][y] = baseTerrain.getRock();
				} else {
					terrain[x][y] = grass[x][y] < 0.5 ? baseTerrain.getGrass() : baseTerrain.getRock();
				}
				terrainCells[x][y] = getCell(terrain[x][y].getTexture());
			}
		}
		world.setTerrain(terrainCells);
		world.setHeight(height);
		return world;
	}
}