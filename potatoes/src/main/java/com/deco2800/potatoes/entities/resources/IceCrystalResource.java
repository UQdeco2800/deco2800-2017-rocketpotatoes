package com.deco2800.potatoes.entities.resources;

/**
 * SeedResource is a subclass of Resource adds seeds. Seeds can be used to plant
 * trees.
 * 
 * @author Dion, Jordan
 *
 */

public class IceCrystalResource extends Resource {

	private static final transient String TEXTURE = "iceCrystal";

	/**
	 * A type of resource that gives seeds to player.
	 */
	public IceCrystalResource() {
		super();
		this.resourceType = "iceCrystal";
		this.texture = TEXTURE;
	}
	

}
