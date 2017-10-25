package com.deco2800.potatoes.worlds;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import com.deco2800.potatoes.entities.enemies.EnemyGate;
import com.deco2800.potatoes.entities.portals.BasePortal;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.worlds.terrain.RandomTerrain;
import com.deco2800.potatoes.worlds.terrain.Terrain;
import com.deco2800.potatoes.worlds.terrain.TerrainType;
import com.deco2800.potatoes.entities.AbstractEntity;

public class ForestWorld extends WorldType {
	private static final TerrainType FOREST_TERRAIN = new TerrainType(RandomTerrain.GRASS, RandomTerrain.DIRT_NO_PLANT, new Terrain(WATER, 0, false));
	private static final List<Supplier<AbstractEntity>> FOREST_ENTITIES = forestDefaultEntities();
	private static final Set<Point> FOREST_CLEAR_SPOTS = forestClearSpots();
	private static final ForestWorld INSTANCE = new ForestWorld(FOREST_TERRAIN, FOREST_ENTITIES, FOREST_CLEAR_SPOTS);
	private static final int EXTRA_SIZE = 2;
	
	private ForestWorld(TerrainType terrain, List<Supplier<AbstractEntity>> entities, Set<Point> clearSpots) {
		super(terrain, entities, clearSpots);
	}

	private static List<Supplier<AbstractEntity>> forestDefaultEntities() {
		List<Supplier<AbstractEntity>> result = new ArrayList<>();
		//add enemy gates to game world
		//W
		result.add(() -> new EnemyGate(WorldManager.WORLD_SIZE / 2, WorldManager.WORLD_SIZE / 8, "enemyCave_SE"));
		//E
		result.add(() -> new EnemyGate(WorldManager.WORLD_SIZE / 2, WorldManager.WORLD_SIZE - WorldManager
				.WORLD_SIZE / 8, "enemyCave_W"));
		//S
		result.add(() -> new EnemyGate(WorldManager.WORLD_SIZE / 8, WorldManager.WORLD_SIZE / 2, "enemyCave_E"));
		//N
		result.add(() -> new EnemyGate(WorldManager.WORLD_SIZE - WorldManager.WORLD_SIZE / 8, WorldManager
				.WORLD_SIZE / 2,
				"enemyCave_WS"));

		result.add(() -> new BasePortal(WorldManager.WORLD_SIZE / 2, WorldManager.WORLD_SIZE / 2, 10000));

		return result;
	}

	private static Set<Point> forestClearSpots() {
		Set<Point> result = clearPoints();

		result.add(new Point(WorldManager.WORLD_SIZE / 2, WorldManager.WORLD_SIZE / 8));
		result.add(new Point(WorldManager.WORLD_SIZE / 2, WorldManager.WORLD_SIZE - WorldManager.WORLD_SIZE / 8));
		result.add(new Point(WorldManager.WORLD_SIZE / 8, WorldManager.WORLD_SIZE / 2));
		result.add(new Point(WorldManager.WORLD_SIZE - WorldManager.WORLD_SIZE / 8, WorldManager.WORLD_SIZE / 2));

		//TODO temporary while pathfinding is still broken
		for (int x = WorldManager.WORLD_SIZE / 8; x < WorldManager.WORLD_SIZE - WorldManager.WORLD_SIZE / 8; x++) {
			result.add(new Point(x, WorldManager.WORLD_SIZE / 2));
		}

		for (int y = WorldManager.WORLD_SIZE / 8; y < WorldManager.WORLD_SIZE - WorldManager.WORLD_SIZE / 8; y++) {
			result.add(new Point(WorldManager.WORLD_SIZE / 2, y));
		}

		Set<Point> extras = new HashSet<>();
		for (Point p : result) {
			for (int x = -EXTRA_SIZE; x < EXTRA_SIZE + 1; x++) {
				for (int y = -EXTRA_SIZE; y < EXTRA_SIZE + 1; y++) {
					extras.add(new Point(p.x + x, p.y + y));
				}
			}
		}

		result.addAll(extras);

		return result;
	}

	/**
	 * Returns the singleton INSTANCE for this world type
	 */
	public static ForestWorld get() {
		return INSTANCE;
	}
}
