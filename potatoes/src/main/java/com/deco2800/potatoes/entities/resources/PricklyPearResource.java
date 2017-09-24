package com.deco2800.potatoes.entities.resources;

/**
 * SeedResource is a subclass of Resource adds seeds. Seeds can be used to plant
 * trees.
 * 
 * @author Dion, Jordan
 *
 */

public class PricklyPearResource extends Resource {

	private static final transient String TEXTURE = "pricklyPear";

	/**
	 * A type of resource that gives seeds to player.
	 */
	public PricklyPearResource() {
		super();
		this.resourceType = "pricklyPear";
		this.texture = TEXTURE;
	}
	

}
