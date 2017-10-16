package com.deco2800.potatoes.managers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.deco2800.potatoes.worlds.World;
import com.deco2800.potatoes.worlds.WorldType;

public class WorldManagerTest {
	WorldManager worldManager;

	private static class GdxTestApplication extends ApplicationAdapter {

	}

	@Before
	public void setup() {
		HeadlessApplicationConfiguration conf = new HeadlessApplicationConfiguration();
		new HeadlessApplication(new GdxTestApplication(), conf);
		Gdx.gl = mock(GL20.class);
		worldManager = GameManager.get().getManager(WorldManager.class);
	}

	@After
	public void tearDown() {
		GameManager.get().clearManagers();
	}

	@Test
	public void testGetWorld() {
		World w1 = worldManager.getWorld(WorldType.FOREST_WORLD);
		World w2 = worldManager.getWorld(WorldType.DESERT_WORLD);
		// Same object, not just .equals
		assertTrue("Getting world of same type did not give the same world",
				w1 == worldManager.getWorld(WorldType.FOREST_WORLD));
		assertFalse("Different world types gave the same world", w1 == w2);
	}

	@Test
	public void testDeleteWorld() {
		World w1 = worldManager.getWorld(WorldType.FOREST_WORLD);
		worldManager.deleteWorld(WorldType.FOREST_WORLD);
		assertFalse("Deleting world resulted in the same world returned",
				w1 == worldManager.getWorld(WorldType.FOREST_WORLD));
	}

	@Test
	public void testClearWorlds() {
		World w1 = worldManager.getWorld(WorldType.FOREST_WORLD);
		World w2 = worldManager.getWorld(WorldType.DESERT_WORLD);
		worldManager.clearWorlds();
		assertFalse("Deleting world resulted in the same world returned",
				w1 == worldManager.getWorld(WorldType.FOREST_WORLD));
		assertFalse("Deleting world resulted in the same world returned",
				w2 == worldManager.getWorld(WorldType.DESERT_WORLD));
	}
	@Test
	public void testCachedWorld() {
		World w1 = worldManager.getWorld(WorldType.FOREST_WORLD);
		worldManager.isWorldCached();
	}

}
