package com.deco2800.potatoes.entities.player;

import java.util.Map;
import java.util.Optional;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Direction;
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
        super.setMoveSpeed(0.09f);
        this.facing = Direction.SE;
        this.state = IDLE;
        //this.currentAnimation = ;
    }

    private Map<Direction, TimeAnimation> wizardIdleAnimations = makePlayerAnimation("wizard", IDLE, 1, 1, null);
    private Map<Direction, TimeAnimation> wizardAttackAnimations = makePlayerAnimation("wizard", IDLE, 1, 200, super::completionHandler);
    private Map<Direction, TimeAnimation> wizardDamagedAnimations = makePlayerAnimation("wizard", DAMAGED, 1, 200, super::damagedCompletionHandler);


    @Override
    public void updateSprites() {
        super.updateSprites();
        switch (this.getState()) {
            case IDLE:
                super.setAnimation(wizardIdleAnimations.get(super.facing));
                break;
            case DAMAGED:
                super.setAnimation(wizardDamagedAnimations.get(super.facing));
                break;
            case ATTACK:
                super.setAnimation(wizardAttackAnimations.get(super.facing));
                break;
            default:
                super.setAnimation(wizardIdleAnimations.get(super.facing));
                break;
        }
    }

    @Override
    public void attack() {
    		super.attack();
        if (this.setState(ATTACK)) {

            GameManager.get().getManager(SoundManager.class).playSound("attack.wav");

            float pPosX = GameManager.get().getManager(PlayerManager.class).getPlayer().getPosX();
            float pPosY = GameManager.get().getManager(PlayerManager.class).getPlayer().getPosY();
            float pPosZ = GameManager.get().getManager(PlayerManager.class).getPlayer().getPosZ();

            Optional<AbstractEntity> target = null;
            target = WorldUtil.getClosestEntityOfClass(EnemyEntity.class, pPosX, pPosY);

            if (target.isPresent()) {
                float targetPosX = target.get().getPosX();
                float targetPosY = target.get().getPosY();

                switch (super.facing) {
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
                        super.facing.toString(), PlayerProjectile.PlayerShootMethod.DIRECTIONAL));

            }
        }
    }
    

}
