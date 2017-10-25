package com.deco2800.potatoes.entities.health;

import java.util.Map;
import java.util.Random;

import com.deco2800.potatoes.collisions.Shape2D;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.entities.enemies.EnemyEntity;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.entities.player.Player.PlayerState;
import com.deco2800.potatoes.gui.RespawnGui;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.GuiManager;
import com.deco2800.potatoes.managers.SoundManager;

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
			
			int newPosX = (int) (GameManager.get().getWorld().getLength() / 2 - 5f);
			int newPosY = (int) (GameManager.get().getWorld().getWidth() / 2 - 5f);

			while (GameManager.get().getWorld().getTerrain(newPosX, newPosY).getMoveScale() == 0) {
				newPosX += GameManager.get().getRandom().nextInt() % 4 - 2;
				newPosY += GameManager.get().getRandom().nextInt() % 4 - 2;
			}
			
			// sets the location of the player to respawn
			((Player) param).setState(PlayerState.IDLE);
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
	 * Checks the respawn location for any impassable terrain or collisions with other entities.
	 * 
	 * @param param
	 *            the mortal entity
	 * @param x
	 *            the x location of the param
	 * @param y
	 *            the y location of the param
	 * @return true if collision or impassable terrain detected, else false.
	 */
	private boolean hasCollisions(MortalEntity param, int x, int y) {
        boolean collided = false;

		// create a box3D and set the location
		Shape2D newPos = param.getMask();
		newPos.setX(x);
		newPos.setY(y);
		// get all entities on the current map
		Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();
		// check if MortalEntity is on a impassable terrain (0f if impassable)
		float epsilon = 0.00000001f;
		float speedScale = GameManager.get().getWorld().getTerrain(x, y).getMoveScale();
		if (Math.abs(speedScale) < epsilon) {
			return true;
		}
		// check for collisions
		for (AbstractEntity entity : entities.values()) {
            if (!param.equals(entity) && entity.isStatic() && newPos.overlaps(entity.getMask())) {
                collided = true;
            }

            if (!param.equals(entity) && entity instanceof EnemyEntity && newPos.overlaps(entity.getMask())) {
                collided = true;
            }
		}

		return collided;
	}

}
