package com.deco2800.potatoes.gui;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.trees.DamageTree;
import com.deco2800.potatoes.entities.trees.ProjectileTree;
import com.deco2800.potatoes.entities.trees.ResourceTree;



public class TreeShopGui extends Gui {
	private Circle shopShape;
	private int selectedSegment;
	private HashMap<AbstractEntity, Color> items;
	private boolean mouseIn;
	private boolean initiated;
	private Stage stage;
	private int shopX;
	private int shopY;
	
	final private float UNSELECTED_ALPHA = 0.2f;
	final private float SELECTED_ALPHA = 0.5f;
	
	public TreeShopGui(Stage stage){
		// Render menu
		this.stage = stage;
		shopX = 300;
		shopY = 300;
		initiated = false;
		items  = new HashMap<AbstractEntity, Color>();
		items.put(new ProjectileTree(), Color.RED);
		items.put(new ResourceTree(), Color.BLUE);
		items.put(new DamageTree(), Color.YELLOW);
		
	}
	
	public void render() {
		createTreeMenu(shopX, shopY, 100);
		
	}

	/**
	 * Creates menu based on input parameters.
	 * 
	 * @param items
	 *            A HashMap with each AbstractEntity as the key and the
	 *            corresponding color as value
	 * @param x
	 *            Center x point
	 * @param y
	 *            Center y point
	 * @param radius
	 *            Radius of circle
	 */
	private void createTreeMenu(int x, int y, int radius) {
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		
		ShapeRenderer shapeRenderer = new ShapeRenderer();
		shapeRenderer.begin(ShapeType.Filled);
		
		int numSegments = items.entrySet().size();
		shapeRenderer.setColor(new Color(0,0,0,SELECTED_ALPHA));
		if (mouseIn) shapeRenderer.setColor(new Color(0,0,0,0.7f));
		if (!initiated) shapeRenderer.setColor(new Color(0,0,0,0f));

		Circle circle = new Circle(x,y,radius);
		float guiY = stage.getHeight()-y;
		shapeRenderer.circle(x, guiY, radius);
		
		shopShape = circle;
	
		int segment = 0;
		int degrees = 360 / numSegments;
		// Draws each subsection of radial menu individually
		for (Map.Entry<? extends AbstractEntity, Color> entry : items.entrySet()) {
			Color c = entry.getValue();
			// Show which segment is highlighted by adjusting opacity
			float alpha = (segment==selectedSegment) ? SELECTED_ALPHA: UNSELECTED_ALPHA;
			// Set color and draw arc
			shapeRenderer.setColor(new Color(c.r, c.g, c.b, alpha));
			int startAngle = 360 * (segment) / (numSegments);
			shapeRenderer.arc(x, guiY, (int) (radius*0.9), startAngle, degrees);
			
			// Add entity texture image
			
			
			segment++;
	
		}
		
		
		shapeRenderer.end();
		
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	
	/**
	 * Determines which segment of a circle the mouse is in. This starts counting
	 * from right hand side counter clockwise.
	 * 
	 * @param mx
	 *            mouse point x
	 * @param my
	 *            mouse point y
	 * @return integer corresponding to the segment number the mouse is within or -1
	 *         if mouse was not within bounds.
	 */
	private void calculateSegment(float mx, float my) {
		
		int n = 3;
		float x = shopShape.x;
		float y = shopShape.y;
		float r = shopShape.radius;
		double mouseAngle = Math.atan((my - y) / (mx - x));
		mouseAngle = mouseAngle*180/Math.PI;
		
		// Calculate actual angle with each quadrant
		if (my-y<0) mouseAngle += (mx-x<0) ? 180 : 360;
		else if (mx-x<0) mouseAngle+=180;
		mouseAngle = 360-mouseAngle; // make it anti clockwise
		
		double segmentAngle = 360 / n;
		int segment = (int) (mouseAngle/segmentAngle);
		this.selectedSegment = segment;

	}
	
	
	private void createSubMenus() {
		
	}
	
	public void checkMouseOver(int x, int y) {
		mouseIn = shopShape.contains(x, y) ? true : false;
		if (initiated)
			calculateSegment(x,y);

		System.out.println(initiated);
	}

	public void initShop(int x, int y) {
		moveLocation(x,y);
		if (initiated) {
			buyTree();
			initiated = false;
		} else {
			initiated = true;
		}
	}
	
	private void buyTree() {
		
	}
	
	private void moveLocation(int x, int y) {
		shopX = x;
		shopY = y;
	}
	
	
}
