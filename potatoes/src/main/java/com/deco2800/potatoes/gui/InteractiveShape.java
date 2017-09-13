package com.deco2800.potatoes.gui;


public interface InteractiveShape{

	/**
	 * Collision detection
	 */
	boolean isDetected(float x, float y);
	
	/**
	 * Renders
	 */
	void render();
	
	/**
	 * Function on mouse click. Override this function.
	 */
	void onClick();

}
