package com.deco2800.potatoes;

import org.junit.*;

import com.deco2800.potatoes.entities.enemies.TankEnemy;

public class TankEnemyTest {
	TankEnemy testTank;
	
	@Before
	public void setup() {
		testTank = new TankEnemy(1,2,3);
	}
	
	@Test
	public void emptyTest() {
		testTank = new TankEnemy();
	}
	
	@Test
	public void toStringTest() {
		Assert.assertEquals("String mismatch", "Tank Enemy at (1, 2)", testTank.toString());
	}

}
