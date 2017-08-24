package com.deco2800.potatoes.entities;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.exceptions.InvalidResourceException;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.Inventory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.deco2800.potatoes.util.Box3D;
import com.deco2800.potatoes.entities.AbstractEntity;

/**
 * A ResourceEntity is a type of AbstractEntity that can be collected. These
 * items need to be a type of resource that a player can collect and store in
 * inventory. ResourceEntities appear on the map and get added to a player's
 * inventory when collided with.
 * 
 * @author Dion
 */
public class ResourceEntity extends AbstractEntity implements Tickable {

	/*
	 * Logger for all info/warning/error logs
	 */
	private static final transient Logger LOGGER = LoggerFactory.getLogger(ResourceEntity.class);
	
	private Resource resourceType;
	private int quantity;
	// The radius of which a collision can be detected
	private final float change = (float) 0.2;
	// The array of positions where a collision needs to be checked
	private final float[][] positions = { { change, 0 }, { change, change }, { 0, change }, { -change, change },
			{ -change, 0 }, { -change, -change }, { 0, -change }, { -change, -change } };

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
	 * Instantiates a ResourceEntity of a particular type of resource at a particlar
	 * position. It requires resource is not null because the name component of it
	 * is input in the constructor.
	 * 
	 * @require resource is not null
	 */
	public ResourceEntity(float posX, float posY, float posZ, Resource resource) {
		super(posX, posY, posZ, 1f, 1f, 1f, 1f, 1f, resource.getTypeName());
		resourceType = resource;
		quantity = 1;
	}

	/**
	 * Sets the current resource quantity to a certain positive amount.
	 */
	public void setQuantity(int quantity) {
		if (quantity < 1)
			throw new InvalidResourceException("Quantity must be positive");
		this.quantity = quantity;
	}

	/**
	 * Returns the amount of this resource that is stored in this entity. So this
	 * will be the number of extra resource that will be added to the player's
	 * inventory when picked up.
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

		Box3D newPos = getBox3D();
		newPos.setX(xPos);
		newPos.setY(yPos);

		Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();
		// Check surroundings
		for (AbstractEntity entity : entities.values()) {
			if (entity instanceof Player) {
				// Player detected
				for (int i = 0; i < 8; i++) {
					newPos.setX(xPos + positions[i][0]);
					newPos.setY(yPos + positions[i][1]);

					if (newPos.overlaps(entity.getBox3D())) {
						collided = true;
						player = (Player) entity;
					}
				}
			}
		}

		if (collided) {
			try {
				GameManager.get().getWorld().removeEntity(this);
				player.getInventory().updateQuantity(this.resourceType, this.getQuantity());
				LOGGER.info("Collected resource: " + this.resourceType);
			} catch (Exception e) {
				LOGGER.warn("Issue colliding with resource");
			}

		}

	}

	/**
	 * Returns the resource type that this entity is of. The type of resource
	 * determines the sprite image and what the player can do with the item when
	 * collected.
	 */
	public Resource getType() {
		return resourceType;
	}

	/**
	 * Sets the resource type to a certain resource. Often called when
	 * ResourceEntity is instantiated with the default constructor and resource
	 * needs to be changed from default.
	 * 
	 * @throws InvalidResourceException
	 *             if resource is null
	 */
	public void setResourceType(Resource resource) {
		if (resource == null)
			throw new InvalidResourceException();
		resourceType = resource;
	}

	/**
	 * <p>
	 * Returns the string representation of the resource.
	 * </p>
	 * 
	 * @return string The string representation of the resource.
	 */
	public String toString() {
		return quantity + " " + resourceType.toString();
	}

}
