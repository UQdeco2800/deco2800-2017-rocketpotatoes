package com.deco2800.potatoes.entities;

public class FoodResource extends Resource {
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
	public FoodResource() {
		name = "Food";
	}
	
	/**
	 * <p>
	 * Creates a new instance of the class and assigns the name of 
	 * the resource.
	 * </p>
	 * 
	 * <p>
	 * Also sets the location of the resource on the map
	 * </p>
	 * 
	 * @param xCoord
	 * 		The x location of the resource.
	 * @param yCoord
	 * 		The y location of the resource.
	 * @param zCoord
	 * 		The z location of the resource.
	 */
	public FoodResource(int xCoord, int yCoord, int zCoord) {
		name = "Food";
		
		this.move(xCoord, yCoord, zCoord);
	}

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