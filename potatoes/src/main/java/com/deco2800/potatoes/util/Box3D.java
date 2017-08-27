package com.deco2800.potatoes.util;

import java.util.Objects;

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

	/**
	 * Find the angle between two _Box3D_. Starts from calling box to target box
	 * @param target end point of the line
	 * @return angle in radians
	 */
	public float angle(Box3D target) {

		// Find the difference of X and Y coordinates
		float deltaX = this.getX() - target.getX();
		float deltaY = this.getY() - target.getY();
		// Return angle
		return  (float)(Math.atan2(deltaY, deltaX)) + (float)(Math.PI);
	}

	/**
	 * Checks to see if a line intersects with this Box3D.
	 * The line goes from point (x1,y1,z1) to (x2,y2,z2)
	 *
	 * @param x1 The x coord of point 1 of the line
	 * @param y1 The y coord of point 1 of the line
	 * @param z1 The z coord of point 1 of the line
	 * @param x2 The x coord of point 2 of the line
	 * @param y2 The y coord of point 2 of the line
	 * @param z2 The z coord of point 2 of the line
	 * @return
	 */
	public boolean doesIntersectLine(float x1, float y1, float z1, float x2, float y2, float z2) {
		float fMin = 0;
		float fMax = 1;

		float[] lineMin = {Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2)};
		float[] lineMax = {Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2)};
		float[] boxMin = {this.x, this.y, this.z};
		float[] boxMax = {this.x + this.xLength, this.y + this.yLength, this.z + this.zLength};

		for (int i = 0; i < 3; i++) {
			float lineDist = lineMax[i] - lineMin[i];
			if (lineDist != 0) {
				fMin = Math.max(fMin, (boxMax[i] - lineMin[i]) / lineDist);
				fMax = Math.min(fMax, (boxMax[i] - lineMax[i]) / lineDist);
				if (fMin > fMax) { return false;}
			} else if (lineMin[i] < boxMin[i] || lineMax[i] > boxMax[i]) { return false; }

		}
		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z, xLength, yLength, zLength);
	}

	private boolean compareFloat(float a, float b) {
		float delta = 0.00001f;
		return Math.abs(a-b) < delta;

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

		//since equality necessitates hash equality, this equals() method does
		//effectivelly float == float. I haven't found a way around this that enforces
		//transitivity, hashCode equality, and equality between very similar values.
		return hashCode() == that.hashCode() &&
			compareFloat(that.getX(), getX()) &&
			compareFloat(that.getY(), getY()) &&
			compareFloat(that.getZ(), getZ()) &&
			compareFloat(that.getXLength(), getXLength()) &&
			compareFloat(that.getYLength(), getYLength()) &&
			compareFloat(that.getZLength(), getZLength());

	}

}
