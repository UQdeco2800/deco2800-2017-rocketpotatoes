package com.deco2800.potatoes.entities;

import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.renderering.Renderable;
import com.deco2800.potatoes.util.Box3D;
import com.deco2800.potatoes.worlds.AbstractWorld;

/**
 * A AbstractEntity is an item that can exist in both 3D and 2D worlds
 * AbstractEntities are rendered by Render2D and Render3D An item that does not
 * need to be rendered should not be a WorldEntity
 */
public abstract class AbstractEntity implements Renderable, Comparable<AbstractEntity> {

	private Box3D position;

	private float xRenderLength;

	private float yRenderLength;

	private boolean centered;

	private String texture = "error_box";

	public AbstractEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength) {
		this(posX, posY, posZ, xLength, yLength, zLength, xLength, yLength, false);
	}



	public AbstractEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
						  float xRenderLength, float yRenderLength, boolean centered) {
		this.xRenderLength = xRenderLength;
		this.yRenderLength = yRenderLength;
		this.centered = centered;

		if (centered) {
			posX += (1-xLength/2);
			posY += (1-yLength/2);
		}
		this.position = new Box3D(posX, posY, posZ, xLength, yLength, zLength);
	}

	public AbstractEntity(Box3D position, float xRenderLength, float yRenderLength, boolean centered) {
		this.position = new Box3D(position);
		this.xRenderLength = xRenderLength;
		this.yRenderLength = yRenderLength;
		this.centered = centered;
	}

	/**
	 * Get the X position of this AbstractWorld Entity
	 * 
	 * @return The X position
	 */
	public float getPosX() {
		float x = position.getX();
		if (this.centered) {
			x -= (1 - this.position.getYLength() / 2);
		}
		return x;
	}

	/**
	 * Get the Y position of this AbstractWorld Entity
	 * 
	 * @return The Y position
	 */
	public float getPosY() {
		float y = position.getY();
		if (this.centered) {
			y -= (1 - this.position.getYLength() / 2);
		}
		return y;
	}

	/**
	 * Get the Z position of this AbstractWorld Entity
	 * 
	 * @return The Z position
	 */
	public float getPosZ() {
		return position.getZ();
	}


	public void setPosition(float x, float y, float z) {
		if (this.centered) {
			y += (1 - this.position.getYLength() / 2);
			x += (1 - this.position.getXLength() / 2);
		}
		this.position.setX(x);
		this.position.setY(y);
		this.position.setZ(z);
	}

	public void setPosX(float x) {
		if (this.centered) {
			x += (1-this.position.getXLength() / 2);
		}
		this.position.setX(x);
	}

	public void setPosY(float y) {
		if (this.centered) {
			y += (1 - this.position.getYLength() / 2);
		}
		this.position.setY(y);
	}

	public void setPosZ(float z) {
		this.position.setZ(z);
	}

	/**
	 * Get the height value of this item. In 3D worlds this is the stack index. In
	 * 2D worlds this is the height of an object
	 * 
	 * @return height
	 */
	public float getZLength() {
		return position.getZLength();
	}

	public float getXLength() {
		return position.getXLength();
	}

	public float getYLength() {
		return position.getYLength();
	}

	public boolean collidesWith(AbstractEntity entity) {
		return this.position.overlaps(entity.position);
	}

	@Override
	public float getXRenderLength() {
		return this.xRenderLength;
	}

	@Override
	public float getYRenderLength() {
		return this.yRenderLength;
	}

	/**
	 * Returns a Box3D representing the location.
	 * 
	 * @return Returns a Box3D representing the location.
	 */
	public Box3D getBox3D() {
		return new Box3D(position);
	}

	/**
	 * Gives the string for the texture of this entity. This does not mean the
	 * texture is currently registered
	 * 
	 * @return texture string
	 */
	public String getTexture() {
		return texture;
	}

	/**
	 * Sets the texture string for this entity. Check the texture is registered with
	 * the TextureRegister
	 * 
	 * @param texture
	 *            String texture id
	 */
	public void setTexture(String texture) {
		this.texture = texture;
	}

	/**
	 * Allows sorting of WorldEntities for Isometric rendering
	 * 
	 * @param o
	 * @return
	 */
	@Override
	public int compareTo(AbstractEntity o) {
		float cartX = this.position.getX();
		float cartY = this.getParent().getLength() - this.position.getY();

		float isoX = ((cartX - cartY) / 2.0f);
		float isoY = ((cartX + cartY) / 2.0f);

		float cartX_o = o.getPosX();
		float cartY_o = o.getParent().getLength() - o.getPosY();

		float isoX_o = ((cartX_o - cartY_o) / 2.0f);
		float isoY_o = ((cartX_o + cartY_o) / 2.0f);

		if (isoY == isoY_o) {
			if (isoX < isoX_o) {
				return 1;
			} else if (isoX > isoX_o) {
				return -1;
			} else {
				return 0;
			}
		} else if (isoY < isoY_o) {
			return 1;
		} else {
			return -1;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		AbstractEntity that = (AbstractEntity) o;

		if (position != null ? !position.equals(that.position) : that.position != null)
			return false;
		return texture != null ? texture.equals(that.texture) : that.texture == null;
	}

	@Override
	public int hashCode() {
		int result = position != null ? position.hashCode() : 0;
		result = 31 * result + (texture != null ? texture.hashCode() : 0);
		return result;
	}

	@Deprecated
	public AbstractWorld getParent() {
		return GameManager.get().getWorld();
	}

	public float distance(AbstractEntity e) {
		return this.getBox3D().distance(e.getBox3D());
	}
}
