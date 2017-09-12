package com.deco2800.potatoes.worlds;

/**
 * Utility class for randomly generating worlds. Will probably be moved somewhere at some point.
 */
public class RandomWorldGeneration {
	
	private RandomWorldGeneration() {
		// Never should be called
	}

	private static final int[][] DIAMOND_SAMPLES = { { 0, -1 }, { -1, 0 }, { 0, 1 }, { 1, 0 } };
	private static final int[][] SQUARE_SAMPLES = { { 1, 1 }, { -1, 1 }, { 1, -1 }, { -1, -1 } };

	/**
	 * Diamond square algorithm for generating noise Definitely has a bug at the
	 * moment
	 */
	public static float[][] diamondSquareAlgorithm(float[][] result, int gridSize, float roughness, float roughScale) {
		int size = gridSize / 2;
		float rough = roughness;
		while (size > 0) {
			for (int x = size; x < result.length; x += size) {
				for (int y = size; y < result[0].length; y += size) {
					sampleStep(result, SQUARE_SAMPLES, x, y, size, (float) (rough * (Math.random())));
				}
			}
			boolean offset = true;
			for (int x = 0; x < result.length - size; x += size) {
				for (int y = 0; y < result[0].length - size; y += size) {
					sampleStep(result, DIAMOND_SAMPLES, x + size, y, size, (float) (rough * (Math.random() * 2 - 1)));
					sampleStep(result, DIAMOND_SAMPLES, x, y + size, size, (float) (rough * (Math.random() * 2 - 1)));
				}
				offset = !offset;
			}
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

	public static float[][] smoothDiamondSquareAlgorithm(int size, float roughness,
			int iterations) {
		float[][] result = new float[size][size];
		for (int x = 0; x < result.length; x++) {
			for (int y = 0; y < result[x].length; y++) {
				result[x][y] = 0.5f;
			}
		}
		for (int i = 0; i < iterations; i++) {
			normalize(diamondSquareAlgorithm(result, size, roughness, roughness));
		}
		normalize(result);
		return result;
	}

	public static float[][] normalize(float[][] array) {
		float min = Float.MAX_VALUE;
		float max = Float.MIN_VALUE;
		for (int x = 0; x < array.length; x++) {
			for (int y = 0; y < array[x].length; y++) {
				min = Math.min(min, array[x][y]);
				max = Math.max(max, array[x][y]);
			}
		}
		for (int x = 0; x < array.length; x++) {
			for (int y = 0; y < array[x].length; y++) {
				array[x][y] = (array[x][y] - min) / (max - min);
			}
		}
		return array;
	}

	private static void sampleStep(float[][] array, int[][] sampleType, float x, float y, int size, float random) {
		float sum = 0;
		int count = 0;
		for (int[] is : sampleType) {
			int sampleX = (int) (Math.floor(x) + is[0] * size);
			int sampleY = (int) (Math.floor(y) + is[1] * size);
			count++;
			sum += get(array, sampleX, sampleY);
		}
		array[(int) Math.floor(x)][(int) Math.floor(y)] = random + sum / count;
	}

	private static float get(float[][] array, int x, int y) {
		// Wraps around
		int adjustedX = x < 0 ? x + array.length : x >= array.length ? x - array.length : x;
		int adjustedY = y < 0 ? y + array[0].length : y >= array[0].length ? y - array[0].length : y;
		return array[adjustedX][adjustedY];
	}
}
