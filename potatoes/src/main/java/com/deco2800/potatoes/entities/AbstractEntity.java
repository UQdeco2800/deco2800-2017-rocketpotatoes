package com.deco2800.potatoes.entities;

import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.renderering.Renderable;
import com.deco2800.potatoes.util.Box3D;

/**
 * A AbstractEntity is an item that can exist in both 3D and 2D worlds
 * AbstractEntities are rendered by Render2D and Render3D An item that does not
 * need to be rendered should not be a WorldEntity
 */
public abstract class AbstractEntity implements Renderable, Comparable<AbstractEntity> {

	protected GameManager gameManager = GameManager.get();

	private Box3D position;

	private float xRenderLength;

	private float yRenderLength;

	private boolean centered;

	private String texture = "error_box";

	/**
	 * Constructs a new AbstractEntity. The entity will be rendered at the same size
	 * used for collision between entities.
	 * 
	 * @param posX
	 *            The x-coordinate of the entity.
	 * @param posY
	 *            The y-coordinate of the entity.
	 * @param posZ
	 *            The z-coordinate of the entity.
	 * @param xLength
	 *            The length of the entity, in x. Used in rendering and collision
	 *            detection.
	 * @param yLength
	 *            The length of the entity, in y. Used in rendering and collision
	 *            detection.
	 * @param zLength
	 *            The length of the entity, in z. Used in rendering and collision
	 *            detection.
	 * @param texture
	 *            The id of the texture for this entity.
	 */
	public AbstractEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
			String texture) {
		this(posX, posY, posZ, xLength, yLength, zLength, xLength, yLength, false, texture);
	}

	/**
	 * Constructs a new AbstractEntity with specific render lengths. Allows
	 * specification of rendering dimensions different to those used for collision.
	 * For example, could be used to have collision on the trunk of a tree but not
	 * the leaves/branches.
	 * 
	 * @param posX
	 *            The x-coordinate of the entity.
	 * @param posY
	 *            The y-coordinate of the entity.
	 * @param posZ
	 *            The z-coordinate of the entity.
	 * @param xLength
	 *            The length of the entity, in x. Used in collision detection.
	 * @param yLength
	 *            The length of the entity, in y. Used in collision detection.
	 * @param zLength
	 *            The length of the entity, in z. Used in collision detection.
	 * @param xRenderLength
	 *            The length of the entity, in x. Used in collision detection.
	 * @param yRenderLength
	 *            The length of the entity, in y. Used in collision detection.
	 * @param texture
	 *            The id of the texture for this entity.
	 */
	public AbstractEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
			float xRenderLength, float yRenderLength, String texture) {
		this(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, false, texture);
	}

	/**
	 * Constructs a new AbstractEntity with specific render lengths. Allows
	 * specification of rendering dimensions different to those used for collision.
	 * For example, could be used to have collision on the trunk of a tree but not
	 * the leaves/branches. Allows rendering of entities to be centered on their
	 * coordinates if centered is true.
	 * 
	 * @param posX
	 *            The x-coordinate of the entity.
	 * @param posY
	 *            The y-coordinate of the entity.
	 * @param posZ
	 *            The z-coordinate of the entity.
	 * @param xLength
	 *            The length of the entity, in x. Used in collision detection.
	 * @param yLength
	 *            The length of the entity, in y. Used in collision detection.
	 * @param zLength
	 *            The length of the entity, in z. Used in collision detection.
	 * @param xRenderLength
	 *            The length of the entity, in x. Used in collision detection.
	 * @param yRenderLength
	 *            The length of the entity, in y. Used in collision detection.
	 * @param centered
	 *            True if the entity is to be rendered centered, false otherwise.
	 * @param texture
	 *            The id of the texture for this entity.
	 */
	public AbstractEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
			float xRenderLength, float yRenderLength, boolean centered, String texture) {
		this.xRenderLength = xRenderLength;
		this.yRenderLength = yRenderLength;
		this.centered = centered;
		
		this.texture = texture;

		if (centered) {
			posX += (1 - xLength / 2);
			posY += (1 - yLength / 2);
		}
		this.position = new Box3D(posX, posY, posZ, xLength, yLength, zLength);
	}

	/**
	 * Get the X coordinate of this AbstractEntity.
	 * 
	 * @return The X coordinate.
	 */
	public float getPosX() {
		float x = position.getX();
		if (this.centered) {
			x -= (1 - this.position.getYLength() / 2);
		}
		return x;
	}

	/**
	 * Get the Y coordinate of this AbstractEntity.
	 * 
	 * @return The Y coordinate.
	 */
	public float getPosY() {
		float y = position.getY();
		if (this.centered) {
			y -= (1 - this.position.getYLength() / 2);
		}
		return y;
	}

	/**
	 * Get the Z coordinate of this AbstractEntity.
	 * 
	 * @return The Z coordinate.
	 */
	public float getPosZ() {
		return position.getZ();
	}

	/**
	 * Sets the position of this to the coordinates given.
	 * 
	 * @param x
	 *            The x-coordinate.
	 * @param y
	 *            The y-coordinate.
	 * @param z
	 *            The z-coordinate.
	 */
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
			x += (1 - this.position.getXLength() / 2);
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
		float cartY = gameManager.getWorld().getLength() - this.position.getY();

		float isoX = ((cartX - cartY) / 2.0f);
		float isoY = ((cartX + cartY) / 2.0f);

		float cartX_o = o.getPosX();
		float cartY_o = gameManager.getWorld().getLength() - o.getPosY();

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
		if (this == o) {
			return true;
		}

		if (!(o instanceof AbstractEntity)) {
			return false;
		}

		AbstractEntity that = (AbstractEntity) o;

		if (position != null ? !position.equals(that.position) : that.position != null) {
			return false;
		}
		return texture != null ? texture.equals(that.texture) : that.texture == null;
	}

	@Override
	public int hashCode() {
		int result = position != null ? position.hashCode() : 0;
		result = 31 * result + (texture != null ? texture.hashCode() : 0);
		return result;
	}

	/**
	 * Calculates the distance between two entities.
	 * 
	 * @param e
	 *            The entity to calculate the distance to.
	 * @return Returns the euclidean distance between this and the specified entity.
	 */
	public float distance(AbstractEntity entity) {
		return this.getBox3D().distance(entity.getBox3D());
	}
	

	/**
	 * Calculates the distance this entity and the given coordinates.
	 * @param x	The x-coordinate.
	 * @param y	The y-coordinate.
	 * @param z	The z-coordinate.
	 * @return	Returns the euclidean distance between this and the specified coordinates.
	 */
	public float distance(float x, float y, float z) {
		return this.getBox3D().distance(x, y, z);
	}
	
	public void moveTowards(float x, float y, float z) {
		
	}
}
