package com.deco2800.potatoes.entities.player;

import java.util.Map;
import java.util.Optional;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Direction;
import com.deco2800.potatoes.entities.animation.TimeAnimation;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;
import com.deco2800.potatoes.entities.projectiles.BallisticProjectile;
import com.deco2800.potatoes.entities.projectiles.Projectile;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.SoundManager;

public class Archer extends Player {
	private Optional<AbstractEntity> target;
	/**
     * Creates a new Archer instance.
     *
     * @param posX The x-coordinate.
     * @param posY The y-coordinate.
     */
    public Archer(float posX, float posY) {
    		super(posX, posY);
    		this.defaultSpeed = 0.07f;
    		projectileClass = BallisticProjectile.class;
            super.setMoveSpeed(defaultSpeed);
    		this.facing = Direction.SE;
        this.resetState();
        progressBar = new ProgressBarEntity("healthBarGreen", "archerIcon", 4);
		projectileTexture= Projectile.ProjectileTexture.ARROW;
    }

	private Map<Direction, TimeAnimation> archerIdleAnimations = makePlayerAnimation("archer", IDLE, 1, 1, null);
	private Map<Direction, TimeAnimation> archerWalkAnimations = makePlayerAnimation("archer", WALK, 8, 750, null);
	private Map<Direction, TimeAnimation> archerAttackAnimations = makePlayerAnimation("archer", ATTACK, 5, 200,
			super::completionHandler);
	private Map<Direction, TimeAnimation> archerDamagedAnimations = makePlayerAnimation("archer", DEATH, 3, 200,
			this::damagedCompletionHandler);
	private Map<Direction, TimeAnimation> archerInteractAnimations = makePlayerAnimation("archer", INTERACT, 5, 400,
			super::completionHandler);

	/**
	 * Custom damaged handling for the archer
	 */
	public Void damagedCompletionHandler() {
		GameManager.get().getManager(SoundManager.class).playSound("damage.wav");
		state = IDLE;
		updateMovingAndFacing();
		return null;
	}

	@Override
	public void updateSprites() {
		super.updateSprites();
		switch (this.getState()) {
			case IDLE:
				super.setAnimation(archerIdleAnimations.get(super.facing));
				break;
			case WALK:
				super.setAnimation(archerWalkAnimations.get(super.facing));
				break;
			case ATTACK:
				super.setAnimation(archerAttackAnimations.get(super.facing));
				break;
			case DAMAGED:
				super.setAnimation(archerDamagedAnimations.get(super.facing));
				break;
			case INTERACT:
				super.setAnimation(archerInteractAnimations.get(super.facing));
				break;
			default:
				super.setAnimation(archerIdleAnimations.get(super.facing));
				break;
		}
	}

	@Override
	protected void interact() {
		super.interact();
		// Custom interaction code here
	}

}