package com.deco2800.potatoes.managers;

import java.util.HashMap;

import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.deco2800.potatoes.entities.portals.AbstractPortal;
import com.deco2800.potatoes.entities.trees.DamageTree;
//import com.deco2800.potatoes.worlds.RandomWorldGeneration;
import com.deco2800.potatoes.util.GridUtil;
import com.deco2800.potatoes.worlds.World;
import com.deco2800.potatoes.worlds.WorldType;
import com.deco2800.potatoes.worlds.terrain.Terrain;
import com.deco2800.potatoes.worlds.terrain.TerrainType;

/**
 * Manager for worlds. Stores and generates all the worlds.
 */
public class WorldManager extends Manager {
	private static final int WORLD_SIZE = 25;

	private Map<WorldType, World> worlds;
	private Map<String, Cell> cells;
	private float[][][] randomGrids;
	private Terrain[][] terrain;

	public WorldManager() {
		worlds = new HashMap<>();
		cells = new HashMap<>();
		terrain = new Terrain[WORLD_SIZE][WORLD_SIZE];
		randomGrids = new float[20][][];
		for (int i = 0; i < randomGrids.length; i++) {
			randomGrids[i] = GridUtil.smoothDiamondSquareAlgorithm(WORLD_SIZE, 0.4f, 2);
		}
	}

	/**
	 * Gets the given world where 0 is the main world. The world is generated if it
	 * hasn't already been.
	 *
	 * @param key the number of the world to get
	 * @return the world
	 */
	public World getWorld(WorldType key) {
		if (worlds.containsKey(key)) {
			return worlds.get(key);
		} else {
			// Generate the world here
			worlds.put(key, generateWorld(new WorldType(new TerrainType(null, new Terrain("grass", 1, true),
					new Terrain("ground_1", 1, false), new Terrain("w1", 0, false)))));
			//add some entities to the worlds that aren't the main world
			addPortal(key);

			return worlds.get(key);
		}
	}

	/**
	 * Adds a portal to a new world
	 *
	 * @param key The ID of the world to add the portal to
	 */
	private void addPortal(WorldType key) {
		// the location of the portal
		int xLocation = 5;
		int yLocation = 5;
		int zLocation = 0;

		// add the portal with the appropriate texture
		if (key == WorldType.ICE_WORLD) {
			worlds.get(key).addEntity(new AbstractPortal(xLocation, yLocation, zLocation,
					"desert_portal"));
		} else if (key == WorldType.DESERT_WORLD) {
			worlds.get(key).addEntity(new AbstractPortal(xLocation, yLocation, zLocation,
					"iceland_portal"));
		} else if (key == WorldType.VOLCANO_WORLD) {
			worlds.get(key).addEntity(new AbstractPortal(xLocation, yLocation, zLocation,
					"volcano_portal"));
		} else if (key == WorldType.OCEAN_WORLD) {
			worlds.get(key).addEntity(new AbstractPortal(xLocation, yLocation, zLocation,
					"sea_portal"));
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
		terrain = worldType.generateWorld(WORLD_SIZE, height);
		for (int x = 0; x < WORLD_SIZE; x++) {
			for (int y = 0; y < WORLD_SIZE; y++) {
				terrainCells[x][y] = getCell(terrain[x][y].getTexture());
			}
		}
		world.setCells(terrainCells);
		world.setTerrain(terrain);
		return world;
	}

	public Terrain getTerrain(float x, float y) {
		// x and y are flipped on map
		return terrain[Math.round(y)][Math.round(x)];
	}
}
