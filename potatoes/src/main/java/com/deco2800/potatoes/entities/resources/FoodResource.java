package com.deco2800.potatoes.entities.resources;

/**
 * FoodResource is a type of Resource that grants players health when used.
 * 
 * @author Dion, Jordan
 *
 */

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