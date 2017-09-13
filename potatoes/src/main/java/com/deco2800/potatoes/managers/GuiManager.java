package com.deco2800.potatoes.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.gui.Gui;
import com.deco2800.potatoes.gui.InteractiveShape;

public class GuiManager extends Manager {
    private List<Gui> gui;
    private List<InteractiveShape> shapeZones;	// used to detect collision on shapes
    private Stage stage;

    /**
     * Initialize the basic GuiManager. Just creates the internal gui storage
     */
    public GuiManager() {
        gui = new ArrayList<>();
    }

    /**
     * Set's the internal stage to operate on
     * @param stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Add's a gui element to be tracked by the GuiManager. Duplicates are not well supported
     * TODO better support for duplicates
     * @param g the gui element
     */
    public void addGui(Gui g) {
        gui.add(g);
    }

    /**
     * Get's the Gui of the given type, if it exists. Doesn't work for duplicates.
     * If the Gui doesn't exist in the manager, then this returns null.
     * @param type the type e.g. 'Type.class'
     * @return the gui element, or null
     */
    public Gui getGui(Class<?> type) {
        /* Find gui */
        for (Gui g : gui) {
            if (g.getClass() == type) {
                return g;
            }
        }

		/* If it doesn't exist, we return null. TODO log this? error? */

        return null;
    }

    /**
     * @return the internal stage variable
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Resizes the stage appropriately and also
     * call's resize(...) on all Gui's. Allowing them to appropriately adjust to the new window size.
     */
    public void resize(int width, int height) {
        if (stage != null) {
            stage.getViewport().update(width, height, true);

            for (Gui c : gui) {
                c.resize(stage);
            }
        }
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
	public void createTreeMenu(HashMap<AbstractEntity, Color> items, int x, int y, int radius) {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		
		ShapeRenderer shapeRenderer = new ShapeRenderer();
		shapeRenderer.begin(ShapeType.Filled);
		
		
		float a = 0.5f;
		int numSegments = items.entrySet().size();
		//System.out.println(numSegments);
		shapeRenderer.setColor(new Color(0,0,0,0.7f));
		shapeRenderer.circle(x, y, radius);
		Circle circle = new Circle(x,y,radius);
		InteractiveShape treeShopZone = new InteractiveShape() {

			@Override
			public boolean isDetected(float x, float y) {
				return circle.contains(x, y);
			};
			
			@Override
			public void render() {				
			}

			@Override
			public void onClick() {
				System.out.println("Click worked!");
				
			}
		};
		
		shapeZones.add(treeShopZone);
	
		int segment = 0;
		int degrees = 360 / numSegments;
		for (Map.Entry<AbstractEntity, Color> entry : items.entrySet()) {
			Color c = entry.getValue();
			shapeRenderer.setColor(new Color(c.r, c.g, c.b, a));
			int startAngle = 360 * (segment) / (numSegments);
			shapeRenderer.arc(x, y, (int) (radius*0.9), startAngle, degrees);
			
			segment++;
	
		}
		
		
		
		shapeRenderer.end();
		
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	public void checkShapes(float x, float y) {
		for (InteractiveShape shape: shapeZones) {
			if (shape.isDetected(x, y)) {
				shape.onClick();
			}
		}
	}
	
	
}
