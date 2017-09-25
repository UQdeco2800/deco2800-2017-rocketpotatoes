package com.deco2800.potatoes.entities.player;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.entities.TimeTriggerEvent;
import com.deco2800.potatoes.entities.animation.TimeAnimation;
import com.deco2800.potatoes.entities.enemies.EnemyEntity;
import com.deco2800.potatoes.entities.projectiles.PlayerProjectile;
import com.deco2800.potatoes.entities.projectiles.Projectile;
import com.deco2800.potatoes.managers.EventManager;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.managers.SoundManager;
import com.deco2800.potatoes.util.WorldUtil;

public class Caveman extends Player {

	/**

     * Creates a new Caveman instance.
     *
     * @param posX The x-coordinate.
     * @param posY The y-coordinate.
     */
    public Caveman(float posX, float posY) {
    		super(posX, posY);
    		this.movementSpeed = 0.08f;
    		this.currentDirection = Direction.SouthEast;
        this.currentState = PlayerState.idle;
        //this.currentAnimation = ;
    }
    
    /* Caveman Animations */
    private Map<Direction, TimeAnimation> cavemanWalkAnimations = makePlayerAnimation("caveman", PlayerState.walk, 8, 750, null);
    private Map<Direction, TimeAnimation> cavemanIdleAnimations = makePlayerAnimation("caveman", PlayerState.idle, 1, 1, null);
    private Map<Direction, TimeAnimation> cavemanDamagedAnimations = makePlayerAnimation("caveman", PlayerState.damaged, 1, 200, this::damagedCompletionHandler);
    private Map<Direction, TimeAnimation> cavemanDeathAnimations = makePlayerAnimation("caveman", PlayerState.death, 3, 300, this::completionHandler);
    private Map<Direction, TimeAnimation> cavemanAttackAnimations = makePlayerAnimation("caveman", PlayerState.attack, 5, 200, this::completionHandler);
    private Map<Direction, TimeAnimation> cavemanInteractAnimations = makePlayerAnimation("caveman", PlayerState.interact, 5, 400, this::completionHandler);
    
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
            case idle:
        			this.setAnimation(cavemanIdleAnimations.get(this.getDirection()));
        			break;
            case walk:
            		this.setAnimation(cavemanWalkAnimations.get(this.getDirection()));
            		break;
            case attack:
            		this.setAnimation(cavemanAttackAnimations.get(this.getDirection()));
        			break;
            case damaged:
            		this.setAnimation(cavemanDamagedAnimations.get(this.getDirection()));
        			break;
            case death:
            		this.setAnimation(cavemanDeathAnimations.get(this.getDirection()));
            		break;
            case interact:
        			this.setAnimation(cavemanInteractAnimations.get(this.getDirection()));
        			break;
            default:
            		this.setAnimation(cavemanIdleAnimations.get(this.getDirection()));
            		break;
            }
    }
    
    @Override
    public void attack() {
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
					Vector3 startPos = new Vector3(pPosX - 1, pPosY, pPosZ);
					Vector3 endPos = new Vector3(targetPosX, targetPosY, 0);

					GameManager.get().getWorld().addEntity(new PlayerProjectile(target.get().getClass(), startPos, endPos, 8f, 100, Projectile.ProjectileTexture.CHILLI, null, null,
							this.getDirection().toString(), PlayerProjectile.PlayerShootMethod.DIRECTIONAL));
    	        } else if (!target.isPresent()) {
    	            //Disable shooting when no enemies is present until new fix is found.
    	        }
    		}
    }
    
    /* Custom walk sound handling */
	private int stepNumber = 1;	// Used for playing left and right foot steps
	private boolean alternateSound = false;	// Used for playing alternate sounds
	private TimeTriggerEvent walkSound = new TimeTriggerEvent(350, true, this::walkHandler);
	private Void walkHandler() {
		if (alternateSound) {
			GameManager.get().getManager(SoundManager.class).playSound("/walking/walk" + (stepNumber+2) + ".wav");
		} else {
			GameManager.get().getManager(SoundManager.class).playSound("/walking/walk" + stepNumber + ".wav");
		}
		
		stepNumber++;
		if (stepNumber == 3) { stepNumber = 1; }
		alternateSound = new Random().nextBoolean();
		return null;
	}
    
    @Override
	protected void walk(boolean active) {
		super.walk(active);
		if (active) {
			// Caveman starts walking
			GameManager.get().getManager(EventManager.class).registerEvent(this, walkSound);
		} else {
			// Caveman stops walking
			GameManager.get().getManager(EventManager.class).unregisterEvent(this, walkSound);
		}
	}
    
    @Override
    public void interact() {
    		super.interact();
	    	if (this.setState(PlayerState.interact)) {
	    		// Caveman interacts
	    		GameManager.get().getManager(SoundManager.class).playSound("interact.wav");
	    	}
    }

}
