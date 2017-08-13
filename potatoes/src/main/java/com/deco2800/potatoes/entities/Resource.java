package com.deco2800.potatoes.entities;

public interface Resource {
	
	/**
	 * <p>
	 * Returns the ID of the resource
	 * </p>
	 * 
	 * <p>
	 * Method to be implemented by each resource class.
	 * </p>
	 * 
	 * @return ID
	 * 		The numerical representation of the resource.
	 * 		1 - ???
	 * 		2 - ???
	 * 		3 - ???
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
