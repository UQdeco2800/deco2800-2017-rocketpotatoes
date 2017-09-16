package com.deco2800.potatoes.entities;

/**
 * 
 * An interface for handling entity direction.
 * 
 * @author petercondoleon
 *
 */
public interface HasDirection {
	
	/**
	 * An enumeration for defining the direction an entity may take. The
	 * directions are described as follows: North, North-East, East,
	 * South-East, South, South-West, West, North-West.
	 */
	public enum Direction { North, NorthEast, East, SouthEast, South, 
		SouthWest, West, NorthWest };
	
	
	/**
	 * A method responsible for returning the current direction of
	 * an entity.
	 */
	public Direction getDirection();
	
}
