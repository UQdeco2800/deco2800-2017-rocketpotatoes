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
public class MeleeAttackEvent extends TimeEvent<AbstractEnemy>{

    /**
     * Default constructor for serialization
     */
    public MeleeAttackEvent() {
    }

    public MeleeAttackEvent(int attackSpeed) {
        setDoReset(true);
        setResetAmount(attackSpeed);
        reset();
    }

    public void action(AbstractEnemy enemy) {
        Optional<AbstractEntity> target1 = WorldUtil.getClosestEntityOfClass(Tower.class, enemy.getPosX(),
                enemy.getPosY());

        //target is out of range
        if (!target1.isPresent() || (enemy.distance(target1.get()) > 10)) {
            return;
        }

        GameManager.get().getWorld().addEntity(new BallisticProjectile(
                enemy.getPosX(), enemy.getPosY(), enemy.getPosZ(), target1, 10, 10));
    }

    @Override
    public TimeEvent<AbstractEnemy> copy() {
        return new MeleeAttackEvent(getResetAmount());
    }
}
