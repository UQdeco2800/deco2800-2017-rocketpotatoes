/**
 *
 */
package com.deco2800.potatoes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.effects.AOEEffect;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.entities.enemies.EnemyEntity;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.entities.projectiles.PlayerProjectile;
import com.deco2800.potatoes.entities.projectiles.Projectile;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.WorldUtil;
import com.deco2800.potatoes.worlds.World;
import org.junit.Test;

import com.deco2800.potatoes.entities.projectiles.BallisticProjectile;
import com.deco2800.potatoes.entities.projectiles.HomingProjectile;

import java.util.Optional;

/**
 * @author Tristan Cargill
 */
public class ProjectileTest {
    protected Projectile testProjectile;
    protected PlayerProjectile testPlayerProjectile;
    protected BallisticProjectile testBallisticProjectile;
    protected HomingProjectile testHomingProjectile;
    protected Optional<AbstractEntity> target = null;
    protected Class<?> targetClass = MortalEntity.class;
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

    protected PlayerProjectile.PlayerShootMethod playerShootMethod = PlayerProjectile.PlayerShootMethod.DIRECTIONAL;

    private class TestWorld extends World {

    }

    @Test
    public void TestProjectile() {

        GameManager.get().setWorld(new ProjectileTest.TestWorld());
        target = WorldUtil.getClosestEntityOfClass(EnemyEntity.class, 0, 0);
        assertTrue(target.toString().equalsIgnoreCase("optional.empty"));
        testProjectile = new Projectile(targetClass.getClass(), startPos, targetPos, range, damage, projectileTexture, startEffect, endEffect, Directions, playerShootMethod);
        assertNotNull(testProjectile);
        assertNotNull(target);
        assertTrue(testProjectile.getDamage() == 10);
        assertTrue(testProjectile.getTexture().contains("rocket"));
        assertTrue(testProjectile.getRange() == 8);
        assertEquals(4.910557270050049, testProjectile.getPosX(), 0.2);
        assertEquals(9.821114540100098, testProjectile.getPosY(), 0.2);
        assertEquals(0, testProjectile.getPosZ(), 0.0);
        assertEquals(TargetPosX, testProjectile.getTargetPosX(), 0);
        assertEquals(TargetPosY, testProjectile.getTargetPosY(), 0);
        assertEquals(startEffect,testProjectile.getStartEffect());
        assertEquals(endEffect,testProjectile.getEndEffect());
        assertEquals(targetClass.getClass(), testProjectile.getTargetClass());
        assertEquals(PlayerProjectile.PlayerShootMethod.DIRECTIONAL, playerShootMethod);

    }

    @Test
    public void TestBallisticProjectile() {
        GameManager.get().setWorld(new ProjectileTest.TestWorld());
        target = WorldUtil.getClosestEntityOfClass(EnemyEntity.class, 0, 0);
        assertTrue(target.toString().equalsIgnoreCase("optional.empty"));
        endEffect = new AOEEffect(target.getClass(), targetPos, 1, 1);
        testBallisticProjectile = new BallisticProjectile(targetClass.getClass(), startPos, targetPos, range, damage, projectileTexture, startEffect, endEffect, Directions, playerShootMethod);
        assertNotNull(testBallisticProjectile);
        assertNotNull(target);
        assertTrue(testBallisticProjectile.getDamage() == 10);
        assertEquals(startEffect,testBallisticProjectile.getStartEffect());
        assertEquals(endEffect,testBallisticProjectile.getEndEffect());
        assertTrue(testBallisticProjectile.getTexture().contains("rocket"));
        assertTrue(testBallisticProjectile.getRange() == 8);
        assertEquals(4.910557270050049, testBallisticProjectile.getPosX(), 0.2);
        assertEquals(9.821114540100098, testBallisticProjectile.getPosY(), 0.2);
        assertEquals(0, testBallisticProjectile.getPosZ(), 0.0);
        assertEquals(TargetPosX, testBallisticProjectile.getTargetPosX(), 0);
        assertEquals(TargetPosY, testBallisticProjectile.getTargetPosY(), 0);
        assertEquals(targetClass.getClass(), testBallisticProjectile.getTargetClass());
        assertEquals(PlayerProjectile.PlayerShootMethod.DIRECTIONAL, playerShootMethod);

    }

    @Test
    public void TestHomingProjectile() {
        GameManager.get().setWorld(new ProjectileTest.TestWorld());
        target = WorldUtil.getClosestEntityOfClass(EnemyEntity.class, 0, 0);
        assertTrue(target.toString().equalsIgnoreCase("optional.empty"));
        endEffect = new AOEEffect(target.getClass(), targetPos, 1, 1);
        testHomingProjectile = new HomingProjectile(targetClass.getClass(), startPos, targetPos, range, damage, projectileTexture, startEffect, endEffect, Directions, playerShootMethod);
        assertNotNull(testHomingProjectile);
        assertNotNull(target);
        assertTrue(testHomingProjectile.getDamage() == 10);
        assertEquals(startEffect,testHomingProjectile.getStartEffect());
        assertEquals(endEffect,testHomingProjectile.getEndEffect());
        assertTrue(testHomingProjectile.getTexture().contains("rocket"));
        assertTrue(testHomingProjectile.getRange() == 8);
        assertEquals(4.910557270050049, testHomingProjectile.getPosX(), 0.2);
        assertEquals(9.821114540100098, testHomingProjectile.getPosY(), 0.2);
        assertEquals(0, testHomingProjectile.getPosZ(), 0.0);
        assertEquals(TargetPosX, testHomingProjectile.getTargetPosX(), 0);
        assertEquals(TargetPosY, testHomingProjectile.getTargetPosY(), 0);
        assertEquals(targetClass.getClass(), testHomingProjectile.getTargetClass());
        assertEquals(PlayerProjectile.PlayerShootMethod.DIRECTIONAL, playerShootMethod);

    }


}
