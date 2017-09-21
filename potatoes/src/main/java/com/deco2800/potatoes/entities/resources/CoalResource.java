package com.deco2800.potatoes.entities.resources;

/**
 * SeedResource is a subclass of Resource adds seeds. Seeds can be used to plant
 * trees.
 * 
 * @author Dion, Jordan
 *
 */

public class CoalResource extends Resource {

	private static final transient String TEXTURE = "coal";

	/**
	 * A type of resource that gives seeds to player.
	 */
	public CoalResource() {
		super();
		this.resourceType = "coal";
		this.texture = TEXTURE;
	}
	

}
