package com.deco2800.potatoes.entities.trees;
import com.deco2800.potatoes.entities.TimeEvent;

/**
 * An time event that increments the resource count of a resource tree.
 */
public class ResourceGatherEvent extends TimeEvent<AbstractTree> {
	
	private int gatherAmount;	// The number of resources gathered per reset
	
	/**
	 * Default constructor for serialization
	 */
	public ResourceGatherEvent() {
	}
	
	/**
	 * @param gatherRate
	 *            the rate at which resources are collected
	 * @param gatherAmount
	 *            the number of resources collected per action
	 */
	public ResourceGatherEvent(int gatherRate, int gatherAmount) {
		setDoReset(true);
		setResetAmount(gatherRate);
		this.gatherAmount = gatherAmount;
		reset();
	}
	
	/**
	 * Action for incrementing resources in the resource tree. 
	 */
	@Override
	public void action(AbstractTree tree) {
		ResourceTree resourceTree = (ResourceTree) tree;
		
		// Keep adding resources until the resource tree is full. The player
		// Will need to remove resources for more to produce.
		
		if ((resourceTree.resourceCount + gatherAmount) <= ResourceTree.MAX_RESOURCE_COUNT) {
			resourceTree.resourceCount += gatherAmount;
		} else {
			resourceTree.resourceCount = ResourceTree.MAX_RESOURCE_COUNT;
		}
	}

	@Override
	public TimeEvent<AbstractTree> copy() {
		return new ResourceGatherEvent(getResetAmount(), gatherAmount);
	}

	
}
