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
		System.out.println(basicEntity.getType());
		assert(basicEntity.getType().equals(new Resource()));
		assert(basicEntity.getType().getTypeName().equals("ordinary"));
		
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
	public void toStringTest() {
		ResourceEntity foodEntity = new ResourceEntity(1,2,3,new FoodResource());
		// ToString
		assert(foodEntity.toString().equals(new FoodResource().toString()));
	}
	
	
}
