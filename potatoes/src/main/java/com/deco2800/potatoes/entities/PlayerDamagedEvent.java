package com.deco2800.potatoes.entities;

import com.deco2800.potatoes.entities.Player.PlayerState;

/**
 * An event for handling the state in which a player is damaged.
 * 
 * @author petercondoleon
 */
public class PlayerDamagedEvent extends TimeEvent<Player> {
	
	/**
	 * Default constructor for serialization
	 */
	public PlayerDamagedEvent() {
		//Blank comment for Sonar
	}
	
	/**
	 * Initialises with a gather rate and amount.
	 * 
	 * @param damageTime
	 *            the time in which a player remains in a damaged state.
	 */
	public PlayerDamagedEvent(int damageTime) {
		setDoReset(true);
		setResetAmount(damageTime);
		reset();
	}

	@Override
	public void action(Player player) {
		player.clearState();
		player.updateSprites();
		setDoReset(false);
	}

}
