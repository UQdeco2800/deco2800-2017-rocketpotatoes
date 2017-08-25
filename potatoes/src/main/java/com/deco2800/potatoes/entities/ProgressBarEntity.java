package com.deco2800.potatoes.entities;

import java.util.List;

import com.badlogic.gdx.graphics.Color;

public class ProgressBarEntity implements ProgressBar {
	// the textureID used for the entity
	protected String texture;
	// the colour palette used for the progress bar
	protected List<Color> colours;
	// the of the progress bar in relation to the location of the entity
	protected int height;

	ProgressBarEntity() {
		// empty because serialization
	}

	/**
	 * 
	 * @param texture
	 *            the textureID used for the entity
	 * @param colours
	 *            the colour palette used for the progress bar
	 * @param height
	 *            the of the progress bar in relation to the location of the entity
	 */
	ProgressBarEntity(String texture, List<Color> colours, int height) {
		this.texture = texture;
		this.colours = colours;
		this.height = height;

	}

	@Override
	public String getTexture() {
		return texture;
	}

	@Override
	public Color getColour(int currentHealth) {
		// based on percentage of health
		int ratio = 100 / (colours.size() - 1);
		return colours.get(currentHealth / ratio);
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public List<Color> getColours() {
		return colours;
	}

}
