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
	
	int testAmount = ResourceTree.DEFAULT_GATHER_CAPACITY/2; // Use a value less than the default
	
	@Before
	public void setup() {
		defaultResourceTree = new ResourceTree(0, 0, 0);
		customResourceTree = new ResourceTree(1, 0, 0, new FoodResource(), 100);
		nullTypeResourceTree = new ResourceTree(2, 0, 0, null, -10);
		
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
	
	/* Test initialising the resource tree */
	@Test
	public void initTest() {
		// Resource type tests
		assertTrue(defaultResourceTree.getResourceType() instanceof SeedResource); // Defaults to SeedResource
		assertTrue(customResourceTree.getResourceType() instanceof FoodResource); // Set to FoodResource
		assertTrue(nullTypeResourceTree.getResourceType() instanceof SeedResource); // Defaults to SeedResource
		
		// Gather capcity tests
		assertTrue(defaultResourceTree.getGatherCapacity() == ResourceTree.DEFAULT_GATHER_CAPACITY); // Defaults
		assertTrue(customResourceTree.getGatherCapacity() == 100); // Set to 100
		assertTrue(nullTypeResourceTree.getGatherCapacity() == ResourceTree.DEFAULT_GATHER_CAPACITY); // Defaults
	}
	
	/* Test setting the gather capacity of the resource tree */
	@Test
	public void capacityTest() {
		// Setting to a general number
		defaultResourceTree.setGatherCapacity(50);
		assertTrue(defaultResourceTree.getGatherCapacity() == 50);
		
		// Setting to 0
		defaultResourceTree.setGatherCapacity(0);
		assertTrue(defaultResourceTree.getGatherCapacity() == ResourceTree.DEFAULT_GATHER_CAPACITY);
		
		// Setting to negative number
		defaultResourceTree.setGatherCapacity(-50);
		assertTrue(defaultResourceTree.getGatherCapacity() == ResourceTree.DEFAULT_GATHER_CAPACITY);
	}
	
	/* Test adding resources to the Resource Tree */
	@Test
	public void addTest() {
		// Count should be zero by default
		assertTrue(defaultResourceTree.getResourceCount() == 0);
		
		// Test positive amount
		defaultResourceTree.addResources(testAmount);
		assertTrue(defaultResourceTree.getResourceCount() == testAmount);
		defaultResourceTree.addResources(testAmount);
		assertTrue(defaultResourceTree.getResourceCount() == 2*testAmount);
		
		// Test negative amount
		defaultResourceTree.addResources(-testAmount);
		assertTrue(defaultResourceTree.getResourceCount() == testAmount);
		
		// Test adding above upper limit
		defaultResourceTree.addResources(defaultResourceTree.getGatherCapacity()*2);
		assertTrue(defaultResourceTree.getResourceCount() == defaultResourceTree.getGatherCapacity());
		
		// Test adding below lower limit
		defaultResourceTree.addResources(-defaultResourceTree.getGatherCapacity()*2);
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
	@Test
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
