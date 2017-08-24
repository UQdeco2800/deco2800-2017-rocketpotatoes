package com.deco2800.potatoes;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.*;

import com.deco2800.potatoes.entities.FoodResource;
import com.deco2800.potatoes.entities.Resource;
import com.deco2800.potatoes.entities.SeedResource;
import com.deco2800.potatoes.managers.Inventory;
import com.deco2800.potatoes.exceptions.InvalidResourceException;
import com.deco2800.potatoes.exceptions.InvalidInventoryException;

/**
 * Tests for the inventory class
 * 
 * @author Dion
 *
 */
public class InventoryTest {
	
	Resource seed1;
	Resource food1;
	Resource nullResource = null;
	Object nonResource;
	
	HashSet<Resource> validResources;
	Inventory validInventory;
	
	@Before
	public void setUp() throws InvalidResourceException {
		nonResource = new Object();
		seed1 = new SeedResource();
		food1 = new FoodResource();
		validResources = new HashSet<Resource>();
		validResources.add(seed1);
		validResources.add(food1);
		validInventory = new Inventory(validResources);
	}

	@Test(expected = InvalidResourceException.class)
	public void testNullResourceInstantiation() throws InvalidResourceException {
		Inventory items = new Inventory(null);
	}
	
	@Test(expected = InvalidResourceException.class)
	public void testInvalidResource() throws InvalidResourceException {
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
	
	@Test(expected = NullPointerException.class)
	public void testAddingNullResource() throws Exception {
		// Test null
		HashSet<Resource> emptyResources = new HashSet<Resource>();
		Inventory inventory = new Inventory(emptyResources);
		inventory.addInventoryResource(null);
		assert(inventory.getQuantity(null) == 0);
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
	
	@Test(expected = NullPointerException.class)
	public void testRemoveNullResource() throws Exception {
		validInventory.removeInventoryResource(null);
	}
	
	@Test
	public void removeResourceTest() throws Exception {
		// Test remove existing
		HashSet<Resource> resources = new HashSet<Resource>();
		Resource resource = new Resource();
		resources.add(resource);
		Inventory inventory = new Inventory(resources);
		inventory.removeInventoryResource(resource);
		assert(inventory.getInventoryResources().equals(new HashSet<Resource>()));
		
	}
	
	@Test(expected = InvalidResourceException.class)
	public void testRemoveNonExistingResource() throws InvalidResourceException {
		Inventory inventory = new Inventory(new HashSet<Resource>());
		inventory.removeInventoryResource(food1);
	}
	
	@Test(expected = NullPointerException.class)
	public void getNullResourceQuantityTest() throws Exception {
		validInventory.getQuantity(null);
	}
	
	@Test
	public void getQuantityTest() throws Exception {
		// Test existing resource
		Resource seed = new SeedResource();
		HashSet<Resource> resources = new HashSet<Resource>();
		resources.add(seed);
		Inventory inventory = new Inventory(resources);
		assert(inventory.getQuantity(seed)==0);
	}
	
	@Test(expected = NullPointerException.class)
	public void getAbsoluteQuantityTest() throws Exception {
		// Test existing resource
		Resource seed = new SeedResource();
		HashSet<Resource> resources = new HashSet<Resource>();
		resources.add(seed);
		Inventory inventory = new Inventory(resources);
		assert(inventory.getAbsoluteQuantity(seed)==0);
		assert(inventory.getAbsoluteQuantity(new FoodResource())==0);
	}
	
	@Test(expected = Exception.class)
	public void getInvalidResourceQuantityTest() throws Exception {
		Inventory inventory = new Inventory(new HashSet<Resource>());
		inventory.getQuantity(seed1);
	}
	
	@Test(expected = NullPointerException.class)
	public void testNullResourceUpdate() throws Exception {
		validInventory.updateQuantity(null, 10);
	}
	
	@Test(expected = NullPointerException.class)
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
	
	@Test(expected = NullPointerException.class)
	public void testInvalidQuantityUpdate() throws Exception {
		validInventory.updateQuantity(seed1, -2);
	}
	
	@Test
	public void updateInventoryTest() throws Exception {
		// Valid entry
		Inventory inventory = new Inventory(new HashSet<Resource>());
		inventory.updateInventory(validInventory);
	}
	
	@Test(expected = NullPointerException.class)
	public void testNullInventoryUpdate() throws Exception {
		validInventory.updateInventory(null);
	}
	

}
