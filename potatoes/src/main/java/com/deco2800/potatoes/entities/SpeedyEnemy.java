package com.deco2800.potatoes.entities;

import java.util.*;

import com.deco2800.potatoes.entities.Enemies.BasicStats;
import com.deco2800.potatoes.entities.Enemies.MeleeAttackEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.potatoes.entities.AbstractEntity;
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
 * A class for speedy enemy
 */
public class SpeedyEnemy extends EnemyEntity implements Tickable, HasProgress, ProgressBar{

    private static final Logger LOGGER = LoggerFactory.getLogger(SpeedyEnemy.class);
    private static final transient String TEXTURE = "speedySquirrel";
    private static final transient float HEALTH = 65f;
    private transient Random random = new Random();

    /*Testing attacking*/
    private static final BasicStats STATS = initStats();
	/*Testing attacking*/

    private static float speed = 0.2f;
    private static Class goal = ResourceEntity.class;

    public SpeedyEnemy() {
        super(0, 0, 0, 1f, 1f, 1f, 1f, 1f, TEXTURE, HEALTH);
    }

    public SpeedyEnemy(float posX, float posY, float posZ) {
        super(posX, posY, posZ, 1f, 1f, 1f, 1f, 1f, TEXTURE, HEALTH);
    }

 
    @Override
	public void onTick(long i) {
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
		}
	}
    
    @Override
    public String toString() {
        return "SpeedyEnemy";
    }

    //set colour of health bar.
    @Override
    public void setProgressBar(AbstractEntity entity, Texture progressBar, SpriteBatch batch, int xLength, int yLength) {
        if (health > 60) {
            batch.setColor(Color.GREEN);
        } else if (health > 20) {
            batch.setColor(Color.ORANGE);
        } else {
            batch.setColor(Color.RED);
        }

        batch.draw(progressBar, xLength, yLength, health/3, 5);
        batch.setColor(Color.WHITE);

    }

    @Override
    public BasicStats getBasicStats() {
        return STATS;
    }

    private static BasicStats initStats() {
        List<TimeEvent<EnemyEntity>> normalEvents = new LinkedList<>();
        BasicStats result = new BasicStats(200, 500, 8f, 500, normalEvents,"tankBear");
        //result.getNormalEventsReference().add(new MeleeAttackEvent(500));
        return result;
    }

}
