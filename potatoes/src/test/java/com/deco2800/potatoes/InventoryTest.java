package com.deco2800.potatoes;

import static org.junit.Assert.*;

import java.util.HashSet;
import org.junit.*;

import com.deco2800.potatoes.entities.resources.FoodResource;
import com.deco2800.potatoes.entities.resources.Resource;
import com.deco2800.potatoes.entities.resources.SeedResource;
import com.deco2800.potatoes.managers.Inventory;
import com.deco2800.potatoes.exceptions.InvalidResourceException;

/**
 * Tests for the inventory class
 * 
 * @author Dion Lao
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

	@After
	public void tearDown() {

		validResources.clear();
		validResources = null;
		seed1 = null;
		food1 = null;
		nonResource = null;
		validInventory = null;

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
	
	@Test
	public void testAddingNullResource() {
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
	
	@Test
	public void testRemoveNonExistingResource() {
		Inventory inventory = new Inventory(new HashSet<Resource>());
		inventory.removeInventoryResource(food1);
		assert(inventory.getInventoryResources().equals(new HashSet<Resource>()));
	}
	
	@Test
	public void getNullResourceQuantityTest() {
		assert(validInventory.getQuantity(null) == 0);
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

	@Test
	public void getInvalidResourceQuantityTest() {
		Inventory inventory = new Inventory(new HashSet<Resource>());
		assert(inventory.getQuantity(seed1) == 0);
	}
	
	@Test
	public void testNullResourceUpdate() {
		int result = validInventory.updateQuantity(null, 10);
		assert(result == 0);
	}
	
	@Test
	public void testNonExistingResourceUpdate() {
		Inventory inventory = new Inventory(new HashSet<Resource>());
		int result = inventory.updateQuantity(seed1, 10);
		assert(result == 0);
	}
	
	@Test
	public void updateQuantityTest() throws Exception {
		// Existing resource, valid quantity
		validInventory.updateQuantity(seed1, 10);
		assert(validInventory.getQuantity(seed1) == 10);
		validInventory.updateQuantity(seed1, -10);
		assert(validInventory.getQuantity(seed1) == 0);	
		
	}
	
	@Test
	public void testInvalidQuantityUpdate() {
		int result = validInventory.updateQuantity(seed1, -2);
		assert(result == 0);
	}
	
	@Test
	public void updateInventoryTest() {
		// Valid entry
		HashSet<Resource> resources = new HashSet<Resource>();
		Inventory inventory = new Inventory(resources);
		inventory.updateInventory(validInventory);
		assert(validInventory.getMap().equals(inventory.getMap()));
		// Update with existing resources
		resources.add(new FoodResource());
		inventory = new Inventory(resources);
		inventory.updateQuantity(new FoodResource(), 2);
		resources.add(new SeedResource());
		Inventory inventory2 = new Inventory(resources);
		inventory2.updateQuantity(new SeedResource(), 1);
		inventory2.updateQuantity(new FoodResource(), 1);
		inventory.updateInventory(inventory2);
		assert(inventory.getQuantity(new SeedResource())==1);
		assert(inventory.getQuantity(new FoodResource())==3);
		
	}
	
	@Test
	public void testNullInventoryUpdate() {
		// Test null inventory
		Inventory inventory = new Inventory(new HashSet<Resource>());
		inventory.updateInventory(null);
		assert(inventory.getInventoryResources().equals(new HashSet<Resource>()));
	}
	
	@Test
	public void testToString() {
		Inventory inventory = new Inventory(new HashSet<Resource>());
		assert(inventory.toString().equals(""));
		inventory.addInventoryResource(new SeedResource());
		assert(inventory.toString().equals("seed count = 0"+System.getProperty("line.separator")));
		inventory.updateQuantity(new SeedResource(), 2);
		assert(inventory.toString().equals("seed count = 2"+System.getProperty("line.separator")));
		inventory.addInventoryResource(new FoodResource());
		inventory.updateQuantity(new FoodResource(), 5);
		assert(inventory.toString().equals("food count = 5"+System.getProperty("line.separator")+"seed count = 2"+System.getProperty("line.separator")));
		inventory.hashCode();
		
	}
	

}
