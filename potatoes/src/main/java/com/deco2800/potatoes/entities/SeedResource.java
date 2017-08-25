package com.deco2800.potatoes.entities;

public class SeedResource extends Resource {
	
	private static final transient String TEXTURE = "seed";
	
	
	/**
	 * A type of resource that gives seeds to player.
	 */
	public SeedResource() {
		super();
		this.resourceType = "seed";
		this.texture = TEXTURE;
	}	
		
}
