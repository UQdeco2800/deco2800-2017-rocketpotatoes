package com.deco2800.potatoes.entities.portals;

import com.badlogic.gdx.utils.compression.lzma.Base;
import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.entities.enemies.Squirrel;
import com.deco2800.potatoes.entities.health.ProgressBar;
import com.deco2800.potatoes.entities.portals.AbstractPortal;
import com.deco2800.potatoes.entities.portals.BasePortal;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.worlds.World;
import org.junit.Test;
import org.junit.Before;


import static org.junit.Assert.*;

import org.junit.After;

import com.badlogic.gdx.Input;

public class BasePortalTest{

	BasePortal testPortal;

	@Before
	public void setup() {
		testPortal = new BasePortal(0, 0, 0, 3);
		GameManager.get().setWorld(new TestWorld());
	}
	
	@After
    public void cleanUp() {
    	GameManager.get().clearManagers();
    }

	private class TestWorld extends World {

	}

	@Test
	public void ProgressBar() {
		assertTrue(testPortal.getProgressBar() instanceof ProgressBar);
		assertTrue(testPortal.showProgress() == true);
	}


	@Test
	public void getWorldCollidedTest() {
		/*
		PlayerManager playerManager = GameManager.get().getManager(PlayerManager.class);
		Player testPlayer;
		//player added to the world
		Player addedPlayer = new Player(1, 1, 1);
		GameManager.get().getWorld().addEntity(addedPlayer);

		testPortal.onTick(0);

		System.out.println(GameManager.get().getWorld().toString());

		assertEquals(WorldType.FOREST_WORLD, GameManager.get().getManager(WorldManager.class).getWorld(WorldType.FOREST_WORLD));

*/
		testPortal.onTick(0);
	}

}