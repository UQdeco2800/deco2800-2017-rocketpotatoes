package com.deco2800.potatoes.entities.player;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.TimeEvent;
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
    	this.moveSpeed = 0.08f;
    	this.currentDirection = Direction.SE;
        this.currentState = PlayerState.IDLE;
        //this.currentAnimation = ;

		this.setYRenderOffset(9);
    }
    
    /* Caveman Animations */
    private Map<Direction, TimeAnimation> cavemanWalkAnimations = makePlayerAnimation("caveman", PlayerState.WALK, 8, 750, null);
    private Map<Direction, TimeAnimation> cavemanIdleAnimations = makePlayerAnimation("caveman", PlayerState.IDLE, 1, 1, null);
    private Map<Direction, TimeAnimation> cavemanDamagedAnimations = makePlayerAnimation("caveman", PlayerState.DAMAGED, 1, 200, this::damagedCompletionHandler);
    private Map<Direction, TimeAnimation> cavemanDeathAnimations = makePlayerAnimation("caveman", PlayerState.DEATH, 3, 300, this::completionHandler);
    private Map<Direction, TimeAnimation> cavemanAttackAnimations = makePlayerAnimation("caveman", PlayerState.ATTACK, 5, 200, this::completionHandler);
    private Map<Direction, TimeAnimation> cavemanInteractAnimations = makePlayerAnimation("caveman", PlayerState.INTERACT, 5, 400, this::completionHandler);
    
    private Void completionHandler() {
    		setWalkEnabled(true); // Re-enable walking
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
        			this.setAnimation(cavemanIdleAnimations.get(this.getDirection()));
        			break;
            case WALK:
            		this.setAnimation(cavemanWalkAnimations.get(this.getDirection()));
            		break;
            case ATTACK:
            		this.setAnimation(cavemanAttackAnimations.get(this.getDirection()));
        			break;
            case DAMAGED:
            		this.setAnimation(cavemanDamagedAnimations.get(this.getDirection()));
        			break;
            case DEATH:
            		this.setAnimation(cavemanDeathAnimations.get(this.getDirection()));
            		break;
            case INTERACT:
        			this.setAnimation(cavemanInteractAnimations.get(this.getDirection()));
        			break;
            default:
            		this.setAnimation(cavemanIdleAnimations.get(this.getDirection()));
            		break;
            }
    }
    
    @Override
    public void attack() {
    		super.attack();
    		setWalkEnabled(false); // Stop walking for attacking
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
	private TimeEvent<Player> walkSound = TimeEvent.createWithSimpleAction(350, true, this::walkHandler);
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
    		setWalkEnabled(false); // Stop walking for interacting
	    	if (this.setState(PlayerState.INTERACT)) {
	    		// Caveman interacts
	    		GameManager.get().getManager(SoundManager.class).playSound("interact.wav");
	    	}
    }

}
