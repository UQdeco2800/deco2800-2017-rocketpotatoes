package com.deco2800.potatoes.worlds;

import com.deco2800.potatoes.BaseTest;
import com.deco2800.potatoes.collisions.Box2D;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.worlds.terrain.Terrain;
import org.junit.After;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.*;

public class WorldTest extends BaseTest {
	private World world;
	private TestEntity test;
	private TestEntity[] testEntities;
	@Rule
	public ExpectedException execption = ExpectedException.none();

	@Before
	public void setup() {
		test = new TestEntity();
		world = new World();
		testEntities = new TestEntity[]{new TestEntity(), new TestEntity(), new TestEntity(), new TestEntity(), new
				TestEntity(), new TestEntity(), new TestEntity(), new TestEntity(), new TestEntity(), new TestEntity()};
		testEntities[0].setPosition(0, 0);
		testEntities[1].setPosition(2, 0);
		testEntities[2].setPosition(0, 2);
		testEntities[3].setPosition(1, 1);
		testEntities[4].setPosition(6, 7);
		testEntities[5].setPosition(8, 8);
		testEntities[6].setPosition(8, 9);
		testEntities[7].setPosition(2, 1);
		testEntities[8].setPosition(2, 3);
		testEntities[9].setPosition(10, 10);
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
	public void entityTest2() {
		world.addEntity(test);
		world.addEntity(test);
		world.addEntity(test);
		world.addEntity(test);
		world.deSelectAll();
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

	@Test
	public void testGetClosestEntity() {
		for (TestEntity e : testEntities) {
			world.addEntity(e);
		}
		assertTrue("Closest entity was not as expected", world.getClosestEntity(1, 1, TestEntity.class).get()
				== testEntities[3]);
		assertTrue("Closest entity was not as expected", world.getClosestEntity(15, 12, TestEntity.class).get()
				== testEntities[9]);
	}

	@Test
	public void testEntitiesWithinDistance() {
		for (TestEntity e : testEntities) {
			world.addEntity(e);
		}
		Iterator<AbstractEntity> entitiesIterator = world.getEntitiesWithinDistance(1, 1, (float)Math.sqrt(1 + 1));
		Set<AbstractEntity> entities = new HashSet<>();
		while (entitiesIterator.hasNext()) {
			entities.add(entitiesIterator.next());
		}
		assertEquals(5, entities.size());
		assertTrue(entities.contains(testEntities[0]));
		assertTrue(entities.contains(testEntities[1]));
		assertTrue(entities.contains(testEntities[2]));
		assertTrue(entities.contains(testEntities[3]));
		assertTrue(entities.contains(testEntities[7]));
	}

	private class TestEntity extends AbstractEntity {
		public TestEntity() {
			super(new Box2D(0, 0, 0.01f, 0.01f), 0, 0, "");
		}

		@Override
		public boolean isSolid() {
			return true;
		}
	}
}
