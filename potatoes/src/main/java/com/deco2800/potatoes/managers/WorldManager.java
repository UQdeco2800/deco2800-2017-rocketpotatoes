package com.deco2800.potatoes.managers;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.util.GridUtil;
import com.deco2800.potatoes.worlds.ForestWorld;
import com.deco2800.potatoes.worlds.World;
import com.deco2800.potatoes.worlds.WorldType;
import com.deco2800.potatoes.worlds.terrain.Terrain;

import java.util.HashMap;
import java.util.Map;

/**
 * Manager for worlds. Stores and generates all the worlds.
 */
public class WorldManager extends Manager implements TickableManager {
	public static final int WORLD_SIZE = 50;

	private Map<WorldType, World> worlds;
	private Map<String, Cell> cells;
	private float[][][] randomGrids;
	private float[][][] randomGridEdges;

	private boolean worldCached = false;

	/**
	 * Initializes the world manager and generates random grids to use for
	 * generating worlds.
	 */
	public WorldManager() {
		worlds = new HashMap<>();
		cells = new HashMap<>();
	}

	/**
	 * Returns a random grid that was the output from
	 * RandomWorldGeneration.smoothDiamondSquareAlgorithm
	 */
	public float[][] getRandomGrid() {
		return GridUtil.smoothDiamondSquareAlgorithm(GridUtil.seedGrid(WORLD_SIZE), 0.42f, 2);
	}

	/**
	 * Returns a random grid with the edges pulled to 0
	 */
	public float[][] getRandomGridEdge() {
		return GridUtil.smoothDiamondSquareAlgorithm(GridUtil.seedGrid(WORLD_SIZE), 0, 0.5f, 2);
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
			worlds.put(key, generateWorld(key));
			//add some entities to the worlds that aren't the main world
			//addDefaultEntities(worlds.get(key), key);

			return worlds.get(key);
		}
	}

	/**
	 * Add all the default entities from the given world type to the given world
	 */
	private void addDefaultEntities(World world, WorldType key) {
		// Temporary, entities are already added to forest world in game screen
		if (true || key != ForestWorld.get()) {
			for (AbstractEntity entity : key.getEntities()) {
				world.addEntity(entity);
			}
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
		Terrain[][] terrain = worldType.generateWorld(WORLD_SIZE);
		for (int x = 0; x < WORLD_SIZE; x++) {
			for (int y = 0; y < WORLD_SIZE; y++) {
				terrainCells[x][y] = getCell(terrain[x][y].getTexture());
			}
		}
		world.setBackground(worldType.getTerrain().getWater());
		world.setTerrain(terrain);
		return world;
	}

	public boolean isWorldCached() {
		return worldCached;
	}

	public void setWorldCached(boolean worldCached) {
		this.worldCached = worldCached;
	}

	@Override
	public void onTick(long i) {
		GameManager.get().getWorld().updatePositions();
	}
}
