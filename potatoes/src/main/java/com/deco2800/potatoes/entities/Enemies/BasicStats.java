package com.deco2800.potatoes.entities.Enemies;

import com.deco2800.potatoes.entities.Squirrel;
import com.deco2800.potatoes.entities.TimeEvent;

import java.util.LinkedList;
import java.util.List;

/**
 * Class to represent attributes for enemy stats
 *
 * (implementation inspired by trees/UpgradeStats - THANK YOU TREES TEAM)
 */
public class BasicStats {

    /*Example stats for an enemy*/
    private float speed = 0.1f;
    private float health = 100f;
    private float range = 0;
    private Class<?> goal = Squirrel.class;

    private List<TimeEvent<AbstractEnemy>> normalEvents = new LinkedList<>();
    private String texture = "";

    /**
     * Default constructor for serialization
     */
    public BasicStats() {
    }

    /**
     * Constructor for basic stats of an enemy
     * @param health
     *          The enemy's starting health
     * @param speed
     *          The enemy's starting speed
     * @param range
     *          The enemy's starting attack range
     * @param normalEvents
     *          The enemy's starting normalEvents
     * @param texture
     *          The enemy's starting texture
     */
    public BasicStats(float health, float speed, float range, List<TimeEvent<AbstractEnemy>> normalEvents, String texture) {
        this.health = health;
        this.speed = speed;
        this.range = range;

        this.normalEvents = normalEvents;
        this.texture = texture;
    }

    /**
     *
     * @return return's enemy's current health
     */
    public float getHealth() {
        return this.health;
    }

    /**
     *
     * @return returns enemy's current speed
     */
    public float getSpeed() {
        return this.speed;
    }

    /**
     * @return return enemy's current attack range
     */
    public float getRange() {
        return this.range;
    }

    /*RETURNS A REFERENCE*/
    /**
     * @return returns a reference to the normal events list of these stats
     */
    public List<TimeEvent<AbstractEnemy>> getNormalEvents() {
        return normalEvents;
    }

    /**
     * @return return enemy's current texture
     */
    public String getTexture() {
        return this.texture;
    }









}
