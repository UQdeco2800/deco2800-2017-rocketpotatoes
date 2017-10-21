package com.deco2800.potatoes.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.BaseTest;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.entities.projectiles.BallisticProjectile;
import com.deco2800.potatoes.entities.projectiles.Projectile;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.worlds.WorldType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class to test the TankEnemy enemy type
 *
 * @author ryanjphelan & qishi
 */
public class TankEnemyTest extends BaseTest {

	private TankEnemy tankEmpty;
	private TankEnemy tank1;
	private long sTime=0;
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
//		assertEquals(true, tankEmpty.getDirection() == null);

		for (String data:tankEmpty.getEnemyType()) {
			assertEquals("bear", data);

		}

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
	@Test
	public void onTickTest() throws Exception {


	}

	@Test
	public void isAttackingTest() throws Exception {
		Assert.assertFalse(tank1.isAttacking());
		Projectile proj=new BallisticProjectile(null,new Vector3(0,0,0), new Vector3(1,1,1), 8, 10, Projectile.ProjectileTexture.ROCKET, null,
				null);
		tank1.getShot(proj);
		Assert.assertTrue(tank1.isAttacking());
	}

	@Test
	public void getBasicStatsTest() throws Exception {


	}
	@Test
	public void enemyStateTest(){
		tank1.enemyState();

		Assert.assertEquals("walk",tank1.getEnemyStatus());

		Projectile proj=new BallisticProjectile(null,new Vector3(0,0,0), new Vector3(1,1,1), 8, 10, Projectile.ProjectileTexture.ROCKET, null,
				null);
		tank1.getShot(proj);
		tank1.enemyState();
		Assert.assertEquals("attack",tank1.getEnemyStatus());

		sTime = System.currentTimeMillis();
		while((System.currentTimeMillis()-sTime)/1000.0<4);
		tank1.enemyState();
		Assert.assertEquals("walk",tank1.getEnemyStatus());



	}

	@Test
	public void getEnemyTypeTest() throws Exception {
		Assert.assertEquals(8,tank1.getEnemyType().length);

	}


	@Test
	public void getProgressBarTest() throws Exception {
//		System.out.print(tank1.getProgressBar());
	}

	/*
	 * Test the toString method
	 */
	@Test
	public void toStringTest() {
		Assert.assertEquals("String mismatch", "Tank Enemy at (0, 0)", tank1.toString());
	}
}
