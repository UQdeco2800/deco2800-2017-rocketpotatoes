package com.deco2800.potatoes.entities.health;

import java.util.List;

import com.badlogic.gdx.graphics.Color;

/**
 * An interface to handle the progress bar. WARNING will conflict with
 * ProgressBar from com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
 * 
 * Created by fff134 on 23/08/17.
 *
 */
public interface ProgressBar {
	/**
	 * 
	 * @return a string of the textureID
	 */
	public String getTexture();

	/**
	 * 
	 * @param currentHealth
	 * @return the colour based on the current health of the entity
	 */
	public Color getColour(float progressRatio);

	/**
	 * 
	 * @return A list of colours used for the progress bar
	 */
	public List<Color> getColours();

	/**
	 * 
	 * @return the height of the progress bar in relation to the location of the
	 *         entity
	 */
	public int getHeight();

	/**
	 * 
	 * @return the scale of the progress bar in relation to its entity
	 */
	public float getWidthScale();
	
	/**
	 * 
	 * @return The layout texture.
	 */
	public String getLayoutTexture();


}
