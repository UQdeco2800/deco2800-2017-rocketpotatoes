package com.deco2800.potatoes.entities.resources;

/**
 * SeedResource is a subclass of Resource adds seeds. Seeds can be used to plant
 * trees.
 * 
 * @author Dion, Jordan
 *
 */

public class ObsidianResource extends Resource {

	private static final transient String TEXTURE = "obsidian";

	/**
	 * A type of resource that gives seeds to player.
	 */
	public ObsidianResource() {
		super();
		this.resourceType = "obsidian";
		this.texture = TEXTURE;
	}
	

}
