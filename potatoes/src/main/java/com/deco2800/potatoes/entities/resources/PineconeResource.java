package com.deco2800.potatoes.entities.resources;

/**
 * SeedResource is a subclass of Resource adds seeds. Seeds can be used to plant
 * trees.
 * 
 * @author Dion, Jordan
 *
 */

public class PineconeResource extends Resource {

	private static final transient String TEXTURE = "pinecone";

	/**
	 * A type of resource that gives seeds to player.
	 */
	public PineconeResource() {
		super();
		this.resourceType = "pinecone";
		this.texture = TEXTURE;
	}
	

}
