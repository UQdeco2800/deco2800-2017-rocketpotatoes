package com.deco2800.potatoes.entities.health;

import java.util.Random;

import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.entities.enemies.EnemyEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.SoundManager;

/**
 * 
 * Time event which allows MortalEntity to respawn after a certain amount of time.
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
			// sets the location of the player to respawn
			param.setPosition(5, 10, 0);
			try {
				// play respawn sound effect if player is respawning
				SoundManager soundManager = new SoundManager();
				soundManager.playSound("respawnEvent.wav");
			} catch (NullPointerException e) {

			}
		} else if (param instanceof EnemyEntity) {
			// sets the location of the EnemyEntity to respawn
			param.setPosition(10 + random.nextFloat() * 10, 10 + random.nextFloat() * 10, 0);
		}
		
		// sets players health to maximum health
		param.setHealth(param.getMaxHealth());
		// readd player to world
		GameManager.get().getWorld().addEntity(param);
	}
	
}
