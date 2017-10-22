package com.deco2800.potatoes.entities.player;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Direction;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.entities.animation.TimeAnimation;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;
import com.deco2800.potatoes.entities.projectiles.Projectile;
import com.deco2800.potatoes.managers.EventManager;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.SoundManager;

public class Caveman extends Player {
    private Optional<AbstractEntity> target;

	public Caveman() {
		this(0, 0);
	}

	/**
     * Creates a new Caveman instance.
     *
     * @param posX The x-coordinate.
     * @param posY The y-coordinate.
     */
    public Caveman(float posX, float posY) {
    	super(posX, posY);
    	this.defaultSpeed = 0.08f;
        super.setMoveSpeed(defaultSpeed);
		updateSprites();
		super.setYRenderOffset(9);
		progressBar = new ProgressBarEntity("healthBarGreen", "caveIcon", 4);
        projectileTexture= Projectile.ProjectileTexture.AXE;
    }

    /* Caveman Animations */
    private transient Map<Direction, TimeAnimation> cavemanWalkAnimations = makePlayerAnimation("caveman", WALK, 8, 750, null);
    private transient Map<Direction, TimeAnimation> cavemanIdleAnimations = makePlayerAnimation("caveman", IDLE, 1, 1, null);
    private transient Map<Direction, TimeAnimation> cavemanDamagedAnimations = makePlayerAnimation("caveman", DAMAGED, 1, 200, this::damagedCompletionHandler);
    private transient Map<Direction, TimeAnimation> cavemanDeathAnimations = makePlayerAnimation("caveman", DEATH, 3, 300, super::completionHandler);
    private transient Map<Direction, TimeAnimation> cavemanAttackAnimations = makePlayerAnimation("caveman", ATTACK, 5, 200, super::completionHandler);
    private transient Map<Direction, TimeAnimation> cavemanInteractAnimations = makePlayerAnimation("caveman", INTERACT, 5, 400, super::completionHandler);
    
    /**
     * Custom damaged handling for the caveman
     */
    protected Void damagedCompletionHandler() {
        GameManager.get().getManager(SoundManager.class).playSound("damage.wav");
        state = IDLE;
        updateMovingAndFacing();
        return null;
    }

    @Override
    public void updateSprites() {

        switch (super.getState()) {
            case IDLE:
                super.setAnimation(cavemanIdleAnimations.get(super.facing));
                break;
            case WALK:
                super.setAnimation(cavemanWalkAnimations.get(super.facing));
                break;
            case ATTACK:
                super.setAnimation(cavemanAttackAnimations.get(super.facing));
                break;
            case DAMAGED:
                super.setAnimation(cavemanDamagedAnimations.get(super.facing));
                break;
            case DEATH:
                super.setAnimation(cavemanDeathAnimations.get(super.facing));
                break;
            case INTERACT:
                super.setAnimation(cavemanInteractAnimations.get(super.facing));
                break;
            default:
                super.setAnimation(cavemanIdleAnimations.get(super.facing));
                break;
        }
    }

    @Override
    protected void attack() {
        super.attack();

        setMoveSpeedModifier(0);
    }

    // Custom walk sound handling //
    private int stepNumber = 1; // Used for playing left and right foot steps
    private boolean alternateSound = false; // Used for playing alternate sounds
    private TimeEvent<Player> walkSound = TimeEvent.createWithSimpleAction(350, true, this::walkHandler);

    private Void walkHandler() {
        if (alternateSound) {
            GameManager.get().getManager(SoundManager.class).playSound("/walking/walk" + (stepNumber + 2) + ".wav");
        } else {
            GameManager.get().getManager(SoundManager.class).playSound("/walking/walk" + stepNumber + ".wav");
        }

        stepNumber++;
        if (stepNumber == 3) {
            stepNumber = 1;
        }
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
    protected void interact() {
        super.interact();
        // Custom interaction code here
    }

}