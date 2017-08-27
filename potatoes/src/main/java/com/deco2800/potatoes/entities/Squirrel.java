package com.deco2800.potatoes.entities;

import java.util.LinkedList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.deco2800.potatoes.entities.Enemies.BasicStats;
import com.deco2800.potatoes.entities.Enemies.MeleeAttackEvent;


/**
 * A generic player instance for the game
 */
public class Squirrel extends EnemyEntity implements Tickable, HasProgress{
	

	private static final transient String TEXTURE_LEFT = "squirrel";
	private static final transient String TEXTURE_RIGHT = "squirrel_right";
	private static final transient float HEALTH = 100f;

	/*Testing attacking*/
	private static final BasicStats STATS = initStats();
	/*Testing attacking*/


	private static float speed = 0.04f;
	private static Class<?> goal = Player.class;

	private static final ProgressBarEntity progressBar = new ProgressBarEntity("progress_bar", 40, 1);	
	
	public Squirrel() {
		super(0, 0, 0, 0.47f, 0.47f, 0.47f, 0.60f, 0.60f, TEXTURE_LEFT, HEALTH, speed, goal);
		this.speed = speed;
		this.goal = goal;
	}

	public Squirrel(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 0.47f, 0.47f, 0.47f, 0.60f, 0.60f, TEXTURE_LEFT, HEALTH, speed, goal);
		this.speed = speed;
		this.goal = goal;
	}
	

//	@Override
//	public void onTick(long i) {
//
//		PlayerManager playerManager = (PlayerManager) GameManager.get().getManager(PlayerManager.class);
//		SoundManager soundManager = (SoundManager) GameManager.get().getManager(SoundManager.class);
//
////		float goalX = playerManager.getPlayer().getPosX() + random.nextFloat() * 6 - 3;
////		float goalY = playerManager.getPlayer().getPosY() + random.nextFloat() * 6 - 3;
//
//		//The X and Y position of the player without random floats generated
//		float goalX = playerManager.getPlayer().getPosX() ;
//		float goalY = playerManager.getPlayer().getPosY() + random.nextFloat() * 6 -3;
//		
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
//
//
//		float changeX = (float)(speed * Math.cos(angle));
//		float changeY = (float)(speed * Math.sin(angle));
//
//		Box3D newPos = getBox3D();
//
//		newPos.setX(getPosX() + changeX);
//		newPos.setY(getPosY() + changeY);
//
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
//			//Squirrel changes direction when moving towards player.
//
//			if(this.getPosX()>goalX){
//				this.setTexture(TEXTURE_LEFT);
//			}
//			else{
//				this.setTexture(TEXTURE_RIGHT);
//			}
//		}
//	}

	@Override
	public String toString() {
		return String.format("Squirrel at (%d, %d)", (int) getPosX(), (int) getPosY());
	}

	@Override
	public ProgressBarEntity getProgressBar() {
		return progressBar;
	}

	private static BasicStats initStats() {
		List<TimeEvent<EnemyEntity>> normalEvents = new LinkedList<>();
		BasicStats result = new BasicStats(200, 500, 8f, 500, normalEvents,"squirrel");
		//result.getNormalEventsReference().add(new MeleeAttackEvent(500));
		return result;
	}

	@Override
	public BasicStats getBasicStats() {
		return STATS;
	}

}
