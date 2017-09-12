package com.deco2800.potatoes.entities;

import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.renderering.Render3D;
import com.deco2800.potatoes.renderering.Renderable;
import com.deco2800.potatoes.util.Box3D;

/**
 * A AbstractEntity is an item that can exist in both 3D and 2D worlds
 * AbstractEntities are rendered by Render2D and Render3D An item that does not
 * need to be rendered should not be a WorldEntity
 */
public abstract class AbstractEntity extends Render3D implements Renderable, Comparable<AbstractEntity> {

	protected transient GameManager gameManager = GameManager.get();

	private Box3D position;

	private float xRenderLength;

	private float yRenderLength;

	private boolean centered;

	private boolean staticCollideable = false;

	private String texture = "error_box";

	public int rotateAngle() {
		return 0;
	}

	/**
	 * Default constructor for the purposes of serialization
	 */
	public AbstractEntity() {
		this(0, 0, 0, 0, 0, 0, "error_box");
	}

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

		this.position = new Box3D(posX + getCenterOffset(xLength), posY + getCenterOffset(yLength), posZ, xLength,
				yLength, zLength);
	}

	/**
	 * Get the X coordinate of this AbstractEntity. This may have a bug, please
	 * check before using.
	 * 
	 * @return The X coordinate.
	 */
	@Override
	public float getPosX() {
		// Using Y offset seems wrong but passes test and leggy was using Y offset here
		return position.getX() - getCenterOffsetY();
	}

	/**
	 * Get the Y coordinate of this AbstractEntity.
	 * 
	 * @return The Y coordinate.
	 */
	@Override
	public float getPosY() {
		return position.getY() - getCenterOffsetY();
	}

	/**
	 * Get the Z coordinate of this AbstractEntity.
	 * 
	 * @return The Z coordinate.
	 */
	@Override
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
		setPosX(x);
		setPosY(y);
		setPosZ(z);
	}

	public void setPosX(float x) {
		this.position.setX(x + getCenterOffsetX());
	}

	public void setPosY(float y) {
		this.position.setY(y + getCenterOffsetY());
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
	@Override
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
	 * Sets this entity to be a static collideable entity that gets pathed around
	 * when considering path finding in the PathManager
	 *
	 * @param staticCollideable
	 *            true iff this entity is intended to be stationary and have a
	 *            collision box
	 */
	public void setStaticCollideable(boolean staticCollideable) {
		this.staticCollideable = staticCollideable;
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

		float isoX = (cartX - cartY) / 2.0f;
		float isoY = (cartX + cartY) / 2.0f;

		float otherCartX = o.getPosX();
		float otherCartY = gameManager.getWorld().getLength() - o.getPosY();

		float otherIsoX = (otherCartX - otherCartY) / 2.0f;
		float otherIsoY = (otherCartX + otherCartY) / 2.0f;

		if (Float.compare(isoY, otherIsoY) == 0) {
			return Float.compare(otherIsoX, isoX);
		}
		if (o instanceof Effect) {
			return 2;
		}
		return Float.compare(otherIsoY, isoY);
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

		// be wary: Box3D is very precise in its equality
		if (position != null ? !position.equals(that.position) : that.position != null) {
			return false;
		}
		return texture != null ? texture.equals(that.texture) : that.texture == null;
	}

	@Override
	public int hashCode() {
		int result = position != null ? position.hashCode() : 0;
		result = 31 * result + (texture != null ? texture.hashCode() : 0);
		result = 31 * result + this.getClass().hashCode();
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
	 * 
	 * @param x
	 *            The x-coordinate.
	 * @param y
	 *            The y-coordinate.
	 * @param z
	 *            The z-coordinate.
	 * @return Returns the euclidean distance between this and the specified
	 *         coordinates.
	 */
	public float distance(float x, float y, float z) {
		return this.getBox3D().distance(x, y, z);
	}

	public void moveTowards(float x, float y, float z) {

	}

	/**
	 * Gets the offset if this entity is centered, 0 if it isn't. The offset is 1 -
	 * size / 2
	 */
	private float getCenterOffset(float size) {
		return this.centered ? (1 - size / 2) : 0;
	}

	/**
	 * Returns the centered offset for X
	 */
	private float getCenterOffsetX() {
		return getCenterOffset(this.position.getXLength());
	}

	/**
	 * Returns the centered offset for Y
	 */
	private float getCenterOffsetY() {
		return getCenterOffset(this.position.getYLength());
	}

	/**
	 * Checks if this entity is a static collideable entity that gets pathed around
	 * when considering path finding in the PathManager
	 *
	 * @return true iff this entity is intended to be stationary and have a
	 *         collision box
	 */
	public boolean isStaticCollideable() {
		return this.staticCollideable;
	}
}
