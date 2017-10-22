/**
 *
 */
package com.deco2800.potatoes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.After;
import org.junit.Test;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.collisions.Box2D;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.effects.AOEEffect;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.entities.effects.LazerEffect;
import com.deco2800.potatoes.entities.enemies.EnemyEntity;
import com.deco2800.potatoes.entities.health.MortalEntity;

import com.deco2800.potatoes.entities.projectiles.*;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.WorldUtil;
import com.deco2800.potatoes.worlds.World;
import org.junit.After;
import org.junit.Test;
import com.deco2800.potatoes.entities.projectiles.Projectile.ProjectileTexture;

import java.util.Optional;

import com.deco2800.potatoes.entities.projectiles.BallisticProjectile;
import com.deco2800.potatoes.entities.projectiles.HomingProjectile;
import com.deco2800.potatoes.entities.projectiles.Projectile;
import com.deco2800.potatoes.entities.projectiles.Projectile.ProjectileTexture;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.WorldUtil;
import com.deco2800.potatoes.worlds.World;


/**
 * @author Tristan Cargill
 */
public class ProjectileTest {


	protected Projectile testProjectile;
//	protected PlayerProjectile testPlayerProjectile;
	protected BallisticProjectile testBallisticProjectile;
	protected HomingProjectile testHomingProjectile;
	protected MineBomb testMineBomb;
	protected OrbProjectile testOrbProjectile;
	protected Optional<AbstractEntity> target = null;
	protected Class<?> targetClass = EnemyEntity.class;
	protected float posX = 5f;
	protected float posY = 10f;
	protected float posZ = 0;
	protected float TargetPosX = 9;
	protected float TargetPosY = 12;
	protected float range = 8;
	protected float damage = 10;
	protected Vector3 startPos = new Vector3(posX, posY, posZ);
	protected Vector3 targetPos = new Vector3(TargetPosX, TargetPosY, posZ);
	protected Projectile.ProjectileTexture projectileTexture = Projectile.ProjectileTexture.ROCKET;
	protected Effect startEffect = new AOEEffect();
	protected Effect endEffect = null;
	protected String Directions = "E";
	protected Vector3 getStartPos;

//	protected PlayerProjectile.PlayerShootMethod playerShootMethod = PlayerProjectile.PlayerShootMethod.DIRECTIONAL;

	private class TestWorld extends World {

	}

	@After
	public void tearDown() {
		GameManager.get().clearManagers();
		testProjectile = null;
//		testPlayerProjectile = null;
		testBallisticProjectile = null;
		testHomingProjectile = null;
		testMineBomb = null;
		testOrbProjectile = null;
	}

	@Test
	public void TestProjectile() {

		GameManager.get().setWorld(new ProjectileTest.TestWorld());
		target = WorldUtil.getClosestEntityOfClass(EnemyEntity.class, 0, 0);
		assertTrue(target.toString().equalsIgnoreCase("optional.empty"));
		testProjectile = new Projectile(targetClass.getClass(), startPos, targetPos, range, damage, projectileTexture,
				startEffect, endEffect);
		assertNotNull(testProjectile);
		assertNotNull(target);
		assertTrue(testProjectile.getDamage() == 10);
		assertTrue(testProjectile.getTexture().contains("rocket"));
		assertTrue(testProjectile.getRange() == 8);
		assertEquals(5.178885459899902, testProjectile.getPosX(), 0.2);
		assertEquals(10.089442253112793, testProjectile.getPosY(), 0.2);
		assertEquals(0, testProjectile.getPosZ(), 0.0);
		assertEquals(startEffect, testProjectile.getStartEffect());
		assertEquals(endEffect, testProjectile.getEndEffect());
		assertEquals(targetClass.getClass(), testProjectile.getTargetClass());
//		assertEquals(PlayerProjectile.PlayerShootMethod.DIRECTIONAL, playerShootMethod);

	}

