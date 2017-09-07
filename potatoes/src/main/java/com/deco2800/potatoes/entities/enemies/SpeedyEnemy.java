package com.deco2800.potatoes.entities.enemies;


import java.util.*;

import com.deco2800.potatoes.entities.*;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.graphics.Color;
import com.deco2800.potatoes.entities.trees.ResourceTree;
import com.deco2800.potatoes.managers.GameManager;

/**
 * A class for speedy enemy
 */
public class SpeedyEnemy extends EnemyEntity implements Tickable{

    private static final Logger LOGGER = LoggerFactory.getLogger(SpeedyEnemy.class);
    private static final transient String TEXTURE = "speedyRaccoon";
    private static final transient String TEXTURE_RIGHT = "speedyRaccoonFaceRight";
    private static final transient float HEALTH = 80f;

	private static final List<Color> COLOURS = Arrays.asList(Color.RED, Color.ORANGE);
	private static final ProgressBarEntity PROGRESS_BAR = new ProgressBarEntity(COLOURS);
    private static final BasicStats STATS = initStats();

    private static float speed = 0.08f;
    private static Class<?> goal = ResourceTree.class;

    public SpeedyEnemy() {
        super(0, 0, 0, 0.50f, 0.50f, 0.50f, 0.55f, 0.55f, TEXTURE, HEALTH, speed, goal);
        //this.speed = speed;
        //this.goal = goal;
        //resetStats();
    }

    public SpeedyEnemy(float posX, float posY, float posZ) {
        super(posX, posY, posZ, 0.50f, 0.50f, 0.50f, 0.55f, 0.55f, TEXTURE, HEALTH, speed, goal);
        // this.steal
        //this.speed = speed;
        //this.goal = goal;
        //resetStats();
    }


  /*  public void onTick(long i) {
        double interactRange = 3f;
        Collection<AbstractEntity> entities = GameManager.get().getWorld().getEntities().values();

        for (AbstractEntity entitiy : entities) {
            if (entitiy instanceof ResourceTree && entitiy.distance(this) <= interactRange) {
                ((ResourceTree) entitiy).gather(-2);
            }
        }

    }

    /*
    public void gather(int amount) {
        int oldCount = gather.gatherCount;
        this.gatherCount += amount;

        // Check that the new amount is bounded
        if (this.gatherCount > this.gatherCapacity) {
            this.gatherCount = this.gatherCapacity;
        } else if (this.gatherCount < 0) {
            this.gatherCount = 0;
        }

        if (this.gatherCount - oldCount != 0) {
            LOGGER.info("Added " + (this.gatherCount - oldCount) + " to " + this);
        }
    }


	// @Override
	// public void onTick(long i) {
	//
	// /**
	// set the target of speedy enemy to the closest tree/tower
	// testing for enemy set target
	// it might change the target of speedy enemy
	// **/
	// Optional<AbstractEntity> target =
	// WorldUtil.getClosestEntityOfClass(ResourceTree.class, getPosX(), getPosY());
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
	// }
	// collided = true;
	// }
	// }
	//
	// if (!collided) {
	// setPosX(getPosX() + changeX);
	// setPosY(getPosY() + changeY);
	// // speedy enemy change direction if something blocked.
	//
	// if(this.getPosX()>goalX){
	// this.setTexture(TEXTURE);
	// }
	// else{
	// this.setTexture(TEXTURE);
	// }
	// }
	// }
  /**   public void onTick(long i){
        harvestResources();
    }**/
    @Override
    public String toString() {
        return String.format("Speedy Enemy at (%d, %d)", (int) getPosX(), (int) getPosY());
    }

    @Override
    public BasicStats getBasicStats() {
        return STATS;
    }

/**    private void harvestResources() {
        double interactRange = 3f; // TODO: Could this be a class variable?
        Collection<AbstractEntity> entities = GameManager.get().getWorld().getEntities().values();
        for (AbstractEntity entitiy : entities) {
            if (entitiy instanceof ResourceTree && entitiy.distance(this) <= interactRange) {
                if (((ResourceTree) entitiy).getGatherCount() > 0) {
                    ((ResourceTree) entitiy).gather(-1);
                }
            }
        }
    }
**/
    private static BasicStats initStats() {
        List<TimeEvent<EnemyEntity>> normalEvents = new LinkedList<>();
        BasicStats result = new BasicStats(HEALTH, speed, 2f, 500, normalEvents, TEXTURE);
        //result.getNormalEventsReference().add(new MeleeAttackEvent(500));
        return result;
    }

	@Override
	public ProgressBarEntity getProgressBar() {
		return PROGRESS_BAR;
	}


}
