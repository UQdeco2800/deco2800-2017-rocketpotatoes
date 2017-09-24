package com.deco2800.potatoes.entities.player;

import java.util.Map;
import java.util.Optional;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.animation.TimeAnimation;
import com.deco2800.potatoes.entities.enemies.EnemyEntity;
import com.deco2800.potatoes.entities.projectiles.PlayerProjectile;
import com.deco2800.potatoes.entities.projectiles.Projectile;
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
<<<<<<< HEAD
    private Map<Direction, TimeAnimation> wizardDamagedAnimations = makePlayerAnimation("wizard", PlayerState.damaged, 1, 200, this::completionHandler);

    private Void completionHandler() {
        //TODO: update to use damaged sprites
        // Handle finishing attack
        clearState();
        updateSprites();
        return null;
=======
    private Map<Direction, TimeAnimation> wizardDamagedAnimations = makePlayerAnimation("wizard", PlayerState.damaged, 1, 200, this::damagedCompletionHandler);
    
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
>>>>>>> 1634d943d200d4ddb1df059901c6a73d74f1ed44
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
<<<<<<< HEAD
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

                GameManager.get().getWorld().addEntity(new PlayerProjectile(target.get().getClass(), startPos, endPos, 8f, 100, Projectile.ProjectileTexture.ROCKET, null, null,
                        this.getDirection().toString(), PlayerProjectile.PlayerShootMethod.DIRECTIONAL));


            } else if (!target.isPresent()) {
                //Disable shooting when no enemies is present until new fix is found.
            }
        }
=======
	    // Archer attack
    		if (this.setState(PlayerState.attack)) {
    			
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
	        		GameManager.get().getWorld().addEntity(new PlayerProjectile(target.get().getClass(), pPosX-1, pPosY, pPosZ,  1f, 100, ProjectileType.ROCKET, null,
	                        /*new ExplosionEffect(target1.get().getClass(), target1.get().getPosX() -2, target1.get().getPosY(), target1.get().getPosZ(), 0, 2f)*/null, this.getDirection().toString(),targetPosX,targetPosY));
	        } else if (!target.isPresent()) {
	            //Disable shooting when no enemies is present until new fix is found.
	        }
		}
>>>>>>> 1634d943d200d4ddb1df059901c6a73d74f1ed44
    }

    @Override
    public void interact() {
<<<<<<< HEAD
        // Archer interact
=======
    		super.interact();
	    	if (this.setState(PlayerState.interact)) {
	    		// Wizard interacts
	    		GameManager.get().getManager(SoundManager.class).playSound("interact.wav");
	    	}
>>>>>>> 1634d943d200d4ddb1df059901c6a73d74f1ed44
    }

}
