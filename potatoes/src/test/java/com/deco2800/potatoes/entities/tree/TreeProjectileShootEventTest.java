package com.deco2800.potatoes.entities.tree;

import com.deco2800.potatoes.entities.enemies.Squirrel;
import com.deco2800.potatoes.entities.projectiles.BallisticProjectile;
import com.deco2800.potatoes.entities.projectiles.Projectile.ProjectileTexture;
import com.deco2800.potatoes.entities.trees.*;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.worlds.World;

import org.junit.After;
import org.junit.Test;

public class TreeProjectileShootEventTest {
    TreeProjectileShootEvent testEvent = new TreeProjectileShootEvent(10,  BallisticProjectile.class,ProjectileTexture.ROCKET);
    private static final int RELOAD = 100;
    private static final float HEALTH = 10f;
    private static final float RANGE = 8f;
    ProjectileTree testTree = new ProjectileTree(10, 10, RELOAD, RANGE, HEALTH);

    @After
    public void cleanUp() {
    	GameManager.get().clearManagers();
    }
    
    @Test
    public void emptyTest() {
        TreeProjectileShootEvent nullEvent = new TreeProjectileShootEvent();
    }

    @Test
    public void copyTest() {
        testEvent.copy();
    }

    @Test
    public void actionTest() {
        GameManager.get().setWorld(new TestWorld());
        GameManager.get().getWorld().addEntity(testTree);
        testEvent.action(testTree);
        GameManager.get().getWorld().addEntity(new Squirrel(9, 9));
        testEvent.action(testTree);
    }
    private class TestWorld extends World {

    }

}
