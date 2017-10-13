package com.deco2800.potatoes.util;

import com.deco2800.potatoes.managers.GameManager;

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
		genericFloodFill(new Point(startX, startY), p -> gridCheck(p, grid, start), edges, filled);
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
		genericFloodFill(new Point(startX, startY), p -> gridCheck(p, grid, start), edges, filled);
		return edges;
	}

	private static boolean gridCheck(Point p, Object[][] grid, Object start) {
		return p.x >= 0 && p.x < grid.length && p.y >= 0 && p.y < grid[p.x].length && grid[p.x][p.y].equals(start);
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
			Point p = q.peek();

			for (int[] offset : DIAMOND_SAMPLES) {
				Point newPoint = new Point(p.x + offset[0], p.y + offset[1]);
				if (!filled.contains(newPoint)) {
					if (func.apply(newPoint)) {
						q.add(newPoint);
						filled.add(newPoint);
					} else {
						edges.add(p);
					}
				}
			}
			q.poll();
		}
	}

	/**
	 * Blends the 2 grid given together and returns the result. The grids must be
	 * the same size.
	 * 
	 * @param g1
	 * @param g2
	 * @return
	 */
	public static float[][] blend(float[][] g1, float[][] g2) {
		float[][] result = new float[g1.length][g1[0].length];
		for (int x = 0; x < g1.length; x++) {
			for (int y = 0; y < g1[x].length; y++) {
				// Just multiplicative blending, not that great but good enough
				result[x][y] = g1[x][y] * g2[x][y];
			}
		}
		normalize(result);
		return result;
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
					sampleStep(result, SQUARE_SAMPLES, x, y, size, (float) (rough * GameManager.get().getRandom().nextDouble()));
				}
			}
			for (int x = 0; x < result.length - size; x += size) {
				for (int y = 0; y < result[0].length - size; y += size) {
					// Square step
					sampleStep(result, DIAMOND_SAMPLES, x + size, y, size, (float) (rough * (GameManager.get().getRandom().nextDouble() * 2 - 1)));
					sampleStep(result, DIAMOND_SAMPLES, x, y + size, size, (float) (rough * (GameManager.get().getRandom().nextDouble() * 2 - 1)));
				}
			}
			// Reduce variance and size
			size = size / 2;
			rough *= roughScale;
		}
		// Hacky fix, reiterate over edges
		for (int x = 0; x < result.length; x++) {
			sampleStep(result, DIAMOND_SAMPLES, x, 0, 1, (float) (rough * GameManager.get().getRandom().nextDouble()));
			sampleStep(result, DIAMOND_SAMPLES, x, result.length - 1, 1, (float) (rough * GameManager.get().getRandom().nextDouble()));
		}
		for (int y = 0; y < result.length; y++) {
			sampleStep(result, DIAMOND_SAMPLES, 0, y, 1, (float) (rough * GameManager.get().getRandom().nextDouble()));
			sampleStep(result, DIAMOND_SAMPLES, result.length - 1, y, 1, (float) (rough * GameManager.get().getRandom().nextDouble()));
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
	public static float[][] smoothDiamondSquareAlgorithm(float[][] grid, float roughness, int iterations) {
		// Normalize and diamond square repeatedly, with the output the seed for the
		// next iteration
		for (int i = 0; i < iterations; i++) {
			normalize(diamondSquareAlgorithm(grid, grid.length, roughness, roughness));
		}
		// For some reason the result sometimes isn't normalized
		normalize(grid);
		return grid;
	}

	/**
	 * The edge parameter determines the edge of the grid. The grid will be less
	 * noisy than without an edge, due to the way seeding works.
	 * 
	 * @see smoothDiamondSquareAlgorithm(grid, roughness, iterations)
	 */
	public static float[][] smoothDiamondSquareAlgorithm(float[][] grid, float edge, float roughness, int iterations) {
		// Normalize and diamond square repeatedly, with the output the seed for the
		// next iteration
		for (int i = 0; i < iterations; i++) {
			setEdges(grid, edge);
			normalize(diamondSquareAlgorithm(grid, grid.length, roughness, roughness));
		}
		// For some reason the result sometimes isn't normalized
		normalize(grid);
		setEdges(grid, edge);
		return grid;
	}

	public static float[][] seedGrid(int size) {
		float[][] result = new float[size][size];
		for (int x = 0; x < result.length; x++) {
			for (int y = 0; y < result[x].length; y++) {
				result[x][y] = (float) GameManager.get().getRandom().nextDouble();
			}
		}
		return result;
	}

	private static void setEdges(float[][] grid, float edge) {
		for (int x = 0; x < grid.length; x++) {
			grid[x][0] = edge;
			grid[x][grid.length - 1] = edge;
		}
		for (int y = 0; y < grid[0].length; y++) {
			grid[0][y] = edge;
			grid[grid.length - 1][y] = edge;
		}
	}

	private static void sampleStep(float[][] array, int[][] sampleType, int x, int y, int size, float random) {
		float sum = 0;
		int count = 0;
		for (int[] is : sampleType) {
			// Sample using the given pattern
			int sampleX = x + is[0] * size;
			int sampleY = y + is[1] * size;
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
