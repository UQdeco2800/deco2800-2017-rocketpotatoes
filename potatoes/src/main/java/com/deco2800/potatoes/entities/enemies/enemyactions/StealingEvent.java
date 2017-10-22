package com.deco2800.potatoes.entities.enemies.enemyactions;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.entities.enemies.EnemyEntity;
import com.deco2800.potatoes.entities.enemies.SpeedyEnemy;
import com.deco2800.potatoes.entities.trees.ResourceTree;
import com.deco2800.potatoes.managers.EventManager;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.WorldUtil;
import java.util.Optional;

/**
 * A stealing TimeEvent which, when belonging to an enemy, allows the enemy to steal from resource trees.
 *
 * @author tl & craig
 **/
public class StealingEvent extends TimeEvent<EnemyEntity> {

    private float range = .8f;

    /**
     * Default constructor for serialization
     */
    public StealingEvent() {
        // Empty for serialization purposes
    }

    /**
     * Constructor for stealing event which when held by an enemy will cause it to steal resources
     * from a resource tree if within range. The event repeats every eventRate number of game ticks
     *
     * @param eventRate
     *            the number of ticks between thefts
     */
    public StealingEvent(int eventRate) {
        setDoReset(true);
        setResetAmount(eventRate);
        reset();
    }

    /**
     * The stealing action to occur when this event is triggered. The closest resource tree to the event's enemy
     * is searched for and if it is present and within a close enough range, will cause the enemy to stop, face the
     * tree and remove 1 unit of resources. The enemy repeats this until the tree has no more resources and moves
     * on.
     *
     * @param enemy
     *            The enemy that this steal action belongs to
     */
    @Override
    public void action(EnemyEntity enemy) {
        Optional<AbstractEntity> target = WorldUtil.getClosestEntityOfClass(ResourceTree.class, enemy.getPosX(), enemy.getPosY());

        // no target exists or target is out of range
        if (!target.isPresent() || enemy.distanceTo(target.get()) > range) {
            return;
        }

        if (target.get() instanceof ResourceTree) {
            ResourceTree foundTree = (ResourceTree) target.get();
            if (foundTree.getGatherCount() > 0) {
                foundTree.gather(-1);
                enemy.setDirectionToCoords(foundTree.getPosX(), foundTree.getPosY());
                enemy.setMoving(false);
            } else {
                //resource tree has 0 or less resources - tell raccoon its a good boy and move on.
                if (enemy instanceof SpeedyEnemy) {
                    ((SpeedyEnemy) enemy).addTreeToVisited(foundTree);
                    enemy.setMoving(true);
                }
            }
        }
    }

    /**
     * @return a copy of this StealingEvent
     */
    @Override
    public TimeEvent<EnemyEntity> copy() {
        return new StealingEvent(getResetAmount());
    }

    /**
     * @return string representation of stealing event
     */
    @Override
    public String toString() {
        return String.format("Steal occurring every %d ticks", this.getResetAmount());
    }
}
