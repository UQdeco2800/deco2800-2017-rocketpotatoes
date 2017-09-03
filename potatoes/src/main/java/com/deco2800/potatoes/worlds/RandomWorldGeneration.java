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
	 * Outputs and image from the algorithm output, try not to add the heightmap file to git
	 */
	public static void main(String[] args) throws IOException {
		final int SIZE = 201;
		BufferedImage image = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB);
		float[][] heightmap = new float[SIZE][SIZE];
		for (int i = 0; i < 100; i++) {
			heightmap = diamondSquareAlgorithm(heightmap, SIZE, 0.8f);
		}

		for (int i = 0; i < heightmap.length; i++) {
			for (int j = 0; j < heightmap[i].length; j++) {
				Color color = new Color(heightmap[i][j], heightmap[i][j], heightmap[i][j]);
				image.setRGB(i, j, color.getRGB());
			}
		}
		ImageIO.write(image, "png", new File("random_image.png"));
	}

	private static final int[][] SQUARE_SAMPLES = { { 0, -1 }, { -1, 0 }, { 0, 1 }, { 1, 0 } };
	private static final int[][] DIAMOND_SAMPLES = { { 1, 1 }, { -1, 1 }, { 1, -1 }, { -1, -1 } };

	/**
	 * Diamond square algorithm for generating noise
	 * Definitely has a bug at the moment
	 */
	public static float[][] diamondSquareAlgorithm(float[][] result, int gridSize, float roughness) {
		int size = gridSize % 2 == 0 ? gridSize + 1 : gridSize;
		size /= 2;
		float rough = roughness;
		Set<int[]> nodes = new HashSet<>();
		nodes.add(new int[] { size, size });
		while (size > 0) {
			Set<int[]> newNodes = new HashSet<>();
			for (int[] coords : nodes) {
				// Diamond step
				sampleStep(result, SQUARE_SAMPLES, coords[0], coords[1], size, rough * Math.random());
				for (int j = 0; j < 4; j++) {
					// Square step
					sampleStep(result, DIAMOND_SAMPLES, coords[0] + SQUARE_SAMPLES[j][0] * size,
							coords[1] + SQUARE_SAMPLES[j][1] * size, size, rough * Math.random());
					// Nodes for diamond step
					newNodes.add(new int[] { coords[0] + DIAMOND_SAMPLES[j][0] * size / 2,
							coords[1] + DIAMOND_SAMPLES[j][1] * size / 2 });
				}
			}
			nodes = newNodes;
			size /= 2;
			//rough *= roughness;
		}
		return result;
	}

	private static void sampleStep(float[][] array, int[][] sampleType, int x, int y, int size, double random) {
		float sum = 0;
		for (int[] is : sampleType) {
			sum += get(array, x + is[0] * size, y + is[1] * size);
		}
		float newValue = (float) (random * 2) - 1f + sum / sampleType.length;
		// Make sure its 0.0 to 1.0
		array[x][y] = newValue > 1 ? 1f : newValue < 0 ? 0f : newValue;

	}

	private static float get(float[][] array, int x, int y) {
		return x >= 0 && y >= 0 && x < array.length && y < array[0].length ? array[x][y] : 0;
	}
}
