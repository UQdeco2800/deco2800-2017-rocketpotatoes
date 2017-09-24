package com.deco2800.potatoes.entities.trees;
import com.deco2800.potatoes.entities.TimeEvent;

/**
 * An time event that increments the resource count of a resource tree.
 * 
 * @author petercondoleon
 */
public class ResourceGatherEvent extends TimeEvent<ResourceTree> {
	
	private int gatherAmount;	// The number of resources gathered per interval
	
	/**
	 * Default constructor for serialization
	 */
	public ResourceGatherEvent() {
		//Blank comment for Sonar
	}
	
	/**
	 * Initialises with a gather rate and amount.
	 * 
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
	public void action(ResourceTree tree) {
		if (tree.isGatherEnabled()) {
			tree.gather(gatherAmount);
		}
	}

	@Override
	public TimeEvent<ResourceTree> copy() {
		return new ResourceGatherEvent(getResetAmount(), gatherAmount);
	}

	
}