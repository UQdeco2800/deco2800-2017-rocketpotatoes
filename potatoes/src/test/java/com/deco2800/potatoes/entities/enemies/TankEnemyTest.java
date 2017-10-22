package com.deco2800.potatoes.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.BaseTest;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.entities.projectiles.BallisticProjectile;
import com.deco2800.potatoes.entities.projectiles.Projectile;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.worlds.ForestWorld;
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
		GameManager.get().getManager(WorldManager.class).setWorld(ForestWorld.get());
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
		for (String data:tankEmpty.getEnemyType()) {
			assertEquals("bear", data);

		}

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

		Assert.assertEquals("_walk",tank1.getEnemyStatus());

		Projectile proj=new BallisticProjectile(null,new Vector3(0,0,0), new Vector3(1,1,1), 8, 10, Projectile.ProjectileTexture.ROCKET, null,
				null);
		tank1.getShot(proj);
		tank1.enemyState();
		Assert.assertEquals("_attack",tank1.getEnemyStatus());

		sTime = System.currentTimeMillis();
		while((System.currentTimeMillis()-sTime)/1000.0<4);
		tank1.enemyState();
		Assert.assertEquals("_walk",tank1.getEnemyStatus());



	}

	@Test
	public void getEnemyTypeTest() throws Exception {
		Assert.assertEquals(2,tank1.getEnemyType().length);

	}

	@Test
	public void getProgressBarTest() {
        assertEquals("healthBarRed", tank1.getProgressBar().getTexture());
        
	}


	/*
	 * Test the toString method
	 */
	@Test
	public void toStringTest() {
		Assert.assertEquals("String mismatch", "Tank Enemy at (0, 0)", tank1.toString());
	}
}
