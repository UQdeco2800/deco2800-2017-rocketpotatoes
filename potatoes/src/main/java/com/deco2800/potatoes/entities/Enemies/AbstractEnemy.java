package com.deco2800.potatoes.entities.Enemies;

import com.deco2800.potatoes.entities.EnemyEntity;
import com.deco2800.potatoes.entities.Tickable;


/**
 * AbstractEnemy defines a generic enemy in the game.
 */
public abstract class AbstractEnemy extends EnemyEntity implements Tickable {

    /**
     * Default constructor for serialization
     */
    public AbstractEnemy() {
        //empty for serialization
    }

    public AbstractEnemy(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
                       String texture, float maxHealth) {
        super(posX, posY, posZ, xLength, yLength, zLength, texture, maxHealth);
    }
}
