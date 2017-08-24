package com.deco2800.potatoes.entities;

public class FoodResource extends Resource {

	private static final transient String TEXTURE = "food";
	
	/**
	 * A type of resource that gives health to player.
	 */
	public FoodResource() {
		super();
		this.resourceType = "food";
		this.texture = TEXTURE;
	}
	
}