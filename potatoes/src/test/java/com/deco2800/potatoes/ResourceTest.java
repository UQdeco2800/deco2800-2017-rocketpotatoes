package com.deco2800.potatoes;

import org.junit.Before;
import org.junit.Test;

import com.deco2800.potatoes.entities.FoodResource;
import com.deco2800.potatoes.entities.Resource;
import com.deco2800.potatoes.entities.ResourceEntity;
import com.deco2800.potatoes.entities.SeedResource;

public class ResourceTest {

	Resource resource;
	Resource food;
	Resource seed;
	
	@Before
	public void setup() {
		resource = new Resource();
		food = new FoodResource();
		seed = new SeedResource();
	}
	
	@Test
	public void testResourceEntityCreation() {
		// Position at (0,0,0) with empty constructer
		ResourceEntity basicEntity = new ResourceEntity();
		// Make sure type name and resourceType is consistent
		assert(basicEntity.getType().equals(new Resource()));
		assert(basicEntity.getType().getTypeName().equals("default"));
		
		// Default position and type seed
		ResourceEntity seedEntity = new ResourceEntity();
		seedEntity.setResourceType(seed);
		assert(seedEntity.getType().equals(new SeedResource()));
		assert(seedEntity.getType().getTypeName().equals("seed"));
		
	}
	
	@Test
	public void instantiationPositionTest() {
		// Set position and type food
		ResourceEntity foodEntity = new ResourceEntity(1,2,3,new FoodResource());
		assert(foodEntity.getType().equals(new FoodResource()));
		assert(foodEntity.getType().getTypeName().equals("food"));
		assert(foodEntity.getPosX() == 1);
		assert(foodEntity.getPosY() == 2);
		assert(foodEntity.getPosZ() == 3);
	}
	
	@Test
	public void resourceEntityQuantityTest() {
		ResourceEntity foodEntity = new ResourceEntity(1,2,3,new FoodResource());
		assert(foodEntity.getQuantity() == 1);
		foodEntity.setQuantity(3);
		assert(foodEntity.getQuantity() == 3);
	}
	
	@Test
	public void toStringTest() {
		ResourceEntity foodEntity = new ResourceEntity(1,2,3,new FoodResource());
		// ToString
		foodEntity.setQuantity(3);
		assert(foodEntity.toString().equals("3 " + new FoodResource().toString()));
	}
	
	@Test
	public void testNullResourceSetFunction() {
		ResourceEntity entity = new ResourceEntity();
		entity.setResourceType(null);
		assert(entity.getType().equals(new Resource()));
	}
	
	@Test
	public void textureTest() {
		ResourceEntity entity = new ResourceEntity();
		ResourceEntity food = new ResourceEntity(0,0,0, new FoodResource());
		ResourceEntity seed = new ResourceEntity(0,0,0, new SeedResource());
		assert(entity.getType().getTexture().equals("default"));
		assert(seed.getType().getTexture().equals("seed"));
		assert(food.getType().getTexture().equals("food"));
	}
	
	@Test
	public void testToString() {
		ResourceEntity entity = new ResourceEntity();
		assert(entity.getType().toString().equals("default"));
		entity.setResourceType(new SeedResource());
		assert(entity.getType().toString().equals("seed"));
	}
}
