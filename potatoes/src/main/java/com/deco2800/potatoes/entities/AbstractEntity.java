package com.deco2800.potatoes.entities;


import com.deco2800.potatoes.collisions.Shape2D;
import com.deco2800.potatoes.collisions.Point2D;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.entities.projectiles.Projectile;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.renderering.Renderable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A AbstractEntity is an item that has a physical position in the 2D world
 * AbstractEntities are rendered by Render2D and Render3D An item that does not
 * need to be rendered should not be an AbstractEntity.
 *
 * This class has support for positioning & scaling a texture for the entity,
 * the rest of the implementation is in Render3D.
 *
 * This class uses a Shape2D as a collision mask to detect it's physical position
 * as well as distance and overlapping methods.
 *
 * A Shape2D is also used for the shadow, by default the shadow is on
 * and just renders the collisionMask
 *
 * This class also handles physical movements and collisions of the AbstractEntity
 *
 * @author Tazman Schmidt
 */
public abstract class AbstractEntity implements Renderable, Comparable<AbstractEntity> {

	// Rendering texture
	private String texture = "error_box";

	private float xRenderLength = 25;
	private float yRenderLength = 25;
	private float xRenderOffset = 0;
	private float yRenderOffset = 0;

	// Rendering shadow
	private Shape2D shadow;				//by default shadow is the same as collisionMask
	private boolean hasShadow = true;

	// Physics
	private Shape2D collisionMask;
	private boolean isStatic = false;	// Whether this is static or mobile
	private boolean isSolid = false;		// Whether to do collisions

	private float moveSpeed = 0; 			// The speed the entity moves at (dist per game step)
	private float moveSpeedModifier = 1; 	// a multiplier for speed
	private float moveAngle = 0; 			// Measured in Radians
	private float mass = 1;					// mass used in mobile on mobile collisions

	private static final float MIN_DIST = 0.000001f; 		// the closest we will move to another entity
	private static final float ESCAPE_SPEED = 0.15f; 		// the speed to get out from inside another object
	//private static final float FRICTION = 0.005f; 		// the amount we will decrease our momentumSpeed by

	Set<AbstractEntity> overlappedEntities = new HashSet<AbstractEntity>();		// entities I overlap this step
	Set<AbstractEntity> pushedEntities = new HashSet<AbstractEntity>();			// entities I pushed this step



	// ----------     Initialisation     ---------- //


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

