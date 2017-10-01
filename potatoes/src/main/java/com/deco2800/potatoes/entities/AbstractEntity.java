package com.deco2800.potatoes.entities;

import com.deco2800.potatoes.collisions.Box2D;
import com.deco2800.potatoes.collisions.Shape2D;
import com.deco2800.potatoes.collisions.Point2D;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.entities.projectiles.Projectile;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.renderering.Render3D;
import com.deco2800.potatoes.renderering.Renderable;
import com.deco2800.potatoes.renderering.Renderer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

/**
 * A AbstractEntity is an item that can exist in both 3D and 2D worlds
 * AbstractEntities are rendered by Render2D and Render3D An item that does not
 * need to be rendered should not be a WorldEntity
 *
 * //TODO elaborate more
 * @author Tazman Schmidt
 */
public abstract class AbstractEntity implements Renderable, Comparable<AbstractEntity> {

	protected transient GameManager gameManager = GameManager.get();



	// Rendering texture
	private String texture = "error_box";
	//TODO there currently is no error_box
	private float xRenderLength = 25;
	private float yRenderLength = 25;
	private float xRenderOffset = 0;
	private float yRenderOffset = 0;

	// Rendering shadow
	private Shape2D shadow;				//by default shadow is the same as collisionMask
	private boolean hasShadow = true;

	// Physics
	private Shape2D collisionMask;
	private boolean isStatic = false;	// Whether this can move or be pushed
	private boolean isSolid = false;	// Whether to do collisions

	private float moveSpeed = 0; 		// How far to move this game step
	private float moveAngle = 0; 		// Measured in Radians
	private float myMass = 1;

	private static final float ESCAPE_SPEED = 0.2f; 	// the speed to get out from inside another object
	private static final float MIN_DIST = 0.00001f; 		// the closest we will move to another entity

	Collection<AbstractEntity> overlappedEntities;		// entities I overlap this step
	Collection<AbstractEntity> pushedEntities;			// entities I pushed this step




	// ----------     Initialisation     ---------- //

