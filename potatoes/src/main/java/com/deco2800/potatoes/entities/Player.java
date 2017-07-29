package com.deco2800.potatoes.entities;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.moos.entities.Tickable;
import com.deco2800.moos.util.Box3D;
import com.deco2800.moos.worlds.AbstractWorld;
import com.deco2800.moos.worlds.WorldEntity;

/**
 * Entity for the playable character.
 * 
 * @author leggy
 *
 */
public class Player extends WorldEntity implements Tickable {

	private static final Logger LOGGER = LoggerFactory.getLogger(Player.class);

	private boolean movingUp;
	private boolean movingDown;
	private boolean movingRight;
	private boolean movingLeft;

	private float speed;

	/**
	 * Creates a new Player instance.
	 * 
	 * @param world
	 *            The world the player is in.
	 * @param posX
	 *            The x-coordinate.
	 * @param posY
	 *            The y-coordinate.
	 * @param posZ
	 *            The z-coordinate.
	 */
	public Player(AbstractWorld world, float posX, float posY, float posZ) {
		super(world, posX, posY, posZ, 1, 1);
		this.speed = 0.2f;

		this.movingUp = false;
		this.movingDown = false;
		this.movingRight = false;
		this.movingLeft = false;

		this.setTexture("selected_black");
	}

	@Override
	public void onTick(int arg0) {
		float newPosX = this.position.getX();
		float newPosY = this.position.getY();

		if (movingUp) {
			newPosX += speed;
			newPosY -= speed;
		} else if (movingDown) {
			newPosX -= speed;
			newPosY += speed;
		}

		if (movingLeft) {
			newPosX -= speed;
			newPosY -= speed;
		} else if (movingRight) {
			newPosX += speed;
			newPosY += speed;
		}

		Box3D newPos = getBox3D();
		newPos.setX(newPosX);
		newPos.setY(newPosY);

		List<WorldEntity> entities = this.getParent().getEntities();
		boolean collided = false;
		for (WorldEntity entity : entities) {
			if (!this.equals(entity) && newPos.overlaps(entity.getBox3D())) {
				LOGGER.info(this + " colliding with " + entity);
				System.out.println(this + " colliding with " + entity);
				collided = true;

			}
		}

		if (!collided) {
			this.position.setX(newPosX);
			this.position.setY(newPosY);
		}
	}

	@Override
	public String toString() {
		return "The player";
	}

	/**
	 * Sets the movingUp flag.
	 * 
	 * @param movingUp
	 *            The value to set.
	 */
	public void setMovingUp(boolean movingUp) {
		this.movingUp = movingUp;
	}

	/**
	 * Sets the movingDown flag.
	 * 
	 * @param movingUp
	 *            The value to set.
	 */
	public void setMovingDown(boolean movingDown) {
		this.movingDown = movingDown;
	}

	/**
	 * Sets the movingRight flag.
	 * 
	 * @param movingUp
	 *            The value to set.
	 */
	public void setMovingRight(boolean movingRight) {
		this.movingRight = movingRight;
	}

	/**
	 * Sets the movingLeft flag.
	 * 
	 * @param movingUp
	 *            The value to set.
	 */
	public void setMovingLeft(boolean movingLeft) {
		this.movingLeft = movingLeft;
	}

}
