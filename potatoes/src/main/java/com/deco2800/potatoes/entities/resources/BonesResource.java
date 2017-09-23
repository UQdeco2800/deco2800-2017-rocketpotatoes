package com.deco2800.potatoes.entities.resources;

/**
 * SeedResource is a subclass of Resource adds seeds. Seeds can be used to plant
 * trees.
 * 
 * @author Dion, Jordan
 *
 */

public class BonesResource extends Resource {

	private static final transient String TEXTURE = "bones";

	/**
	 * A type of resource that gives seeds to player.
	 */
	public BonesResource() {
		super();
		this.resourceType = "bones";
		this.texture = TEXTURE;
	}
	

}