	@Test
	public void TestBallisticProjectile() {
		GameManager.get().setWorld(new ProjectileTest.TestWorld());
		target = WorldUtil.getClosestEntityOfClass(EnemyEntity.class, 0, 0);
		assertTrue(target.toString().equalsIgnoreCase("optional.empty"));
		endEffect = new AOEEffect(target.getClass(), targetPos, 1, 1);
//		playerShootMethod = PlayerProjectile.PlayerShootMethod.DIRECTIONAL;
		testBallisticProjectile = new BallisticProjectile(targetClass.getClass(), startPos, targetPos, range, damage,
				projectileTexture, startEffect, endEffect);
		assertNotNull(testBallisticProjectile);
		assertNotNull(target);
		assertTrue(testBallisticProjectile.getDamage() == 10);
		assertEquals(startEffect, testBallisticProjectile.getStartEffect());
		assertEquals(endEffect, testBallisticProjectile.getEndEffect());
		assertTrue(testBallisticProjectile.getTexture().contains("rocket"));
		assertTrue(testBallisticProjectile.getRange() == 8);
		assertEquals(5.178885459899902, testBallisticProjectile.getPosX(), 0.2);
		assertEquals(10.089442253112793, testBallisticProjectile.getPosY(), 0.2);
		assertEquals(0, testBallisticProjectile.getPosZ(), 0.0);
		assertEquals(TargetPosX, testBallisticProjectile.getTargetPosX(), 0);
		assertEquals(TargetPosY, testBallisticProjectile.getTargetPosY(), 0);
		assertEquals(targetClass.getClass(), testBallisticProjectile.getTargetClass());
//		assertEquals(PlayerProjectile.PlayerShootMethod.DIRECTIONAL, playerShootMethod);

	}

	@Test
	public void TestHomingProjectile() {
		GameManager.get().setWorld(new ProjectileTest.TestWorld());
		target = WorldUtil.getClosestEntityOfClass(EnemyEntity.class, 0, 0);
		assertTrue(target.toString().equalsIgnoreCase("optional.empty"));
		endEffect = new AOEEffect(target.getClass(), targetPos, 1, 1);
//		playerShootMethod = PlayerProjectile.PlayerShootMethod.CLOSEST;
		testHomingProjectile = new HomingProjectile(targetClass.getClass(), startPos, targetPos, range, damage,
				projectileTexture, startEffect, endEffect);
		assertNotNull(testHomingProjectile);
		assertNotNull(target);
		assertTrue(testHomingProjectile.getDamage() == 10);
		assertEquals(startEffect, testHomingProjectile.getStartEffect());
		assertEquals(endEffect, testHomingProjectile.getEndEffect());
		assertTrue(testHomingProjectile.getTexture().contains("rocket"));
		assertTrue(testHomingProjectile.getRange() == 8);
		assertEquals(5.178885459899902, testHomingProjectile.getPosX(), 0.2);
		assertEquals(10.089442253112793, testHomingProjectile.getPosY(), 0.2);
		assertEquals(0, testHomingProjectile.getPosZ(), 0.0);
		assertEquals(TargetPosX, testHomingProjectile.getTargetPosX(), 0);
		assertEquals(TargetPosY, testHomingProjectile.getTargetPosY(), 0);
		assertEquals(targetClass.getClass(), testHomingProjectile.getTargetClass());

	}


//	@Test
//	public void TestPlayerProjectile() {
//		GameManager.get().setWorld(new ProjectileTest.TestWorld());
//		target = WorldUtil.getClosestEntityOfClass(EnemyEntity.class, 0, 0);
//		assertTrue(target.toString().equalsIgnoreCase("optional.empty"));
//		endEffect = new AOEEffect(target.getClass(), targetPos, 1, 1);
//		testPlayerProjectile = new PlayerProjectile(targetClass.getClass(), startPos, targetPos, range, damage,
//				projectileTexture, startEffect, endEffect, Directions, playerShootMethod, BallisticProjectile.class);
//		assertNotNull(testPlayerProjectile);
//		assertNotNull(target);
//		assertTrue(testPlayerProjectile.getDamage() == 10);
//		assertEquals(startEffect, testPlayerProjectile.getStartEffect());
//		assertEquals(endEffect, testPlayerProjectile.getEndEffect());
//		assertEquals(0, testPlayerProjectile.getPosZ(), 0.0);
//		assertEquals(TargetPosX, testPlayerProjectile.getTargetPosX(), 0);
//		assertEquals(TargetPosY, testPlayerProjectile.getTargetPosY(), 0);
//		assertEquals(targetClass.getClass(), testPlayerProjectile.getTargetClass());
//
//	}



