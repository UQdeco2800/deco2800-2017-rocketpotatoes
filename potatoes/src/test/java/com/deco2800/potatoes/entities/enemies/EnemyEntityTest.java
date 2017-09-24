package com.deco2800.potatoes.entities.enemies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.collisions.Box2D;
import com.deco2800.potatoes.entities.PropertiesBuilder;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.entities.projectiles.BallisticProjectile;
import com.deco2800.potatoes.entities.projectiles.Projectile;
import com.deco2800.potatoes.entities.projectiles.Projectile.ProjectileType;
import com.deco2800.potatoes.entities.trees.ProjectileTree;

public class EnemyEntityTest {

	private class TestableEnemyEntity extends EnemyEntity {

		private float speed = 0.1f;
		private Class<?> goal = Player.class;
		private String enemyType = "squirrel";
		private Direction currentDirection;

		public TestableEnemyEntity() {
		}

		public TestableEnemyEntity(float posX, float posY, float xLength, float yLength, String texture, 
                float maxHealth, float speed, Class<?> goal) {
            super(new Box2D(posX, posY, xLength, yLength), xLength, yLength, texture, maxHealth, speed, goal);
		}

		public TestableEnemyEntity(float posX, float posY, float xLength, float yLength, float xRenderLength, 
                float yRenderLength, String texture, float maxHealth, float speed, Class<?> goal) {
            super(new Box2D(posX, posY, xLength, yLength), xRenderLength, yRenderLength, texture, maxHealth, speed, 
                    goal);
		}

		@Override
		public EnemyProperties getBasicStats() {
				EnemyProperties result = new PropertiesBuilder<>().setHealth(200).setSpeed(0.4f).setAttackRange(0.4f)
					.setTexture("tankBear").addEvent(new MeleeAttackEvent(500, Player.class)).createEnemyStatistics();
			return result;
		}

		@Override
		public String getEnemyType() { return enemyType; }

		@Override
		public Direction getDirection() { return currentDirection; }
	}

	TestableEnemyEntity enemyEntity;
	private float speed = 0.1f;
	private Class<?> goal = Player.class;

	// @BeforeClass
	// public static void setUpBeforeClass() throws Exception {
	// //a fake game world so deathHandler can interact with it
	// InitialWorld mockWorld = mock(InitialWorld.class);
	// GameManager gm = GameManager.get();
	// gm.setWorld(mockWorld);
	// }

	@Before
	public void setUp() throws Exception {
		enemyEntity = new TestableEnemyEntity(1, 2, 3, 4, 5, 6, "texture", 100f, speed, goal);
	}

	// Common to all initialization test
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
		Assert.assertEquals("Failed to set Speed", 3f, enemyEntity.getSpeed(), 0f);
	}

	@Test
	public void setGoalTest() {
		enemyEntity.setGoal(ProjectileTree.class);
		Assert.assertEquals("Failed to set Goal", ProjectileTree.class, enemyEntity.getGoal());
	}

	@Test
	public void getShotTest() {
		Projectile proj=new BallisticProjectile(null,new Vector3(0,0,0), new Vector3(1,1,1), 8, 10, ProjectileType.ROCKET, null,
				null);
		enemyEntity.getShot(proj);
		Assert.assertTrue("enemy failed to getShot()", enemyEntity.getHealth() < enemyEntity.getMaxHealth());
	}


	// registerNewEvent test shoudl already have events present
	// and then check they are all gone when the method is called.

	// getShot test would need to check the enemy health and then check the
	// health decreases as expected after some effect (only test one)

	// getter methods, just check you are getting what is expected:
	// not null and right value

	//For deatherHandler, check that the enemy has been removed from the world
	// and that it has been set to respawn in the eventManager



	private class SimpleEnemy extends EnemyEntity {

		private String enemyType = "squirrel";
		private Direction currentDirection;

		@Override
		public EnemyProperties getBasicStats() {
			EnemyProperties result = new PropertiesBuilder<>().setHealth(200).setSpeed(0.4f).setAttackRange(0.4f)
					.setTexture("tankBear").addEvent(new MeleeAttackEvent(500, Player.class)).createEnemyStatistics();
			return result;
		}

		@Override
		public String getEnemyType() { return enemyType; }

		@Override
		public Direction getDirection() { return currentDirection; }
	}
//
//	@Test
//	public void normalConstructor() {
//		SimpleEnemy se = new SimpleEnemy(1f, 1f, 1f, 2f, 2f, 2f,
//				new String("texture"), 10f, 12f, goal);
//	}
}
