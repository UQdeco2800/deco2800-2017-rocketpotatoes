package com.deco2800.potatoes.entities.trees;

import java.util.LinkedList;
import java.util.List;
import com.deco2800.potatoes.entities.PropertiesBuilder;
import com.deco2800.potatoes.entities.resources.FoodResource;

public class FoodTree extends ResourceTree {
	
	/**
	 * Constructor for creating a food resource tree
	 * 
	 * @param posX
	 *            The x-coordinate.
	 * @param posY
	 *            The y-coordinate.
	 */
	public FoodTree(float posX, float posY) {
		super(posX, posY, new FoodResource(), 8); // Set resource to food and capacity to 8
		this.defaultTexture = "food_resource_tree";
	}
	
	/**
	 * Stats for a resource tree that gathers food
	 * 
	 * @return the list of upgrade stats for a food resource tree
	 */
	private static List<TreeProperties> getFoodTreeStats() {
		List<TreeProperties> result = new LinkedList<>();
		List<PropertiesBuilder<ResourceTree>> builders = new LinkedList<>();

		String texture = "food_resource_tree";
		builders.add(new PropertiesBuilder<ResourceTree>().setHealth(5).setBuildTime(8000).setBuildCost(1)
				.setTexture(texture).addEvent(new ResourceGatherEvent(6000, 1)));
		builders.add(new PropertiesBuilder<ResourceTree>().setHealth(10).setBuildTime(7000).setBuildCost(1)
				.setTexture(texture).addEvent(new ResourceGatherEvent(5500, 1)));
		builders.add(new PropertiesBuilder<ResourceTree>().setHealth(15).setBuildTime(6500).setBuildCost(1)
				.setTexture(texture).addEvent(new ResourceGatherEvent(5000, 2)));

		for (PropertiesBuilder<ResourceTree> statisticsBuilder : builders) {
			result.add(statisticsBuilder.createTreeStatistics());
		}
		return result;
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
