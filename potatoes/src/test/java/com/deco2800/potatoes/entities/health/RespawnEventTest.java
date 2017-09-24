package com.deco2800.potatoes.entities.health;

import com.deco2800.potatoes.entities.health.RespawnEvent;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.worlds.World;

import org.junit.After;
import org.junit.Test;

public class RespawnEventTest {
	private static final int respawnTime = 5000;
	RespawnEvent testEvent = new RespawnEvent(respawnTime);
	Player player = new Player(5, 10);

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
		GameManager.get().setWorld(new TestWorld());
		GameManager.get().addManager(new PlayerManager());
		GameManager.get().getManager(PlayerManager.class).setPlayer(player);
		testEvent.action(player);
	}

	private class TestWorld extends World {
		
	}

}