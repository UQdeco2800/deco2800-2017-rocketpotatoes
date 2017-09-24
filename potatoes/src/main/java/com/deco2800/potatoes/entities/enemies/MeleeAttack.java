package com.deco2800.potatoes.entities.enemies;

import com.deco2800.potatoes.collisions.Circle2D;
import com.deco2800.potatoes.collisions.Box2D;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.effects.ExplosionEffect;
import com.deco2800.potatoes.entities.effects.SwipeEffect;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.entities.projectiles.Projectile;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.Box3D;

import java.util.Map;
import java.util.Optional;

public class MeleeAttack extends Projectile {

    private final static transient String TEXTURE = "empty";
    private float DAMAGE = 1;

    private float goalX;
    private float goalY;
    private float goalZ;

    private int rotateAngle = 0;

    private float range = 3f;

    private final float speed = 1f;

    private float changeX;
    private float changeY;
    private Optional<AbstractEntity> mainTarget;
    private boolean maxRange = false;

    private int rocketEffectTimer;
    private int rocketCurrentSpriteIndexCount = 0;
    private String[] rocketSpriteArray = { "swipe1", "swipe2", "swipe3", "swipe4", "swipe5" };

    private Class<?> targetClass;

    private final static float effect_width = 1f;
    private final static float effect_height = 1f;

    public MeleeAttack() {
        // empty for serialization
        DAMAGE = 1;
        rotateAngle = 0;
        maxRange = false;
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

    public MeleeAttack(Class<?> targetClass, float posX, float posY, Optional<AbstractEntity> target, float DAMAGE) {
        // TODO -- find the correct collision mask for this
        super(new Circle2D(posX, posY, 1.414f), TEXTURE);
        this.DAMAGE = DAMAGE;
        this.mainTarget = target;
        this.goalX = target.get().getPosX();
        this.goalY = target.get().getPosY();

        this.targetClass = targetClass;

        float deltaX = getPosX() - goalX;
        float deltaY = getPosY() - goalY;

        float angle = (float) (Math.atan2(deltaY, deltaX)) + (float) (Math.PI);

        changeX = (float) (speed * Math.cos(angle));
        changeY = (float) (speed * Math.sin(angle));

        rotateAngle = (int) ((angle * 180 / Math.PI) + 45 + 90);
    }

    @Override
    public int rotateAngle() {
        return rotateAngle;
    }

    @Override
    public void onTick(long time) {

        if (mainTarget != null) {
            this.goalX = mainTarget.get().getPosX();
            this.goalY = mainTarget.get().getPosY();
        } else {
            GameManager.get().getWorld().removeEntity(this);
        }

        float deltaX = getPosX() - this.goalX;
        float deltaY = getPosY() - this.goalY;

        float angle = (float) (Math.atan2(deltaY, deltaX)) + (float) (Math.PI);

        changeX = (float) (speed * Math.cos(angle));
        changeY = (float) (speed * Math.sin(angle));

        setPosX(getPosX() + changeX);
        setPosY(getPosY() + changeY);

        //Has projectile reached its max range
        if (range < speed) {
            maxRange = true;
        }

        range -= speed;

        rotateAngle = (int) ((angle * 180 / Math.PI) + 45 + 90);

        Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();
        // Check surroundings
        for (AbstractEntity entity : entities.values()) {
            if (targetClass.isInstance(entity)) {
                ((MortalEntity) entity).damage(DAMAGE);
                //ExplosionEffect expEffect = new ExplosionEffect(goalX, goalY, goalZ, 5f, 5f, 0, 1f, 1f);
                //GameManager.get().getWorld().addEntity(expEffect);

                // TODO -- find correct collision mask for the swipe effect
                SwipeEffect swipe = new SwipeEffect(new Box2D(goalX, goalY + 1, effect_width, effect_height),
                        effect_width, effect_height);
                GameManager.get().getWorld().addEntity(swipe);


                GameManager.get().getWorld().removeEntity(this);
                }

        }
        if (maxRange) {
            GameManager.get().getWorld().removeEntity(this);
        }

    }

    @Override
    public float getDamage() {
        return DAMAGE;
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