package com.deco2800.potatoes;


import com.deco2800.potatoes.collisions.Box2D;
import com.deco2800.potatoes.entities.AbstractEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class AbstractEntityTest {

	private class TestableAbstractEntity extends AbstractEntity {

		public TestableAbstractEntity() {};

		public TestableAbstractEntity(float posX, float posY, float xLength, float yLength, String texture) {

            super(new Box2D(posX, posY, xLength, yLength), xLength, yLength, texture);
		}


		public TestableAbstractEntity(float posX, float posY, float xLength, float yLength, float xRenderLength, 
                float yRenderLength, String texture) {
            super(new Box2D(posX, posY, xLength, yLength), xRenderLength, yRenderLength, texture);
		}
		public TestableAbstractEntity(boolean isSolid, boolean isStatic, float xRenderLength,
									  float yRenderLength, float xRenderOffset, float yRenderOffset, String texture){
			super(new Box2D(1, 1, 1, 1), isSolid, isStatic, new Box2D(1, 1, 1, 1), xRenderLength,
			 yRenderLength, xRenderOffset, yRenderOffset, texture);
		}
		public TestableAbstractEntity(boolean isSolid, boolean isStatic, boolean hasShadow ,float xRenderLength,
									  float yRenderLength, float xRenderOffset, float yRenderOffset, String texture){
			super(new Box2D(1, 1, 1, 1), isSolid, isStatic, hasShadow, xRenderLength,
					yRenderLength, xRenderOffset, yRenderOffset, texture);
		}


	}

	TestableAbstractEntity abstractEntity;
	TestableAbstractEntity abstractEntity2;
	TestableAbstractEntity abstractEntity3;
	TestableAbstractEntity abstractEntity4;

	@Before
	public void setUp() throws Exception {
		abstractEntity = new TestableAbstractEntity(1, 2, 4, 5, "texture");
		abstractEntity2 = new TestableAbstractEntity(1, 2, 4, 5, 4,
				5, "texture");
		abstractEntity3 = new TestableAbstractEntity(true, true, 4, 5,
				4,5, "texture");
		abstractEntity4 = new TestableAbstractEntity(true, true, false,4, 
				5, 4,5, "texture");
	}

	@After
	public void tearDown() {

		abstractEntity = null;
		abstractEntity2 = null;
		abstractEntity3 = null;
		abstractEntity4 = null;
	}

	//Common to all initialisation test
	private void initTestCommon() {
		assertEquals("getPosY() bad init ", 1f, abstractEntity.getPosX(), 0f);
		assertEquals("getPosY() bad init ", 2f, abstractEntity.getPosY(), 0f);
		// assertEquals("getXLength() bad init ", 4f, abstractEntity.getXLength(), 0f);
		// assertEquals("getYLength() bad init ", 5f, abstractEntity.getYLength(), 0f);
	}

	@Test
	public void initTest() {
		abstractEntity = new TestableAbstractEntity(1, 2, 4, 5, "texture");
		initTestCommon();
		assertEquals("getPosX() bad init ", 1f, abstractEntity.getPosX(), 0f);
	}

	@Test
	public void initTestEmpty() {
		try {
			abstractEntity = new TestableAbstractEntity();
		} catch (Exception E) {
			fail("No AbstractEntity serializable constructor");
		}
	}

	private void testGetRenderLength() {
		assertEquals("getXRenderLength() bad init ", 7f, abstractEntity.getXRenderLength(), 0f);
		assertEquals("getYRenderLength() bad init ", 8f, abstractEntity.getYRenderLength(), 0f);
	}

	@Test
	public void initTestVariableRendering() {
		abstractEntity = new TestableAbstractEntity(1, 2, 4, 5, 7, 8, "texture");
		initTestCommon();
		testGetRenderLength();
		assertEquals("getPosX() bad init ", 1f, abstractEntity.getPosX(), 0f);
	}

	@Test
	public void textureTest() {
		assertEquals("texture", abstractEntity.getTexture());
		abstractEntity.setTexture("differentTexture");
		assertEquals("differentTexture", abstractEntity.getTexture());
	}


	@Test
	public void equalsTest() {
		TestableAbstractEntity original = new TestableAbstractEntity(1, 2, 3, 4, "texture");
		TestableAbstractEntity imposter = new TestableAbstractEntity(1, 2, 3, 4, "texture");
		assertTrue(original.equals(imposter));
		assertTrue(imposter.equals(original));

		//iterate through each parameter of AbstractEntity, changing one at a time
		float p[] = {1, 2, 3, 4}; //parameters
		for (int i = 0; i < 4; i++) {
			p[i] = i;
			original = new TestableAbstractEntity(p[0], p[1], p[2], p[3], "texture");
			assertFalse(original.equals(imposter));
			assertFalse(imposter.equals(original));
			p[i] = i + 1;
			original = new TestableAbstractEntity(p[0], p[1], p[2], p[3], "texture");
			assertTrue(p[i] + " is not " + (i + 1f) + " for " + i, original.equals(imposter));
			assertTrue(p[i] + " is not " + (i + 1f) + " for " + i, imposter.equals(original));
		}

		original.setTexture("FAKE NEWS");
		assertFalse(original.equals(imposter));
		assertFalse(imposter.equals(original));
	}
	@Test
	public void getTest() {
		TestableAbstractEntity original = new TestableAbstractEntity(1, 2, 3, 4, "texture");
		original.getXRenderOffset();
		original.getYRenderOffset();
		original.getShadow();
        original.distance(9,9);
		original.hasShadow();
		original.isSolid();
		original.setMass(9);
		original.getMass();
		abstractEntity4.setMass();
	}
	@Test
	public void moveTest() {
		TestableAbstractEntity original = new TestableAbstractEntity(1, 2, 3, 4, "texture");
		original.setStatic(true);
		original.setMoveTowards(5,6);
		original.onTickMovement();
		original.setMoveTowards(new Box2D(1, 1, 1, 1));
	}
}
