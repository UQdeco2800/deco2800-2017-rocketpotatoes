package com.deco2800.potatoes.entities.player;

import java.util.Map;
import java.util.Optional;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.HasDirection;
import com.deco2800.potatoes.entities.HasDirection.Direction;
import com.deco2800.potatoes.entities.animation.TimeAnimation;
import com.deco2800.potatoes.entities.enemies.EnemyEntity;
import com.deco2800.potatoes.entities.player.Player.PlayerState;
import com.deco2800.potatoes.entities.projectiles.PlayerProjectile;
import com.deco2800.potatoes.entities.projectiles.Projectile;
import com.deco2800.potatoes.entities.projectiles.Projectile.ProjectileType;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.util.WorldUtil;

public class Wizard extends Player {


    /**
     * Creates a new Archer instance.
     *
     * @param posX The x-coordinate.
     * @param posY The y-coordinate.
     * @param posZ The z-coordinate.
     */
    public Wizard(float posX, float posY, float posZ) {
        super(posX, posY, posZ);
        this.movementSpeed = 0.09f;
        this.currentDirection = Direction.SouthEast;
        this.currentState = PlayerState.idle;
        //this.currentAnimation = ;
    }

    private Map<Direction, TimeAnimation> wizardIdleAnimations = makePlayerAnimation("wizard", PlayerState.idle, 1, 1, null);
    private Map<Direction, TimeAnimation> wizardAttackAnimations = makePlayerAnimation("wizard", PlayerState.idle, 1, 200, this::completionHandler);
    private Map<Direction, TimeAnimation> wizardDamagedAnimations = makePlayerAnimation("wizard", PlayerState.damaged, 1, 200, this::completionHandler);

    private Void completionHandler() {
        //TODO: update to use damaged sprites
        // Handle finishing attack
        clearState();
        updateSprites();
        return null;
    }

    @Override
    public void updateSprites() {
        super.updateSprites();
        switch (this.getState()) {
            case idle:
                this.setAnimation(wizardIdleAnimations.get(this.getDirection()));
                break;
            case damaged:
                this.setAnimation(wizardDamagedAnimations.get(this.getDirection()));
                break;
            case attack:
                this.setAnimation(wizardAttackAnimations.get(this.getDirection()));
                break;
            default:
                this.setAnimation(wizardIdleAnimations.get(this.getDirection()));
                break;
        }
    }

    @Override
    public void attack() {
        // Archer attack
        if (this.setState(PlayerState.attack)) {
            float pPosX = GameManager.get().getManager(PlayerManager.class).getPlayer().getPosX();
            float pPosY = GameManager.get().getManager(PlayerManager.class).getPlayer().getPosY();
            float pPosZ = GameManager.get().getManager(PlayerManager.class).getPlayer().getPosZ();

            Optional<AbstractEntity> target = null;
            target = WorldUtil.getClosestEntityOfClass(EnemyEntity.class, pPosX, pPosY);

            if (target.isPresent()) {
                float targetPosX = target.get().getPosX();
                float targetPosY = target.get().getPosY();

                switch (this.getDirection()) {
                    case North:
                        break;
                    case NorthEast:
                        pPosY -= 1;
                        pPosX += 1.5;
                        break;
                    case East:
                        pPosY -= 1;
                        pPosX += 1.5;
                        break;
                    case SouthEast:
                        pPosX += 1;
                        break;
                    case South:
                        pPosX += 1.2;
                        break;
                    case SouthWest:
                        pPosY += 1;
                        pPosX += 1;
                        break;
                    case West:
                        break;
                    case NorthWest:
                        break;
                    default:
                        break;
                }
                Vector3 startPos = new Vector3(pPosX - 1, pPosY, pPosZ);
                Vector3 endPos = new Vector3(targetPosX, targetPosY, 0);

                GameManager.get().getWorld().addEntity(new PlayerProjectile(target.get().getClass(), startPos, endPos, 8f, 100, ProjectileType.ROCKET, null, null,
                        this.getDirection().toString(), Projectile.ShootingStyles.PLAYERDIRECTIONALPROJECTILE));


            } else if (!target.isPresent()) {
                //Disable shooting when no enemies is present until new fix is found.
            }
        }
    }

    @Override
    public void interact() {
        // Archer interact
    }

}
