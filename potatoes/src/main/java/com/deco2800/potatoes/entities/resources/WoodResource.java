package com.deco2800.potatoes.entities.resources;

/**
 * SeedResource is a subclass of Resource adds seeds. Seeds can be used to plant
 * trees.
 * 
 * @author Dion, Jordan
 *
 */

public class WoodResource extends Resource {

	private static final transient String TEXTURE = "wood";

	/**
	 * A type of resource that gives seeds to player.
	 */
	public WoodResource() {
		super();
		this.resourceType = "wood";
		this.texture = TEXTURE;
	}
	

}
