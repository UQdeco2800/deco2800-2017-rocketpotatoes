package com.deco2800.potatoes.util;

/**
 * Utility class for math related operations
 */
public class MathUtil {
	private static final float delta = 0.00001f;

	/**
	 * Compares the 2 given floats using a delta value. Returns true if they are equal or close enough, or false
	 * otherwise.
	 *
	 * @param a The first number to compare
	 * @param b The Second number to compare
	 * @return True if the numbers are equal or close, false otherwise.
	 */
	public static boolean compareFloat(float a, float b) {
		return Math.abs(a - b) < delta;
	}
}
