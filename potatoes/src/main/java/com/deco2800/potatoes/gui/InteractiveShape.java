package com.deco2800.potatoes.gui;

import org.w3c.dom.css.Rect;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

public class InteractiveShape implements Shape2D{
	
	private Shape2D shape;
	
	public InteractiveShape (Shape2D shape) {
		this.shape = shape;
	}
	
	/**
	 * Renders
	 */
	void render() {
		
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
