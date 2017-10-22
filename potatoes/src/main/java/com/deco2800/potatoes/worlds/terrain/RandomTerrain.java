package com.deco2800.potatoes.worlds.terrain;

import com.deco2800.potatoes.managers.GameManager;

public class RandomTerrain extends Terrain {
	public static final RandomTerrain GRASS = new RandomTerrain(new String[]{"grass_tile_1", "grass_tile_2", "grass_tile_3"}, 1, true);
	public static final RandomTerrain WATER = new RandomTerrain(new String[]{"water1", "water2", "water3", "water4", "water5", "water6", "water7", "water8", "water9", "water10", "water11", "water12"}, 0, false);
	public static final RandomTerrain SNOW = new RandomTerrain(new String[]{"snow_tile_1", "snow_tile_2"}, 1, false);
	public static final RandomTerrain DIRT = new RandomTerrain(new String[]{"dirt_tile_1", "dirt_tile_2"}, 1, true);
	public static final RandomTerrain DIRT_NO_PLANT = new RandomTerrain(new String[]{"dirt_tile_1", "dirt_tile_2"}, 1, false);
	public static final RandomTerrain ROCK = new RandomTerrain(new String[]{"rock_tile_1", "rock_tile_2"}, 1, false);

	private final String[] textures;

	/**
	 * @param texture
	 *            the texture
	 * @param moveScale
	 *            the movement speed scale
	 * @param plantable
	 *            whether trees can be planted
	 */
	public RandomTerrain(String[] textures, float moveScale, boolean plantable) {
		super(textures[0], moveScale, plantable);
		this.textures = textures;
	}

	@Override
	public String getTexture() {
		return textures[GameManager.get().getRandom().nextInt(textures.length)];
	}
}
