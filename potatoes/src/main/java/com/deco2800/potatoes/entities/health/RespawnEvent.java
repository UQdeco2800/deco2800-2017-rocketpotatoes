package com.deco2800.potatoes.entities.health;

import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.managers.GameManager;

/**
 * 
 * Time event which allows players to respawn after a certain amount of time.
 * 
 * created by fff134 on 31/08/17.
 *
 */
public class RespawnEvent extends TimeEvent<Player> {

	public RespawnEvent() {
		// empty because serialization
	}

	public RespawnEvent(int respawnTime) {
		setDoReset(false);
		setProgress(respawnTime);
	}

	@Override
	public void action(Player param) {
		// sets the location of the player to respawn
		param.setPosition(5, 10);
		// sets players health to maximum health
		param.setProgress(param.getMaxHealth());
		// readd player to world
		GameManager.get().getWorld().addEntity(param);
	}

}
