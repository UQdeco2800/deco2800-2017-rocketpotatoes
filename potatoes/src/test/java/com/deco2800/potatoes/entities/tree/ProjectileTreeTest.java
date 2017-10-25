package com.deco2800.potatoes.entities.tree;

import com.deco2800.potatoes.entities.trees.*;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.worlds.World;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.After;

public class ProjectileTreeTest {

	private static final int RELOAD = 100;
	private static final float HEALTH = 10f;
	private static final float RANGE = 8f;
	ProjectileTree testTree;

	@Before
	public void setup() {
		GameManager.get().setWorld(new TestWorld());
		testTree = new ProjectileTree(10, 10, RELOAD, RANGE, HEALTH);
		GameManager.get().getWorld().addEntity(testTree);
		testTree.createCopy();
	}
	
	@After
    public void cleanUp() {
    	GameManager.get().clearManagers();
    	testTree = null;
    }

	@Test
	public void emptyTest() {
		ProjectileTree nullTree = new ProjectileTree();
	}

	@Test
	public void constructTest() {
		assertEquals("getConstructionLeft() incorect construction time", 100, testTree.getConstructionLeft());
		testTree.decrementConstructionLeft();
		assertEquals("decrementConstructionLeft() incorect construction time", 99, testTree.getConstructionLeft());
	}

	@Test
	public void constructSetTest() {
		testTree.onTick(1);
		testTree.setConstructionLeft(10);
		testTree.setConstructionLeft(0);
	}

	@Test
	public void progressTest() {
		testTree.getProgress();
		testTree.showProgress();
		testTree.setProgress(10);
		testTree.showProgress();
		testTree.setProgress(-1);
		testTree.showProgress();
		testTree.getProgressRatio();
	}

	@Test
	public void getUpgradeTest() {
		assertNotNull("getUpgradeStats() returns null", testTree.getUpgradeStats());
		assertNotNull("getAllUpgradeStats() returns null", testTree.getAllUpgradeStats());
	}

	@Test
	public void healthChangeTest() {
		testTree.onTick(1);
		assertTrue(HEALTH == testTree.getHealth());
		testTree.damage(2);
		assertTrue(HEALTH - 2 == testTree.getHealth());
		testTree.resetStats();
		assertTrue(HEALTH == testTree.getHealth());
		testTree.onTick(1);
	}

	@Test
	public void upgradeHealthTest() {
		testTree.upgrade();
		assertTrue(20f == testTree.getHealth());
		assertNotNull("getUpgradeStats() returns null", testTree.getUpgradeStats());
		testTree.onTick(1);
		testTree.upgrade();
		assertTrue(30f == testTree.getHealth());
		assertNotNull("getUpgradeStats() returns null", testTree.getUpgradeStats());
		testTree.upgrade();
		assertTrue(30f == testTree.getHealth());
		assertNotNull("getUpgradeStats() returns null", testTree.getUpgradeStats());
	}

	@Test
	public void progressTest2() {
		testTree.getProgressRatio();
		testTree.getMaxProgress();
		testTree.getProgressBar();
		testTree.setProgress(3);
		testTree.getProgressBar();
		testTree.getTexture();
		testTree.getName();
		testTree.dyingHandler();
		
	}

	private class TestWorld extends World {

	}
}
