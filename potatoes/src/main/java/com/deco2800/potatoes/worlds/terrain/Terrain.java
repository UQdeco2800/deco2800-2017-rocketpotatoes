package com.deco2800.potatoes.worlds.terrain;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.TextureManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Class representing a type of terrain
 */
public class Terrain {
	public static final Terrain[] WATER_ARRAY = {new Terrain("water1", 0, false), new Terrain
			("water2", 0, false), new Terrain("water3", 0, false), new Terrain("water4", 0, false), new Terrain("water5", 0, false), new Terrain("water6", 0, false), new Terrain("water7", 0, false), new Terrain("water8", 0, false), new Terrain("water9", 0, false), new Terrain("water10", 0, false), new Terrain("water11", 0, false), new Terrain("water12", 0, false)};

	private final String texture;
	private final float moveScale;
	private final boolean plantable;

	/**
	 * @param texture
	 *            the texture
	 * @param moveScale
	 *            the movement speed scale
	 * @param plantable
	 *            whether trees can be planted
	 */
	public Terrain(String texture, float moveScale, boolean plantable) {
		this.texture = texture;
		this.moveScale = moveScale;
		this.plantable = plantable;
	}

	/**
	 * Adds a cell associated with this terrain to the given map
	 */
	public void putCell(Map<String, Cell> m) {
		m.put(getTexture(), new Cell().setTile(new StaticTiledMapTile(GameManager.get().getManager(TextureManager.class)
				.getTextureRegion(getTexture()))));
	}

	/**
	 * @return the texture
	 */
	public String getTexture() {
		return texture;
	}

	/**
	 * @return the move scale
	 */
	public float getMoveScale() {
		return moveScale;
	}

	/**
	 * @return if the terrain can have trees
	 */
	public boolean isPlantable() {
		return plantable;
	}

	/*
	 * Auto generated, no need to manually test. Created from fields: texture,
	 * moveScale, plantable
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(moveScale);
		result = prime * result + (plantable ? 1231 : 1237);
		result = prime * result + (getTexture() == null ? 0 : getTexture().hashCode());
		return result;
	}

	/*
	 * Auto generated, no need to manually test. Created from fields: texture,
	 * moveScale, plantable
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Terrain other = (Terrain) obj;
		if (Float.floatToIntBits(moveScale) != Float.floatToIntBits(other.moveScale))
			return false;
		if (plantable != other.plantable)
			return false;
		if (getTexture() == null) {
			if (other.getTexture() != null)
				return false;
		} else if (!getTexture().equals(other.getTexture()))
			return false;
		return true;
	}
}
