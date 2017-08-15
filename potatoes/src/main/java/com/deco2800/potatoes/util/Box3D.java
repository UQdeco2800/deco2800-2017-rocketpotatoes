package com.deco2800.potatoes.util;

/**
 * Representation of a box in 3d space, defined by a corner point in XYZ and
 * extends in x (xLength), y (yLength), and z (zLength).
 * 
 * @author leggy
 *
 */
public class Box3D {

	private float x, y, z;

	private float xLength, yLength, zLength;

	/**
	 * Default constructor for the purposes of serialization
	 */
	public Box3D() { }

	/**
	 * Constructs a new Box3D with the given corner point and dimensions.
	 * 
	 * @param x
	 *            The corner point x-coordinate.
	 * @param y
	 *            The corner point y-coordinate.
	 * @param z
	 *            The corner point z-coordinate.
	 * @param xLength
	 *            The xLength (in x).
	 * @param yLength
	 *            The yLength (in y).
	 * @param zLength
	 *            The zLength (in z).
	 */
	public Box3D(float x, float y, float z, float xLength, float yLength, float zLength) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.xLength = xLength;
		this.yLength = yLength;
		this.zLength = zLength;
	}





	/**
	 * Constructs a new Box3D based on the given box.
	 * 
	 * @param box
	 *            The box to copy.
	 */
	public Box3D(Box3D box) {
		this.x = box.x;
		this.y = box.y;
		this.z = box.z;
		this.xLength = box.xLength;
		this.yLength = box.yLength;
		this.zLength = box.zLength;
	}

	/**
	 * Returns the x coordinate.
	 * 
	 * @return Returns the x coordinate.
	 */
	public float getX() {
		return x;
	}

	/**
	 * Sets the x coordinate.
	 * 
	 * @param x
	 *            The new x coordinate.
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Returns the y coordinate.
	 * 
	 * @return Returns the y coordinate.
	 */
	public float getY() {
		return y;
	}

	/**
	 * Sets the y coordinate.
	 * 
	 * @param y
	 *            The new y coordinate.
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Returns the z coordinate.
	 * 
	 * @return Returns the z coordinate.
	 */
	public float getZ() {
		return z;
	}

	/**
	 * Sets the z coordinate.
	 * 
	 * @param z
	 *            The new z coordinate.
	 */
	public void setZ(float z) {
		this.z = z;
	}

	/**
	 * Returns the xLength (in x).
	 * 
	 * @return Returns the xLength.
	 */
	public float getXLength() {
		return xLength;
	}

	/**
	 * Returns the yLength (in y).
	 * 
	 * @return Returns the yLength.
	 */
	public float getYLength() {
		return yLength;
	}

	/**
	 * Returns the zLength (in z).
	 * 
	 * @return Returns the zLength.
	 */
	public float getZLength() {
		return zLength;
	}


	public boolean overlaps(Box3D box) {
		/*
		 * Checking non-collision on all 6 directions.
		 */

		// x smaller
		if (x + xLength < box.x) {
			return false;
		}

		// x larger
		if (x > box.x + box.xLength) {
			return false;
		}

		// y smaller
		if (y + yLength < box.y) {
			return false;
		}

		// y larger
		if (y > box.y + box.yLength) {
			return false;
		}

		// z smaller
		if (z + zLength < box.z) {
			return false;
		}

		// z larger
		if (z > box.z + box.zLength) {
			return false;
		}

		return true;
	}

	public float distance(Box3D box) {
		return distance(box.x, box.y, box.z);
	}
	
	public float distance(float x, float y, float z) {
		return (float)(Math.sqrt(Math.pow((x - this.x), 2) + Math.pow((y - this.y), 2) + Math.pow((z - this.z), 2)));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof Box3D)) {
			return false;
		}

		Box3D that = (Box3D) o;

		return that.getX() == getX() && 
			that.getY() == getY() &&
			that.getZ() == getZ() &&
			that.getXLength() == getXLength() &&
			that.getYLength() == getYLength() &&
			that.getZLength() == getZLength();

	}


}
