package com.deco2800.potatoes.entities.enemies.enemyactions;

import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.entities.enemies.EnemyEntity;

public class Channel extends TimeEvent<EnemyEntity> {

    private float duration;
    private int rate;
    private TimeEvent<EnemyEntity> channeledEvent;

    public Channel() {
        //Blank comment for sonar
    }

    public Channel(int rate, float duration, TimeEvent<EnemyEntity> channeledEvent) {
        setDoReset(true);
        setResetAmount(rate);
        this.duration = duration;
        this.rate = rate;
        this.channeledEvent = channeledEvent;
    }

    public void action(EnemyEntity enemy) {
        float channellingStart = getChanneledEvent().getProgress() - duration;
        if (enemy.getChannelTimer() % getChanneledEvent().getProgress() > channellingStart) {
            enemy.setMoving(false);
            //animation??
        } else {
            enemy.setMoving(true);
        }
        //Update channelling timer in enemy
        enemy.setChannellingTimer(enemy.getChannelTimer() + this.rate);
    }

    private TimeEvent<EnemyEntity> getChanneledEvent() { return this.channeledEvent; }

    /**
     * @return a copy of this MeleeAttackEvent
     */
    @Override
    public TimeEvent<EnemyEntity> copy() {
        return new Channel(this.rate, this.duration, getChanneledEvent());
    }

    /**
     * @return string representation of melee attack
     */
    @Override
    public String toString() {
        return String.format("Channel with %d duration", this.duration);
    }
}
