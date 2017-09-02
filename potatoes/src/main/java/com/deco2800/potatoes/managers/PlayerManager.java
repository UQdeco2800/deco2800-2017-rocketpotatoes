package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.entities.Player;

/**
 * PlayerManager for managing the Player instance.
 * 
 * @author leggy
 *
 */
public class PlayerManager extends Manager {

	private Player player;

	public PlayerManager() {
		InputManager input = GameManager.get().getManager(InputManager.class);

		input.addKeyDownListener(this::handleKeyDown);
		input.addKeyUpListener(this::handleKeyUp);
	}

	/**
	 * Sets the player.
	 * 
	 * @param player
	 */
	public void setPlayer(Player player) {
		this.player = player;

		// Set camera manager to target the player
		GameManager.get().getManager(CameraManager.class).setTarget(player);
	}

	public void handleKeyDown(int keycode) {
		if (player != null) {
			player.handleKeyDown(keycode);
		}
	}

	public void handleKeyUp(int keycode) {
		if (player != null) {
			player.handleKeyUp(keycode);
		}
	}

	/**
	 * Gets the player.
	 * 
	 * @return Returns the player.
	 */
	public Player getPlayer() {
		return this.player;
	}

}
