package com.deco2800.potatoes.entities.health;

import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;

/**
 * 
 * Time event which allows players to respawn after a certain amount of time.
 * 
 * created by fff134 on 31/08/17.
 *
 */
public class RespawnEvent extends TimeEvent<Player> {

	// A player manager used to add a player
	private PlayerManager playerManager;

	public RespawnEvent() {
		// empty because serialization
	}

	public RespawnEvent(int respawnTime) {
		setDoReset(false);
		setProgress(respawnTime);
	}

	@Override
	public void action(Player param) {
		playerManager = GameManager.get().getManager(PlayerManager.class);
		// sets the location of the player to respawn
		param.setPosition(5, 10, 0);
		// sets players health to maximum health
		param.setProgress(param.getMaxHealth());
		// readd player to world
		GameManager.get().getWorld().addEntity(param);
	}

}
