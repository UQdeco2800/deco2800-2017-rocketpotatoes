package com.deco2800.potatoes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.entities.resources.*;

public class ResourceTest {

	Resource resource;
	Resource food;
	Resource seed;
	Resource bones;
	Resource cactusThorn;
	Resource coal;
	Resource fishMeat;
	Resource iceCrystal;
	Resource obsidian;
	Resource pearl;
	Resource pinecone;
	Resource pricklyPear;
	Resource sealSkin;
	Resource snowBall;
	Resource treasure;
	Resource tumbleweed;
	Resource wood;
	
	@Before
	public void setup() {
		resource = new Resource();
		food = new FoodResource();
		seed = new SeedResource();
		bones = new BonesResource();
		cactusThorn = new CactusThornResource();
		coal = new CoalResource();
		fishMeat = new FishMeatResource();
		iceCrystal = new IceCrystalResource();
		obsidian = new ObsidianResource();
		pearl = new PearlResource();
		pinecone = new PineconeResource();
		pricklyPear = new PricklyPearResource();
		sealSkin = new SealSkinResource();
		snowBall = new SnowBallResource();
		treasure = new TreasureResource();
		tumbleweed = new TumbleweedResource();
		wood = new WoodResource();
	}

	@After
	public void tearDown() {
		resource = null;
		food = null;
		seed = null;
		bones = null;
		cactusThorn = null;
		coal = null;
		fishMeat = null;
		iceCrystal = null;
		obsidian = null;
		pearl = null;
		pinecone = null;
		pricklyPear = null;
		sealSkin = null;
		snowBall = null;
		treasure = null;
		tumbleweed = null;
		wood = null;
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
		
		// Test Bones resource
		ResourceEntity bonesEntity = new ResourceEntity();
		bonesEntity.setResourceType(bones);
		assert(bonesEntity.getType().equals(new BonesResource()));
		assert(bonesEntity.getType().getTypeName().equals("bones"));
		
		// Test cactus thorn resource
		ResourceEntity cactusThornEntity = new ResourceEntity();
		cactusThornEntity.setResourceType(cactusThorn);
		assert(cactusThornEntity.getType().equals(new CactusThornResource()));
		assert(cactusThornEntity.getType().getTypeName().equals("cactusThorn"));
		
		// Test coal resource
		ResourceEntity coalEntity = new ResourceEntity();
		coalEntity.setResourceType(coal);
		assert(coalEntity.getType().equals(new CoalResource()));
		assert(coalEntity.getType().getTypeName().equals("coal"));
		
		// Test ice crystal resource
		ResourceEntity iceCrystalEntity = new ResourceEntity();
		iceCrystalEntity.setResourceType(iceCrystal);
		assert(iceCrystalEntity.getType().equals(new IceCrystalResource()));
		assert(iceCrystalEntity.getType().getTypeName().equals("iceCrystal"));
		
		// Test fish meat resource
		ResourceEntity fishMeatEntity = new ResourceEntity();
		fishMeatEntity.setResourceType(fishMeat);
		assert(fishMeatEntity.getType().equals(new FishMeatResource()));
		assert(fishMeatEntity.getType().getTypeName().equals("fishMeat"));
		
		// Test obsidian resource
		ResourceEntity obsidianEntity = new ResourceEntity();
		obsidianEntity.setResourceType(obsidian);
		assert(obsidianEntity.getType().equals(new ObsidianResource()));
		assert(obsidianEntity.getType().getTypeName().equals("obsidian"));
		
		// Test pearl resource
		ResourceEntity pearlEntity = new ResourceEntity();
		pearlEntity.setResourceType(pearl);
		assert(pearlEntity.getType().equals(new PearlResource()));
		assert(pearlEntity.getType().getTypeName().equals("pearl"));
		
		// Test pinecone resource
		ResourceEntity pineconeEntity = new ResourceEntity();
		pineconeEntity.setResourceType(pinecone);
		assert(pineconeEntity.getType().equals(new PineconeResource()));
		assert(pineconeEntity.getType().getTypeName().equals("pinecone"));
		
		// Test prickly pear resource
		ResourceEntity pricklyPearEntity = new ResourceEntity();
		pricklyPearEntity.setResourceType(pricklyPear);
		assert(pricklyPearEntity.getType().equals(new PricklyPearResource()));
		assert(pricklyPearEntity.getType().getTypeName().equals("pricklyPear"));
		
		// Test seal skin resource
		ResourceEntity sealSkinEntity = new ResourceEntity();
		sealSkinEntity.setResourceType(sealSkin);
		assert(sealSkinEntity.getType().equals(new SealSkinResource()));
		assert(sealSkinEntity.getType().getTypeName().equals("sealSkin"));
		
		// Test snow ball resource
		ResourceEntity snowBallEntity = new ResourceEntity();
		snowBallEntity.setResourceType(snowBall);
		assert(snowBallEntity.getType().equals(new SnowBallResource()));
		assert(snowBallEntity.getType().getTypeName().equals("snowBall"));
		
		// Test treasure resource
		ResourceEntity treasureEntity = new ResourceEntity();
		treasureEntity.setResourceType(treasure);
		assert(treasureEntity.getType().equals(new TreasureResource()));
		assert(treasureEntity.getType().getTypeName().equals("treasure"));
		
		// Test tumbleweed resource
		ResourceEntity tumbleweedEntity = new ResourceEntity();
		tumbleweedEntity.setResourceType(tumbleweed);
		assert(tumbleweedEntity.getType().equals(new TumbleweedResource()));
		assert(tumbleweedEntity.getType().getTypeName().equals("tumbleweed"));
		
		// Test wood resource
		ResourceEntity woodEntity = new ResourceEntity();
		woodEntity.setResourceType(wood);
		assert(woodEntity.getType().equals(new WoodResource()));
		assert(woodEntity.getType().getTypeName().equals("wood"));
		
	}
	
	@Test
	public void instantiationPositionTest() {
		// Set position and type food
		ResourceEntity foodEntity = new ResourceEntity(1,2,new FoodResource());
		assert(foodEntity.getType().equals(new FoodResource()));
		assert(foodEntity.getType().getTypeName().equals("food"));
		assert(foodEntity.getPosX() == 1);
		assert(foodEntity.getPosY() == 2);
	}
	
	@Test
	public void resourceEntityQuantityTest() {
		ResourceEntity foodEntity = new ResourceEntity(1,2,new FoodResource());
		assert(foodEntity.getQuantity() == 1);
		foodEntity.setQuantity(3);
		assert(foodEntity.getQuantity() == 3);
	}
	
	@Test
	public void toStringTest() {
		ResourceEntity foodEntity = new ResourceEntity(1,2,new FoodResource());
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
		ResourceEntity food = new ResourceEntity(0,0, new FoodResource());
		ResourceEntity seed = new ResourceEntity(0,0, new SeedResource());
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
	@Test
	public void tickTest(){
		resource.equals(new Player());
	}
}
