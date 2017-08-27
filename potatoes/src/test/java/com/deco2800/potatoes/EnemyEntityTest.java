package com.deco2800.potatoes;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.graphics.Color;
import com.deco2800.potatoes.entities.EnemyEntity;
import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.entities.ProgressBarEntity;
import com.deco2800.potatoes.entities.Enemies.BasicStats;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.worlds.InitialWorld;


public class EnemyEntityTest {
	
	private class TestableEnemyEntity extends EnemyEntity {

		public TestableEnemyEntity() {};

		public TestableEnemyEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
				String texture, float maxHealth, float speed, Class<?> goal) {
			super(posX, posY, posZ, xLength, yLength, zLength, xLength, yLength, false, texture, maxHealth, speed, goal);
		}


		public TestableEnemyEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
				float xRenderLength, float yRenderLength, String texture, float maxHealth, float speed, Class<?> goal) {
			super(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, texture, maxHealth, speed, goal);
		}

		public TestableEnemyEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
				float xRenderLength, float yRenderLength, boolean centered, String texture, float maxHealth, float speed, Class<?> goal) {
			super(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, centered, texture, maxHealth, speed, goal);
		}

		@Override
		public BasicStats getBasicStats() {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	

	TestableEnemyEntity enemyEntity;	
	private float speed = 0.1f;
	private Class<?> goal = Player.class;
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//a fake game world so deathHandler can interact with it
		InitialWorld mockWorld = mock(InitialWorld.class);
		GameManager gm = GameManager.get();
		gm.setWorld(mockWorld);
	}

	
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
		enemyEntity = new TestableEnemyEntity(1, 2, 3, 4, 5, 6, "texture", 100f, speed, goal);
		initTestCommon();
	}
	
	
	@Test
	public void initTest2() {
		enemyEntity = new TestableEnemyEntity(1, 2, 3, 4, 5, 6, 7, 8, "texture", 100f, speed, goal);
		initTestCommon();
	}
	
	
	@Test
	public void initTest3() {
		enemyEntity = new TestableEnemyEntity(1, 2, 3, 4, 5, 6, 7, 8, true, "texture", 100f, speed, goal);
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
	
	
}
