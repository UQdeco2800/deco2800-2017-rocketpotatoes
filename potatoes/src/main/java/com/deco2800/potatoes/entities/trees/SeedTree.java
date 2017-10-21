package com.deco2800.potatoes.entities.trees;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import com.deco2800.potatoes.entities.PropertiesBuilder;
import com.deco2800.potatoes.entities.animation.Animation;
import com.deco2800.potatoes.entities.animation.AnimationFactory;
import com.deco2800.potatoes.entities.animation.SingleFrameAnimation;
import com.deco2800.potatoes.entities.animation.TimeAnimation;
import com.deco2800.potatoes.entities.resources.SeedResource;

public class SeedTree extends ResourceTree {
	
	/* Stats for the seed resource tree */
	private static final transient int HEALTH = 8;			// The health of the tree
	private static final transient int BUILD_TIME = 7500;	// Time taken to build the tree
	private static final transient int BUILD_COST = 1;		// Cost of building the tree
	private static final transient int GATHER_CAPACITY = 12;	// Max resource capacity of the tree
	private static final transient int GATHER_RATE = 10000;	// Time interval of gathering resources
	private static final transient int GATHER_AMOUNT = 2;	// Amount of resources obtained per gather
	
	private static final transient String[] GROW_ANIMATION = getFrames();
	private TimeAnimation produceAnimation = makeResourceTreeAnimation("seedtree", "produce", 31, 6000, this::finishedProduce);
	private SingleFrameAnimation defaultAnimation = new SingleFrameAnimation("seed_resource_tree");
	
	
//	@Override
//	public void gather(int amount) {
//		// TODO Auto-generated method stub
//		super.gather(amount);
//		this.setAnimation(defaultAnimation);
//	}
	
	/**
	 * Constructor for creating a seed resource tree
	 * 
	 * @param posX
	 *            The x-coordinate.
	 * @param posY
	 *            The y-coordinate.
	 */
	public SeedTree(float posX, float posY) {
		super(posX, posY, new SeedResource(), GATHER_CAPACITY); // Set resource to seed
		this.defaultTexture = "seed_resource_tree";
	}
	
	/**
     * Creates an array of frames for the grow animation
     */
    private static String[] getFrames() {
		String[] frames = new String[43];
		for (int i = 1; i <= 43; i++) {
			frames[i - 1] = "seedtree" + "_" + "grow" + "_" + i;
		}
		return frames;
    }
    
    /**
     * Custom animation handling for the seed resource tree
     */
    private Void finishedProduce() {
        System.out.println("Finished Produce");
        this.setAnimation(defaultAnimation);
        return null;
    }
    
	/**
	 * Stats for a resource tree that gathers seeds
	 * 
	 * @return the list of upgrade stats for a seed resource tree
	 */
	private static List<TreeProperties> getSeedTreeStats() {
		List<TreeProperties> result = new LinkedList<>();
		List<PropertiesBuilder<ResourceTree>> builders = new LinkedList<>();
		
		Function<ResourceTree, Animation> growAnimation = x -> AnimationFactory.createSimpleStateAnimation(100, 0,
				GROW_ANIMATION, () -> (float) x.getConstructionLeft());
		
		builders.add(new PropertiesBuilder<ResourceTree>().setHealth(HEALTH).setBuildTime(BUILD_TIME).setBuildCost(BUILD_COST)
				.setTexture("seed_resource_tree").addEvent(new ResourceGatherEvent(GATHER_RATE, GATHER_AMOUNT)).setAnimation(growAnimation));

		for (PropertiesBuilder<ResourceTree> statisticsBuilder : builders) {
			result.add(statisticsBuilder.createTreeStatistics());
		}
		return result;
	}
	
	@Override
	public List<TreeProperties> getAllUpgradeStats() {
		this.setTexture("seed_resource_tree");
		return getSeedTreeStats();
	}
	
	@Override
	public ResourceTree createCopy() {
		return new SeedTree(this.getPosX(), this.getPosY());
	}
	
	@Override
	public String getName() {
		return "Seed Tree";
	}
	
}
