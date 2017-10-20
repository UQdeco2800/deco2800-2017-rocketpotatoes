package com.deco2800.potatoes.entities.enemies.enemyactions;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.TimeEvent;
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

    private float range = 1.5f;
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
     * @param attackSpeed
     *            the delay between shots
     *
     */
    public StealingEvent(int attackSpeed, Class target) {
        setDoReset(true);
        setResetAmount(attackSpeed);
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

        // no target exists or target is out of range
        if (!target1.isPresent() || enemy.distanceTo(target1.get()) > range) {
            return;
        }

        /*Might not need to loop through all enemies -- just work with the target1.get()????*/
        Collection<AbstractEntity> entities = GameManager.get().getWorld().getEntities().values();
        for (AbstractEntity entity : entities) {
            if (entity instanceof ResourceTree) {
                if (target1.get().equals(entity)) {
                    if (((ResourceTree) entity).getGatherCount() > 0) {
                        ((ResourceTree) entity).gather(-1);
                    } else {
                        //resource tree has 0 or less resources - tell raccoon its a good boy and move on.
                        if (enemy instanceof SpeedyEnemy) {
                            System.err.println("I'm adding " + entity.toString() + " to my visited trees");
                            ((SpeedyEnemy) enemy).addTreeToVisited((ResourceTree) entity);
                        }
                    }
                }
            }
        }
		/*Stop attacking if dead (deathHandler of mortal entity will eventually unregister the event).*/
        if (enemy.isDead()) {
            GameManager.get().getManager(EventManager.class).unregisterEvent(enemy, this);
            setDoReset(false);
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
