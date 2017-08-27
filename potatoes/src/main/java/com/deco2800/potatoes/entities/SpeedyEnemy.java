package com.deco2800.potatoes.entities;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

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
public class SpeedyEnemy extends EnemyEntity implements Tickable{

    private static final Logger LOGGER = LoggerFactory.getLogger(SpeedyEnemy.class);
    private static final transient String TEXTURE = "speedyRaccoon";
    private static final transient String TEXTURE_RIGHT = "speedyRaccoonFaceRight";
    private static final transient float HEALTH = 65f;
    private static float speed = 0.15f;
    private static Class goal = ResourceTree.class;

    public SpeedyEnemy() {
        super(0, 0, 0, 1f, 1f, 1f, 1f, 1f, TEXTURE, HEALTH, speed, goal);
        this.speed = speed;
        this.goal = goal;
    }

    public SpeedyEnemy(float posX, float posY, float posZ) {
        super(posX, posY, posZ, 1f, 1f, 1f, 1f, 1f, TEXTURE, HEALTH, speed, goal);
        this.speed = speed;
        this.goal = goal;
    }

//    @Override
//    public void onTick(long i) {
//
//        /**
//        set the target of speedy enemy to the closest tree/tower
//         testing for enemy set target
//         it might change the target of speedy enemy
//        **/
//        Optional<AbstractEntity> target = WorldUtil.getClosestEntityOfClass(ResourceTree.class, getPosX(), getPosY());
//
//        //get the position of the target
//        float goalX = target.get().getPosX();
//        float goalY = target.get().getPosY();
//
//
//        if(this.distance(target.get()) < speed) {
//            this.setPosX(goalX);
//            this.setPosY(goalY);
//            return;
//        }
//
//        float deltaX = getPosX() - goalX;
//        float deltaY = getPosY() - goalY;
//
//        float angle = (float)(Math.atan2(deltaY, deltaX)) + (float)(Math.PI);
//
//
//
//        float changeX = (float)(speed * Math.cos(angle));
//        float changeY = (float)(speed * Math.sin(angle));
//
//        Box3D newPos = getBox3D();
//
//        newPos.setX(getPosX() + changeX);
//        newPos.setY(getPosY() + changeY);
//
//
//        Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();
//        boolean collided = false;
//        for (AbstractEntity entity : entities.values()) {
//            if (!this.equals(entity) && !(entity instanceof Projectile) && newPos.overlaps(entity.getBox3D()) ) {
//                if(entity instanceof Tower) {
//                }
//                collided = true;
//            }
//        }
//
//        if (!collided) {
//            setPosX(getPosX() + changeX);
//            setPosY(getPosY() + changeY);
//            //speedy enemy change direction if something blocked.
//
//            if(this.getPosX()>goalX){
//                this.setTexture(TEXTURE);
//            }
//            else{
//                this.setTexture(TEXTURE);
//            }
//        }
//    }

    @Override
    public String toString() {
        return String.format("Speedy Enemy at (%d, %d)", (int) getPosX(), (int) getPosY());
    }
}