	//TODO add more descriptive declerations

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
    public AbstractEntity(Shape2D mask, float xRenderLength, float yRenderLength, String texture) {
		this.collisionMask = mask;
		this.shadow = mask;

    		this.xRenderLength = xRenderLength;
		this.yRenderLength = yRenderLength;

		this.texture = texture;
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
	 */ //TODO
	public AbstractEntity(Shape2D mask, boolean isSolid, boolean isStatic, boolean hasShadow,
		  float xRenderLength, float yRenderLength, float xRenderOffset, float yRenderOffset, String texture) {
		this.collisionMask = mask;
		this.isSolid = isSolid;
		this.isStatic = isStatic;

		this.shadow = mask;
		this.hasShadow = hasShadow;

		this.xRenderLength = xRenderLength;
		this.yRenderLength = yRenderLength;
		this.xRenderOffset = xRenderOffset;
		this.yRenderOffset = yRenderOffset;

		this.texture = texture;
	}

	//TODO
	public AbstractEntity(Shape2D mask, boolean isSolid, boolean isStatic, Shape2D shadow,
			  float xRenderLength, float yRenderLength, float xRenderOffset, float yRenderOffset, String texture) {
		this.collisionMask = mask;
		this.isSolid = isSolid;
		this.isStatic = isStatic;

		this.shadow = shadow;
		this.hasShadow = true;

		this.xRenderLength = xRenderLength;
		this.yRenderLength = yRenderLength;
		this.xRenderOffset = xRenderOffset;
		this.yRenderOffset = yRenderOffset;

		this.texture = texture;
	}


	// ----------     Position Getters / Setters     ---------- //

	/**
	 * Get the X coordinate of this AbstractEntity. This may have a bug, please
	 * check before using.
	 * 
	 * @return The X coordinate.
	 */
	@Override
	public float getPosX() {
		return collisionMask.getX();
	}

	/**
	 * Get the Y coordinate of this AbstractEntity.
	 * 
	 * @return The Y coordinate.
	 */
	@Override
	public float getPosY() {
		return collisionMask.getY();
	}

	// TODO comment / depreciate 3D methods
	@Override
	public float getPosZ() {
		return 0;
	}

	//TODO
	public void setPosX(float x) {
		this.collisionMask.setX(x);
		this.shadow.setX(x);		// if shadow is same as collisionMask, this is redundant but acceptable
	}

	//TODO
	public void setPosY(float y) {
		this.collisionMask.setY(y);
		this.shadow.setY(y);		// if shadow is same as collisionMask, this is redundant but acceptable
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




    // ----------     Rendering  Texture   ---------- //

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

	//TODO comment, maybe remove & only rotate on projectile
	public float rotationAngle() {
		return 0;
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




	// ----------     Rendering  Shadow   ---------- //

	/**
	 * Returns The Shape2D to be used as a shadow.
	 * By default, the same as the collisionMask Shape2D.
	 *
	 * @return	The shadow of this entity
	 */
	public Shape2D getShadow() {
		return shadow;
	}

	/**
	 * Set the shadow to be rendered under this entity.
	 * Set to null to remove shadow.
	 *
	 * @param shadow	The new shadow for this entity
	 */
	public void setShadow(Shape2D shadow) {
		this.shadow = shadow;
		shadow.setX(collisionMask.getX());
		shadow.setY(collisionMask.getY());
	}

	/**
	 * turn shadow on or off in rendering
	 * @param hasShadow whether the shadow is on
	 */
	public void setHasShadow(boolean hasShadow) {
		this.hasShadow = hasShadow;
	}

	//TODO
	public boolean hasShadow() {
		return hasShadow;
	}




	// ----------     Physics getters / setters / util    ---------- //

	/**
	 * Returns a Shape2D representing the location.
	 *
	 * @return Returns a Shape2D representing the location.
	 */
	public Shape2D getMask() {
		return collisionMask.copy();
	}



	/**
	 * Sets this entity to be a static collideable entity that gets pathed around
	 * when considering path finding in the PathManager
	 *
	 * @param isStatic
	 *            true iff this entity is intended to be stationary and have a
	 *            collision box
	 */ //TODO desc is static or mobile entity
	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	/**
	 * Checks if this entity is a static collideable entity that gets pathed around
	 * when considering path finding in the PathManager
	 *
	 * @return true iff this entity is intended to be stationary and have a
	 *         collision box
	 */ //TODO
	public boolean isStatic() {
		return isStatic;
	}

	/**
	 * Sets this entity to be a static collideable entity that gets pathed around
	 * when considering path finding in the PathManager
	 *
	 * @param isSolid
	 *            true iff this entity is intended to be stationary and have a
	 *            collision box
	 */ //TODO desc is solid or collision-free
	public void setSolid(boolean isSolid) {
		this.isStatic = isSolid;
	}

	/**
	 * Checks if this entity is a static collideable entity that gets pathed around
	 * when considering path finding in the PathManager
	 *
	 * @return true iff this entity is intended to be stationary and have a
	 *         collision box
	 */ //TODO
	public boolean isSolid() {
		return isSolid;
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
		return this.collisionMask.distance(new Point2D(x, y));
	}

	/**
	 * Calculates the distance between two entities.
	 *
	 * @param entity
	 *            The entity to calculate the distance to.
	 * @return Returns the euclidean distance between this and the specified entity.
	 */
	public float distanceTo(AbstractEntity entity) {
		return this.collisionMask.distance(entity.collisionMask);
	}

	//TODO
	public boolean collidesWith(AbstractEntity entity) {
		return this.collisionMask.overlaps(entity.collisionMask);
	}

	//TODO
	private float getDistance(AbstractEntity entity) {
		return this.collisionMask.distance(entity.collisionMask);
	}



	//TODO
	public void setMoveSpeed(float speed) {
		this.moveSpeed =  speed;
	}

	//TODO
	public void setMoveAngle(float angle) {
		this.moveAngle = angle;
	}

	//TODO
	public void setMoveTowards(Shape2D target) {
		this.moveAngle = this.collisionMask.getAngle(target);
	}

	//TODO
	public void setMoveTowards(float x, float y) {
		this.moveAngle = (float) Math.atan2( y - this.collisionMask.getY(), x - this.collisionMask.getX());
	}

	//TODO
	public void moveVector(float theta, float dist) {
		float dx = (float) (dist * Math.cos(theta));
		float dy = (float) (dist * Math.sin(theta));

		setPosX(getPosX() + dx);
		setPosY(getPosY() + dy);
	}




	// ----------     Physics onTick    ---------- //

	// does not run if you have set isStatic
	public void onTickMovement() {

		if (isStatic) //does not run if i'm static
			return;

		if (!isSolid) {
			// projectile style movement, without collision
			moveVector(moveSpeed, moveAngle);
			return;
		}

		// use the area of my shadow as my mass
		// more massive entities will move less during mobile entity to mobile entity collision
		myMass = shadow.getArea();	//TODO this doesn't need to be so frequent


		// TODO only consider nearby entities using some sort of World.getNearbySolid, should implement a quadtree
		// TODO get a collection of Box2D shapes that represent tiles & consider them like entities
		Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();

		// collection of entities I overlap during this step
		Collection<AbstractEntity> overlappedEntities = new HashSet<AbstractEntity>();


		// If I'm overlapping any entities, move away from them/try push them away
		// This method modifies overlappedEntities to contain entities I overlap this step
		 moveEscapeOverlapping( entities );


		// Move me and distribute my momentum with any entities I collide with
		// This method modifies pushedEntities to contain entities I overlap this step
		moveAndPush( entities );

	}

	//TODO
	private float getMassRatio(AbstractEntity entity) {
		// when objects collide the resulting speed of each is dependent on their ratio of masses, PHYSICS!
		float massRatio;		//ratio (my mass) / (my mass + their mass)

		if (entity.isStatic) {
			// it's not moving, so I do all the moving
			massRatio = 1;
		} else {
			// move out of each others way
			massRatio = myMass / (myMass + entity.getShadow().getArea());
		}

		return massRatio;
	}

	// TODO
	private void moveEscapeOverlapping(Map<Integer, AbstractEntity> entities) {

		overlappedEntities.clear();

		// Check for entities i'm already overlapping, and move away from those entities centres
		for (Map.Entry<Integer, AbstractEntity> entity : entities.entrySet()) {
			AbstractEntity e = entity.getValue();

			if (this.equals(e) || !e.isSolid)         // don't collide with yourself silly, or non solid entities
				continue;

			if (this.collidesWith(e)) {

				// push this against e
				float massRatio = getMassRatio(e);
				float angle = collisionMask.getAngle(e.collisionMask);

				this.moveVector(angle, ESCAPE_SPEED * (1 - massRatio));
				e.moveVector(angle + (float) Math.PI, ESCAPE_SPEED * massRatio);

				// note: if the other entity runs onTickMovement in the same tick, it will also
				// push us, meaning we get out of those entities twice as fast as static ones

				// keep list of entities I overlap with for next step
				overlappedEntities.add(e);
			}
		}
	}

	// TODO
	private void moveAndPush(Map<Integer, AbstractEntity> entities){

		//set next pos
		Shape2D nextPos = collisionMask.copy();
		nextPos.moveVector(moveSpeed, moveAngle);

		//check if next position overlaps with any new entities, push them out of the way if possible
		//let them push this entity too, do not allow nextPos to overlap another entity
		for (Map.Entry<Integer, AbstractEntity> entity : entities.entrySet()) {
			AbstractEntity e = entity.getValue();

			if (this.collidesWith(e) && ! overlappedEntities.contains(e)) {
				// if this is a new overlap: push this away from e and e away from this

				//get the max distance that I could move before collision, and do that before I
				float dist = this.getDistance(e);
				/* TODO this method returns min dist, not min dist in the direction of movement
				probably would be getting a bit pedantic though */

				// move backwards until not in collision
				nextPos.moveVector(moveSpeed - dist - MIN_DIST,  moveAngle + (float) Math.PI);

				// push nextPos against e
				float massRatio = getMassRatio(e);
				float angle = nextPos.getAngle(e.collisionMask);

				nextPos.moveVector(angle, dist * (1 - massRatio));
				e.moveVector(angle + (float) Math.PI, dist * massRatio);
			}

		}
	}




	// ----------     Generic Object Methods    ---------- //

	/**
	 * Allows sorting of WorldEntities for Isometric rendering
	 * 
	 * @param o
	 * @return
	 */
	@Override
	public int compareTo(AbstractEntity o) {
		float cartX = this.collisionMask.getX();
		float cartY = gameManager.getWorld().getLength() - this.collisionMask.getY();

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
		if (collisionMask != null ? !collisionMask.equals(that.collisionMask) : that.collisionMask != null) {
			return false;
		}
		return texture != null ? texture.equals(that.texture) : that.texture == null;
	}

	@Override
	public int hashCode() {
		int result = collisionMask != null ? collisionMask.hashCode() : 0;
		result = 31 * result + (texture != null ? texture.hashCode() : 0);
		result = 31 * result + this.getClass().hashCode();
		return result;
	}

}
