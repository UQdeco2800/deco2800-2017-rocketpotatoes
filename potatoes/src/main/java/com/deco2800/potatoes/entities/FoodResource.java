package com.deco2800.potatoes.entities;

public class FoodResource extends Resource {

	private static final transient String TEXTURE = "food";
	
	/**
	 * <p>
	 * Creates a new instance of the class and assigns the name of 
	 * the resource.
	 * </p>
	 * 
	 * <p>
	 * Only to be used when the instance of the resource isn't appearing
	 * on the map.
	 * </p>
	 */
	public FoodResource() {
		super();
		this.resourceType = "food";
	}
	
	public String getTexture() {
		return TEXTURE;
	}

}