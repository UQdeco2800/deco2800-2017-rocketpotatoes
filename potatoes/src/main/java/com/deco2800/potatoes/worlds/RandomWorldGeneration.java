package com.deco2800.potatoes.worlds;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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
		final int SIZE = 512;
		BufferedImage image = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB);
		float[][] heightmap = new float[SIZE][SIZE];
		for (int x = 0; x < heightmap.length; x++) {
			for (int y = 0; y < heightmap[x].length; y++) {
				heightmap[x][y] = 0.5f;
			}
		}
		for (int i = 0; i < 1; i++) {
			heightmap = diamondSquareAlgorithm(heightmap, SIZE, 0.4f, 0.9f);
		}

		for (int i = 0; i < heightmap.length; i++) {
			for (int j = 0; j < heightmap[i].length; j++) {
				Color color = new Color(0, heightmap[i][j] / 2, 0);
				image.setRGB(i, j, color.getRGB());
			}
		}
		ImageIO.write(image, "png", new File("random_image.png"));
	}

	private static final int[][] SQUARE_SAMPLES = { { 0, -1 }, { -1, 0 }, { 0, 1 }, { 1, 0 } };
	private static final int[][] DIAMOND_SAMPLES = { { 1, 1 }, { -1, 1 }, { 1, -1 }, { -1, -1 } };

	/**
	 * Diamond square algorithm for generating noise Definitely has a bug at the
	 * moment
	 */
	public static float[][] diamondSquareAlgorithm(float[][] result, int gridSize, float roughness, float roughScale) {
		float size = gridSize % 2 == 0 ? gridSize + 1 : gridSize;
		size /= 2;
		float rough = roughness;
		while (Math.round(size) > 0) {
			for (float x = 0; x < result.length; x += size) {
				for (float y = 0; y < result[0].length; y += size) {
					sampleStep(result, SQUARE_SAMPLES, x, y, size, (float) (rough * (Math.random() * 2 - 1)));
				}
			}
			float half = size / 2;
			for (float x = half; x < result.length; x += half) {
				for (float y = half; y < result[0].length; y += half) {
					sampleStep(result, DIAMOND_SAMPLES, x, y, half, (float) (rough * (Math.random() * 2 - 1)));
				}
			}
			size = half;
			rough *= roughScale;
		}
		return result;
	}

	private static void sampleStep(float[][] array, int[][] sampleType, float x, float y, float size, float random) {
		if (isValidCoords(array, (int) Math.floor(x), (int) Math.floor(y))) {
			float sum = 0;
			int count = 0;
			for (int[] is : sampleType) {
				int sampleX = (int) Math.floor(x + is[0] * size);
				int sampleY = (int) Math.floor(y + is[1] * size);
				if (isValidCoords(array, sampleX, sampleY)) {
					count++;
					sum += array[sampleX][sampleY];
				}
			}
			
			float newValue = random + sum / count;
			// Make sure its 0.0 to 1.0
			array[(int) Math.floor(x)][(int) Math.floor(y)] = newValue > 1 ? 1f : newValue < 0 ? 0f : newValue;
		}
	}

	private static boolean isValidCoords(float[][] array, int x, int y) {
		return x >= 0 && y >= 0 && x < array.length && y < array[0].length;
	}
}
