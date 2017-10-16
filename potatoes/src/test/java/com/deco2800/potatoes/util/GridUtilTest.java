package com.deco2800.potatoes.util;

import org.junit.Test;

import java.awt.*;
import java.util.Set;

import static org.junit.Assert.*;

public class GridUtilTest {
	@Test
	public void testFloodFill() {
		Integer[][] grid = { { 1, 2, 1, 1, 0 }, { 0, 0, 0, 0, 0 }, { 0, 0, 1, 1, 0 }, { 1, 1, 0, 1, 0 },
				{ 0, 2, 0, 0, 0 } };
		Set<Point> result = GridUtil.floodFill(grid, 1, 1);
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid.length; y++) {
				// All 0s except for corner
				if (grid[x][y] == 0 && !(x == 4 && y == 0)) {
					assertTrue("Expected point x:" + x + " y:" + y + " wasn't in result",
							result.contains(new Point(x, y)));
				}
			}
		}
	}

	@Test
	public void testFloodEdges() {
		Integer[][] grid = { { 1, 2, 2, 1, 0 }, { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 }, { 1, 0, 0, 0, 0 },
				{ 0, 2, 0, 0, 0 } };
		int[][] expected = { { 0, 4 }, { 1, 0 }, { 1, 1 }, { 1, 2 }, { 1, 3 }, { 1, 4 }, { 2, 0 }, { 2, 4 }, { 3, 1 },
				{ 3, 4 }, { 4, 2 }, { 4, 3 }, { 4, 4 } };
		Set<Point> result = GridUtil.floodEdges(grid, 1, 1);
		assertEquals("Incorrect amount of edge points", expected.length, result.size());
		for (int[] point : expected) {
			assertTrue("Point not in result x:" + point[0] + " y:" + point[1],
					result.contains(new Point(point[0], point[1])));
		}
	}

	@Test
	public void testBlend() {
		float[][] grid1 = { { 1, 1, 1 }, { 2, 2, 2 }, { 3, 3, 3 } };
		float[][] grid2 = { { 0.5f, 0.1f, 1 }, { 0.5f, 0.1f, 1 }, { 0.5f, 0.1f, 1 } };
		float[][] blended = GridUtil.blend(grid1, grid2);
		assertEquals("Size was not correct", grid1.length, blended.length);
		assertEquals("Size was not correct", grid1[0].length, blended[0].length);
	}

	@Test
	public void testNormalize() {
		float[][] grid = { { 1, 2, 1 }, { 0, 0, 0 }, { 2, 2, 1 } };
		float[][] result = { { 0.5f, 1, 0.5f }, { 0, 0, 0 }, { 1, 1, 0.5f } };
		GridUtil.normalize(grid);
		for (int x = 0; x < result.length; x++) {
			assertArrayEquals("Row " + x + " was not normalized", result[x], grid[x], 0.00001f);
		}
	}

	@Test
	public void testSmoothDiamondSquareAlgorithm() {
		// Really just testing it terminates without errors
		float[][] result = GridUtil.smoothDiamondSquareAlgorithm(GridUtil.seedGrid(10), 0.6f, 3);
		assertEquals("X size was not correct", 10, result.length);
		assertEquals("Y size was not correct", 10, result[0].length);
	}
}
