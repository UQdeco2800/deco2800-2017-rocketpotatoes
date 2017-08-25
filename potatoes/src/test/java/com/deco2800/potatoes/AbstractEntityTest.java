package com.deco2800.potatoes;

import com.deco2800.potatoes.entities.AbstractEntity;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class AbstractEntityTest {

	private class TestableAbstractEntity extends AbstractEntity {

		public TestableAbstractEntity() {};

		public TestableAbstractEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
				String texture) {
			super(posX, posY, posZ, xLength, yLength, zLength, texture);
		}


		public TestableAbstractEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
				float xRenderLength, float yRenderLength, String texture) {
			super(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, texture);
		}

		public TestableAbstractEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
				float xRenderLength, float yRenderLength, boolean centered, String texture) {
			super(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, centered, texture);
		}

	}

	TestableAbstractEntity abstractEntity;

	@Before
	public void setUp() throws Exception {
		abstractEntity = new TestableAbstractEntity(1, 2, 3, 4, 5, 6, "texture");
	}

	//Common to all initialisation test
	private void initTestCommon() {
		assertEquals("getPosY() bad init ", 2f, abstractEntity.getPosY(), 0f);
		assertEquals("getPosZ() bad init ", 3f, abstractEntity.getPosZ(), 0f);
		assertEquals("getXLength() bad init ", 4f, abstractEntity.getXLength(), 0f);
		assertEquals("getYLength() bad init ", 5f, abstractEntity.getYLength(), 0f);
		assertEquals("getZLength() bad init ", 6f, abstractEntity.getZLength(), 0f);
	}

	@Test
	public void initTest() {
		abstractEntity = new TestableAbstractEntity(1, 2, 3, 4, 5, 6, "texture");
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
		abstractEntity = new TestableAbstractEntity(1, 2, 3, 4, 5, 6, 7, 8, "texture");
		initTestCommon();
		testGetRenderLength();
		assertEquals("getPosX() bad init ", 1f, abstractEntity.getPosX(), 0f);
	}

	@Test
	public void initTestCentred() {
		abstractEntity = new TestableAbstractEntity(1, 2, 3, 4, 5, 6, 7, 8, true, "texture");
		initTestCommon();
		assertEquals("getPosX() bad init ", 1.5f, abstractEntity.getPosX(), 0f);
		testGetRenderLength();
	}

	@Test
	public void textureTest() {
		assertEquals("texture", abstractEntity.getTexture());
		abstractEntity.setTexture("differentTexture");
		assertEquals("differentTexture", abstractEntity.getTexture());
	}


	@Test
	public void equalsTest() {
		TestableAbstractEntity original = new TestableAbstractEntity(1, 2, 3, 4, 5, 6, "texture");
		TestableAbstractEntity imposter = new TestableAbstractEntity(1, 2, 3, 4, 5, 6, "texture");
		assertTrue(original.equals(imposter));
		assertTrue(imposter.equals(original));

		//iterate through each parameter of AbstractEntity, changing one at a time
		float p[] = {1, 2, 3, 4, 5, 6}; //parameters
		for (int i = 0; i < 6; i++) {
			p[i] = i;
			original = new TestableAbstractEntity(p[0], p[1], p[2], p[3], p[4], p[5], "texture");
			assertFalse(original.equals(imposter));
			assertFalse(imposter.equals(original));
			p[i] = i + 1;
			original = new TestableAbstractEntity(p[0], p[1], p[2], p[3], p[4], p[5], "texture");
			assertTrue(p[i] + " is not " + (i + 1f) + " for " + i, original.equals(imposter));
			assertTrue(p[i] + " is not " + (i + 1f) + " for " + i, imposter.equals(original));
		}

		original.setTexture("FAKE NEWS");
		assertFalse(original.equals(imposter));
		assertFalse(imposter.equals(original));
	}
}
