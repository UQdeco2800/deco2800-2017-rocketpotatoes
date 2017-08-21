package com.deco2800.potatoes.entities;

public class Resource {
	
	
	/*
	 * The location of the resource on the x axis
	 */
	private int xValue;
	/*
	 * The location of the resource on the y axis
	 */
	private int yValue;
	/*
	 * The location of the resource on the z axis
	 */
	private int zValue;
	
	/**
	 * <p>Returns the x location of the resource</p>
	 * 
	 * @return xValue
	 * 		The x location of the resource
	 */
	public int getX() {
		return xValue;
	}
	
	/**
	 * <p>Returns the y location of the resource</p>
	 * 
	 * @return yValue
	 * 		The y location of the resource
	 */
	public int getY() {
		return yValue;
	}
	
	/**
	 * <p>Returns the z location of the resource</p>
	 * 
	 * @return zValue
	 * 		The z location of the resource
	 */
	public int getZ() {
		return zValue;
	}
	
	/**
	 * <p>Returns the three coordinates of the resource.</p>
	 * 
	 * @return coordinates
	 * 		The x, y and z coordinates for the resource in that order.
	 */
	public int[] getLocation() {
		int[] coordinates = new int[] {xValue, yValue, zValue};
		
		return coordinates;
	}
	
	/**
	 * <p>Updates the coordinates of the resource on the map.</p>
	 * 
	 * <p>
	 * Can simply call with getX(), getY() and getZ() to update only one value.
	 * </p>
	 * 
	 * @param xNew
	 * 		The new x coordinate.
	 * @param yNew
	 * 		The new y coordinate.
	 * @param zNew
	 * 		The new z coordinate.
	 */
	public void move(int xNew, int yNew, int zNew) {
		xValue = xNew;
		yValue = yNew;
		zValue = zNew;
	}
	
	/**
	 * <p>
	 * Removes the resource from the map.
	 * </p>
	 */
	public void delete() {
		//to be completed after further discussion
	}
	
	/**
	 * <p>
	 * Returns the texture data for the resource.
	 * </p>
	 */
	public void getTexture() {
		//to be completed after further discussion
	}

}
