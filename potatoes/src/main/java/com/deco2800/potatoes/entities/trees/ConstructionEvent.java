package com.deco2800.potatoes.entities.trees;

import com.deco2800.potatoes.entities.TimeEvent;

public class ConstructionEvent extends TimeEvent {
	
	private AbstractTree tree;
	
	/**
	 * @param tree the tree this event will change
	 * @param constructionTime the time 100% construction will take
	 */
	public ConstructionEvent(AbstractTree tree, int constructionTime) {
		this.tree = tree;
		setDoReset(true);
		setResetAmount(constructionTime / 100);
	}
	
	/**
	 * Reduces the tree construction left percent by 1
	 */
	@Override
	public void action() {
		if (tree.getConstructionLeft() > 0) {
			tree.setConstructionLeft(tree.getConstructionLeft() - 1);
		}
	}

}
