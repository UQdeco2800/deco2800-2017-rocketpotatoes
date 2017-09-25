package com.deco2800.potatoes.entities.health;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.deco2800.potatoes.entities.health.RespawnEvent;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.worlds.World;
import com.deco2800.potatoes.worlds.WorldType;
import com.deco2800.potatoes.worlds.terrain.Terrain;
import com.deco2800.potatoes.worlds.terrain.TerrainType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RespawnEventTest {
	private static final int respawnTime = 5000;
	RespawnEvent testEvent = new RespawnEvent(respawnTime);
	Player player = new Player(5, 10);

	@Before
	public void initWorld() {
		int worldSize = 50;
		// create a test world of size 50 by 50
		World testWorld = new TestWorld();
		GameManager.get().setWorld(testWorld);
		// set the test world to forest world terrain
		WorldType worldType = new WorldType(new TerrainType(null, new Terrain("grass", 1, true),
				new Terrain("ground_1", 1, false), new Terrain("w1", 0, false)), null);
		Cell[][] terrainCells = new Cell[worldSize][worldSize];
		Terrain[][] terrain = worldType.generateWorld(worldSize);
		for (int x = 0; x < worldSize; x++) {
			for (int y = 0; y < worldSize; y++) {
				terrainCells[x][y] = GameManager.get().getManager(WorldManager.class).getCell(terrain[x][y].getTexture());
			}
		}
		testWorld.setTerrain(terrain);
		GameManager.get().getWorld().setTerrain(terrain);
	}
	
	@After
    public void cleanUp() {
    	GameManager.get().clearManagers();
    }
	
	@Test
	public void emptyTest() {
		RespawnEvent nullEvent = new RespawnEvent();
	}

	@Test 
	public void actionTest() {
		GameManager.get().clearManagers();
		GameManager.get().addManager(new PlayerManager());
		GameManager.get().getManager(PlayerManager.class).setPlayer(player);
		testEvent.action(player);
	}

	private class TestWorld extends World {
		
	}

}