package com.deco2800.potatoes.entities.player;

import java.util.Map;
import java.util.Optional;

import com.badlogic.gdx.Input;
import com.deco2800.potatoes.collisions.Circle2D;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Direction;
import com.deco2800.potatoes.entities.animation.TimeAnimation;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;
import com.deco2800.potatoes.entities.projectiles.OrbProjectile;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.SoundManager;

public class Wizard extends Player {

	private Optional<AbstractEntity> target;
	/**
	 * Creates a new Archer instance.
	 *
	 * @param posX The x-coordinate.
	 * @param posY The y-coordinate.
	 */

	public Wizard(float posX, float posY) {
		super(posX, posY);
		this.defaultSpeed = 0.09f;
		this.projectileClass = OrbProjectile.class;
		super.setMoveSpeed(defaultSpeed);
		this.facing = Direction.SE;
		this.resetState();
		this.setShadow(shadow);
		progressBar = new ProgressBarEntity("healthBarGreen", "wizardIcon", 4);
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

	private float hoverTime = 0;  // A value used to determine the height of the hover
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
	public void handleKeyDown(int keycode) {
		super.handleKeyDown(keycode);
		switch (keycode) {
			case Input.Keys.SPACE:
				if(!(currentShootStage==ShootStage.LOOSE))
					currentShootStage=ShootStage.HOLDING;
				break;
		}
	}

	@Override
	public void handleKeyUp(int keycode) {
		super.handleKeyUp(keycode);
		switch (keycode) {
			case Input.Keys.SPACE:
				currentShootStage=ShootStage.LOOSE;
				if(OrbProjectile.class.isAssignableFrom(projectileClass))
					((OrbProjectile)projectile).hasBeenReleased(true);
				break;
		}
	}

	@Override
	protected void attack() {
		super.attack();

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
	}

	@Override
	public void onTick(long arg0) {
		super.onTick(arg0);
		hoverAnimation();
	}

}