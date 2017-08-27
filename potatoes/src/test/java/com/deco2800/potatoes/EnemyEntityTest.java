package com.deco2800.potatoes;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.deco2800.potatoes.entities.EnemyEntity;
import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.worlds.InitialWorld;


public class EnemyEntityTest {
	EnemyEntity enemyEntity;
	private float speed = 0.1f;
	private Class<?> goal = Player.class;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//a fake game world so deathHandler can interact with it
		InitialWorld mockWorld = mock(InitialWorld.class);
		GameManager gm = GameManager.get();
		gm.setWorld(mockWorld);
	}

//	@Before
//	public void setUp() throws Exception {
//		enemyEntity = new EnemyEntity(1, 2, 3, 4, 5, 6, "texture", 100f, speed, goal);
//	}
//	
//	//Common to all initialisation test
//	private void initTestCommon() {
//		assertEquals("getMaxHealth() bad init ", speed, enemyEntity.getSpeed(), 0f);
//		assertEquals("getHealth() bad init ", goal, enemyEntity.getGoal());
//	}
//	
//	
//	@Test
//	public void enptyTest() {
//		try {
//			enemyEntity = new EnemyEntity();
//		} catch (Exception E) {
//			fail("No EnemyEntity serializable constructor");
//		}
//	}
	
	
}
