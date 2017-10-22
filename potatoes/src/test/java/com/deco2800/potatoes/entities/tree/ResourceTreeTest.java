package com.deco2800.potatoes.entities.tree;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.HashSet;

import com.deco2800.potatoes.entities.resources.FoodResource;
import com.deco2800.potatoes.entities.resources.Resource;
import com.deco2800.potatoes.entities.resources.SeedResource;
import com.deco2800.potatoes.entities.trees.ResourceTree;
import com.deco2800.potatoes.managers.Inventory;

public class ResourceTreeTest {
	ResourceTree defaultResourceTree;
	ResourceTree customResourceTree;
	ResourceTree nullTypeResourceTree;
	
	Resource seedResource;
	Resource foodResource;
	HashSet<Resource> resources;
	Inventory emptyInventory;	
	Inventory usedInventory;
	
	int testAmount = 16; // Use a value less than the default
	
	@Before
	public void setup() {
		defaultResourceTree = new ResourceTree(0, 0);
		customResourceTree = new ResourceTree(1, 0, new FoodResource(), 100);
		nullTypeResourceTree = new ResourceTree(2, 0, null, -10);
		
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

		defaultResourceTree = null;
		customResourceTree = null;
		nullTypeResourceTree = null;
		emptyInventory = null;
		resources = null;
		seedResource = null;
		foodResource = null;
		usedInventory = null;

	}

	/**  
	 * Test initialising the resource tree
	 */
	@Test
	public void initTest() {
		// Resource type tests
		assertTrue(defaultResourceTree.getGatherType() instanceof SeedResource); // Defaults to SeedResource
		assertTrue(customResourceTree.getGatherType() instanceof FoodResource); // Set to FoodResource
		assertTrue(nullTypeResourceTree.getGatherType() instanceof SeedResource); // Defaults to SeedResource
		
		// Gather capcity tests
		assertTrue(defaultResourceTree.getGatherCapacity() == 32); // Defaults
		assertTrue(customResourceTree.getGatherCapacity() == 100); // Set to 100
		assertTrue(nullTypeResourceTree.getGatherCapacity() == 32); // Defaults
	}
	
	/**
	 *  Test setting the gather capacity of the resource tree 
	 */
	@Test
	public void capacityTest() {
		// Setting to a general number
		defaultResourceTree.setGatherCapacity(50);
		assertTrue(defaultResourceTree.getGatherCapacity() == 50);
		
		// Setting to 0
		defaultResourceTree.setGatherCapacity(0);
		assertTrue(defaultResourceTree.getGatherCapacity() == 32);
		
		// Setting to negative number
		defaultResourceTree.setGatherCapacity(-50);
		assertTrue(defaultResourceTree.getGatherCapacity() == 32);
	}
	
	/** 
	 * Test adding resources to the Resource Tree 
	 */
	@Test
	public void addTest() {
		// Count should be zero by default
		assertTrue(defaultResourceTree.getGatherCount() == 0);
		
		// Test positive amount
		defaultResourceTree.gather(testAmount);
		assertTrue(defaultResourceTree.getGatherCount() == testAmount);
		defaultResourceTree.gather(testAmount);
		assertTrue(defaultResourceTree.getGatherCount() == 2*testAmount);
		
		// Test negative amount
		defaultResourceTree.gather(-testAmount);
		assertTrue(defaultResourceTree.getGatherCount() == testAmount);
		
		// Test adding above upper limit
		defaultResourceTree.gather(defaultResourceTree.getGatherCapacity()*2);
		assertTrue(defaultResourceTree.getGatherCount() == defaultResourceTree.getGatherCapacity());
		
		// Test adding below lower limit
		defaultResourceTree.gather(-defaultResourceTree.getGatherCapacity()*2);
		assertTrue(defaultResourceTree.getGatherCount() == 0);
		
	}
	
	/**
	 *  Test toggling gather status 
	 */
	@Test
	public void gatherStatusTest() {
		assertTrue(defaultResourceTree.isGatherEnabled());
		defaultResourceTree.toggleGatherEnabled();
		assertFalse(defaultResourceTree.isGatherEnabled());
		defaultResourceTree.toggleGatherEnabled();
		assertTrue(defaultResourceTree.isGatherEnabled());
	}
	
	/** 
	 * Test transferring resources to an inventory 
	 */
	@Test
	public void inventoryTransferTest() {
		/* Test for default case */
		assertTrue(defaultResourceTree.getGatherCount() == 0); // 0 by default
		defaultResourceTree.gather(testAmount);
		assertTrue(defaultResourceTree.getGatherCount() == testAmount); // Check that resources were added
		
		defaultResourceTree.transferResources(emptyInventory);
		assertTrue(defaultResourceTree.getGatherCount() == 0); // All resources should be removed from tree
		assertTrue(emptyInventory.getQuantity(seedResource) == testAmount); // All resources should be added to inventory
		
		/* Test for custom case */
		assertTrue(customResourceTree.getGatherCount() == 0); // 0 by default
		customResourceTree.gather(testAmount);
		assertTrue(customResourceTree.getGatherCount() == testAmount); // Check that resources were added
		
		int initalAmount = usedInventory.getQuantity(foodResource);
		customResourceTree.transferResources(usedInventory);
		assertTrue(customResourceTree.getGatherCount() == 0); // All resources should be removed from tree
		assertTrue(usedInventory.getQuantity(foodResource) == testAmount + initalAmount); // All resources should be added to inventory
	}
	
	/**
	 * Test getting the name
	 */
	@Test
	public void stringTest() {
		defaultResourceTree.getName();
	}
	
	/**
	 * Test making an array of frames for resource tree animations
	 */
	@Test
	public void makeFramesTest() {
		String[] testFrames = ResourceTree.makeFrames("type", "state", 10, 1);
		assertTrue(testFrames[0].equals("type_state_1"));
		assertTrue(testFrames[5].equals("type_state_6"));
		assertTrue(testFrames[9].equals("type_state_10"));
	}
	
	/**
	 * Test updating animations
	 */
	@Test
	public void updateAnimation() {
		defaultResourceTree.updateAnimations();
	}
	
}
