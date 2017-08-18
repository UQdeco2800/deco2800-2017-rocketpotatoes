package com.deco2800.potatoes;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.*;

import com.deco2800.potatoes.entities.FoodResource;
import com.deco2800.potatoes.entities.Resource;
import com.deco2800.potatoes.entities.SeedResource;
import com.deco2800.potatoes.managers.Inventory;

public class InventoryTest {
	
	Resource seed1 = new SeedResource();
	Resource food1 = new FoodResource();
	Resource nullResource = null;
	Object nonResource = new Object();
	
	HashSet<Resource> validResources = new HashSet<Resource>();
	Inventory validInventory;
	
	@Before
	public void setUp() throws Exception {
		validResources.add(seed1);
		validResources.add(food1);
		validInventory = new Inventory(validResources);
	}

	@Test(expected = Exception.class)
	public void testNullResourceInstantiation() throws Exception {
		Inventory items = new Inventory(null);
	}
	
	@Test(expected = Exception.class)
	public void testInvalidResource() throws Exception {
		// Non resource items
		HashSet<Resource> invalidResources1 = new HashSet<Resource>();
		
		invalidResources1.add(seed1);
		invalidResources1.add(nullResource);
		
		Inventory items = new Inventory(invalidResources1);
	}
	
	@Test
	public void initInventoryTest() throws Exception {
		
		// Empty resource
		try {
			HashSet<Resource> resources1 = new HashSet<Resource>();
			Inventory items1 = new Inventory(resources1);
		} catch (Exception e) {
			fail("No resources is a valid option");
		}
		
		// Valid resources
		HashSet<Resource> resources2 = new HashSet<Resource>();
		Resource seed = new SeedResource();
		Resource food = new FoodResource();
		resources2.add(seed);
		resources2.add(food);
		Inventory items2 = new Inventory(resources2);	
		
		// Check all quantities set to 0
		assert(items2.getQuantity(seed)==0);
		assert(items2.getQuantity(food)==0);
		
	}
	
	@Test
	public void getInventoryTest() throws Exception {
		// Test no resources
		HashSet<Resource> resources = new HashSet<Resource>();
		Inventory items = new Inventory(resources);
		assert(items.getInventoryResources().equals(resources));
		// Make sure resources are deep cloned
		resources.add(new SeedResource());
		assert(!items.getInventoryResources().equals(resources));
		
		// Test with resources
		Inventory items2 = new Inventory(resources);
		assert(items2.getInventoryResources().equals(resources));
		
	}
	
	@Test(expected = Exception.class)
	public void testAddingNullResource() throws Exception {
		// Test null
		HashSet<Resource> emptyResources = new HashSet<Resource>();
		Inventory inventory = new Inventory(emptyResources);
		inventory.addInventoryResource(null);
	}
	
	@Test
	public void addResourceTest() throws Exception {
		// Without existing resource
		Inventory inventory = new Inventory(new HashSet<Resource>());
		inventory.addInventoryResource(new SeedResource());
		HashSet<Resource> resources = new HashSet<Resource>();
		resources.add(new SeedResource());
		assert(inventory.getInventoryResources().equals(resources));
		
		// With existing resource
		resources.add(new FoodResource());
		inventory.addInventoryResource(new FoodResource());
		assert(inventory.getInventoryResources().equals(resources));
		
	}
	
	@Test(expected = Exception.class)
	public void testRemoveNullResource() throws Exception {
		validInventory.removeInventoryResource(null);
	}
	
	@Test
	public void removeResourceTest() throws Exception {
		// Test remove existing
		validInventory.removeInventoryResource(food1);
		
		// Test remove non exsisting
		validInventory.removeInventoryResource(food1);
	}
	
	@Test(expected = Exception.class)
	public void testRemoveNonExistingResource() throws Exception {
		Inventory inventory = new Inventory(new HashSet<Resource>());
		validInventory.removeInventoryResource(food1);
	}
	
	@Test(expected = Exception.class)
	public void getNullResourceQuantityTest() throws Exception {
		validInventory.getQuantity(null);
	}
	
	@Test
	public void getQuantityTest() throws Exception {
		// Test existing resource
		Inventory inventory = new Inventory(validResources);
		assert(inventory.getQuantity(seed1)==0);
	}
	
	@Test(expected = Exception.class)
	public void getInvalidResourceQuantityTest() throws Exception {
		Inventory inventory = new Inventory(new HashSet<Resource>());
		inventory.getQuantity(seed1);
	}
	
	@Test(expected = Exception.class)
	public void testNullResourceUpdate() throws Exception {
		validInventory.updateQuantity(null, 10);
	}
	
	@Test(expected = Exception.class)
	public void testNonExistingResourceUpdate() throws Exception {
		Inventory inventory = new Inventory(new HashSet<Resource>());
		inventory.updateQuantity(seed1, 10);
	}
	
	@Test
	public void updateQuantityTest() throws Exception {
		// Existing resource, valid quantity
		validInventory.updateQuantity(seed1, 10);
		assert(validInventory.getQuantity(seed1) == 10);
		validInventory.updateQuantity(seed1, -10);
		assert(validInventory.getQuantity(seed1) == 0);	
		
	}
	
	@Test(expected = Exception.class)
	public void testInvalidQuantityUpdate() throws Exception {
		validInventory.updateQuantity(seed1, -2);
	}
	
	@Test
	public void updateInventoryTest() throws Exception {
		// Valid entry
		Inventory inventory = new Inventory(new HashSet<Resource>());
		inventory.updateInventory(validInventory);
	}
	
	@Test(expected = Exception.class)
	public void testNullInventoryUpdate() throws Exception {
		validInventory.updateInventory(null);
	}
	

}
