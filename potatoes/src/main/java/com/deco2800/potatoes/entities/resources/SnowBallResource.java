package com.deco2800.potatoes.entities.resources;

/**
 * SeedResource is a subclass of Resource adds seeds. Seeds can be used to plant
 * trees.
 * 
 * @author Dion, Jordan
 *
 */

public class SnowBallResource extends Resource {

	private static final transient String TEXTURE = "snowBall";

	/**
	 * A type of resource that gives seeds to player.
	 */
	public SnowBallResource() {
		super();
		this.resourceType = "snowBall";
		this.texture = TEXTURE;
	}
	

}
