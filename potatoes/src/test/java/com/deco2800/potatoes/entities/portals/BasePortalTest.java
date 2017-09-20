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
import com.deco2800.potatoes.screens.GameScreen;
import com.deco2800.potatoes.worlds.World;
import org.junit.Test;
import org.junit.Before;


import static org.junit.Assert.*;
import com.badlogic.gdx.Input;

public class BasePortalTest{

	BasePortal testPortal;

	@Before
	public void setup() {
		testPortal = new BasePortal(0, 0, 0, 3);
		GameManager.get().setWorld(new TestWorld());
	}

	private class TestWorld extends World {

	}

	@Test
	public void ProgressBar() {
		assertTrue(testPortal.getProgressBar() instanceof ProgressBar);
		assertTrue(testPortal.showProgress() == false);
		assertTrue(testPortal.getMaxProgress() == 1);
	}
	
	@Test
	public void textureTest() {
		assertEquals("forest_portal", testPortal.getTexture());
	}

}