package com.deco2800.potatoes;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.deco2800.potatoes.entities.MortalEntity;


public class MortalEntityTest {

	//in order to test death, we have to override deathHandler()
	private class TestableMortalEntity extends MortalEntity {

		public TestableMortalEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
				String texture, float maxHealth) {
			super(posX, posY, posZ, xLength, yLength, zLength, xLength, yLength, false, texture, maxHealth);
		}

		@Override
		public void deathHandler() { }
	}

	MortalEntity mortalEntity;
	private static final float HEALTH = 100f;

	@Before
	public void setUp() throws Exception {
		mortalEntity = new TestableMortalEntity(1, 2, 3, 4, 5, 6, "texture", HEALTH);
	}

	@After
	public void tearDown() throws Exception {
	}

	//Common to all initialisation test
	private void initTestCommon() {
		assertEquals("getMaxHealth() bad init ", HEALTH, mortalEntity.getMaxHealth(), 0f);
		assertEquals("getHealth() bad init ", HEALTH, mortalEntity.getHealth(), 0f);
		assertEquals("isDead() bad init ", false, mortalEntity.isDead());
		assertEquals("getDamageOffset() bad init ", 0f, mortalEntity.getDamageOffset(), 0f);
		assertEquals("getDamageScaling() bad init ", 1f, mortalEntity.getDamageScaling(), 0f);
	}

	@Test
	public void initTest() {
		mortalEntity = new MortalEntity(1, 2, 3, 4, 5, 6, "texture", HEALTH);
		initTestCommon();
	}

	@Test
	public void initTestEmpty() {
		try {
			mortalEntity = new MortalEntity();
		} catch (Exception E) {
			fail("No MortalEntity serializable constructor");
		}
	}

	@Test
	public void initTestVariableRendering() {
		mortalEntity = new MortalEntity(1, 2, 3, 4, 5, 6, 7, 8, "texture", HEALTH);
		initTestCommon();
	}

	@Test
	public void initTestCentred() {
		mortalEntity = new MortalEntity(1, 2, 3, 4, 5, 6, 7, 8, true, "texture", HEALTH);
		initTestCommon();
	}

	@Test
	public void damageHealTest() {
		assertEquals(HEALTH, mortalEntity.getHealth(), 0f);

		//regular case
		mortalEntity.damage(99);
		assertEquals(1, mortalEntity.getHealth(), 0f);
		mortalEntity.heal(98);
		assertEquals(99, mortalEntity.getHealth(), 0f);

		//0 health
		mortalEntity.damage(99);
		assertEquals(0, mortalEntity.getHealth(), 0f);
		assertTrue(mortalEntity.isDead());

		//back from the dead, asser no overheal
		mortalEntity.heal(130);
		assertFalse(mortalEntity.isDead());
		assertEquals(100, mortalEntity.getHealth(), 0f);

		//sub-zero
		mortalEntity.damage(103);
		assertEquals(-3f, mortalEntity.getHealth(), 0f);
		assertTrue(mortalEntity.isDead());
	}

	@Test
	public void damageOffsetTest() {
		//standard test
		mortalEntity.addDamageOffset(50);
		mortalEntity.damage(100);
		assertEquals(50, mortalEntity.getDamageOffset(), 0f);
		assertEquals(50, mortalEntity.getHealth(), 0f);

		//reverse previous offset
		mortalEntity.addDamageOffset(-50);
		mortalEntity.damage(25);
		assertEquals(0, mortalEntity.getDamageOffset(), 0f);
		assertEquals(25, mortalEntity.getHealth(), 0f);

		//ensure damageOffset cannot cause healing
		mortalEntity.addDamageOffset(500);
		mortalEntity.damage(25);
		assertEquals(500, mortalEntity.getDamageOffset(), 0f);
		assertEquals(25, mortalEntity.getHealth(), 0f);

	}

	@Test
	public void damageScalingTest() {
		//damage scaling
		mortalEntity.addDamageScaling(0.5f);
		mortalEntity.damage(100);
		assertEquals(0.5f, mortalEntity.getDamageScaling(), 0f);
		assertEquals(50, mortalEntity.getHealth(), 0f);

		//assert that scaling stacks correctly
		mortalEntity.addDamageScaling(0.5f);
		mortalEntity.damage(100);
		assertEquals(0.25f, mortalEntity.getDamageScaling(), 0f);
		assertEquals(25, mortalEntity.getHealth(), 0f);


		//revert scaling
		mortalEntity.removeDamageScaling(0.5f);
		assertEquals(0.5f, mortalEntity.getDamageScaling(), 0f);
		mortalEntity.removeDamageScaling(0.5f);
		assertEquals(1f, mortalEntity.getDamageScaling(), 0f);

		mortalEntity.heal(75);

		//dealing extra damage (scaling > 1)
		mortalEntity.addDamageScaling(4f);
		mortalEntity.damage(12.5f);
		assertEquals(4f, mortalEntity.getDamageScaling(), 0f);
		assertEquals(50, mortalEntity.getHealth(), 0f);

		//revert scaling again
		mortalEntity.removeDamageScaling(2f);
		assertEquals(2f, mortalEntity.getDamageScaling(), 0f);
		mortalEntity.removeDamageScaling(2f);
		assertEquals(1f, mortalEntity.getDamageScaling(), 0f);

		//negative scaling (healing)
		mortalEntity.addDamageScaling(-0.5f);
		mortalEntity.damage(200);
		assertEquals(-0.5f, mortalEntity.getDamageScaling(), 0f);
		assertEquals(100, mortalEntity.getHealth(), 0f);

		mortalEntity.removeDamageScaling(-0.5f);
		assertEquals(1f, mortalEntity.getDamageScaling(), 0f);

		//scaling and offset (negative)
		mortalEntity.addDamageScaling(0.5f);
		mortalEntity.addDamageOffset(-25);
		mortalEntity.damage(100);
		assertEquals(25, mortalEntity.getHealth(), 0f);

		mortalEntity.removeDamageScaling(0.5f);

		//scaling and offset (negative)
		mortalEntity.addDamageScaling(-1f);
		mortalEntity.addDamageOffset(100);
		mortalEntity.damage(25);
		assertEquals(50, mortalEntity.getHealth(), 0f);

	}

	@Test(expected = NullPointerException.class)
	public void deathHandlerTest() {
		mortalEntity = new MortalEntity(1, 2, 3, 4, 5, 6, "texture", HEALTH);
		mortalEntity.deathHandler();
	}

}
