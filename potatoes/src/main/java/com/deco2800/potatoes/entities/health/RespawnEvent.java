package com.deco2800.potatoes.entities.health;

import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;
/**
 * 
 * Time event which allows players to respawn after a certain amount of time.
 * 
 * create by fff134 31/08/17
 *
 */
public class RespawnEvent extends TimeEvent<Player> {
	
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
        playerManager = (PlayerManager)GameManager.get().getManager(PlayerManager.class);
        // create a new player
        // TODO bug: when respawning if buttons are held down player will move in the opposite direction for eternity.
        playerManager.setPlayer(new Player(5, 10, 0));
        GameManager.get().getWorld().addEntity(playerManager.getPlayer());
	}

}
