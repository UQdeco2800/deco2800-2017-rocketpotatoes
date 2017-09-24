package com.deco2800.potatoes.entities.resources;

/**
 * SeedResource is a subclass of Resource adds seeds. Seeds can be used to plant
 * trees.
 * 
 * @author Dion, Jordan
 *
 */

public class TumbleweedResource extends Resource {

	private static final transient String TEXTURE = "tumbleweed";

	/**
	 * A type of resource that gives seeds to player.
	 */
	public TumbleweedResource() {
		super();
		this.resourceType = "tumbleweed";
		this.texture = TEXTURE;
	}
	

}
