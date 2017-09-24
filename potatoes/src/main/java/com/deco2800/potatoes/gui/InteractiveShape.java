package com.deco2800.potatoes.gui;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

/**
 * An element on screen that the player is able to click on.
 * 
 * @author Dion Lao
 *
 */
public class InteractiveShape implements Shape2D{
	
	private Shape2D shape;
	
	public InteractiveShape (Shape2D shape) {
		this.shape = shape;
	}
	
	/**
	 * Renders
	 */
	void render() {
		//Blank comment for Sonar
	};
	
	/**
	 * Function on mouse click. Override this function.
	 */
	void onClick() {
		System.out.println("Got a "+this.getClass().toString());
	};

	@Override
	public boolean contains(Vector2 point) {
		if (this.shape.getClass().equals(Circle.class)) {
			return ((Circle) this.shape).contains(point);
		} else if (this.shape.getClass().equals(Rectangle.class)) {
			return ((Rectangle) this.shape).contains(point);
		}
		return false;
	}

	@Override
	public boolean contains(float x, float y) {
		if (this.shape.getClass().equals(Circle.class)) {
			return ((Circle) this.shape).contains(x,y);
		} else if (this.shape.getClass().equals(Rectangle.class)) {
			return ((Rectangle) this.shape).contains(x,y);
		}
		return false;
	}


}
