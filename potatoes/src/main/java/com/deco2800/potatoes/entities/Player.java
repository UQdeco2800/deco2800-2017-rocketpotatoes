package com.deco2800.potatoes.entities;

import java.util.List;

import com.badlogic.gdx.Input;
import com.deco2800.moos.managers.InputManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.moos.entities.Tickable;
import com.deco2800.moos.managers.GameManager;
import com.deco2800.moos.util.Box3D;
import com.deco2800.moos.worlds.WorldEntity;

/**
 * Entity for the playable character.
 * 
 * @author leggy
 *
 */
public class Player extends WorldEntity implements Tickable {

	private static final Logger LOGGER = LoggerFactory.getLogger(Player.class);

	private float movementSpeed;
	private float speedx;
	private float speedy;

	/**
	 * Creates a new Player instance.
	 *
	 * @param posX
	 *            The x-coordinate.
	 * @param posY
	 *            The y-coordinate.
	 * @param posZ
	 *            The z-coordinate.
	 */
	public Player(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 1, 1, 1);
		movementSpeed = 0.2f;
		this.speedx = 0.0f;
		this.speedy = 0.0f;
		InputManager input = (InputManager) GameManager.get().getManager(InputManager.class);

		input.addKeyDownListener(this::handleKeyDown);
		input.addKeyUpListener(this::handleKeyUp);

		this.setTexture("selected_black");
	}

	@Override
	public void onTick(int arg0) {
		float newPosX = this.position.getX();
		float newPosY = this.position.getY();

			newPosX += speedx;
			newPosY += speedy;

		Box3D newPos = getBox3D();
		newPos.setX(newPosX);
		newPos.setY(newPosY);

		List<WorldEntity> entities = GameManager.get().getWorld().getEntities();
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

	/**
	 * Handle movement when wasd keys are pressed down
	 * @param keycode
	 */
	private void handleKeyDown(int keycode) {
		switch (keycode) {
			case Input.Keys.W:
				speedy-=movementSpeed;
				speedx+=movementSpeed;
				break;
			case Input.Keys.S:
				speedy+=movementSpeed;
				speedx-=movementSpeed;
				break;
			case Input.Keys.A:
				speedx-=movementSpeed;
				speedy-=movementSpeed;
				break;
			case Input.Keys.D:
				speedx+=movementSpeed;
				speedy+=movementSpeed;
			default:
				break;
		}
	}

	/**
	 * Handle movement when wasd keys are released
	 * @param keycode
	 */
	private void handleKeyUp(int keycode) {
		switch (keycode) {
			case Input.Keys.W:
				speedy+=movementSpeed;
				speedx-=movementSpeed;
				break;
			case Input.Keys.S:
				speedy-=movementSpeed;
				speedx+=movementSpeed;
				break;
			case Input.Keys.A:
				speedx+=movementSpeed;
				speedy+=movementSpeed;
				break;
			case Input.Keys.D:
				speedx-=movementSpeed;
				speedy-=movementSpeed;
				break;
			default:
				break;
		}
	}

	@Override
	public String toString() {
		return "The player";
	}

}
