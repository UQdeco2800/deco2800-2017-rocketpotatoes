package com.deco2800.potatoes.worlds;

/**
 * Utility class for randomly generating worlds. Will probably be moved
 * somewhere at some point.
 */
public class RandomWorldGeneration {

	private RandomWorldGeneration() {
		// Never should be called
	}

	private static final int[][] DIAMOND_SAMPLES = { { 0, -1 }, { -1, 0 }, { 0, 1 }, { 1, 0 } };
	private static final int[][] SQUARE_SAMPLES = { { 1, 1 }, { -1, 1 }, { 1, -1 }, { -1, -1 } };

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
