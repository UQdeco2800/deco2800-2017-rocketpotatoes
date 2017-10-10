package com.deco2800.potatoes.util;

import com.deco2800.potatoes.worlds.terrain.Terrain;

import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
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
					sampleStep(result, SQUARE_SAMPLES, x, y, size, (float) (rough * Math.random()));
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
			sampleStep(result, DIAMOND_SAMPLES, x, 0, 1, (float) (rough * Math.random()));
			sampleStep(result, DIAMOND_SAMPLES, x, result.length - 1, 1, (float) (rough * Math.random()));
		}
		for (int y = 0; y < result.length; y++) {
			sampleStep(result, DIAMOND_SAMPLES, 0, y, 1, (float) (rough * Math.random()));
			sampleStep(result, DIAMOND_SAMPLES, result.length - 1, y, 1, (float) (rough * Math.random()));
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
				result[x][y] = (float)Math.random();
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


	/********************************
	 -	 * FOR TESTING
	 -	 ********************************
	 -	 * Outputs and image from the algorithm output, try not to add the heightmap
	 -	 * file to git
	 -	 */
	public static void main(String[] args) throws IOException {
		final int SIZE = 100;
		BufferedImage image = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB);

		JFrame frame = new JFrame();

		InputVerifier verifer = new InputVerifier() {
			@Override
			public boolean verify(JComponent input) {
				return ((JTextField)input).getText().matches("\\d+(\\.\\d+)?");
			}
		};

		JPanel canvas = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(image, 100, 5,SIZE * 4, SIZE * 4, null);
			}
		};

		JPanel panel = new JPanel();

		JTextField roughnessText = new JTextField();
		roughnessText.setText("0.42");
		roughnessText.setInputVerifier(verifer);
		JTextField iterationsText = new JTextField();
		iterationsText.setText("2");
		iterationsText.setInputVerifier(verifer);
		JTextField heightText = new JTextField();
		heightText.setText("0.4");
		heightText.setInputVerifier(verifer);
		JTextField waterText = new JTextField();
		waterText.setText("0.7");
		waterText.setInputVerifier(verifer);
		JTextField heightDirtEdge = new JTextField();
		heightDirtEdge.setText("0.5");
		heightDirtEdge.setInputVerifier(verifer);
		JTextField waterDirtEdge = new JTextField();
		waterDirtEdge.setText("0.8");
		waterDirtEdge.setInputVerifier(verifer);
		JTextField grassText = new JTextField();
		grassText.setText("0.6");
		grassText.setInputVerifier(verifer);

		JButton button = new JButton();
		button.setText("Go!");
		button.addActionListener(e -> {
			getImage(SIZE, image, (float)Double.parseDouble(roughnessText.getText()), (int)Double.parseDouble
							(iterationsText.getText()),
					new double[] {Double.parseDouble(heightText.getText()), Double.parseDouble(waterText.getText()),
							Double.parseDouble(heightDirtEdge.getText()), Double.parseDouble(waterDirtEdge.getText()),
							Double.parseDouble(grassText.getText())});
			canvas.repaint();
		});


		panel.add(new JLabel("Roughness:"));
		panel.add(roughnessText);
		panel.add(new JLabel("Iterations (rounded to integer):"));
		panel.add(iterationsText);
		panel.add(new JLabel("Height:"));
		panel.add(heightText);
		panel.add(new JLabel("Water:"));
		panel.add(waterText);
		panel.add(new JLabel("Height Dirt Edge:"));
		panel.add(heightDirtEdge);
		panel.add(new JLabel("Water Dirt Edge:"));
		panel.add(waterDirtEdge);
		panel.add(new JLabel("Grass:"));
		panel.add(grassText);

		canvas.setPreferredSize(new Dimension(SIZE * 4 + 200, SIZE * 4 + 10));
		button.setPreferredSize(new Dimension(100, 50));
		panel.setLayout(new GridLayout(8,2));
		frame.setLayout(new BorderLayout());

		frame.add(canvas, BorderLayout.PAGE_START);

		JScrollPane scroller = new JScrollPane(panel);

		//scroller.add(panel);
		frame.add(scroller, BorderLayout.CENTER);
		frame.add(button, BorderLayout.PAGE_END);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setSize(1000, 1000);
	}

	private static void getImage(int SIZE, BufferedImage image, float roughness, int iterations, double[] vals) {
		float[][] height = smoothDiamondSquareAlgorithm(seedGrid(SIZE), roughness, iterations);
		float[][] water = smoothDiamondSquareAlgorithm(seedGrid(SIZE), 0, roughness, iterations);
		float[][] grass = smoothDiamondSquareAlgorithm(seedGrid(SIZE), roughness, iterations);
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				Color color = pickTerrain(height[i][j], water[i][j], grass[i][j], vals);
				image.setRGB(i, j, color.getRGB());
			}
		}
	}

	private static Color pickTerrain(float height, float water, float grass, double[] vals) {
		// Current terrain choosing
		Color spot;
		if (height < vals[0] || water < vals[1]) {
			spot = Color.BLUE;
		} else if (height < vals[2] || water < vals[3]) {
			spot = Color.GRAY;
		} else {
			spot = grass < vals[4] ? Color.GREEN : Color.GRAY;
		}
		return spot;
	}
}
