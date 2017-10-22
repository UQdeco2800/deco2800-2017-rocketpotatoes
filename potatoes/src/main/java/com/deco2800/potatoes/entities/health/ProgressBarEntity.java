package com.deco2800.potatoes.entities.health;

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
	// the textureID used for the progress bar
	protected String texture = "progress_bar";
	// the colour palette used for the progress bar
	protected List<Color> colours = Arrays.asList(Color.RED, Color.valueOf("fff134"), Color.GREEN);
	// the height of the progress bar relative to the entity's height
	protected int height = 0;
	// the scale of the progress bar in relation to its entity
	protected float widthScale = 1;
	// Used for the player layout texture.
	protected String layoutTexture = null;

	public ProgressBarEntity() {
		// empty because serialization
	}

	/**
	 * Default Constructor for solid colour bars
	 * 
	 * @param colours
	 *            the colour palette used for the progress bar
	 */
	public ProgressBarEntity(List<Color> colours) {
		this.colours = colours;
	}

	/**
	 * Default Constructor for custom textures.
	 * 
	 * @param texture
	 *            the textureID used for the entity
	 * @param widthScale
	 *            the scale of the progress bar in relation to its entity
	 */
	public ProgressBarEntity(String texture, float widthScale) {
		this.texture = texture;
		this.widthScale = widthScale;
		this.colours = Arrays.asList(Color.WHITE);
	}
	
	public ProgressBarEntity(String texture, String layoutTexture, float widthScale) {
		this.texture = texture;
		this.layoutTexture = layoutTexture;
		this.widthScale = widthScale;
		this.colours = Arrays.asList(Color.WHITE);
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

	@Override
	public String getLayoutTexture() {
		return layoutTexture;
	}

}
