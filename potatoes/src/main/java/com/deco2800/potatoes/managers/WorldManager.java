package com.deco2800.potatoes.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.deco2800.potatoes.entities.portals.AbstractPortal;
import com.deco2800.potatoes.entities.trees.DamageTree;
import com.deco2800.potatoes.worlds.RandomWorldGeneration;
import com.deco2800.potatoes.worlds.World;
import com.deco2800.potatoes.worlds.WorldType;
import com.deco2800.potatoes.worlds.terrain.Terrain;
import com.deco2800.potatoes.worlds.terrain.TerrainType;

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
			worlds.put(key, generateWorld(new WorldType(new TerrainType(null, new Terrain("grass", 1, true),
					new Terrain("ground_1", 1, false), new Terrain("w1", 0, false)))));
			//add some entities to the worlds that aren't the main world
			if (key > 0) {
				worlds.get(key).addEntity(new DamageTree(16, 11, 0));
				worlds.get(key).addEntity(new AbstractPortal(5, 5, 0, "iceland_portal"));
			}
			
			return worlds.get(key);
		}
	}

	/**
	 * Deletes the world associated with the given key. It will be regenerated if
	 * access is attempted
	 */
	public void deleteWorld(int key) {
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

	private World generateWorld(WorldType worldType) {
		World world = new World();
		Cell[][] terrainCells = new Cell[WORLD_SIZE][WORLD_SIZE];
		float[][] height = getRandomGrid();
		terrain = worldType.generateWorld(WORLD_SIZE, height);
		for (int x = 0; x < WORLD_SIZE; x++) {
			for (int y = 0; y < WORLD_SIZE; y++) {
				terrainCells[x][y] = getCell(terrain[x][y].getTexture());
			}
		}
		world.setTerrain(terrainCells);
		world.setHeight(height);
		return world;
	}

	public Terrain getTerrain(float x, float y) {
		// x and y are flipped on map
		return terrain[Math.round(y)][Math.round(x)];
	}
}
