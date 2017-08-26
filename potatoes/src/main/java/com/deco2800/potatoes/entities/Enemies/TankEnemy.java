package com.deco2800.potatoes.entities.Enemies;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.potatoes.entities.*;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.trees.AbstractTree;
import com.deco2800.potatoes.entities.trees.ProjectileTree;
import com.deco2800.potatoes.entities.trees.ResourceTree;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.managers.SoundManager;
import com.deco2800.potatoes.util.Box3D;
import com.deco2800.potatoes.util.WorldUtil;

/**
 * A generic player instance for the game
 */
public class TankEnemy extends EnemyEntity implements Tickable{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TankEnemy.class);
	private static final transient String TEXTURE = "tankBear";
	private static final transient float HEALTH = 200f;
	private transient Random random = new Random();
	private final float speed = 0.05f;
	private Class goal = Tower.class;

	/*Testing attacking*/
	//List<TimeEvent<EnemyEntity>> normalEvents = new LinkedList<>();
	private static final BasicStats STATS = initStats();
	/*Testing attacking*/

	public TankEnemy() {
		super(0, 0, 0, 1f, 1f, 1f, 1f, 1f, TEXTURE, HEALTH);
	}

	public TankEnemy(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 1f, 1f, 1f, 1f, 1f, TEXTURE, HEALTH);
	}

	@Override
	public void onTick(long i) {
		
//		//set the target of tankEnemy to the closest goal
		Optional<AbstractEntity> target = WorldUtil.getClosestEntityOfClass(goal, getPosX(), getPosY());
	
		//get the position of the target
		float goalX = target.get().getPosX(); 
		float goalY = target.get().getPosY(); 
		
		
		if(this.distance(target.get()) < speed) {
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

		 
		Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();
		boolean collided = false;
		for (AbstractEntity entity : entities.values()) {
			if (!this.equals(entity) && !(entity instanceof Projectile) && newPos.overlaps(entity.getBox3D()) ) {
				if(entity instanceof Tower) {
					//soundManager.playSound("ree1.wav");
				}
				collided = true;
			}
		}

		if (!collided) {
			setPosX(getPosX() + changeX);
			setPosY(getPosY() + changeY);
			//tankEnemy changes direction when moving towards tree/tower

			if(this.getPosX()>goalX){
				this.setTexture(TEXTURE);
			}
			else{
				this.setTexture(TEXTURE);
			}
		}

	}

	@Override
	public BasicStats getBasicStats() {
		return STATS;
	}

	private static BasicStats initStats() {
		List<TimeEvent<EnemyEntity>> normalEvents = new LinkedList<>();
		BasicStats result = new BasicStats(200, 500, 8f, 500, normalEvents,"tankBear");
		result.getNormalEventsReference().add(new MeleeAttackEvent(500));
		return result;
	}
	
	@Override
	public String toString() {
		return String.format("Tank Enemy at (%d, %d)", (int) getPosX(), (int) getPosY());
	}

}
