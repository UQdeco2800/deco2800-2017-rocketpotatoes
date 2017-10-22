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
	protected static final String WATER = "water_tile_1";
	protected static final String GRASS = "grass_tile_1";
	private static final Point PORTAL_POS = new Point(10, 10);

	private final TerrainType terrain;
	// List of suppliers because creating the entities early can cause problems
	private final List<Supplier<AbstractEntity>> entities;
	protected List<Point> clearSpots;
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
		clearSpots = clearPoints();
	}

	protected static List<Point> clearPoints() {
		List<Point> clearPoints = new ArrayList<>();
		// player spot
		clearPoints.add(new Point(5, 10));
		// spot after going through portal
		clearPoints.add(new Point(9, 4));
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
			float[][] water = wm.getRandomGridEdge();
			float[][] height = wm.getRandomGrid();
			float[][] grass = wm.getRandomGrid();
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

	private boolean checkValidLand(int worldSize, Terrain[][] terrainSet) {
		boolean validLand = true;
		Set<Point> filled = new HashSet<>();
		GridUtil.genericFloodFill(clearSpots.get(0), floodFillCheck(worldSize, terrainSet), new HashSet<>(), filled);
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
				&& !terrainSet[p.x][p.y].getTexture().equals(WATER);
	}

	private Terrain chooseTerrain(float water, float height, float grass) {
		Terrain spot;
		if (height < 0.4 || water < 0.7) {
			spot = getTerrain().getWater();
		} else if (height < 0.5 || water < 0.8) {
			spot = getTerrain().getRock();
		} else {
			spot = grass < 0.6 ? getTerrain().getGrass() : getTerrain().getRock();
		}
		return spot;
	}

	/*
	 * Auto generated, no need to manually test. Created from fields: terrain
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (terrain == null ? 0 : terrain.hashCode());
		return result;
	}

	/*
	 * Auto generated, no need to manually test. Created from fields: terrain
	 */
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
