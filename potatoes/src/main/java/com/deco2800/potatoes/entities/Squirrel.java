package com.deco2800.potatoes.entities;

import java.util.Random;

import com.deco2800.moos.entities.AbstractEntity;
import com.deco2800.moos.entities.Tickable;
import com.deco2800.moos.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;

/**
 * A generic player instance for the game
 */
public class Squirrel extends AbstractEntity implements Tickable {
	
	private float speed = 0.1f;
	
	private PlayerManager playerManager;
	
	private Random random;

	public Squirrel(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 1, 1, 1);
		this.setTexture("squirrel");
		this.playerManager = (PlayerManager) GameManager.get().getManager(PlayerManager.class);
		this.random = new Random();
	}

	@Override
	public void onTick(int i) {
		float goalX = playerManager.getPlayer().getPosX() + random.nextFloat() * 6 - 3;
		float goalY = playerManager.getPlayer().getPosY() + random.nextFloat() * 6 - 3;

		if(this.distance(playerManager.getPlayer()) < speed) {
			this.setPosX(goalX);
			this.setPosY(goalY);
			return;
		}
		
		

		float deltaX = getPosX() - goalX;
		float deltaY = getPosY() - goalY;

		float angle = (float)(Math.atan2(deltaY, deltaX)) + (float)(Math.PI);

		float changeX = (float)(speed * Math.cos(angle));
		float changeY = (float)(speed * Math.sin(angle));

		setPosX(getPosX() + changeX);
		setPosY(getPosY() + changeY);
	}
}