	@Test
	public void TestMineBomb(){
		startPos = new Vector3(posX,posY,posZ);
		GameManager.get().setWorld(new ProjectileTest.TestWorld());
		testMineBomb = new MineBomb(startPos,range,damage,startEffect,endEffect);
		assertNotNull(testMineBomb);
		assertTrue(testMineBomb.getPosX() == posX);
		assertTrue(testMineBomb.getPosY() == posY);
		assertEquals(startEffect,testMineBomb.getStartEffect());
		assertEquals(endEffect,testMineBomb.getEndEffect());
	}

	@Test
	public void TestOrbProjectile(){
		GameManager.get().setWorld(new ProjectileTest.TestWorld());
		target = WorldUtil.getClosestEntityOfClass(EnemyEntity.class, 0, 0);
		startPos = new Vector3(posX,posY,posZ);
		targetPos = new Vector3(TargetPosX, TargetPosY, posZ);
		testOrbProjectile = new OrbProjectile(targetClass.getClass(),startPos,targetPos,range,damage,projectileTexture,startEffect,endEffect);
		assertNotNull(testOrbProjectile);
		float orbPosX = testOrbProjectile.getPos().x;
		float orbPosY = testOrbProjectile.getPos().y;
		assertTrue(orbPosX == posX);
		assertTrue(orbPosY == posY);
		assertEquals(startEffect,testOrbProjectile.getStartEffect());
		assertEquals(endEffect,testOrbProjectile.getEndEffect());
		assertTrue(testOrbProjectile.getRange()==range);
		assertTrue(testOrbProjectile.getDamage()==damage);
		assertEquals(testOrbProjectile.getTargetClass(),targetClass.getClass());
		assertEquals(testOrbProjectile.getProjectileTexture(),projectileTexture);
	}



	@Test
	public void TestAOEEffect() {
		AOEEffect testAOEEffect = new AOEEffect(targetClass, targetPos, damage, range);
		assertEquals(damage, testAOEEffect.getDamage(), 0);
		assertEquals(targetPos.x, testAOEEffect.getPosX(), 0.2);
		assertEquals(targetPos.y, testAOEEffect.getPosY(), 0.2);
	}

	private class TestableEffects extends Effect {
		public TestableEffects() {
		}

		public TestableEffects(Class<?> targetClass, Vector3 position, float xLength, float yLength, float zLength,
				float xRenderLength, float yRenderLength, float damage, float range, EffectTexture effectTexture) {
			super(targetClass, new Box2D(position.x, position.y, xLength, yLength), xRenderLength, yRenderLength,
					damage, range, effectTexture);
		}
	}

	@Test
	public void TestEffects() {
		TestableEffects testableEffects = new TestableEffects(targetClass, startPos, 0.4f, 0.4f, 0f, 0.4f, 0.4f, damage,
				range, Effect.EffectTexture.AOE);
		assertNotNull(testableEffects);
		assertEquals(startPos.x, testableEffects.getPosX(), 0);
		assertEquals(startPos.y, testableEffects.getPosY(), 0);
		assertEquals(damage, testableEffects.getDamage(), 0);
		assertEquals(0.4f, testableEffects.getXRenderLength(), 0);
		assertEquals(0.4f, testableEffects.getYRenderLength(), 0);
		// assertEquals(0.4f, testableEffects.getXLength(), 0);
		// assertEquals(0.4f, testableEffects.getYLength(), 0);
		// assertEquals(0.0f, testableEffects.getZLength(), 0);
		assertTrue(testableEffects.getTexture().equalsIgnoreCase("aoe1"));

	}

	@Test
	public void TestLazerEffect() {
		LazerEffect lazerEffect = new LazerEffect(targetClass, startPos, targetPos, damage, range);
		assertEquals(damage, lazerEffect.getDamage(), 0);
		lazerEffect.onTick(4);
	}

	@Test
	public void getTest() {
		testProjectile = new Projectile(targetClass.getClass(), startPos, targetPos, range, damage, projectileTexture,
				startEffect, endEffect);
		testProjectile.rotationAngle();
		testProjectile.onTick(2);
		ProjectileTexture.ROCKET.textures();
		ProjectileTexture.CHILLI.textures();
		ProjectileTexture.LEAVES.textures();
		ProjectileTexture.ACORN.textures();
	}


}
