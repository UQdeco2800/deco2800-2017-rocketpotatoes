package com.deco2800.potatoes.entities.trees;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import com.deco2800.potatoes.entities.PropertiesBuilder;
import com.deco2800.potatoes.entities.animation.Animation;
import com.deco2800.potatoes.entities.animation.AnimationFactory;
import com.deco2800.potatoes.entities.resources.FoodResource;

public class FoodTree extends ResourceTree {
	
	/* Stats for the food resource tree */
	private static final transient int HEALTH = 5;			// The health of the tree
	private static final transient int BUILD_TIME = 10000;	// Time taken to build the tree
	private static final transient int BUILD_COST = 1;		// Cost of building the tree
	private static final transient int GATHER_CAPACITY = 6;	// Max resource capacity of the tree
	private static final transient int GATHER_RATE = 15000;	// Time interval of gathering resources
	private static final transient int GATHER_AMOUNT = 2;	// Amount of resources obtained per gather
	
	/* Food Tree Animations */
	private static final transient String[] GROW_ANIMATION = makeFrames("foodtree", "grow", 50, 1);
	private static final transient String[] PRODUCE_ANIMATION = makeFrames("foodtree", "produce", 35, 1);
	
	private static Function<ResourceTree, Animation> growAnimation = x -> AnimationFactory.createSimpleStateAnimation(100, 0,
			GROW_ANIMATION, () -> (float) x.getConstructionLeft());
	
	private static Function<ResourceTree, Animation> produceAnimation = x -> AnimationFactory.createSimpleStateAnimation(GATHER_CAPACITY, 0,
			PRODUCE_ANIMATION, () -> (float) GATHER_CAPACITY-x.getGatherCount());
	
	/**
	 * Constructor for creating a food resource tree
	 * 
	 * @param posX
	 *            The x-coordinate.
	 * @param posY
	 *            The y-coordinate.
	 */
	public FoodTree(float posX, float posY) {
		super(posX, posY, new FoodResource(), GATHER_CAPACITY); // Set resource to food and capacity to 8
		this.defaultTexture = "food_resource_tree";

	}
	
	/**
	 * Stats for a resource tree that gathers food
	 * 
	 * @return the list of upgrade stats for a food resource tree
	 */
	public static List<TreeProperties> getFoodTreeStats() {
		List<TreeProperties> result = new LinkedList<>();
		List<PropertiesBuilder<ResourceTree>> builders = new LinkedList<>();
		
		builders.add(new PropertiesBuilder<ResourceTree>().setHealth(HEALTH).setBuildTime(BUILD_TIME).setBuildCost(BUILD_COST)
				.setTexture("food_resource_tree").addEvent(new ResourceGatherEvent(GATHER_RATE, GATHER_AMOUNT)).setAnimation(growAnimation));

		for (PropertiesBuilder<ResourceTree> statisticsBuilder : builders) {
			result.add(statisticsBuilder.createTreeStatistics());
		}
		return result;
	}
	
	@Override
	public void updateAnimations() {
		super.updateAnimations();
		this.setAnimation(produceAnimation.apply(this));
	}
	
	@Override
	public List<TreeProperties> getAllUpgradeStats() {
		this.setTexture("food_resource_tree");
		return getFoodTreeStats();
	}
	
	@Override
	public ResourceTree createCopy() {
		return new FoodTree(this.getPosX(), this.getPosY());
	}
	
	@Override
	public String getName() {
		return "Food Tree";
	}

}
