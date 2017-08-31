package com.deco2800.potatoes.entities;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.graphics.Color;

/**
 * 
 * Created by fff134 on 25/08/17.
 *
 */
public class ProgressBarEntity implements ProgressBar {
	// the textureID used for the entity
	protected String texture;
	// the colour palette used for the progress bar
	protected List<Color> colours;
	// the height of the progress bar in relation to the location of the entity
	protected int height;
	// the scale of the progress bar in relation to its entity
	protected float widthScale;

	public ProgressBarEntity() {
		// empty because serialization
	}

	/**
	 * 
	 * @param texture
	 *            the textureID used for the entity
	 * @param height
	 *            the of the progress bar in relation to the location of the entity
	 * @param widthScale
	 *            the scale of the progress bar in relation to its entity
	 */
	public ProgressBarEntity(String texture, int height, float widthScale) {
		List<Color> colours = Arrays.asList(Color.RED, Color.valueOf("fff134"), Color.GREEN);
		this.texture = texture;
		this.colours = colours;
		this.height = height;
		this.widthScale = widthScale;
	}

	/**
	 * 
	 * @param colours
	 *            the colour palette used for the progress bar
	 * @param height
	 *            the of the progress bar in relation to the location of the entity
	 * @param widthScale
	 *            the scale of the progress bar in relation to its entity
	 */
	public ProgressBarEntity(List<Color> colours, int height, float widthScale) {
		this.texture = "progress_bar";
		this.colours = colours;
		this.height = height;
		this.widthScale = widthScale;
	}

	/**
	 * 
	 * @param texture
	 *            the textureID used for the entity
	 * @param colours
	 *            the colour palette used for the progress bar
	 * @param height
	 *            the of the progress bar in relation to the location of the entity
	 * @param widthScale
	 *            the scale of the progress bar in relation to its entity
	 */
	public ProgressBarEntity(String texture, List<Color> colours, int height, float widthScale) {
		this.texture = texture;
		this.colours = colours;
		this.height = height;
		this.widthScale = widthScale;
	}

	@Override
	public String getTexture() {
		return texture;
	}

	@Override
	public Color getColour(float progressRatio) {
		return colours.get(Math.max(0, (int) Math.ceil(progressRatio * colours.size() - 1)));
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public List<Color> getColours() {
		return new ArrayList<Color>(colours);
	}

	@Override
	public float getWidthScale() {
		return widthScale;
	}

}
