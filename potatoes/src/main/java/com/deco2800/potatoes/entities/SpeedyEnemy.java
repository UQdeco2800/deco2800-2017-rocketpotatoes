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
public class SpeedyEnemy extends EnemyEntity implements Tickable, HasProgress, ProgressBar{

    private static final Logger LOGGER = LoggerFactory.getLogger(SpeedyEnemy.class);
    private static final transient String TEXTURE = "speedySquirrel";
    private static final transient float HEALTH = 65f;
    private transient Random random = new Random();

    private static float speed = 0.2f;
    private static Class goal = ResourceEntity.class;
    
    public SpeedyEnemy() {
        super(0, 0, 0, 1f, 1f, 1f, 1f, 1f, TEXTURE, HEALTH, speed, goal);
    }

    public SpeedyEnemy(float posX, float posY, float posZ) {
        super(posX, posY, posZ, 1f, 1f, 1f, 1f, 1f, TEXTURE, HEALTH, speed, goal);
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

}
