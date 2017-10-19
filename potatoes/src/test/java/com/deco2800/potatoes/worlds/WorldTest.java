package com.deco2800.potatoes.worlds;

import com.deco2800.potatoes.BaseTest;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.worlds.World;
import com.deco2800.potatoes.worlds.terrain.Terrain;
import com.deco2800.potatoes.util.WorldUtil;
import org.junit.After;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.Before;
import com.badlogic.gdx.Input;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;

public class WorldTest extends BaseTest {
	World world;
	TestEntity test = new TestEntity();
	@Rule
	public ExpectedException execption = ExpectedException.none();

	@Before
	public void setup() {
		world = new World();
	}

	@After
	public void tearDown() {
		world = null;
		test = null;
	}

	@Test
	public void setTest() {
		world.setWidth(1);
		world.setLength(1);

	}

	@Test
	public void entityTest() {
		world.addEntity(test);
		world.getEntity(0);
		world.removeEntity(0);
		execption.expect(IllegalStateException.class);
		world.addEntity(test, 0);
	}

	@Test
	public void entityTest2() {
		world.addEntity(test);
		world.addEntity(test);
		world.addEntity(test);
		world.addEntity(test);
		world.deSelectAll();
		world.getBackground();
	}

	@Test
	public void testTerrainChanging() {
		Terrain[][] terrain = { { new Terrain("a", 1, false), new Terrain("a", 1, false), new Terrain("a", 1, false) },
				{ new Terrain("", 0, false), new Terrain("a", 1, false), new Terrain("", 0, false) },
				{ new Terrain("", 0, false), new Terrain("", 0, false), new Terrain("", 0, false) } };
		Terrain[][] differentTerrain = { { new Terrain("a", 1, false), new Terrain("a", 1, false), new Terrain("a", 1, false) },
				{ new Terrain("", 0, false), new Terrain("a", 1, false), new Terrain("", 0, false) } };
		world.setTerrain(terrain);
		assertEquals("Terrain was not the same as the grid", terrain[1][1], world.getTerrain(1, 1));
		world.setTile(1, 1, terrain[2][2]);
		assertEquals("Terrain was not same as tile set", terrain[2][2], world.getTerrain(1, 1));
		world.setTerrain(differentTerrain);
		assertEquals("Terrain was not the same as the grid", terrain[1][1], world.getTerrain(1, 1));
	}

	private class TestEntity extends AbstractEntity {

	}
}
