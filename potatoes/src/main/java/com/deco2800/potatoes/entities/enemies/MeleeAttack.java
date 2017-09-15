package com.deco2800.potatoes.entities.enemies;

import java.util.Optional;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.entities.effects.SwipeEffect;
import com.deco2800.potatoes.entities.projectiles.Projectile;

public class MeleeAttack extends Projectile {

   

    private final static float effect_width = 1f;
    private final static float effect_height = 1f;

    public MeleeAttack() {
        // empty for serialization
    }

    /**
     * Creates a melee attack. Homing Projectiles changes direction once
     * fired. The initial direction is based on the direction to the closest entity
     * and follows it.
     *
     * @param posX
     *            x start position
     * @param posY
     *            y start position
     * @param posZ
     *            z start position
     * @param target
     *            Entity target object
     * @param range
     *            Projectile range
     * @param DAMAGE
     *            Projectile damage
     */
//SwipeEffect swipe = new SwipeEffect(goalX - (effect_width / 2), goalY - (effect_height / 2) + 1, 0,
//    effect_width, effect_height, 0, effect_width, effect_height);
    public MeleeAttack(Class<?> targetClass, float posX, float posY, float posZ, Optional<AbstractEntity> target, float DAMAGE, Effect endEffect) {
//    	super(targetClass,  posX,  posY,  posZ,  targetPosX,  targetPosY,
//			 targetPosZ,  range,  damage,  xRenderLength,  yRenderLength, endEffect);
    	SwipeEffect swipe = new SwipeEffect(goalX - (effect_width / 2), goalY - (effect_height / 2) + 1, 0,
      effect_width, effect_height, 0, effect_width, effect_height);
    }

    @Override
    public float rotationAngle() {
        return rotationAngle;
    }

    @Override
    public void onTick(long time) {

       // super.onTick(time);

    }

    @Override
    public float getDamage() {
        return damage;
    }

    /**
     * Remove projectile after waiting a certain amount of ticks*/
    private static void removeProjectileLater(int ticks, Projectile projectile) {

    }
}



/*
*         for (AbstractEntity entity : entities.values()) {
            if (targetClass.isInstance(entity)) {
                if (newPos.overlaps(entity.getBox3D())) {
                    if (ticksWaited == 0) {
                        ((MortalEntity) entity).damage(DAMAGE);
                        ExplosionEffect expEffect = new ExplosionEffect(goalX, goalY, goalZ, 5f, 5f, 0, 1f, 1f);
                        GameManager.get().getWorld().addEntity(expEffect);
                        ticksWaited++;
                    } else if (ticksWaited < 3) {
                        ticksWaited++;
                    } else {
                        GameManager.get().getWorld().removeEntity(this);
                        ticksWaited = 0;
                    }
                }
            }
        }
*/