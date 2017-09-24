package com.deco2800.potatoes.entities.projectiles;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.WorldUtil;

import java.util.Optional;

public class PlayerProjectile extends Projectile {
    protected float pPosX;
    protected float pPosY;
    protected float tPosX;
    protected float tPosY;
    protected String Directions;
    protected PlayerShootMethod shootingStyle;

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
     * Creates a new projectile. A projectile is the vehicle used to deliver damage
     * to a target over a distance
     *
     * @param targetClass       the targets class
     * @param startPos
     * @param targetPos
     * @param range
     * @param damage            damage of projectile
     * @param projectileTexture the texture set to use for animations. Use ProjectileTexture._
     * @param startEffect       the effect to play at the start of the projectile being fired
     * @param endEffect         the effect to be played if a collision occurs
     * @param Directions        Player Directions
     * @param shootingStyle     Directional, Homing or Ballistic?
     */

    public PlayerProjectile(Class<?> targetClass, Vector3 startPos, Vector3 targetPos, float range, float damage, ProjectileTexture projectileTexture, Effect startEffect,
                            Effect endEffect, String Directions, PlayerShootMethod shootingStyle) {
        super(targetClass, startPos, targetPos, range, damage, projectileTexture, startEffect, endEffect);

        this.pPosX = startPos.x;
        this.pPosY = startPos.y;
        this.tPosX = targetPos.x;
        this.tPosY = targetPos.y;
        this.Directions = Directions;
        this.shootingStyle = shootingStyle;
        ShootingStyle(shootingStyle);

    }


    @Override
    public void onTick(long time) {
        if (shootingStyle.toString().equalsIgnoreCase("HOMING")) {
            Optional<AbstractEntity> targetEntity = WorldUtil.getClosestEntityOfClass(targetClass, targetPos.x, targetPos.y);
            if (targetEntity.isPresent()) {
                setTargetPosition(targetEntity.get().getPosX(), targetEntity.get().getPosY(), targetEntity.get().getPosZ());
            } else {
                GameManager.get().getWorld().removeEntity(this);
            }
            updatePosition();
        }
        super.onTick(time);
    }

    /**
     * Returns selected shooting styles
     */
    public PlayerProjectile.PlayerShootMethod getPlayerShootMethod() {
        return shootingStyle;
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

    /**
     * Returns Target Pos X
     */
    public float getTargetPosX() {
        return tPosX;
    }

    /**
     * Returns Target Pos Y
     */
    public float getTargetPosY() {
        return tPosY;
    }
}
