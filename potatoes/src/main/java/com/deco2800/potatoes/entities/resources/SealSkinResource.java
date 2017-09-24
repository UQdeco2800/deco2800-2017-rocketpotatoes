package com.deco2800.potatoes.entities.resources;

/**
 * SeedResource is a subclass of Resource adds seeds. Seeds can be used to plant
 * trees.
 * 
 * @author Dion, Jordan
 *
 */

public class SealSkinResource extends Resource {

	private static final transient String TEXTURE = "sealSkin";

	/**
	 * A type of resource that gives seeds to player.
	 */
	public SealSkinResource() {
		super();
		this.resourceType = "sealSkin";
		this.texture = TEXTURE;
	}
	

}
