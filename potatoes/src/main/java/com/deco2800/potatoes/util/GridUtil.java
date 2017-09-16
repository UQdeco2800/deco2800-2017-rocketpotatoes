package com.deco2800.potatoes.util;

import java.awt.Point;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;

/**
 * Class for utility functions for working with grids.
 */
public class GridUtil {
	private static final int[][] DIAMOND_SAMPLES = { { 0, -1 }, { -1, 0 }, { 0, 1 }, { 1, 0 } };
	private static final int[][] SQUARE_SAMPLES = { { 1, 1 }, { -1, 1 }, { 1, -1 }, { -1, -1 } };
	private static final Point SOUTH = new Point(0, -1);
	private static final Point WEST = new Point(-1, 0);
	private static final Point NORTH = new Point(0, 1);
	private static final Point EAST = new Point(1, 0);

	private GridUtil() {
		// Shouldn't be used
	}

	/**
	 * Returns the flood fill area of the given grid, where the area is continuous
	 * region of equal objects joined by north, south, east and west directions.
	 * 
	 * @param grid
	 *            the grid to find the flood fill area in
	 * @param startX
	 *            the x coordinate for the point to start at
	 * @param startY
	 *            the y coordinate for the point to start at
	 * @return the points representing the flood fill area
	 */
	public static Set<Point> floodFill(Object[][] grid, int startX, int startY) {
		Set<Point> edges = new HashSet<>();
		Set<Point> filled = new HashSet<>();
		Object start = grid[startX][startY];
		genericFloodFill(new Point(startX, startY), p -> grid[p.x][p.y].equals(start), edges, filled);
		return filled;
	}

	/**
	 * Returns the edges of a flood fill area of the given grid, where the area is
	 * continuous region of equal objects joined by north, south, east and west
	 * directions.
	 * 
	 * @param grid
	 *            the grid to find the flood fill area in
	 * @param startX
	 *            the x coordinate for the point to start at
	 * @param startY
	 *            the y coordinate for the point to start at
	 * @return the points representing the flood fill edges
	 */
	public static Set<Point> floodEdges(Object[][] grid, int startX, int startY) {
		Set<Point> edges = new HashSet<>();
		Set<Point> filled = new HashSet<>();
		Object start = grid[startX][startY];
		genericFloodFill(new Point(startX, startY), p -> grid[p.x][p.y].equals(start), edges, filled);
		return edges;
	}

	/**
	 * Performs a generic flood fill using a function to check whether tiles should
	 * be included. The parameter filled will have the points representing the flood
	 * fill area and edges will have points on the edge of the flood fill area,
	 * excluding diagonals
	 * 
	 * @param start
	 *            the point to start at
	 * @param func
	 *            the function that is used to determine whether a tile should be
	 *            part of the flood fill area
	 * @param edges
	 *            the edges of the flood fill area after this has run, initially
	 *            should be an empty set
	 * @param filled
	 *            the flood fill area. Can be initially set to points that will be
	 *            ignored.
	 */
	public static void genericFloodFill(Point start, Function<Point, Boolean> func, Set<Point> edges,
			Set<Point> filled) {
		Queue<Point> q = new LinkedList<>();
		q.add(new Point(start.x, start.y));
		while (!q.isEmpty()) {
			Point p = q.poll();

			checkEastWest(func, edges, filled, q, p, WEST);
			checkEastWest(func, edges, filled, q, p, EAST);
		}
	}

	private static void checkEastWest(Function<Point, Boolean> func, Set<Point> edges, Set<Point> filled,
			Queue<Point> q, Point p, Point direction) {
		// Continues in the given direction until it hits an edge
		do {
			filled.add(new Point(p));
			checkNorthSouth(func, edges, filled, q, new Point(p), NORTH);
			checkNorthSouth(func, edges, filled, q, new Point(p), SOUTH);
			p.move(direction.x, direction.y);
		} while (func.apply(p));
		// Once finished, p is at an edge
		edges.add(new Point(p));
	}

	private static void checkNorthSouth(Function<Point, Boolean> func, Set<Point> edges, Set<Point> filled,
			Queue<Point> q, Point p, Point direction) {
		p.move(direction.x, direction.y);
		if (filled.contains(p)) {
			if (func.apply(p)) {
				q.add(p);
			} else {
				// At an edge
				edges.add(p);
			}
		}
	}

	/**
	 * Takes an array of floats with varying values and scales them all to be
	 * between 0 and 1, inclusive
	 * 
	 * @param array
	 *            The array to normalize. Modified by the process.
	 * @return The same array as the input, now with all values between 0 and 1
	 */
	public static float[][] normalize(float[][] array) {
		float min = Float.MAX_VALUE;
		float max = Float.MIN_VALUE;
		// Find the maximum and minimum of the array
		for (int x = 0; x < array.length; x++) {
			for (int y = 0; y < array[x].length; y++) {
				min = Math.min(min, array[x][y]);
				max = Math.max(max, array[x][y]);
			}
		}
		// Apply the normalization formula to every element
		for (int x = 0; x < array.length; x++) {
			for (int y = 0; y < array[x].length; y++) {
				array[x][y] = (array[x][y] - min) / (max - min);
			}
		}
		return array;
	}

