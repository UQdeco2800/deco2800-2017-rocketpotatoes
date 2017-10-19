package com.deco2800.potatoes.entities.player;

import java.util.Map;
import java.util.Optional;

import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.collisions.Circle2D;
import com.deco2800.potatoes.collisions.Point2D;
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
        this.defaultSpeed = 0.09f;
        super.setMoveSpeed(defaultSpeed);
        this.facing = Direction.SE;
        this.resetState();
        this.setShadow(shadow);
    }

    private Map<Direction, TimeAnimation> wizardIdleAnimations = makePlayerAnimation("wizard", IDLE, 1, 1, null);
    private Map<Direction, TimeAnimation> wizardAttackAnimations = makePlayerAnimation("wizard", ATTACK, 4, 200, this::completionHandler);
    private Map<Direction, TimeAnimation> wizardDamagedAnimations = makePlayerAnimation("wizard", DAMAGED, 1, 200, this::damagedCompletionHandler);
    private Map<Direction, TimeAnimation> wizardInteractAnimations = makePlayerAnimation("wizard", INTERACT, 6, 450, this::completionHandler);
    private Map<Direction, TimeAnimation> wizardDeathAnimations = makePlayerAnimation("wizard", DEATH, 17, 1700, this::completionHandler);
    
    /**
     * Custom damaged handling for the wizard
     */
    private Void damagedCompletionHandler() {
        GameManager.get().getManager(SoundManager.class).playSound("damage.wav");
        super.resetState();
        super.updateMovingAndFacing();
        return null;
    }
    
    private float hoverTime = 0;	 // A value used to determine the height of the hover
    private static final float HOVER_SPEED = 0.1f; // Rate at which wizard hovers
    private static final float HOVER_HEIGHT = 7.5f; // Max height at which wizard hovers
    
    Circle2D shadow = new Circle2D(getPosX(), getPosY(), 0.4f); // Wizard shadow

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
            case INTERACT:
                super.setAnimation(wizardInteractAnimations.get(super.facing));
                break;
            case DEATH:
                super.setAnimation(wizardDeathAnimations.get(super.facing));
                break;
            default:
                super.setAnimation(wizardIdleAnimations.get(super.facing));
                break;
        }
    }

    @Override
    protected void attack() {
    		super.attack();
        if (this.setState(ATTACK)) {

            GameManager.get().getManager(SoundManager.class).playSound("attack.wav");

            float pPosX = GameManager.get().getManager(PlayerManager.class).getPlayer().getPosX();
            float pPosY = GameManager.get().getManager(PlayerManager.class).getPlayer().getPosY();
            float pPosZ = GameManager.get().getManager(PlayerManager.class).getPlayer().getPosZ();

            Optional<AbstractEntity> target;
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
    
    private void hoverAnimation() {
    	// Update shadow position
		shadow.setY(getPosY() + 0.25f);
		shadow.setX(getPosX() - 0.25f);
		
		if (getMoveSpeed() == defaultSpeed) {
			this.setYRenderOffset((float) (HOVER_HEIGHT*Math.sin(hoverTime)));
    		hoverTime += HOVER_SPEED;
    		shadow.setRadius((float) (0.4 - 0.1*Math.sin(hoverTime)));
		} else {
			this.setYRenderOffset((float) (HOVER_HEIGHT/1.5*Math.sin(hoverTime)));
    		hoverTime += HOVER_SPEED*1.5;
    		shadow.setRadius((float) (0.4 - 0.1*Math.sin(hoverTime)));
		}
    }

    @Override
    protected void interact() {
        super.interact();
        // Custom interaction code here
    }
    
    @Override
    public void onTick(long arg0) {
    		super.onTick(arg0);
    		hoverAnimation();
    }

}
