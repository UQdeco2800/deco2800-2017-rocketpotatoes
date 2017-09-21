package com.deco2800.potatoes.entities.portals;

import com.badlogic.gdx.utils.compression.lzma.Base;
import com.deco2800.potatoes.BaseTest;
import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.entities.enemies.Squirrel;
import com.deco2800.potatoes.entities.health.ProgressBar;
import com.deco2800.potatoes.entities.portals.AbstractPortal;
import com.deco2800.potatoes.entities.portals.BasePortal;
import com.deco2800.potatoes.gui.InventoryGui;
import com.deco2800.potatoes.gui.WorldChangeGui;
import com.deco2800.potatoes.managers.*;
import com.deco2800.potatoes.screens.GameScreen;
import com.deco2800.potatoes.worlds.World;
import junit.runner.BaseTestRunner;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import com.badlogic.gdx.Input;
import com.deco2800.potatoes.managers.SoundManager;

public class BasePortalTest extends BaseTest {

	BasePortal testPortal;

	@Before
	public void setup() {
		testPortal = new BasePortal(0, 0, 0, 3);
		SoundManager soundManager = new SoundManager();
		PlayerManager playerManager = new PlayerManager();

		GameManager.get().setWorld(new TestWorld());
		GameManager.get().addManager(soundManager);
		GameManager.get().addManager(playerManager);

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
	public void textureTest() {
		assertEquals("forest_portal", testPortal.getTexture());
	}

}