	/**
	 * Diamond square algorithm for generating smoothish grid noise. The input grid
	 * should be somewhat randomly seeded to remove grid patterns from the output.
	 * The roughScale parameter is what predominately changes the noise of the
	 * output grid.
	 * 
	 * @param result
	 *            The grid to apply the noise to, and the return value
	 * @param gridSize
	 *            The size of the grid
	 * @param roughness
	 *            The initial roughness value. Higher values make more variance near
	 *            the edges.
	 * @param roughScale
	 *            The amount the roughness reduces every iteration. Higher values
	 *            result in more variance in the grid.
	 * @return same as the result parameter
	 */
	public static float[][] diamondSquareAlgorithm(float[][] result, int gridSize, float roughness, float roughScale) {
		int size = gridSize / 2;
		float rough = roughness;
		while (size > 0) {
			for (int x = size; x < result.length; x += size) {
				for (int y = size; y < result[0].length; y += size) {
					// Confusingly named diamond step
					sampleStep(result, SQUARE_SAMPLES, x, y, size, (float) (rough * (Math.random())));
				}
			}
			for (int x = 0; x < result.length - size; x += size) {
				for (int y = 0; y < result[0].length - size; y += size) {
					// Square step
					sampleStep(result, DIAMOND_SAMPLES, x + size, y, size, (float) (rough * (Math.random() * 2 - 1)));
					sampleStep(result, DIAMOND_SAMPLES, x, y + size, size, (float) (rough * (Math.random() * 2 - 1)));
				}
			}
			// Reduce variance and size
			size = size / 2;
			rough *= roughScale;
		}
		// Hacky fix, reiterate over edges
		for (int x = 0; x < result.length; x++) {
			sampleStep(result, DIAMOND_SAMPLES, x, 0, 1, (float) (rough * (Math.random())));
			sampleStep(result, DIAMOND_SAMPLES, x, result.length - 1, 1, (float) (rough * (Math.random())));
		}
		for (int y = 0; y < result.length; y++) {
			sampleStep(result, DIAMOND_SAMPLES, 0, y, 1, (float) (rough * (Math.random())));
			sampleStep(result, DIAMOND_SAMPLES, result.length - 1, y, 1, (float) (rough * (Math.random())));
		}
		return result;
	}

	/**
	 * Generates a square grid with the specified size and applies the diamond
	 * square algorithm to generate a noisy grid. The diamond square algorithm is
	 * applied iterations times, resulting in a smoother grid every time. The
	 * roughness also controls the smoothness, but is much more variable and should
	 * be changed instead of iterations where possible.
	 * 
	 * @param size
	 *            The size of the grid to create
	 * @param roughness
	 *            The roughness for diamond square
	 * @param iterations
	 *            The number of times to run diamond square, to smooth. INCREASING
	 *            THIS IS VERY SLOW
	 * @return The random heightmap
	 */
	public static float[][] smoothDiamondSquareAlgorithm(int size, float roughness, int iterations) {
		float[][] result = new float[size][size];
		// Seed to 0.5
		for (int x = 0; x < result.length; x++) {
			for (int y = 0; y < result[x].length; y++) {
				result[x][y] = 0.5f;
			}
		}
		// Normalize and diamond square repeatedly, with the output the seed for the
		// next iteration
		for (int i = 0; i < iterations; i++) {
			normalize(diamondSquareAlgorithm(result, size, roughness, roughness));
		}
		// For some reason the result sometimes isn't normalized
		normalize(result);
		return result;
	}

	private static void sampleStep(float[][] array, int[][] sampleType, int x, int y, int size, float random) {
		float sum = 0;
		int count = 0;
		for (int[] is : sampleType) {
			// Sample using the given pattern
			int sampleX = (int) x + is[0] * size;
			int sampleY = (int) y + is[1] * size;
			// We're adding something so increase count for average
			count++;
			sum += get(array, sampleX, sampleY);
		}
		// Store the average in the array
		array[x][y] = random + sum / count;
	}

	/**
	 * Gets the value for the array at x and y, where x and y wrap around if they
	 * are bigger or smaller than the array.
	 */
	private static float get(float[][] array, int x, int y) {
		// Wraps around the edges
		int adjustedX = x < 0 ? x + array.length : x >= array.length ? x - array.length : x;
		int adjustedY = y < 0 ? y + array[0].length : y >= array[0].length ? y - array[0].length : y;
		return array[adjustedX][adjustedY];
	}
}
