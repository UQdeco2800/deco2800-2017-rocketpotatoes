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
		boolean playerRespawn = false;
		Random random = new Random();
		if (param instanceof Player) {
			// sets the location of the player to respawn
			param.setPosition(5, 10, 0);
			playerRespawn = true;
		} else if (param instanceof EnemyEntity) {
			// sets the location of the EnemyEntity to respawn
			param.setPosition(10 + random.nextFloat() * 10, 10 + random.nextFloat() * 10, 0);
		}

		/*OURS*/
		// sets MortalEntity's health to maximum health
		//param.setProgress(param.getMaxHealth());
		// read MortalEntity to world

		/*MASTERS*/
		// sets players health to maximum health
		param.setHealth(param.getMaxHealth());
		// read player to world
		if (playerRespawn) {
			// play respawn sound effect if player is respawning
			SoundManager soundManager = new SoundManager();
			soundManager.playSound("respawnEvent.wav");
		}

		GameManager.get().getWorld().addEntity(param);
	}
	
}
