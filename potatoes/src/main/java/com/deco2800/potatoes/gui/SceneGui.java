package com.deco2800.potatoes.gui;

import com.badlogic.gdx.math.Vector2;

/**
 * Gui entities that act like Gui's but move along the screen like an abstract
 * entity. These are only visible by the current player.
 * 
 * @author Dion
 *
 */
public interface SceneGui {
	
	/**
	 * Gets the tile coordinates where the render revolves around
	 */
	Vector2 getTileCoords();
	
	/**
	 * Renders the scene gui
	 */
	void render();
	
}
