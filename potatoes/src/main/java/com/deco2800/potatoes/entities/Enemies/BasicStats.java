package com.deco2800.potatoes.entities.Enemies;

import com.deco2800.potatoes.entities.EnemyEntity;
import com.deco2800.potatoes.entities.Squirrel;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.managers.EventManager;
import com.deco2800.potatoes.managers.GameManager;

import java.util.LinkedList;
import java.util.List;

/**
 * Class to represent attributes for enemy stats
 *
 * (broad implementation of enemy stats inspired by trees upgrade stats (trees/UpgradeStats) - thanks trees team)
 */
public class BasicStats {

    /*Example stats for an enemy*/
    private float speed = 100f;
    private float attackSpeed = 500;
    private float health = 100f;
    private float range = 0;

    private List<TimeEvent<EnemyEntity>> normalEvents = new LinkedList<>();
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
    public BasicStats(float health, float speed, float range, float attackSpeed, List<TimeEvent<EnemyEntity>> normalEvents, String texture) {
        this.health = health;
        this.speed = speed;
        this.range = range;
        this.attackSpeed = attackSpeed;
        this.normalEvents = normalEvents;
        this.normalEvents = getNormalEventsCopy();
        this.texture = texture;
    }



    /**
     * @return A deep copy of the normal events associated with these stats
     * */
    public List<TimeEvent<EnemyEntity>> getNormalEventsCopy() {
        List<TimeEvent<EnemyEntity>> result = new LinkedList<>();
        for (TimeEvent<EnemyEntity> timeEvent : normalEvents) {
            result.add(timeEvent.copy());
        }
        return result;
    }

    /**
     * @return returns a reference to the normal events list of these stats
     */
    public List<TimeEvent<EnemyEntity>> getNormalEventsReference() {
        return normalEvents;
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
     * @return returns enemy's current attack speed
     */
    public float getSpeed() {
        return this.attackSpeed;
    }

    /**
     *
     * @return returns enemy's current attack speed
     */
    public float getAttackSpeed() {
        return this.attackSpeed;
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
    public List<TimeEvent<EnemyEntity>> getNormalEvents() {
        return normalEvents;
    }

    /**
     * @return return enemy's current texture
     */
    public String getTexture() {
        return this.texture;
    }









}
