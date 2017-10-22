package com.deco2800.potatoes.entities.health;

import com.deco2800.potatoes.collisions.Point2D;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.worlds.World;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MortalEntityTest {

	MortalEntity mortalEntity;
	private static final float HEALTH = 100f;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//a fake game world so deathHandler can interact with it
		World mockWorld = mock(World.class);
		GameManager gm = GameManager.get();
		gm.setWorld(mockWorld);
	}

	@Before
	public void setUp() throws Exception {
		mortalEntity = new MortalEntity(1, 2, 4, 5, "texture", HEALTH);
	}
	
	@After
    public void cleanUp() {
    	GameManager.get().clearManagers();
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
		mortalEntity = new MortalEntity(1, 2, 4, 5, "texture", HEALTH);
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
		mortalEntity = new MortalEntity(1, 2, 4, 5, 7, 8, "texture", HEALTH);
		initTestCommon();
	}
	@Test
	public void maxHealthTest() {
		mortalEntity.addMaxHealth(37);
		assertEquals("addMaxHealth() can't add", HEALTH + 37, mortalEntity.getMaxHealth(), 0f);
		mortalEntity.addMaxHealth(-39);
		assertEquals("addMaxHealth() can't subtract", HEALTH - 2, mortalEntity.getMaxHealth(), 0f);
		mortalEntity.addMaxHealth(-99);
		assertEquals("addMaxHealth() can't subtract", 1f, mortalEntity.getMaxHealth(), 0f);

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

	@Test
	public void deathHandlerTest() {
		reset(GameManager.get().getWorld()); //resets all invocation counters related to mockWorld
		mortalEntity.deathHandler();
		verify(GameManager.get().getWorld()).removeEntity(any()); //ensure deathHandler called removeEntity()
	}

	@Test
	public void setHealthTest() {
		assertFalse("Health should be set to 0f", mortalEntity.setHealth(0f));
		assertEquals(0f, mortalEntity.getHealth(), 0f);
		assertTrue("Health should be set to 100f", mortalEntity.setHealth(100f));
		assertEquals(1f, mortalEntity.getHealth(), 100f);

	}
	
	@Test
	public void getProgressTest() {
		assertEquals(100, mortalEntity.getProgress());
		assertFalse("Health should be set to 23.4f", mortalEntity.setHealth(23.4f));
		assertEquals("Progress should equal 23 and be an integer", 23, mortalEntity.getProgress());
		mortalEntity.showProgress();
		
	}

	@Test
	public void getProgressRatio() {
		assertEquals("Progress ratio should be maximum", 1, mortalEntity.getProgressRatio(), 0f);
		assertFalse("Health should be set to 50f", mortalEntity.setHealth(50f));
		assertEquals("Progress ratio should be 0.5", 0.5f, mortalEntity.getProgressRatio(), 0f);
		assertFalse("Health should be set to 0f", mortalEntity.setHealth(0f));
		assertEquals("Progress ratio should be 0", 0f, mortalEntity.getProgressRatio(), 0f);
	}

	@Test
	public void getMaxProgressTest() {
		assertEquals("max progress should be 100f", 100f, mortalEntity.getMaxProgress(), 0f);
		assertEquals("max health should be 120f", 120f, mortalEntity.addMaxHealth(20f), 0f);
		assertEquals("max progress should be 120f", 120f, mortalEntity.getMaxProgress(), 0f);

	  	}

}
