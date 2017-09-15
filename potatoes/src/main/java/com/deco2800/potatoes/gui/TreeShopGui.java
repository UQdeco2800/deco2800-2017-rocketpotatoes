package com.deco2800.potatoes.gui;

import java.util.HashMap;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.trees.DamageTree;
import com.deco2800.potatoes.entities.trees.ProjectileTree;
import com.deco2800.potatoes.entities.trees.ResourceTree;



public class TreeShopGui extends Gui {
	private Circle shopShape;
	private int selectedSegment;
	private HashMap<? extends AbstractEntity, Color> items;
	
	public TreeShopGui(Stage stage){
		// Render menu
	}
	
	public void render() {
		HashMap<AbstractEntity, Color> items = new HashMap<AbstractEntity, Color>();
		items.put(new ProjectileTree(), Color.RED);
		items.put(new ResourceTree(), Color.BLUE);
		items.put(new DamageTree(), Color.YELLOW);
		createTreeMenu(items, 200, 500, 50);
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
	private void createTreeMenu(HashMap<? extends AbstractEntity, Color> items, int x, int y, int radius) {

		this.items = items;
		
		//Gdx.gl.glEnable(GL20.GL_BLEND);
		
		//ShapeRenderer shapeRenderer = new ShapeRenderer();
		//shapeRenderer.begin(ShapeType.Filled);
		
		//float a = 0.4f;
		//float selectedSegmentAlpha = 0.7f;
		float a = 0f;
		float selectedSegmentAlpha = 0f;
		int numSegments = items.entrySet().size();
		//shapeRenderer.setColor(new Color(0,0,0,selectedSegmentAlpha));
		//shapeRenderer.circle(x, y, radius);
		Circle circle = new Circle(x,y,radius);
		
		shopShape = circle;
	
		/*int segment = 0;
		int degrees = 360 / numSegments;
		// Draws each subsection of radial menu individually
		for (Map.Entry<? extends AbstractEntity, Color> entry : items.entrySet()) {
			Color c = entry.getValue();
			// Show which segment is highlighted by adjusting opacity
			float alpha = (segment==selectedSegment) ? selectedSegmentAlpha: a;
			// Set color and draw arc
			shapeRenderer.setColor(new Color(c.r, c.g, c.b, alpha));
			int startAngle = 360 * (segment) / (numSegments);
			shapeRenderer.arc(x, y, (int) (radius*0.9), startAngle, degrees);
			
			// Add entity texture image
			//System.out.println(entry.getKey().getTexture());
			
			segment++;
	
		}
		
		
		shapeRenderer.end();*/
		
		//Gdx.gl.glDisable(GL20.GL_BLEND);
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
	public void calculateSegment(float mx, float my) {
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
	
	private void moveListener() {
		
	}
	
	private void clickListener() {
		
	}
	
}
