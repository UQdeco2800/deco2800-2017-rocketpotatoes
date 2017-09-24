package com.deco2800.potatoes.entities.health;

import java.util.Map;
import java.util.Random;

import com.deco2800.potatoes.collisions.CollisionMask;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.entities.enemies.EnemyEntity;
import com.deco2800.potatoes.entities.enemies.Squirrel;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.entities.projectiles.Projectile;
import com.deco2800.potatoes.gui.RespawnGui;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.GuiManager;
import com.deco2800.potatoes.managers.SoundManager;
import com.deco2800.potatoes.util.Box3D;

/**
 * 
 * Time event which allows MortalEntity to respawn after a certain amount of
 * time.
 * 
 * created by fff134 on 31/08/17.
 *
 */
public class RespawnEvent extends TimeEvent<MortalEntity> {

	public RespawnEvent() {
		// empty because serialization
	}

	public RespawnEvent(int respawnTime) {
		setDoReset(false);
		setProgress(respawnTime);
	}

	@Override
	public void action(MortalEntity param) {
		Random random = new Random();

		if (param instanceof Player) {
			// the dimensions of the map
			float length = GameManager.get().getWorld().getLength();
			float width = GameManager.get().getWorld().getWidth();
			// the new positions
			float newPosX;
			float newPosY;
			
			do {
				// randomly probes for a collision-less position on the map
				newPosX = length * random.nextFloat();
				newPosY = width * random.nextFloat();
			} while (hasCollisions(param, (int) newPosX, (int) newPosY));
			
			// sets the location of the player to respawn
			param.setPosition(newPosX, newPosY);

			try {
				// play respawn sound effect if player is respawning
				GameManager.get().getManager(SoundManager.class).playSound("respawnEvent.wav");
				// display respawn timer for player
				GameManager.get().getManager(GuiManager.class).getGui(RespawnGui.class).hide();
			} catch (NullPointerException e) {

			}
		} else if (param instanceof EnemyEntity) {
			// sets the location of the EnemyEntity to respawn
			param.setPosition(10 + random.nextFloat() * 10, 10 + random.nextFloat() * 10);
		}

		// sets players health to maximum health
		param.setHealth(param.getMaxHealth());
		// readd player to world
		GameManager.get().getWorld().addEntity(param);
	}

	/**
	 * Checks the respawn location for any collisions with other entities.
	 * 
	 * @param param
	 *            the mortal entity
	 * @param x
	 *            the x location of the param
	 * @param y
	 *            the y location of the param
	 * @return true if collision detected, else false.
	 */
	private boolean hasCollisions(MortalEntity param, int x, int y) {
		// create a box3D and set the location
		CollisionMask newPos = param.getMask();
		newPos.setX(x);
		newPos.setY(y);
		// get all entities on the current map
		Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();
		boolean collided = false;
		// check for collisions
		for (AbstractEntity entity : entities.values()) {
            if (!param.equals(entity) && entity.isStaticCollideable() && newPos.overlaps(entity.getMask())) {
                collided = true;
            }

            if (!param.equals(entity) && (entity instanceof EnemyEntity) && newPos.overlaps(entity.getMask())) {
                collided = true;
            }
		}

		return collided;
	}

}
