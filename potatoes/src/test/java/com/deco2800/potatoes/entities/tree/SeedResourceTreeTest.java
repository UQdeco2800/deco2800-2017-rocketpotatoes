package com.deco2800.potatoes.entities.tree;

import java.util.HashSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.deco2800.potatoes.entities.resources.FoodResource;
import com.deco2800.potatoes.entities.resources.Resource;
import com.deco2800.potatoes.entities.resources.SeedResource;
import com.deco2800.potatoes.entities.trees.SeedTree;
import com.deco2800.potatoes.managers.Inventory;

public class SeedResourceTreeTest {
	SeedTree seedTree;
	
	Resource seedResource;
	Resource foodResource;
	HashSet<Resource> resources;
	Inventory emptyInventory;	
	Inventory usedInventory;
	
	int testAmount = 16; // Use a value less than the default
	
	@Before
	public void setup() {
		seedTree = new SeedTree(0, 0);
		emptyInventory = new Inventory();
		resources = new HashSet<>();
		seedResource = new SeedResource();
		foodResource = new FoodResource();
		resources.add(seedResource);
		resources.add(foodResource);
		usedInventory = new Inventory(resources);
		usedInventory.updateQuantity(seedResource, 5);
		usedInventory.updateQuantity(foodResource, 2);
	}

	@After
	public void tearDown() {
		seedTree = null;
		emptyInventory = null;
		resources = null;
		seedResource = null;
		foodResource = null;
		usedInventory = null;
	}
	
	/**
	 * Test getting the stats of the tree
	 */
	@Test
	public void statsTest() {
		SeedTree.getSeedTreeStats();
	}
	
	/**
	 * Test updating animations
	 */
	@Test
	public void updateAnimation() {
		seedTree.updateAnimations();
	}
	
	/**
	 * Test creating a copy
	 */
	@Test
	public void copyTest() {
		seedTree.createCopy();
	}
	
	/**
	 * Test getting the name
	 */
	@Test
	public void stringTest() {
		seedTree.getName();
	}
}