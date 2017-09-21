package com.deco2800.potatoes.entities.portals;

import com.badlogic.gdx.utils.compression.lzma.Base;
import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.entities.enemies.Squirrel;
import com.deco2800.potatoes.entities.health.ProgressBar;
import com.deco2800.potatoes.entities.portals.AbstractPortal;
import com.deco2800.potatoes.entities.portals.BasePortal;
import com.deco2800.potatoes.gui.WorldChangeGui;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.GuiManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.screens.GameScreen;
import com.deco2800.potatoes.worlds.World;
import junit.runner.BaseTestRunner;
import org.junit.Test;
import org.junit.Before;


import static org.junit.Assert.*;
import com.badlogic.gdx.Input;

public class BasePortalTest extends BaseTest{

	BasePortal testPortal;

	@Before
	public void setup() {
		testPortal = new BasePortal(0, 0, 0, 3);
		GameManager.get().setWorld(new TestWorld());
	}

	private class TestWorld extends World {

	}

	@Test
	public void ProgressBarTest() {
		assertTrue(testPortal.getProgressBar() instanceof ProgressBar);
		assertTrue(testPortal.showProgress() == false);
		assertTrue(testPortal.getMaxProgress() == 1);
	}

	@Test
	public void ChangeWorldTest() {
		Player testPlayer = new Player();
		GameManager.get().getManager(PlayerManager.class).setPlayer(testPlayer);
		testPortal.changeWorld();
		testPlayer = GameManager.get().getManager(PlayerManager.class).getPlayer();
		assert(18 == testPlayer.getPosX());
		assert(16 == testPlayer.getPosY());
	}
	
	@Test
	public void textureTest() {
		assertEquals("forest_portal", testPortal.getTexture());
	}

}