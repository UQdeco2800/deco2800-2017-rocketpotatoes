package com.deco2800.potatoes.worlds;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.portals.AbstractPortal;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.util.GridUtil;
import com.deco2800.potatoes.worlds.terrain.Terrain;
import com.deco2800.potatoes.worlds.terrain.TerrainType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Represents a type of world with a set of terrain types and world generation.
 */
public class WorldType {
	private static final Logger LOGGER = LoggerFactory.getLogger(WorldType.class);

	protected static final String GROUND = "mud_tile_1";
	protected static final String WATER = "water1";
	protected static final String GRASS = "grass_tile_1";
	private static final Point PORTAL_POS = new Point(WorldManager.WORLD_SIZE / 2, WorldManager.WORLD_SIZE / 2);

	private final TerrainType terrain;
	// List of suppliers because creating the entities early can cause problems
	private final List<Supplier<AbstractEntity>> entities;
	protected Set<Point> clearSpots;
	private float landAmount = 0.3f;

	/**
	 * The super constructor to create sub world types.
	 * 
	 * @param terrain The terrain type
	 * @param entities The entities for that terrain
	 */
	public WorldType(TerrainType terrain, List<Supplier<AbstractEntity>> entities) {
		this.terrain = terrain;
		this.entities = entities;
		this.clearSpots = clearPoints();
	}

	public WorldType(TerrainType terrain, List<Supplier<AbstractEntity>> entities, Set<Point> clearSpots) {
		this.terrain = terrain;
		this.entities = entities;
		this.clearSpots = clearSpots;
	}

	protected static Set<Point> clearPoints() {
		Set<Point> clearPoints = new HashSet<>();
		// portal and surrounding
		for (int x = PORTAL_POS.x - 2; x < PORTAL_POS.x + 2; x++) {
			for (int y = PORTAL_POS.y - 2; y < PORTAL_POS.y + 2; y++) {
				clearPoints.add(new Point(x, y));
			}
		}
		return clearPoints;
	}

	protected static List<Supplier<AbstractEntity>> defaultEntities(String worldType) {
		List<Supplier<AbstractEntity>> result = new ArrayList<>();
		result.add(() -> new AbstractPortal(PORTAL_POS.x, PORTAL_POS.y, worldType + "_portal"));
		return result;
	}

	/**
	 * @return the terrain type
	 */
	public TerrainType getTerrain() {
		return terrain;
	}

	/**
	 * Returns the list of entities that should start in a world of this type
	 */
	public List<AbstractEntity> getEntities() {
		List<AbstractEntity> result = new ArrayList<>();
		for (Supplier<AbstractEntity> supplier : entities) {
			result.add(supplier.get());
		}
		return result;
	}

	public List<Point> getClearSpots() {
		return new ArrayList<>(clearSpots);
	}

	/**
	 * Generates a grid of terrain based on the given world size. The terrain types
	 * and world generation is based on the details of this world type
	 */
	public Terrain[][] generateWorld(int worldSize) {
		WorldManager wm = GameManager.get().getManager(WorldManager.class);
		Terrain[][] terrainSet = new Terrain[worldSize][worldSize];
		boolean validLand = false;
		int count = 0;
		while (!validLand) {
			float[][] water = GridUtil.smoothDiamondSquareAlgorithm(getWaterSeed(worldSize), 0, 0.5f, 2);
			float[][] height = GridUtil.smoothDiamondSquareAlgorithm(getWaterSeed(worldSize), 0.42f, 2);
			float[][] grass = GridUtil.smoothDiamondSquareAlgorithm(GridUtil.seedGrid(worldSize), 0.42f, 2);
			for (int x = 0; x < worldSize; x++) {
				for (int y = 0; y < worldSize; y++) {
					terrainSet[x][y] = chooseTerrain(water[x][y], height[x][y], grass[x][y]);
				}
			}
			validLand = checkValidLand(worldSize, terrainSet);
			count++;
			if (count == 50000) {
				// Computers are fast let's make a lot more worlds
				LOGGER.warn("world gen is taking a long time, valid location is probably very unlikely");
			}
			if (count > 100000) {
				LOGGER.warn("gave up on valid world");
				break;
			}
		}
		return terrainSet;
	}

	private float[][] getWaterSeed(int worldSize) {
		float[][] result = GridUtil.seedGrid(worldSize);
		for (Point p : clearSpots) {
			if (p.x < worldSize && p.y < worldSize) {
				result[p.x][p.y] = 1;
			}
		}
		return result;
	}

	private boolean checkValidLand(int worldSize, Terrain[][] terrainSet) {
		boolean validLand = true;
		Set<Point> filled = new HashSet<>();
		GridUtil.genericFloodFill(clearSpots.iterator().next(), floodFillCheck(worldSize, terrainSet), new HashSet<>(),
				filled);
		for (Point point : clearSpots) {
			// Positions on the map have x and y swapped
			point = new Point(point.y, point.x);
			if (!filled.contains(point)) {
				validLand = false;
			}
			if (landAmount * worldSize * worldSize > filled.size()) {
				validLand = false;
			}
		}
		return validLand;
	}

	private Function<Point, Boolean> floodFillCheck(int worldSize, Terrain[][] terrainSet) {
		return p -> p.x > 0 && p.y > 0 && p.x < worldSize && p.y < worldSize
				&& !terrainSet[p.x][p.y].getTexture().equals(getTerrain().getWater());
	}

	private Terrain chooseTerrain(float water, float height, float grass) {
		Terrain spot;
		if (height < 0.3 || water < 0.5) {
			spot = getTerrain().getWater();
		} else if (height < 0.4 || water < 0.6) {
			spot = getTerrain().getRock();
		} else {
			spot = grass < 0.6 ? getTerrain().getGrass() : getTerrain().getRock();
		}
		return spot;
	}
}
