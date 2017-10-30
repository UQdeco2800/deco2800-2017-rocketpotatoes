package com.deco2800.potatoes.entities.health;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.managers.*;
import com.deco2800.potatoes.worlds.ForestWorld;
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
		GameManager.get().getManager(WorldManager.class).setWorld(ForestWorld.get());
		GameManager.get().addManager(new SoundManager());
		GameManager.get().addManager(new GuiManager());
	}
	
	@After
    public void cleanUp() {
    	GameManager.get().clearManagers();
    	testEvent = null;
    	player = null;
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
}