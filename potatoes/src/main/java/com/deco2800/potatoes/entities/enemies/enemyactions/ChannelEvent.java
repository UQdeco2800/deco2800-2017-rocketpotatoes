package com.deco2800.potatoes.entities.enemies.enemyactions;

import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.entities.enemies.EnemyEntity;

/**
 * A channelling time event that represents an enemy 'charging up' before performing another TimeEvent action
 *
 * @author craig
 */
public class ChannelEvent extends TimeEvent<EnemyEntity> {
    private float duration;
    private int rate;
    private TimeEvent<EnemyEntity> channeledEvent;

    /**
     * Default constructor for serialization
     */
    public ChannelEvent() {
        //Empty for serialization purposes
    }

    /**
     * Constructs a new channel TimeEvent.
     *
     * @param eventRate
     *          The rate at which this TimeEvent occurs in terms of number of ticks
     * @param channelDuration
     *          The length of time of the channelling before the channeledEvent
     * @param channeledEvent
     *          The TimeEvent to occur on completion of channelling
     */
    public ChannelEvent(int eventRate, float channelDuration, TimeEvent<EnemyEntity> channeledEvent) {
        setDoReset(true);
        setResetAmount(eventRate);
        this.duration = channelDuration;
        this.rate = eventRate;
        this.channeledEvent = channeledEvent;
    }

    /**
     * The channelling (pausing in place) action
     *
     * @param enemy
     *          The enemy this channelling event belongs to
     */
    public void action(EnemyEntity enemy) {
        float channellingStart = channeledEvent.getProgress() - duration;
        if (enemy.getChannelTimer() % channeledEvent.getProgress() > channellingStart) {
            /*Channel timer has reached the percentage of the channelled Events progress
            where we want the enemy to stop moving*/
            enemy.setMoving(false);
        } else {
            enemy.setMoving(true);
        }
        //Update channelling timer in enemy
        enemy.setChannellingTimer(enemy.getChannelTimer() + this.rate);
    }

    /**
     * @return a copy of this MeleeAttackEvent
     */
    @Override
    public TimeEvent<EnemyEntity> copy() {
        return new ChannelEvent(this.rate, this.duration, channeledEvent);
    }

    /**
     * @return string representation of melee attack
     */
    @Override
    public String toString() {
        return String.format("Channel with %f duration", this.duration);
    }
}
