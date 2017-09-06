package com.deco2800.potatoes.entities;

public class TreeShop extends AbstractEntity {
	private final static String TEXTURE = "tree_shop";
	
	public TreeShop(float posX, float posY) {
		super(posX-3, posY+1, 1, 3f, 3f, 0, TEXTURE);
		this.setStaticCollideable(false);
	}
}
