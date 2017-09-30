package com.deco2800.potatoes.entities;

import com.deco2800.potatoes.collisions.CollisionMask;
import com.deco2800.potatoes.collisions.Point2D;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.entities.projectiles.Projectile;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.renderering.Render3D;
import com.deco2800.potatoes.renderering.Renderable;

/**
 * A AbstractEntity is an item that can exist in both 3D and 2D worlds
 * AbstractEntities are rendered by Render2D and Render3D An item that does not
 * need to be rendered should not be a WorldEntity
 */
public abstract class AbstractEntity extends Render3D implements Renderable, Comparable<AbstractEntity> {

	protected transient GameManager gameManager = GameManager.get();

	private CollisionMask position;
	private CollisionMask shadow;

	private float xRenderLength;
	private float yRenderLength;
	private float xRenderOffset;
	private float yRenderOffset;

	private boolean staticCollideable = false;

	private String texture = "error_box";


	public float rotationAngle() {
		return 0;
	}

	/**
	 * Default constructor for the purposes of serialization
	 */
	public AbstractEntity() {
        this(new Point2D(0, 0), 0, 0, "");
	}

	/**
	 * Constructs a new AbstractEntity with specific render lengths. Allows
	 * specification of rendering dimensions different to those used for collision.
	 * For example, could be used to have collision on the trunk of a tree but not
	 * the leaves/branches. 
	 * 
     * @param mask
     *            The collision mask used by the entity.
	 * @param xRenderLength
	 *            The length of the entity, in x. Used in collision detection.
	 * @param yRenderLength
	 *            The length of the entity, in y. Used in collision detection.
	 * @param texture
	 *            The id of the texture for this entity.
	 */
    public AbstractEntity(CollisionMask mask, float xRenderLength, float yRenderLength, String texture) {
		this.xRenderLength = xRenderLength;
		this.yRenderLength = yRenderLength;
		this.xRenderOffset = 0;
		this.yRenderOffset = 0;

		this.texture = texture;

		this.position = mask;
		this.shadow = mask;
	}

	/**
	 * Constructs a new AbstractEntity with specific render lengths. Allows
	 * specification of rendering dimensions different to those used for collision.
	 * For example, could be used to have collision on the trunk of a tree but not
	 * the leaves/branches.
	 *
	 * @param mask
	 *            The collision mask used by the entity.
	 * @param xRenderLength
	 *            The length of the entity, in x. Used in collision detection.
	 * @param yRenderLength
	 *            The length of the entity, in y. Used in collision detection.
	 * @param xRenderOffset
	 *            Offset this entities image, in x, by a percentage (100 = 100%)
	 * @param yRenderOffset
	 *            Offset this entities image, in y, by a percentage (100 = 100%)
	 * @param texture
	 *            The id of the texture for this entity.
	 */
	public AbstractEntity(CollisionMask mask, float xRenderLength, float yRenderLength,
						  float xRenderOffset, float yRenderOffset, String texture) {
		this.xRenderLength = xRenderLength;
		this.yRenderLength = yRenderLength;
		this.xRenderOffset = xRenderOffset;
		this.yRenderOffset = yRenderOffset;

		this.texture = texture;

		this.position = mask;
		this.shadow = mask;
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
		return position.getX();
	}

	/**
	 * Get the Y coordinate of this AbstractEntity.
	 * 
	 * @return The Y coordinate.
	 */
	@Override
	public float getPosY() {
		return position.getY();
	}

	/**
	 * Sets the position of this to the coordinates given.
	 * 
	 * @param x
	 *            The x-coordinate.
	 * @param y
	 *            The y-coordinate.
	 */
	public void setPosition(float x, float y) {
		setPosX(x);
		setPosY(y);
	}

	public void setPosX(float x) {
		this.position.setX(x);
		if (shadow != null)
			this.shadow.setX(x);
	}

	public void setPosY(float y) {
		this.position.setY(y);
		if (shadow != null)
			this.shadow.setY(y);
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
	 * The image is offset along screen horizontal by this value when it renders,
	 * where 0 is a 0% offset & 100 is a full sprite width to the right. */
	public float getXRenderOffset() {
		return this.xRenderOffset;
	}

	/**
	 * The image is offset along screen vertical by this value when it renders,
	 * where 0 is a 0% offset & 100 is a full sprite height up. */
	public float getYRenderOffset() {
		return this.yRenderOffset;
	}

	/**
	 * Offset the image when it renders by this value,
	 * where 0 is no offset & 100 is a full sprite width right.
	 *
	 * @param xRenderOffset offset percentage
	 */
	public void setXRenderOffset(float xRenderOffset) {
		this.xRenderOffset = xRenderOffset;
	}

	/**
	 * Offset the image when it renders by this value,
	 * where 0 is no offset & 100 is a full sprite height up.
	 *
	 * @param yRenderOffset offset percentage
	 */
	public void setYRenderOffset(float yRenderOffset) {
		this.yRenderOffset = yRenderOffset;
	}

	/**
	 * Returns a CollisionMask representing the location.
	 * 
	 * @return Returns a CollisionMask representing the location.
	 */
	public CollisionMask getMask() {
        return position.copy();
	}

	/**
	 * Returns The CollisionMask to be used as a shadow.
	 * By default, the same as the position CollisionMask.
	 *
	 * @return	The shadow of this entity
	 */
	public CollisionMask getShadow() {
		return shadow;
	}

	/**
	 * Set the shadow to be rendered under this entity.
	 * Set to null to remove shadow.
	 *
	 * @param shadow	The new shadow for this entity
	 */
	/*public void setShadow(CollisionMask shadow) {
		this.shadow = shadow; //TODO need to track shadow with position
	}*/

	/**
	 * turn shadow on or off in rendering
	 * @param on whether the shadow is on
	 */
	public void setShadow(boolean on) {
		this.shadow = on? position : null;
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
		if (o instanceof Effect || o instanceof Projectile) {
			return 2;
		}
		return Float.compare(otherIsoY, isoY);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || !(o instanceof AbstractEntity)) {
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
	 * @param entity
	 *            The entity to calculate the distance to.
	 * @return Returns the euclidean distance between this and the specified entity.
	 */
	public float distance(AbstractEntity entity) {
		return this.position.distance(entity.position);
	}

	/**
	 * Calculates the distance this entity and the given coordinates.
	 * 
	 * @param x
	 *            The x-coordinate.
	 * @param y
	 *            The y-coordinate.
	 * @return Returns the euclidean distance between this and the specified
	 *         coordinates.
	 */
	public float distance(float x, float y) {
		return this.position.distance(new Point2D(x, y));
	}

	public void moveTowards(float x, float y, float z) {

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

    // TODO -- actual sort out 3D
    @Override
    public float getPosZ() {
        return 0;
    }


}
