package com.deco2800.potatoes.entities;

public interface Resource {
	// possible additions:
	// 		- location data
	//		- texture data
	
	
	/**
	 * <p>
	 * Returns the ID of the resource.
	 * </p>
	 * 
	 * <p>
	 * Method to be implemented by each resource class.
	 * </p>
	 * 
	 * @return ID
	 * 		The numerical representation of the resource.
	 * 		Core game resources: 0xx. Eg. 1 for Seeds
	 * 		Offensive resources: 1xx. Eg. possibly 101 for arrows.
	 * 		Defensive resources: 2xx. Eg. possibly 204 for a healing item.
	 * 		Passive items: 3xx. Eg possibly 345 for a shield.
	 * 
	 * 		Extend as needed.
	 */
	public int getResourceType();
	
	/**
	 * <p>
	 * Returns the string representation of the resource.
	 * </p>
	 * 
	 * <p>
	 * Method to be implemented by each resource class.
	 * </p>
	 * 
	 * @return string
	 * 		The string representation of the resource.
	 */
	public String toString();

}
