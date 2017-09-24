package com.deco2800.potatoes.worlds.terrain;

/**
 * Class representing a type of terrain
 */
public class Terrain {
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
		result = prime * result + ((texture == null) ? 0 : texture.hashCode());
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
		if (texture == null) {
			if (other.texture != null)
				return false;
		} else if (!texture.equals(other.texture))
			return false;
		return true;
	}
}
