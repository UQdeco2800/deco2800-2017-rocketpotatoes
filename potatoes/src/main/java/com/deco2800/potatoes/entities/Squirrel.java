package com.deco2800.potatoes.entities;

import java.util.List;
import java.util.Random;

import com.deco2800.moos.entities.AbstractEntity;
import com.deco2800.moos.entities.Tickable;
import com.deco2800.moos.managers.GameManager;
import com.deco2800.moos.managers.SoundManager;
import com.deco2800.moos.util.Box3D;
import com.deco2800.potatoes.managers.PlayerManager;

/**
 * A generic player instance for the game
 */
public class Squirrel extends AbstractEntity implements Tickable {
	
	private float speed = 0.1f;
	
	private PlayerManager playerManager;
	private SoundManager soundManager;
	
	private Random random;

	public Squirrel(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 1, 1, 1);
		this.setTexture("squirrel");
		this.playerManager = (PlayerManager) GameManager.get().getManager(PlayerManager.class);
		this.soundManager = (SoundManager) GameManager.get().getManager(SoundManager.class);

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

		Box3D newPos = getBox3D();
		newPos.setX(getPosX() + changeX);
		newPos.setY(getPosY() + changeY);
		
		List<AbstractEntity> entities = GameManager.get().getWorld().getEntities();
		boolean collided = false;
		for (AbstractEntity entity : entities) {
			if (!this.equals(entity) & newPos.overlaps(entity.getBox3D())) {
				if(entity instanceof Player) {
					soundManager.playSound("ree1.wav");
				}
				collided = true;
			}
		}

		if (!collided) {
			setPosX(getPosX() + changeX);
			setPosY(getPosY() + changeY);
		}
	}
}