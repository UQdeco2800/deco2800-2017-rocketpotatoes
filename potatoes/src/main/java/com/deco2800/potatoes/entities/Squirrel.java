package com.deco2800.potatoes.entities;

import java.util.Map;
import java.util.Random;

import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PathManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.managers.SoundManager;
import com.deco2800.potatoes.util.Box3D;
import com.deco2800.potatoes.util.Path;

/**
 * A generic player instance for the game
 */
public class Squirrel extends EnemyEntity implements Tickable, HasProgress {
	
	private static final transient String TEXTURE = "squirrel";
	private static final transient float HEALTH = 100f;
	private transient Random random = new Random();

	private float speed = 0.1f;

	
	private PlayerManager playerManager;
	private SoundManager soundManager;
	private PathManager pathManager;
	


	public Squirrel(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 1f, 1f, 1f, 1f, 1f, TEXTURE, HEALTH);
		//this.setTexture("squirrel");
		//this.random = new Random();
	}

	@Override
	public void onTick(long i) {

		PlayerManager playerManager = (PlayerManager) GameManager.get().getManager(PlayerManager.class);
		SoundManager soundManager = (SoundManager) GameManager.get().getManager(SoundManager.class);

		PathManager pathManager = (PathManager) GameManager.get().getManager(PathManager.class);

		Path nodes = pathManager.generatePath(this.getBox3D(),playerManager.getPlayer().getBox3D());



//		float goalX = playerManager.getPlayer().getPosX() + random.nextFloat() * 6 - 3;
//		float goalY = playerManager.getPlayer().getPosY() + random.nextFloat() * 6 - 3;
//
//		if(this.distance(playerManager.getPlayer()) < speed) {
//			this.setPosX(goalX);
//			this.setPosY(goalY);
//			return;
//		}
//
//		float deltaX = getPosX() - goalX;
//		float deltaY = getPosY() - goalY;
//
//		float angle = (float)(Math.atan2(deltaY, deltaX)) + (float)(Math.PI);
//
//		float changeX = (float)(speed * Math.cos(angle));
//		float changeY = (float)(speed * Math.sin(angle));
//
//		Box3D newPos = getBox3D();
//		newPos.setX(getPosX() + changeX);
//		newPos.setY(getPosY() + changeY);
//
//		Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();
//		boolean collided = false;
//		for (AbstractEntity entity : entities.values()) {
//			if (!this.equals(entity) && !(entity instanceof Projectile) && newPos.overlaps(entity.getBox3D()) ) {
//				if(entity instanceof Player) {
//					//soundManager.playSound("ree1.wav");
//				}
//				collided = true;
//			}
//		}
//
//		if (!collided) {
//			setPosX(getPosX() + changeX);
//			setPosY(getPosY() + changeY);
//		}
	}
	
	@Override
	public String toString() {
		return "Squirrel";
	}

}
