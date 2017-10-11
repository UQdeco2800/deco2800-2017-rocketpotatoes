package com.deco2800.potatoes.entities.resources;

import java.util.Map;

import com.deco2800.potatoes.collisions.Shape2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.potatoes.collisions.Circle2D;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.SoundManager;
import com.deco2800.potatoes.entities.AbstractEntity;

/**
 * A ResourceEntity is a type of AbstractEntity that can be collected. These
 * items need to be a type of resource that a player can collect and store in
 * inventory. ResourceEntities appear on the map and get added to a player's
 * inventory when collided with.
 *
 * @author Dion, Jordan
 */
public class ResourceEntity extends AbstractEntity implements Tickable {

	/*
	 * Logger for all info/warning/error logs
	 */
	private static final transient Logger LOGGER = LoggerFactory.getLogger(ResourceEntity.class);
	/*
	 * stores the type of resource: food, seed etc.
	 */
	private Resource resourceType;
	/*
	 * amount of the resource to add to an inventory
	 */
	private int quantity;
	/*
	 *  The radius of which a collision can be detected
	 */
	private static final float CHANGE = (float) 0.2;
	/*
	 * The array of calculatePositions where a collision needs to be checked
	 */
	private static final float[][] POSITIONS = { { CHANGE, 0 }, { CHANGE, CHANGE }, { 0, CHANGE }, { -CHANGE, CHANGE },
			{ -CHANGE, 0 }, { -CHANGE, -CHANGE }, { 0, -CHANGE }, { -CHANGE, -CHANGE } };

	/**
	 * Default constructor which sets the ResourceEntity to an ordinary Resource at
	 * position 0,0,0.
	 */
	public ResourceEntity() {
		super();
		resourceType = new Resource();
		quantity = 1;
	}

	/**
	 * Instantiates a ResourceEntity of a particular type of resource at a particular
	 * position. It requires resource is not null because the name component of it
	 * is input in the constructor.
	 *
	 * @require resource is not null
	 *
	 * @param posX
	 *            The x-coordinate of the entity.
	 * @param posY
	 *            The y-coordinate of the entity.
	 * @param posZ
	 *            The z-coordinate of the entity.
	 * @param resource
	 * 			  The type of resource to be created.
	 */
	public ResourceEntity(float posX, float posY, Resource resource) {
        super(new Circle2D(posX, posY, 0.707f), 1f, 1f, resource.getTypeName());
		resourceType = resource;
		quantity = 1;
	}

	/**
	 * Sets the current resource quantity to a certain positive amount.
	 *
	 * @param quantity
	 * 				The new amount to add to an inventory when a resource is
	 * 				collected.
	 */
	public void setQuantity(int quantity) {
		if (quantity < 1) {
			LOGGER.warn("Quantity must be positive");
		} else {
			this.quantity = quantity;
		}

	}

	/**
	 * Returns the amount of this resource that is stored in this entity. So this
	 * will be the number of extra resource that will be added to the player's
	 * inventory when picked up.
	 *
	 * @return quantity
	 * 				The amount to be added to an inventory.
	 */
	public int getQuantity() {
		return this.quantity;
	}

	/**
	 * Checks around to see if a player has collided with it every tick. If a
	 * collision has occurred, the ResourceEntity will disappear from the map and a
	 * set quantity of it will be added to the player's inventory.
	 */
	@Override
	public void onTick(long time) {
		float xPos = getPosX();
		float yPos = getPosY();
		boolean collided = false;
		Player player = null;

		Shape2D newPos = getMask();
		newPos.setX(xPos);
		newPos.setY(yPos);

		Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();
		// Check surroundings
		for (AbstractEntity entity : entities.values()) {
			if (!(entity instanceof Player)) {
				continue;
			}
			// Player detected
			for (int i = 0; i < 8; i++) {
				newPos.setX(xPos + POSITIONS[i][0]);
				newPos.setY(yPos + POSITIONS[i][1]);
				// Player next to this resource
				if (newPos.overlaps(entity.getMask())) {
					collided = true;
					player = (Player) entity;
				}
			}
		}

		// remove from game world and add to inventory if a player has collided with
		// this resource
		if (collided) {
			GameManager.get().getManager(SoundManager.class).playSound("harvesting.mp3");
			try {
				GameManager.get().getWorld().removeEntity(this);
				player.getInventory().updateQuantity(this.resourceType, this.getQuantity());
				LOGGER.info("Collected resource: " + this.resourceType);
			} catch (Exception e) {
				LOGGER.warn("Issue colliding with resource; \n" + e);
			}

		}

	}

	/**
	 * Returns the resource type that this entity is of. The type of resource
	 * determines the sprite image and what the player can do with the item when
	 * collected.
	 *
	 * @return resourceType
	 * 				The type of resource: food, seed etc.
	 */
	public Resource getType() {
		return resourceType;
	}

	/**
	 * Sets the resource type to a certain resource. Often called when
	 * ResourceEntity is instantiated with the default constructor and resource
	 * needs to be changed from default.
	 *
	 * @param resource
	 * 				The new type of the resource: food, seed etc.
	 */
	public void setResourceType(Resource resource) {
		if (resource == null) {
			LOGGER.error("Resource type can't be null");
		} else {
			resourceType = resource;
		}
	}

	/**
	 * Returns the string representation of the resource.
	 *
	 * @return string The string representation of the resource.
	 */
	@Override
	public String toString() {
		return quantity + " " + resourceType.toString();
	}

}
