package com.deco2800.potatoes.entities.Enemies;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

import com.deco2800.potatoes.entities.*;
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
public class TestEnemy extends AbstractEnemy implements Tickable, HasProgress {

    private static final transient String TEXTURE = "pronograde";
    private static final transient float HEALTH = 100f;
    private transient Random random = new Random();



    private float speed = 0.2f;

    public TestEnemy() {
        super(0, 0, 0, 1f, 1f, 1f, 1f, 1f, TEXTURE, HEALTH);
    }

    public TestEnemy(float posX, float posY, float posZ) {
        super(posX, posY, posZ, 1f, 1f, 1f, 1f, 1f, TEXTURE, HEALTH);

        //this.setTexture("squirrel");
        //this.random = new Random();
    }



    @Override
    public String toString() {
        return "TestEnemy";
    }

}
