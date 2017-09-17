package com.deco2800.potatoes.entities.portals;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.resources.ResourceEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.managers.SoundManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.util.Box3D;
import com.deco2800.potatoes.worlds.WorldType;

/**
 * A class that can create portals which are not the base portal. Because these
 * are not in the first world, there are no enemies and therefore these portals
 * do not have health. AbstractPortals need to be instantiated with an
 * appropriate texture.
 * 
 * @author Jordan Holder, Katie Gray
 *
 */
public class AbstractPortal extends AbstractEntity implements Tickable {	
	/*
	 * Logger for all info/warning/error logs
	 */
	private static final transient Logger LOGGER = LoggerFactory.getLogger(ResourceEntity.class);
	/* Create a player manager. */
    private PlayerManager playerManager = GameManager.get().getManager(PlayerManager.class);
	/*
	 * The radius of which a collision can be detected
	 */
	private static final float CHANGE = 0.2f;
	/*
	 * The array of calculatePositions where a collision needs to be checked
	 */
	private static final float[][] POSITIONS = { {CHANGE, 0 }, {CHANGE, CHANGE}, { 0, CHANGE}, { -CHANGE, CHANGE},
			{ -CHANGE, 0 }, { -CHANGE, -CHANGE}, { 0, -CHANGE}, { -CHANGE, -CHANGE} };

	/**
	 * This instantiates an AbstractPortal given the appropriate parameters.
	 * 
	 * @param posX
	 *            the x coordinate of the spite
	 * @param posY
	 *            the y coordinate of the sprite
	 * @param posZ
	 *            the z coordinate of the sprite
	 * @param texture
	 *            the texture which represents the portal
	 */
	public AbstractPortal(float posX, float posY, float posZ, String texture) {
		super(posX, posY, posZ, 3, 3, 3, texture);
	}


	@Override
	public void onTick(long time) {
		float xPos = getPosX();
		float yPos = getPosY();
		boolean collided = false;
		AbstractEntity player = null;

		Box3D newPos = getBox3D();
		newPos.setX(xPos);
		newPos.setY(yPos);

		Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();
		// Check surroundings
		for (AbstractEntity entity : entities.values()) {
			if (!(entity instanceof Player)) {
				continue;
			}

			// Player detected
			player = entity;

			for (int i = 0; i < 8; i++) {
				newPos.setX(xPos + POSITIONS[i][0]);
				newPos.setY(yPos + POSITIONS[i][1]);
				// Player next to this resource
				if (newPos.overlaps(entity.getBox3D())) {
					collided = true;

				}
			}
		}

		// remove from game world and add to inventory if a player has collided with
		// this resource
		if (collided) {
			try {
				LOGGER.info("Entered portal");
				//play warping sound effect
				SoundManager soundManager = new SoundManager();
				soundManager.playSound("warpSound.wav");
				//remover player from old world
				GameManager.get().getWorld().removeEntity(player);
				//change to new world
				GameManager.get().getManager(WorldManager.class).setWorld(WorldType.FOREST_WORLD);
				//add player to new world
				GameManager.get().getWorld().addEntity(playerManager.getPlayer());
				
				// Bring up portal interface
			} catch (Exception e) {
				LOGGER.warn("Issue entering portal; " + e);
			}

		}
	}

}
