package com.deco2800.potatoes.entities.portals;

import com.deco2800.potatoes.entities.health.ProgressBar;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.worlds.World;
import org.junit.Test;
import org.junit.Before;


import static org.junit.Assert.*;

import org.junit.After;

public class BasePortalTest{

	BasePortal testPortal;

	@Before
	public void setup() {
		testPortal = new BasePortal(0, 0, 3);
		GameManager.get().setWorld(new TestWorld());
	}
	
	@After
    public void cleanUp() {
    	GameManager.get().clearManagers();
    	testPortal = null;
    }

	private class TestWorld extends World {

	}

	@Test
	public void ProgressBar() {
		assertTrue(testPortal.getProgressBar() instanceof ProgressBar);
		assertTrue(testPortal.showProgress() == true);
        assertEquals("healthBarGreen", testPortal.getProgressBar().getTexture());

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

		assertEquals(ForestWorld.get(), GameManager.get().getManager(WorldManager.class).getWorld(ForestWorld.get()));

*/
		testPortal.onTick(0);
	}

}
