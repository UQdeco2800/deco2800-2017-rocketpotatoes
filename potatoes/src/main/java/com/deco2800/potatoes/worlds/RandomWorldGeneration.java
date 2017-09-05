package com.deco2800.potatoes.worlds;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RandomWorldGeneration {

	private float[][] heightMap;
	// List of properties used to choose between different tiles
	private float[][][] tilePropertyMaps;

	public RandomWorldGeneration(int width, int length) {
		heightMap = new float[width][length];
		tilePropertyMaps = new float[width][length][3];
	}

	/********************************
	 * FOR TESTING
	 ********************************
	 * Outputs and image from the algorithm output, try not to add the heightmap
	 * file to git
	 */
	public static void main(String[] args) throws IOException {
		final int SIZE = 25;
		BufferedImage image = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB);
		float[][] heightmap = smoothDiamondSquareAlgorithm(SIZE, 0.4f, 2);
		for (int i = 0; i < heightmap.length; i++) {
			for (int j = 0; j < heightmap[i].length; j++) {
				Color color = new Color(heightmap[i][j], heightmap[i][j], heightmap[i][j]);
				image.setRGB(i, j, color.getRGB());
			}
		}
		ImageIO.write(image, "png", new File("random_image.png"));
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
				if (array[x][y] > 1 || array[x][y] < 0) {
					System.out.println(min + ":" + max);
				}
			}
		}
		return array;
	}

	private static void sampleStep(float[][] array, int[][] sampleType, float x, float y, float size, float random) {
		float sum = 0;
		int count = 0;
		for (int[] is : sampleType) {
			int sampleX = (int) (Math.floor(x) + is[0] * Math.floor(size));
			int sampleY = (int) (Math.floor(y) + is[1] * Math.floor(size));
			count++;
			sum += get(array, sampleX, sampleY);
		}
		array[(int) Math.floor(x)][(int) Math.floor(y)] = random + sum / count;
	}

	private static float get(float[][] array, int x, int y) {
		int adjustedX = x < 0 ? x += array.length : x >= array.length ? x -= array.length : x;
		int adjustedY = y < 0 ? y += array[0].length : y >= array[0].length ? y -= array[0].length : y;
		return array[adjustedX][adjustedY];
	}

	private static boolean isValidCoords(float[][] array, int x, int y) {
		return x >= 0 && y >= 0 && x < array.length && y < array[0].length;
	}
}
