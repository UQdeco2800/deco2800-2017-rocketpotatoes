package com.deco2800.potatoes.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * 
 * Created by fff134 on 23/08/17.
 *
 */
public interface ProgressBar {

	/**
	 * Renders the progress bar
	 * 
	 * @param entity
	 *            The entity
	 * @param progressBar
	 *            The texture of the progress bar
	 * @param batch
	 *            The sprite batch used for rendering
	 * @param xLength
	 *            The x-axis of where the progress bar will be rendered
	 * @param yLength
	 *            The y-axis of where the progress bar will be rendered
	 */
	void setProgressBar(AbstractEntity entity, Texture progressBar, SpriteBatch batch, int xLength, int yLength);

}
