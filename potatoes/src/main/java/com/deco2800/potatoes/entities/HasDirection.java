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
	public enum Direction { N("North"), NE("North-East"), E("East"), SE("South-East"), 
					S(("South")), SW("South-West"), W("West"), NW("North-West");
		
		private final String name;
		
		private Direction(String name) {
			this.name = name;
		}
		
		/**
		 * Returns the string name of the direction. For example,
		 * a direction of 'NE' will return 'North-East'.
		 */
		@Override
		public String toString() {
			return name;
		}
	};
	
	
	/**
	 * A method responsible for returning the current direction of
	 * an entity.
	 */
	public Direction getDirection();
	
}
