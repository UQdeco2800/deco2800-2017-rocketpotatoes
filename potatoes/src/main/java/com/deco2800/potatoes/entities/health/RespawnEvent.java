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
		playerManager = (PlayerManager) GameManager.get().getManager(PlayerManager.class);
		// creates a new player
		// TODO bug: pressed movement keys during respawn will cause unwanted movement.
		playerManager.setPlayer(new Player(5, 10, 0));
		GameManager.get().getWorld().addEntity(playerManager.getPlayer());
	}

}
