package com.deco2800.potatoes.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.deco2800.potatoes.util.GridUtil;
import com.deco2800.potatoes.worlds.World;
import com.deco2800.potatoes.worlds.WorldType;
import com.deco2800.potatoes.worlds.terrain.Terrain;

/**
 * Manager for worlds. Stores and generates all the worlds.
 */
public class WorldManager extends Manager {
	private static final int WORLD_SIZE = 25;

	private Map<WorldType, World> worlds;
	private Map<String, Cell> cells;
	private float[][][] randomGrids;

	public WorldManager() {
		worlds = new HashMap<>();
		cells = new HashMap<>();
		randomGrids = new float[20][][];
		for (int i = 0; i < randomGrids.length; i++) {
			randomGrids[i] = GridUtil.smoothDiamondSquareAlgorithm(WORLD_SIZE, 0.4f, 2);
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
	public World getWorld(WorldType key) {
		if (worlds.containsKey(key)) {
			return worlds.get(key);
		} else {
			// Generate the world here
			worlds.put(key, generateWorld(key));
			return worlds.get(key);
		}
	}

	/**
	 * Deletes the world associated with the given key. It will be regenerated if
	 * access is attempted
	 */
	public void deleteWorld(WorldType key) {
		worlds.remove(key);
	}

	/**
	 * Deletes all worlds
	 */
	public void clearWorlds() {
		worlds.clear();
	}

	/**
	 * Sets the world to the world with the given index. If it does not exist, it
	 * will be created.
	 */
	public void setWorld(WorldType key) {
		// GameManager.setWorld will probably need to be updated. Some managers need to
		// be reloaded, etc.
		GameManager.get().setWorld(getWorld(key));
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
	public Cell getCell(String texture) {
		if (!cells.containsKey(texture)) {
			cells.put(texture, new Cell().setTile(new StaticTiledMapTile(
					GameManager.get().getManager(TextureManager.class).getTextureRegion(texture))));
		}
		return cells.get(texture);
	}

	private World generateWorld(WorldType worldType) {
		World world = new World();
		Cell[][] terrainCells = new Cell[WORLD_SIZE][WORLD_SIZE];
		float[][] height = getRandomGrid();
		Terrain[][] terrain = worldType.generateWorld(WORLD_SIZE, height);
		for (int x = 0; x < WORLD_SIZE; x++) {
			for (int y = 0; y < WORLD_SIZE; y++) {
				terrainCells[x][y] = getCell(terrain[x][y].getTexture());
			}
		}
		world.setCells(terrainCells);
		world.setTerrain(terrain);
		return world;
	}
}
