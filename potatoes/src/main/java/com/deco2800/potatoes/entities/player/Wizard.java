package com.deco2800.potatoes.entities.player;

import java.util.Map;
import java.util.Optional;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.animation.TimeAnimation;
import com.deco2800.potatoes.entities.enemies.EnemyEntity;
import com.deco2800.potatoes.entities.projectiles.PlayerProjectile;
import com.deco2800.potatoes.entities.projectiles.Projectile.ProjectileTexture;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.managers.SoundManager;
import com.deco2800.potatoes.util.WorldUtil;

public class Wizard extends Player {


    /**
     * Creates a new Archer instance.
     *
     * @param posX The x-coordinate.
     * @param posY The y-coordinate.
     */

    public Wizard(float posX, float posY) {
        super(posX, posY);
        this.movementSpeed = 0.09f;
        this.currentDirection = Direction.SE;
        this.currentState = PlayerState.IDLE;
        //this.currentAnimation = ;
    }

    private Map<Direction, TimeAnimation> wizardIdleAnimations = makePlayerAnimation("wizard", PlayerState.IDLE, 1, 1, null);
    private Map<Direction, TimeAnimation> wizardAttackAnimations = makePlayerAnimation("wizard", PlayerState.IDLE, 1, 200, this::completionHandler);
    private Map<Direction, TimeAnimation> wizardDamagedAnimations = makePlayerAnimation("wizard", PlayerState.DAMAGED, 1, 200, this::damagedCompletionHandler);

    private Void completionHandler() {
        clearState();
        updateSprites();
        return null;
    }

    private Void damagedCompletionHandler() {
        GameManager.get().getManager(SoundManager.class).playSound("damage.wav");
        clearState();
        updateSprites();
        return null;
    }

    @Override
    public void updateSprites() {
        super.updateSprites();
        switch (this.getState()) {
            case IDLE:
                this.setAnimation(wizardIdleAnimations.get(this.getDirection()));
                break;
            case DAMAGED:
                this.setAnimation(wizardDamagedAnimations.get(this.getDirection()));
                break;
            case ATTACK:
                this.setAnimation(wizardAttackAnimations.get(this.getDirection()));
                break;
            default:
                this.setAnimation(wizardIdleAnimations.get(this.getDirection()));
                break;
        }
    }

    @Override
    public void attack() {
    		super.attack();
        if (this.setState(PlayerState.ATTACK)) {

            GameManager.get().getManager(SoundManager.class).playSound("attack.wav");

            float pPosX = GameManager.get().getManager(PlayerManager.class).getPlayer().getPosX();
            float pPosY = GameManager.get().getManager(PlayerManager.class).getPlayer().getPosY();
            float pPosZ = GameManager.get().getManager(PlayerManager.class).getPlayer().getPosZ();

            Optional<AbstractEntity> target = null;
            target = WorldUtil.getClosestEntityOfClass(EnemyEntity.class, pPosX, pPosY);

            if (target.isPresent()) {
                float targetPosX = target.get().getPosX();
                float targetPosY = target.get().getPosY();

                switch (this.getDirection()) {
                    case N:
                        break;
                    case NE:
                        pPosY -= 1;
                        pPosX += 1.5;
                        break;
                    case E:
                        pPosY -= 1;
                        pPosX += 1.5;
                        break;
                    case SE:
                        pPosX += 1;
                        break;
                    case S:
                        pPosX += 1.2;
                        break;
                    case SW:
                        pPosY += 1;
                        pPosX += 1;
                        break;
                    case W:
                        break;
                    case NW:
                        break;
                    default:
                        break;
                }

                Vector3 startPos = new Vector3(pPosX - 1, pPosY, pPosZ);
                Vector3 endPos = new Vector3(targetPosX, targetPosY, 0);

                GameManager.get().getWorld().addEntity(new PlayerProjectile(target.get().getClass(), startPos, endPos, 8f, 100, ProjectileTexture.ROCKET, null, null,
                        this.getDirection().toString(), PlayerProjectile.PlayerShootMethod.DIRECTIONAL));

            } else if (!target.isPresent()) {
                //Disable shooting when no enemies is present until new fix is found.
            }
        }
    }

    @Override
    public void interact() {
        super.interact();
        if (this.setState(PlayerState.INTERACT)) {
            // Wizard interacts
            GameManager.get().getManager(SoundManager.class).playSound("interact.wav");
        }
    }

}
