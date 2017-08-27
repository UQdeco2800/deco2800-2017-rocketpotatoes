package com.deco2800.potatoes.entities.Enemies;

import java.util.Optional;

import com.deco2800.potatoes.entities.*;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.WorldUtil;

/**
 * A melee attack from enemy to a target
 *
 * -Implementation inspired by "../trees/TreeProjectileShootEvent" - ty trees team
 **/
public class MeleeAttackEvent extends TimeEvent<EnemyEntity> {

    /**
     * Default constructor for serialization
     */
    public MeleeAttackEvent() {
    }

    /**
     * Constructor for melee attack event, set up to repeat an attack according to attackSpeed
     *
     * @param attackSpeed
     *            the delay between shots
     */
    public MeleeAttackEvent(int attackSpeed) {
        setDoReset(true);
        setResetAmount(attackSpeed);
        reset();
    }

    /**
     * Creates action as per TimeEvent shoots a projectile at small range to simulate melee attack
     *
     * @param enemy
     *          The enemy that this melee attack belongs to
     * */
    public void action(EnemyEntity enemy) {
        Optional<AbstractEntity> target1 = WorldUtil.getClosestEntityOfClass(Player.class, enemy.getPosX(),
                enemy.getPosY());

        // no target exists or target is out of range
        if (!target1.isPresent() || (enemy.distance(target1.get()) > 10)) {
            return;
        }

        /*Currently BallisticProjectile assumes Enemies are the ones being attacked which results in
        * the enemies that are shooting being the ones being damaged (suicidal),
        * solutions:
        *   -create new EnemyMelee attack extending from Projectile (hacky to use melee as projectile?)
        *   -create new EnemyMelee attack (may be duplicating work from Projectile)
        *   -re-work BallisticProjectile*/
        GameManager.get().getWorld().addEntity(new BallisticProjectile(
                enemy.getPosX(), enemy.getPosY(), enemy.getPosZ(), target1, enemy.getBasicStats().getRange(), 1, 1));

        /*If the enemy this attack event belongs to, stop firing
        * !DOES NOT REMOVE EVENT, JUST STOPS  REPEATING IT!*/
        if (enemy.isDead()) {
            setDoReset(false);
        }
    }

    /**
     * @return a copy of this MeleeAttackEvent
     * */
    @Override
    public TimeEvent<EnemyEntity> copy() {
        return new MeleeAttackEvent(getResetAmount());
    }

    /**
     * @return string representation of meleee attack
     * */
    @Override
    public String toString() {
        return String.format("Melee attack with %d attackspeed", this.getResetAmount());
    }
}