		setMass();
	}

	/**
	 * Constructs a new AbstractEntity with specified physical & rendering properties.
	 *
	 * @param mask
	 *            The collision mask used by the entity.
	 * @param isSolid
	 * 			  Whether the entity should collide with other entities when it moves
	 * @param isStatic
	 * 			  Whether the entity can move (mobile), or cannot move or be moved (static)
	 * @param hasShadow
	 * 			  Whether the entity should render a shadow, by default the shadow is the mask
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
		setMass();
	}

	/**
	 * Constructs a new AbstractEntity with specified physical & rendering properties.
	 *
	 * @param mask
	 *            The collision mask used by the entity.
	 * @param isSolid
	 * 			  Whether the entity should collide with other entities when it moves
	 * @param isStatic
	 * 			  Whether the entity can move (mobile), or cannot move or be moved (static)
	 * @param shadow
	 * 			  The shadow that should be rendered below this entity
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
		setMass();
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
	
	@Override
	public float getPosZ() {
		return 0;
	}

	/**
	 * Sets the x position of this entity and it's shadow
	 * @param x		The new x value of this entity
	 */
	public void setPosX(float x) {
		this.collisionMask.setX(x);
		this.shadow.setX(x);		// if shadow is same as collisionMask, this is redundant but acceptable
	}

	/**
	 * Sets the y position of this entity and it's shadow
	 * @param y		The new x value of this entity
	 */
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
		return this.texture;
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

	/**
	 * @return Whether or not this entities shadow should be rendered
	 */
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
	 * Sets whether this entity is static or mobile.
	 * During collisions a static entity will not move.
	 * If this entity is static & solid other entities will bounce off, e.g. walls
	 *
	 * The PathManager will also consider static & solid entities & path around them
	 *
	 * @param isStatic
	 *            True iff this entity is intended to be stationary.
	 */
	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	/**
	 * @return 	True if this entity is intended to be stationary,
	 *       	False if this entity is intended to be mobile
	 */
	public boolean isStatic() {
		return isStatic;
	}

	/**
	 * Sets whether this entity is solid or collision-free.
	 * During collisions a solid entity will not overlap with other solid entities.
	 * If this entity is static & solid other entities will bounce off, e.g. walls
	 *
	 * The PathManager will also consider static & solid entities & path around them
	 *
	 * @param isSolid
	 *            True iff this entity is intended to be solid
	 */
	public void setSolid(boolean isSolid) {
		this.isSolid = isSolid;
	}

	/**
	 * @return 	True if this entity is intended to be solid,
	 *       	False if this entity is intended to be collision-free
	 */
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


	/**
	 * @param entity
	 * 				An entity to check collision with
	 * @return
	 * 				Whether this entities collisionMask overlaps the other entities collisionMask
	 */
	public boolean collidesWith(AbstractEntity entity) {
		return this.collisionMask.overlaps(entity.collisionMask);
	}


	/**
	 * Sets the mass of this entity.
	 * Mass is used during collisions, so that if a large entity tries to walk through a small entity,
	 * the small entity will be pushed more that the large one.
	 *
	 * This method sets the mass to be the area of this entities shadow or if no shadow, the collisionMask
	 */
	public void setMass() {
		if (hasShadow)
			this.mass = shadow.getArea();
		else
			this.mass = collisionMask.getArea();
	}

	/**
	 * Sets the mass of this entity for use in physical collisions
	 *
	 * @param mass	The new mass of this entity		*/
	public void setMass(float mass) {
		this.mass = mass;
	}

	/**
	 * @return 		The mass of this entity
	 */
	public float getMass() {
		return this.mass;
	}

	/**
	 * The movement speed of this entity, to be used in the onTickMove() method.
	 * Typically this should only be done once, during initialisation.
	 * If a speed buff or terrain modifies the speed of the entity try to use the speedModifier
	 *
	 * @param speed		The movement speed of this entity.
	 */
	public void setMoveSpeed(float speed) {
		this.moveSpeed =  speed;
	}

	/**
	 * @return 		The movement speed of this entity
	 */
	public  float getMoveSpeed() {
		return this.moveSpeed;
	}

	/**
	 * @return		The movement speed modifier of this entity
	 */
	public float getMoveSpeedModifier() {
		return moveSpeedModifier;
	}

	/**
	 * The movement speed modifier of this entity, multiplies the moveSpeed during the onTickMove() method.
	 * Typically set to 0, 1, a terrain or buff scalar.
	 *
	 * @param moveSpeedModifier		The speed scalar for this entities movement
	 */
	public void setMoveSpeedModifier(float moveSpeedModifier) {
		this.moveSpeedModifier = moveSpeedModifier;
	}

	/**
	 * Sets the angle that this entity should move in.
	 * The angle is measured in radians.
	 * Try using the methods in the Direction class with this call.
	 *
	 * @param angle		The angle of this entities movement in radians
	 */
	public void setMoveAngle(float angle) {
		this.moveAngle = angle;
	}

	/**
	 * @return		The angle of this entities movement in radians
	 */
	public float getMoveAngle() {
		return moveAngle;
	}

	/**
	 * Sets the angle that this entity should move in.
	 * Sets the angle of movement to point towards the centre of an entity
	 *
	 * @param target		The target entity
	 */
	public void setMoveTowards(Shape2D target) {
		this.moveAngle = this.collisionMask.getAngle(target);
	}

	/**
	 * Sets the angle that this entity should move in.
	 * Sets the angle of movement to point towards the given coordinates
	 *
	 * @param x		The x coordinate to point towards
	 * @param y		The y coordinate to point towards
	 */
	public void setMoveTowards(float x, float y) {
		this.moveAngle = (float) Math.atan2( y - this.collisionMask.getY(), x - this.collisionMask.getX());
	}

	/**
	 * Move this entity a given distance, in a given direction (in radians)
	 *
	 * @param theta		The angle to move
	 * @param dist		The distance to move
	 */
	public void moveVector(float theta, float dist) {
		float dx = (float) (dist * Math.cos(theta));
		float dy = (float) (dist * Math.sin(theta));
		float length = GameManager.get().getWorld().getLength();
		float width = GameManager.get().getWorld().getWidth();
		float terrainModifierCheck;


		terrainModifierCheck = GameManager.get().getWorld()
				.getTerrain(Math.round(Math.min(getPosX() + dx-1 , width - 1)), Math.round(Math.min(getPosY() + dy, length - 1)))
				.getMoveScale();
		if (terrainModifierCheck>0) {
			setPosX(Math.max(Math.min(getPosX() + dx, width), 0));
			setPosY(Math.max(Math.min(getPosY() + dy, length), 0));
		}
	}




	// ----------     Physics onTick    ---------- //

	/**
	 * Used primarily for solid mobile entities
	 *
	 * First, this entity is pushed away from any entities it might overlap
	 * Second, this entity moves the distance and direction, defined by moveSpeed & moveAngle
	 * If it collides with any solid entities it will push that entity, and this entity,
	 * away from each other, dependent on the masses of the entities.
	 *
	 * Moves without collision if !isSolid
	 * Exits running if isStatic
	 */
	public void onTickMovement() {


		if (isStatic) //does not run if i'm static
			return;

		if (!isSolid) {
			// projectile style movement, without collision
			moveVector(moveAngle, moveSpeed * moveSpeedModifier);
			return;
		}

		// use the area of my shadow as my mass
		// more massive entities will move less during mobile entity to mobile entity collision


		
		Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();

		// collection of entities I overlap during this step
		overlappedEntities.clear();


		// If I'm overlapping any entities, move away from them/try push them away
		// This method modifies overlappedEntities to contain entities I overlap this step
		moveEscapeOverlapping( entities );


		if (moveSpeedModifier <= 0)
			return;

		// Move me and distribute my momentum with any entities I collide with
		// This method modifies pushedEntities to contain entities I overlap this step
		moveAndPush( entities );

	}


	/**
	 * Used in onTickMovement
	 * Gets the ratio of masses.
	 *
	 * If the other entity isStatic, considers its mass as infinity,
	 * returns 0
	 *
	 * result will always be bounded by 1 and 0
	 *
	 * @param entity
	 * 				The other entity to get a ratio to
	 * @return
	 * 				The ratio of myMass / (myMass + entityMass)
	 */
	private float getMassRatio(AbstractEntity entity) {
		// when objects collide the resulting speed of each is dependent on their ratio of masses, PHYSICS!
		float massRatio;		//ratio (my mass) / (my mass + their mass)

		if (entity.isStatic) {
			// it's not moving, so I do all the moving
			massRatio = 0;
		} else {
			// move out of each others way
			massRatio = mass / (mass + entity.getMass());
		}

		return massRatio;
	}

	/**
	 * Used in onTickMovement
	 * Tries to escape any entities currently overlapped.
	 *
	 * @param entities	The entities to check collision against
	 */
	private void moveEscapeOverlapping(Map<Integer, AbstractEntity> entities) {

		// Check for entities i'm already overlapping, and move away from those entities centres
		for (Map.Entry<Integer, AbstractEntity> entity : entities.entrySet()) {
			AbstractEntity e = entity.getValue();

			if (this.equals(e) || !e.isSolid)         // don't collide with yourself silly, or non solid entities
				continue;

			if (this.collidesWith(e)) {

				// push this against e
				float massRatio = getMassRatio(e);
				float angle = collisionMask.getAngle(e.collisionMask);

				this.moveVector(angle+ (float) Math.PI, ESCAPE_SPEED * (1 - massRatio));
				e.moveVector(angle, ESCAPE_SPEED * massRatio);

				// note: if the other entity runs onTickMovement in the same tick, it will also
				// push us, meaning we get out of those entities twice as fast as static ones

				// keep list of entities I overlap with for next step
				overlappedEntities.add(e);
			}
		}
	}

	/**
	 * Used in onTickMovement
	 * Tries to move the distance and direction, defined by moveSpeed & moveAngle.
	 * Pushes entities where possible.
	 *
	 * @param entities 	The entities to check collision against
	 */
	private void moveAndPush(Map<Integer, AbstractEntity> entities){
		float length = GameManager.get().getWorld().getLength();
		float width = GameManager.get().getWorld().getWidth();
		float terrainModifierCheck;
		float movDist = moveSpeed * moveSpeedModifier;

		//set next pos
		Shape2D nextPos = collisionMask.copy();
		nextPos.moveVector(moveAngle, movDist);



		//check if next position overlaps with any new entities, push them out of the way if possible
		//let them push this entity too, do not allow nextPos to overlap another entity
		for (Map.Entry<Integer, AbstractEntity> entity : entities.entrySet()) {
			AbstractEntity e = entity.getValue();

			if (this.equals(e) || !e.isSolid)         // don't collide with yourself silly, or non solid entities
				continue;

			if (this.collidesWith(e) && ! overlappedEntities.contains(e)) {



				// if this is a new overlap: push this away from e and e away from this

				//get the max distance that I could move before collision
				float dist = this.distanceTo(e);
				/* this method returns min dist, not min dist in the direction of movement*/

				float remainDist = movDist - dist;

				if (e.isSolid) {

					// move backwards until not in collision
					nextPos.moveVector(moveAngle + (float) Math.PI, remainDist - MIN_DIST);
					return;
				} else {

					// push nextPos against e
					float massRatio = getMassRatio(e);
					float angle = nextPos.getAngle(e.collisionMask);

					nextPos.moveVector(angle, remainDist * (1 - massRatio));
					e.moveVector(angle + (float) Math.PI, remainDist * massRatio);
					return;
				}
			}

		}

		terrainModifierCheck = GameManager.get().getWorld()
				.getTerrain(Math.round(Math.min(nextPos.getX()-1 , width - 1)), Math.round(Math.min(nextPos.getY(), length - 1)))
				.getMoveScale();
		if (terrainModifierCheck>0) {
			setPosX(Math.max(Math.min(nextPos.getX(), width), 0));
			setPosY(Math.max(Math.min(nextPos.getY(), length), 0));
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
		float cartY = GameManager.get().getWorld().getLength() - this.collisionMask.getY();
		float isoX = (cartX - cartY) / 2.0f;
		float isoY = (cartX + cartY) / 2.0f;

		float otherCartX = o.getPosX();
		float otherCartY = GameManager.get().getWorld().getLength() - o.getPosY();

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
