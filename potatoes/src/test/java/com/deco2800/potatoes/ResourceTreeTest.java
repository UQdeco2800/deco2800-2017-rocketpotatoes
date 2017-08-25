package com.deco2800.potatoes;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.HashSet;

import com.deco2800.potatoes.entities.FoodResource;
import com.deco2800.potatoes.entities.Resource;
import com.deco2800.potatoes.entities.SeedResource;
import com.deco2800.potatoes.entities.trees.ResourceTree;
import com.deco2800.potatoes.managers.Inventory;

public class ResourceTreeTest {
	ResourceTree defaultResourceTree;
	ResourceTree customResourceTree;
	ResourceTree nullTypeResourceTree;
	
	Resource seedResource;
	Resource foodResource;
	
	Inventory emptyInventory;	
	Inventory usedInventory;
	
	int testAmount = 10;
	
	@Before
	public void setup() {
		defaultResourceTree = new ResourceTree(0, 0, 0);
		customResourceTree = new ResourceTree(1, 0, 0, new FoodResource());
		nullTypeResourceTree = new ResourceTree(2, 0, 0, null);
		
		emptyInventory = new Inventory();
		
		HashSet<Resource> resources = new HashSet<Resource>();
		seedResource = new SeedResource();
		foodResource = new FoodResource();
		resources.add(seedResource);
		resources.add(foodResource);
		usedInventory = new Inventory(resources);
		usedInventory.updateQuantity(seedResource, 5);
		usedInventory.updateQuantity(foodResource, 2);
	}
	
	/* Test Type of Initialised Resource Tree */
	@Test
	public void initTypeTest() {
		assertTrue(defaultResourceTree.getResourceType() instanceof SeedResource); // Should default to SeedResource
		assertTrue(customResourceTree.getResourceType() instanceof FoodResource); // Should be set to FoodResource
		assertTrue(nullTypeResourceTree.getResourceType() instanceof SeedResource); // Should default to SeedResource
	}
	
	@Test
	/* Ensure that the max resource count is always non-negative */
	public void maxResourceCountTest() {
		assertTrue(ResourceTree.MAX_RESOURCE_COUNT >= 0);
	}
	
	/* Test adding resources to the Resource Tree */
	//@Test
	public void addTest() {
		// Count should be zero by default
		assertTrue(defaultResourceTree.getResourceCount() == 0);
		
		// Test adding amount
		defaultResourceTree.addResources(testAmount);
		assertTrue(defaultResourceTree.getResourceCount() == testAmount);
		defaultResourceTree.addResources(testAmount);
		assertTrue(defaultResourceTree.getResourceCount() == 2*testAmount);
		
		// Test removing amount
		defaultResourceTree.addResources(-testAmount);
		assertTrue(defaultResourceTree.getResourceCount() == testAmount);
		
		// Test adding above limit
		defaultResourceTree.addResources(ResourceTree.MAX_RESOURCE_COUNT*2);
		assertTrue(defaultResourceTree.getResourceCount() == ResourceTree.MAX_RESOURCE_COUNT);
		
		// Test removing below zero
		defaultResourceTree.addResources(-ResourceTree.MAX_RESOURCE_COUNT*2);
		assertTrue(defaultResourceTree.getResourceCount() == 0);
		
	}
	
	/* Test toggling gather status */
	@Test
	public void gatherStatusTest() {
		assertTrue(defaultResourceTree.gatherEnabled);
		defaultResourceTree.toggleGatherEnabled();
		assertFalse(defaultResourceTree.gatherEnabled);
		defaultResourceTree.toggleGatherEnabled();
		assertTrue(defaultResourceTree.gatherEnabled);
	}
	
	/* Test transferring resources to an inventory */
	//@Test
	public void inventoryTransferTest() {
		/* Test for default case */
		assertTrue(defaultResourceTree.getResourceCount() == 0); // 0 by default
		defaultResourceTree.addResources(testAmount);
		assertTrue(defaultResourceTree.getResourceCount() == testAmount); // Check that resources were added
		
		defaultResourceTree.transferResources(emptyInventory);
		assertTrue(defaultResourceTree.getResourceCount() == 0); // All resources should be removed from tree
		assertTrue(emptyInventory.getQuantity(seedResource) == testAmount); // All resources should be added to inventory
		
		/* Test for custom case */
		assertTrue(customResourceTree.getResourceCount() == 0); // 0 by default
		customResourceTree.addResources(testAmount);
		assertTrue(customResourceTree.getResourceCount() == testAmount); // Check that resources were added
		
		int initalAmount = usedInventory.getQuantity(foodResource);
		customResourceTree.transferResources(usedInventory);
		assertTrue(customResourceTree.getResourceCount() == 0); // All resources should be removed from tree
		assertTrue(usedInventory.getQuantity(foodResource) == testAmount + initalAmount); // All resources should be added to inventory
	}
	
}
