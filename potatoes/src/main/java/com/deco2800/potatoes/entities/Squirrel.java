package com.deco2800.potatoes.entities;

import java.util.Map;
import java.util.Random;

import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.managers.SoundManager;
import com.deco2800.potatoes.util.Box3D;

/**
 * A generic player instance for the game
 */
public class Squirrel extends EnemyEntity implements Tickable, HasProgress {
	
	private static final transient String TEXTURE_LEFT = "squirrel";
	private static final transient String TEXTURE_RIGHT = "squirrel2";
	private static final transient float HEALTH = 100f;
	private transient Random random = new Random();

	private float speed = 0.1f;

	public Squirrel() {
		super(0, 0, 0, 1f, 1f, 1f, 1f, 1f, TEXTURE_LEFT, HEALTH);
	}

	public Squirrel(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 1f, 1f, 1f, 1f, 1f, TEXTURE_LEFT, HEALTH);
		//this.setTexture("squirrel");
		//this.random = new Random();
	}

	@Override
	public void onTick(long i) {

		PlayerManager playerManager = (PlayerManager) GameManager.get().getManager(PlayerManager.class);
		SoundManager soundManager = (SoundManager) GameManager.get().getManager(SoundManager.class);

//		float goalX = playerManager.getPlayer().getPosX() + random.nextFloat() * 6 - 3;
//		float goalY = playerManager.getPlayer().getPosY() + random.nextFloat() * 6 - 3;

		//The X and Y position of the player without random floats generated
		float goalX = playerManager.getPlayer().getPosX();
		float goalY = playerManager.getPlayer().getPosY();

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

		//Squirrel changes direction when moving towards player.

		if(this.getPosX()>goalX){
			this.setTexture(TEXTURE_LEFT);
		}
		else{
			this.setTexture(TEXTURE_RIGHT);
		}
		
		Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();
		boolean collided = false;
		for (AbstractEntity entity : entities.values()) {
			if (!this.equals(entity) && !(entity instanceof Projectile) && newPos.overlaps(entity.getBox3D()) ) {
				if(entity instanceof Player) {
					//soundManager.playSound("ree1.wav");
				}
				collided = true;
			}
		}

		if (!collided) {
			setPosX(getPosX() + changeX);
			setPosY(getPosY() + changeY);
		}
	}
	
	@Override
	public String toString() {
		return "Squirrel";
	}

}
