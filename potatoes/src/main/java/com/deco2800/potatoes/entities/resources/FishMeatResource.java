package com.deco2800.potatoes.entities.resources;

/**
 * SeedResource is a subclass of Resource adds seeds. Seeds can be used to plant
 * trees.
 * 
 * @author Dion, Jordan
 *
 */

public class FishMeatResource extends Resource {

	private static final transient String TEXTURE = "fishMeat";

	/**
	 * A type of resource that gives seeds to player.
	 */
	public FishMeatResource() {
		super();
		this.resourceType = "fishMeat";
		this.texture = TEXTURE;
	}
	

}
