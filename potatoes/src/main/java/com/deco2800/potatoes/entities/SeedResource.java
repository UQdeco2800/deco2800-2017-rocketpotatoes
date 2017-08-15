package com.deco2800.potatoes.entities;

public class SeedResource implements Resource {
	/*
	 * Unique ID assigned to each resource.
	 */
	private int type;
	/*
	 * Unique name for each resource.
	 */
	private String name;
	
	
	/**
	 * <p>
	 * Creates a new instance of the class. Also assigns it the unique ID and
	 * name of the resource.
	 * </p>
	 */
	public SeedResource() {
		type = 1;
		name = "Seeds";
	}

	@Override
	/**
	 * <p>
	 * Returns the unique ID for a resource. For seeds this is 1.
	 * </p>
	 * 
	 * @return ID
	 * 		The numerical representation of the resource.
	 */
	public int getResourceType() {
		return type;
	}
	
	@Override
	/**
	 * <p>
	 * Returns the string representation of the resource.
	 * </p>
	 * 
	 * @return string
	 * 		The string representation of the resource.
	 */
	public String toString() {
		return name;
	}
}