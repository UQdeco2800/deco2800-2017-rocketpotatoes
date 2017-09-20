package com.deco2800.potatoes.entities.resources;

/**
 * SeedResource is a subclass of Resource adds seeds. Seeds can be used to plant
 * trees.
 * 
 * @author Dion, Jordan
 *
 */

public class CactusThornResource extends Resource {

	private static final transient String TEXTURE = "cactusThorn";

	/**
	 * A type of resource that gives seeds to player.
	 */
	public CactusThornResource() {
		super();
		this.resourceType = "cactusThorn";
		this.texture = TEXTURE;
	}
	

}
