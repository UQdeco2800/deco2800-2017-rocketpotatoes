package com.deco2800.potatoes.entities;

import com.deco2800.potatoes.entities.HasDirection.Direction;

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
		SouthWest, West, NorthWest;
		
		
		/**
		 * Returns the initials representing a direction. For example,
		 * a direction of 'NorthEast' will return 'NE'.
		 */
		public String toString() {
			String initials = "";
			for (int i = 0; i < this.name().length(); i++) {
				char c = name().charAt(i);
				if (Character.isUpperCase(c)) { 
					initials += c;
				}
			}
			return initials;
		}
	};
	
	/**
	 * A method responsible for returning the current direction of
	 * an entity.
	 */
	public Direction getDirection();
	
	/**
     * A method responsible for setting the direction of 
     * an entity.
     *
     * @param direction 
     * 			The direction to set the entity to.
     */
	public void setDirection(Direction direction);
	
}
