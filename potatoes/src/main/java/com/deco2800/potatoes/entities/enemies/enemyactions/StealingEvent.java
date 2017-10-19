package com.deco2800.potatoes.entities.enemies.enemyactions;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.entities.Direction;
import com.deco2800.potatoes.entities.enemies.EnemyEntity;
import com.deco2800.potatoes.entities.enemies.SpeedyEnemy;
import com.deco2800.potatoes.entities.trees.ResourceTree;
import com.deco2800.potatoes.managers.EventManager;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.WorldUtil;
import java.util.Collection;
import java.util.Optional;

/**
 * A stealing event from speedy enemy to a target
 *
 **/
public class StealingEvent extends TimeEvent<EnemyEntity> {

    private float range = 1.0f;
    private Class target;

    /**
     * Default constructor for serialization
     */
    public StealingEvent() {
        // Blank comment for the great lord Sonar
    }

    /**
     * Constructor for stealing event, set up to repeat an attack according to
     * attackSpeed
     *
     * @param eventRate
     *            the delay between thefts
     *
     */
    public StealingEvent(int eventRate, Class target) {
        setDoReset(true);
        setResetAmount(eventRate);
        this.target = target;
        reset();
    }

    /**
     * Creates action as per TimeEvent shoots a projectile at small range to
     * simulate stealing
     *
     * @param enemy
     *            The enemy that this melee attack belongs to
     */
    @Override
    public void action(EnemyEntity enemy) {
        Optional<AbstractEntity> target1 = WorldUtil.getClosestEntityOfClass(target, enemy.getPosX(), enemy.getPosY());

		/*Stop event if dead (deathHandler of mortal entity will eventually unregister the event).*/
        if (enemy.isDead()) {
            GameManager.get().getManager(EventManager.class).unregisterEvent(enemy, this);
            setDoReset(false);
        }

        // no target exists or target is out of range
        if (!target1.isPresent() || enemy.distanceTo(target1.get()) > range) {
            return;
        }

        if (target1.get() instanceof ResourceTree) {
            ResourceTree foundTree = (ResourceTree) target1.get();
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
     * @return a copy of this MeleeAttackEvent
     */
    @Override
    public TimeEvent<EnemyEntity> copy() {
        return new StealingEvent(getResetAmount(), this.target);
    }

    /**
     * @return string representation of melee attack
     */
    @Override
    public String toString() {
        return String.format("Steal with %d attackspeed", this.getResetAmount());
    }
}
