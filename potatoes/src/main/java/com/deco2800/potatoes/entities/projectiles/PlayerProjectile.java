package com.deco2800.potatoes.entities.projectiles;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.effects.Effect;

public class PlayerProjectile extends Projectile {
    protected float pPosX;
    protected float pPosY;
    protected float tPosX;
    protected float tPosY;

    public enum PlayerShootMethod {
        DIRECTIONAL {

        },
        HOMING {

        },
        BALLISTIC {

        }

    }


    public PlayerProjectile() {
        //Blank comment to please the lord Sonar
    }


    /**
     * Creates a new Ballistic Projectile. Ballistic Projectiles do not change
     * direction once fired. The initial direction is based on the direction to the
     * closest entity
     *
     * @param posX x start position
     * @param posY y start position
     * @param posZ z start position
     */

    public PlayerProjectile(Class<?> targetClass, Vector3 startPos, Vector3 targetPos, float range, float damage, ProjectileTexture projectileTexture, Effect startEffect,
                            Effect endEffect, String Directions, PlayerShootMethod shootingStyle) {
        super(targetClass, startPos, targetPos, range, damage, projectileTexture, startEffect, endEffect, Directions, shootingStyle);

        this.pPosX = startPos.x;
        this.pPosY = startPos.y;
        this.tPosX = targetPos.x;
        this.tPosY = targetPos.y;
        ShootingStyle(shootingStyle);

    }


    @Override
    public void onTick(long time) {
        super.onTick(time);
    }

    public PlayerProjectile.PlayerShootMethod getPlayerShootMethod() {
        return playerShootMethod;
    }

    public void ShootingStyle(PlayerShootMethod shootingStyle) {

        /**
         * Shoots enemies base on the player directions
         */
        if (shootingStyle.toString().equalsIgnoreCase("DIRECTIONAL")) {
            if (Directions.equalsIgnoreCase("w")) {
                setTargetPosition(pPosX - 5, pPosY - 5, 0);
                // setTargetPosition(TargetPosX, TargetPosY, posZ);
                updatePosition();
                setPosition();
            } else if (Directions.equalsIgnoreCase("e")) {
                setTargetPosition(pPosX + 5, pPosY + 5, 0);
                updatePosition();
                setPosition();
                // setTargetPosition(TargetPosX, TargetPosY, posZ);
            } else if (Directions.equalsIgnoreCase("n")) {
                setTargetPosition(pPosX + 15, pPosY - 15, 0);
                updatePosition();
                setPosition();
            } else if (Directions.equalsIgnoreCase("s")) {
                setTargetPosition(pPosX - 15, pPosY + 15, 0);
                updatePosition();
                setPosition();
            } else if (Directions.equalsIgnoreCase("ne")) {
                setTargetPosition(pPosX + 15, pPosY + 1, 0);
                updatePosition();
                setPosition();
            } else if (Directions.equalsIgnoreCase("nw")) {
                setTargetPosition(pPosX - 15, pPosY - 200, 0);
                updatePosition();
                setPosition();
            } else if (Directions.equalsIgnoreCase("se")) {
                setTargetPosition(pPosX + 20, pPosY + 200, 0);
                updatePosition();
                setPosition();
            } else if (Directions.equalsIgnoreCase("sw")) {
                setTargetPosition(pPosX - 200, pPosY - 20, 0);
                updatePosition();
                setPosition();
            }
        }

    }

    public float getTargetPosX() {
        return tPosX;
    }

    public float getTargetPosY() {
        return tPosY;
    }
}
