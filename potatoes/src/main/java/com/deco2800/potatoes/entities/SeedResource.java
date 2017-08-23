package com.deco2800.potatoes.entities;

public class SeedResource extends Resource {
	
	private static final transient String TEXTURE = "seed";
	
	
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
	public SeedResource() {
		super();
		this.resourceType = "seed";
	}
	
	public String getTexture() {
		return TEXTURE;
	}
	
	/*public SeedResource(String name, float posX, float posY, float posZ) {
		super(name, posX, posY, posZ, TEXTURE);
		this.resourceType = "seed";
	}*/

	
		
}
