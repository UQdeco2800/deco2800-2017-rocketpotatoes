package com.deco2800.potatoes.entities;

public class SeedResource extends Resource {
	/*
	 * Unique name for each resource.
	 */
	private String name;
	
	
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
	
	public SeedResource(String name) {
		super(name);
		this.resourceType = "seed";
	}


}