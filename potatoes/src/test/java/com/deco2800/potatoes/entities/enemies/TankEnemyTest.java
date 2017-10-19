package com.deco2800.potatoes.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.deco2800.potatoes.BaseTest;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.worlds.WorldType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.deco2800.potatoes.entities.player.*;

import static org.junit.Assert.assertEquals;

/**
 * Test class to test the TankEnemy enemy type
 *
 * @author ryanjphelan & qishi
 */
public class TankEnemyTest extends BaseTest {

	private TankEnemy tankEmpty;
	private TankEnemy tank1;

	@Before
	public void setup() throws Exception {
		tankEmpty = new TankEnemy();
		tank1 = new TankEnemy(0, 0);
		GameManager.get().getManager(WorldManager.class).setWorld(WorldType.FOREST_WORLD);
	}

	@After
	public void cleanUp() {
		GameManager.get().clearManagers();
		tank1 = null;
		tankEmpty = null;
	}

	/*
	 * Test the bear when it is initialised using the empty constructor
	 */
	@Test
	public void emptyConstructor() {
		assertEquals(true, tankEmpty.getDirection() == null);
		assertEquals("bear", tankEmpty.getEnemyType());
		assertEquals(Color.PURPLE, tank1.getProgressBar().getColours().get(0));
		assertEquals(Color.RED, tank1.getProgressBar().getColours().get(1));
		assertEquals(Color.ORANGE, tank1.getProgressBar().getColours().get(2));
		assertEquals(Color.YELLOW, tank1.getProgressBar().getColours().get(3));
	}
	@Test
	public void bearTickTest(){
		tank1.onTick(4);
		tank1.setGoal(Player.class);
		tank1.onTick(4);
	}
	/*
	 * Test the toString method
	 */
	@Test
	public void toStringTest() {
		Assert.assertEquals("String mismatch", "Tank Enemy at (0, 0)", tank1.toString());
	}
}
