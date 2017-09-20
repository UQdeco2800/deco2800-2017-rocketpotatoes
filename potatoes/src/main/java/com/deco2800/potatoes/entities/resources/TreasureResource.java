package com.deco2800.potatoes.entities.resources;

/**
 * SeedResource is a subclass of Resource adds seeds. Seeds can be used to plant
 * trees.
 * 
 * @author Dion, Jordan
 *
 */

public class TreasureResource extends Resource {

	private static final transient String TEXTURE = "treasure";

	/**
	 * A type of resource that gives seeds to player.
	 */
	public TreasureResource() {
		super();
		this.resourceType = "treasure";
		this.texture = TEXTURE;
	}
	

}
