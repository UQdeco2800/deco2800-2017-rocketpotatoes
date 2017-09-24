package com.deco2800.potatoes;


import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import com.deco2800.potatoes.entities.enemies.MeleeAttackEvent;
import com.deco2800.potatoes.entities.projectiles.BallisticProjectile;
import com.deco2800.potatoes.entities.projectiles.Projectile;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.entities.Tower;

import org.junit.*;
import org.junit.Before;
import org.junit.Test;

import com.deco2800.potatoes.collisions.Box2D;
import com.deco2800.potatoes.entities.enemies.EnemyEntity;
import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.entities.enemies.BasicStats;


public class EnemyEntityTest {
	
	private class TestableEnemyEntity extends EnemyEntity {

		private float speed = 0.1f;
		private Class<?> goal = Player.class;

		public TestableEnemyEntity() {}

		public TestableEnemyEntity(float posX, float posY, float xLength, float yLength, String texture, 
                float maxHealth, float speed, Class<?> goal) {
            this(posX, posY, xLength, yLength, xLength, yLength, texture, maxHealth, speed, goal);
		}


		public TestableEnemyEntity(float posX, float posY, float xLength, float yLength, float xRenderLength, 
                float yRenderLength, String texture, float maxHealth, float speed, Class<?> goal) {
            super(new Box2D(posX, posY, xLength, yLength), xRenderLength, yRenderLength, texture, maxHealth, speed, 
                    goal);
		}

		@Override
		public BasicStats getBasicStats() {
			List<TimeEvent<EnemyEntity>> normalEvents = new LinkedList<>();
			BasicStats result = new BasicStats(200f, 0.4f, .4f, 500, normalEvents,"tankBear");
			result.getNormalEventsReference().add(new MeleeAttackEvent(500, Player.class));
			return result;
		}
	}
	
	

	TestableEnemyEntity enemyEntity;	
	private float speed = 0.1f;
	private Class<?> goal = Player.class;
	

//	@BeforeClass
//	public static void setUpBeforeClass() throws Exception {
//		//a fake game world so deathHandler can interact with it
//		InitialWorld mockWorld = mock(InitialWorld.class);
//		GameManager gm = GameManager.get();
//		gm.setWorld(mockWorld);
//	}

	
	@Before
	public void setUp() throws Exception {
		enemyEntity = new TestableEnemyEntity(1, 2, 3, 4, 5, 6, "texture", 100f, speed, goal);
	}
	
	
	//Common to all initialization test
	private void initTestCommon() {
		assertEquals("getSpeed() bad init ", speed, enemyEntity.getSpeed(), 0f);
		assertEquals("getGoal() bad init ", goal, enemyEntity.getGoal());
	}
	
	
	@Test
	public void initTest() {
		enemyEntity = new TestableEnemyEntity(1, 2, 4, 5, "texture", 100f, speed, goal);
		initTestCommon();
	}


	@Test
	public void initTest2() {
		enemyEntity = new TestableEnemyEntity(1, 2, 4, 5, 7, 8, "texture", 100f, speed, goal);
		initTestCommon();
	}


	@Test
	public void emptyTest() {
		try {
			enemyEntity = new TestableEnemyEntity();
		} catch (Exception E) {
			fail("No EnemyEntity serializable constructor");
		}
	}
	
	@Test
	public void setSpeedTest() {
		enemyEntity.setSpeed(3f);
		Assert.assertEquals("Failed to set Speed",3f, enemyEntity.getSpeed(), 0f);
	}
	
	@Test
	public void setGoalTest() {
		enemyEntity.setGoal(Tower.class);
		Assert.assertEquals("Failed to set Goal",Tower.class, enemyEntity.getGoal());
	}
	
	@Test
	public void getShotTest() {
		Projectile proj = new BallisticProjectile();
		enemyEntity.getShot(proj);
		Assert.assertTrue("enemy failed to getShot()", enemyEntity.getHealth() < enemyEntity.getMaxHealth());
	}
	
	
	
	
}
