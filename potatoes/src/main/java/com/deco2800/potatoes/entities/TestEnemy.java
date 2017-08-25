package com.deco2800.potatoes.entities;

import java.util.*;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.potatoes.entities.*;
import com.deco2800.potatoes.entities.Enemies.AbstractEnemy;
import com.deco2800.potatoes.entities.Enemies.MeleeAttackEvent;
import com.deco2800.potatoes.entities.trees.AbstractTree;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.managers.SoundManager;
import com.deco2800.potatoes.util.Box3D;
import com.deco2800.potatoes.util.WorldUtil;

/**
 * A generic player instance for the game
 * -We want this to extend from AbstractEnemy eventually, at the moment it is simply extending from EnemyEntity
 * -I.e. we need to still need to develop the basic functions and elements of enemies before creating individual enemies.
 */
public class TestEnemy extends AbstractEnemy implements Tickable, HasProgress, ProgressBar {

    private static final transient String TEXTURE = "tankBear";
    private static final transient float HEALTH = 100f;
    private transient Random random = new Random();

    List<TimeEvent<AbstractEnemy>> normalEvents = new LinkedList<>();
    //normalEvents.add(new MeleeAttackEvent(500));
    //getNormalEventsReference().add(new MeleeAttackEvent(500));





    private float speed = 0.2f;

    public TestEnemy() {
        super(0, 0, 0, 1f, 1f, 1f, 1f, 1f, TEXTURE, HEALTH);
        getNormalEventsReference().add(new MeleeAttackEvent(500));
    }

    public TestEnemy(float posX, float posY, float posZ) {
        super(posX, posY, posZ, 1f, 1f, 1f, 1f, 1f, TEXTURE, HEALTH);
        getNormalEventsReference().add(new MeleeAttackEvent(500));
        //this.setTexture("squirrel");
        //this.random = new Random();
    }

    	/*testing attacking*/



    public void setProgressBar(AbstractEntity entity, Texture progressBar, SpriteBatch batch, int xLength, int yLength) {
        if (health > 160f) {
            batch.setColor(Color.GREEN);
        } else if (health > 100f) {
            batch.setColor(Color.ORANGE);
        } else {
            batch.setColor(Color.RED);
        }

        batch.draw(progressBar, xLength, yLength/3, health/3, 5);
        batch.setColor(Color.WHITE);
    }

    @Override
    public String toString() {
        return "TestEnemy";
    }

}
