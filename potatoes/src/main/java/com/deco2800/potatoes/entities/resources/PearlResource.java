package com.deco2800.potatoes.entities.resources;

/**
 * SeedResource is a subclass of Resource adds seeds. Seeds can be used to plant
 * trees.
 * 
 * @author Dion, Jordan
 *
 */

public class PearlResource extends Resource {

	private static final transient String TEXTURE = "pearl";

	/**
	 * A type of resource that gives seeds to player.
	 */
	public PearlResource() {
		super();
		this.resourceType = "pearl";
		this.texture = TEXTURE;
	}
	

}
