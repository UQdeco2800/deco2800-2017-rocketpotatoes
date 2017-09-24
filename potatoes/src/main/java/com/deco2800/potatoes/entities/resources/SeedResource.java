package com.deco2800.potatoes.entities.resources;

/**
 * SeedResource is a subclass of Resource adds seeds. Seeds can be used to plant
 * trees.
 * 
 * @author Dion, Jordan
 *
 */

public class SeedResource extends Resource {

	private static final transient String TEXTURE = "seed";

	/**
	 * A type of resource that gives seeds to player.
	 */
	public SeedResource() {
		super();
		this.resourceType = "seed";
		this.texture = TEXTURE;
	}
	

}
