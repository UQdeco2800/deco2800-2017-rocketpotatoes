package com.deco2800.potatoes.entities;

import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;


import com.deco2800.potatoes.entities.Enemies.BasicStats;
import com.deco2800.potatoes.entities.Enemies.MeleeAttackEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.graphics.Color;

/**
 * A generic player instance for the game
 */
public class TankEnemy extends EnemyEntity implements Tickable {

	private static final Logger LOGGER = LoggerFactory.getLogger(TankEnemy.class);
	private static final BasicStats STATS = initStats();
	private static final transient String TEXTURE = "tankBear";
	private static final transient float HEALTH = 400f;

	private static float speed = 0.02f;
	private static Class<?> goal = Tower.class;
	private static final List<Color> colours = Arrays.asList(Color.PURPLE, Color.RED, Color.ORANGE, Color.YELLOW);
	private static final ProgressBarEntity progressBar = new ProgressBarEntity("progress_bar", colours, 90, 1);

	public TankEnemy() {
		super(0, 0, 0, 1f, 1f, 1f, 1f, 1f, TEXTURE, HEALTH, speed, goal);
		//this.speed = getBasicStats().getSpeed();
		//this.goal = goal;
		//resetStats();
	}

	public TankEnemy(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 1f, 1f, 1f, 1f, 1f, TEXTURE, HEALTH, speed, goal);
		//this.speed = getBasicStats().getSpeed();
		//this.goal = goal;
		//resetStats();
	}

	private static BasicStats initStats() {
		List<TimeEvent<EnemyEntity>> normalEvents = new LinkedList<>();
		BasicStats result = new BasicStats(HEALTH, speed, .5f, 1000, normalEvents, TEXTURE);
		result.getNormalEventsReference().add(new MeleeAttackEvent(result.getAttackSpeed()));
		return result;
	}

	@Override
	public BasicStats getBasicStats() {
		return STATS;
	}
	
	@Override
	public String toString() {
		return String.format("Tank Enemy at (%d, %d)", (int) getPosX(), (int) getPosY());
	}

	@Override
	public ProgressBarEntity getProgressBar() {
		return progressBar;
	}


	// @Override
	// public void onTick(long i) {
	//
	// // //set the target of tankEnemy to the closest goal
	// Optional<AbstractEntity> target = WorldUtil.getClosestEntityOfClass(goal,
	// getPosX(), getPosY());
	//
	// // get the position of the target
	// float goalX = target.get().getPosX();
	// float goalY = target.get().getPosY();
	//
	//
	// if(this.distance(target.get()) < speed) {
	// this.setPosX(goalX);
	// this.setPosY(goalY);
	// return;
	// }
	//
	//
	// float deltaX = getPosX() - goalX;
	// float deltaY = getPosY() - goalY;
	//
	// float angle = (float)(Math.atan2(deltaY, deltaX)) + (float)(Math.PI);
	//
	//
	//
	// float changeX = (float)(speed * Math.cos(angle));
	// float changeY = (float)(speed * Math.sin(angle));
	//
	// Box3D newPos = getBox3D();
	//
	// newPos.setX(getPosX() + changeX);
	// newPos.setY(getPosY() + changeY);
	//
	//
	// Map<Integer, AbstractEntity> entities =
	// GameManager.get().getWorld().getEntities();
	// boolean collided = false;
	// for (AbstractEntity entity : entities.values()) {
	// if (!this.equals(entity) && !(entity instanceof Projectile) &&
	// newPos.overlaps(entity.getBox3D()) ) {
	// if(entity instanceof Tower) {
	// //soundManager.playSound("ree1.wav");
	// }
	// collided = true;
	// }
	// }
	//
	// if (!collided) {
	// setPosX(getPosX() + changeX);
	// setPosY(getPosY() + changeY);
	// // tankEnemy changes direction when moving towards tree/tower
	//
	// if(this.getPosX()>goalX){
	// this.setTexture(TEXTURE);
	// }
	// else{
	// this.setTexture(TEXTURE);
	// }
	// }
	//
	// }
}
