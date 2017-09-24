package com.deco2800.potatoes.entities.projectiles;

import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.Box3D;

public class Projectile extends AbstractEntity implements Tickable {

	protected static final float SPEED = 0.2f;

	protected ProjectileTexture projectileTexture;
	protected boolean loopAnimation = true;
	protected boolean animate = true;

	protected Vector3 targetPos = new Vector3();
	protected Vector3 change = new Vector3();
	protected Vector2 delta = new Vector2();

	protected static float xRenderLength = 1.4f;
	protected static float yRenderLength = 1.4f;
	protected static float xLength = 0.4f;
	protected static float yLength = 0.4f;
	protected static float zLength = 0.4f;

	protected Class<?> targetClass;
	protected boolean rangeReached;
	protected float maxRange;
	protected float range;
	protected float damage;
	protected float rotationAngle = 0;

	protected Effect startEffect;
	protected Effect endEffect;

	/**
	 * A container to hold the textures for easy lookup
	 */
	public enum ProjectileTexture {
		ROCKET {
			public String[] textures() {
				return new String[] { "rocket1", "rocket2", "rocket3" };
			}
		},
		CHILLI {
			public String[] textures() {
				return new String[] { "chilli1", "chilli2", "chilli3" };
			}
		},
		LEAVES {
			public String[] textures() {
				return new String[] { "leaves1" };
			}
		},
		ACORN {
			public String[] textures() {

				return new String[] { "acorn1" };

			}
		};

		public String[] textures() {
			return new String[] { "default" };
		}
	}

	public Projectile() {
		// nothing yet
	}

	/**
	 * Creates a new projectile. A projectile is the vehicle used to deliver damage
	 * to a target over a distance
	 * 
	 * @param targetClass
	 *            the targets class
	 * @param startPos
	 * @param targetPos
	 * @param range
	 * @param damage
	 *            damage of projectile
	 * @param projectileTexture
	 *            the texture set to use for animations. Use ProjectileTexture._
	 * @param startEffect
	 *            the effect to play at the start of the projectile being fired
	 * @param endEffect
	 *            the effect to be played if a collision occurs
	 */
	public Projectile(Class<?> targetClass, Vector3 startPos, Vector3 targetPos, float range, float damage,
			ProjectileTexture projectileTexture, Effect startEffect, Effect endEffect) {
		super(startPos.x, startPos.y, startPos.z, xLength + 1f, yLength + 1f, zLength, xRenderLength, yRenderLength,
				true, projectileTexture.textures()[0]);

		if (targetClass != null)
			this.targetClass = targetClass;
		else
			this.targetClass = MortalEntity.class;

		if (projectileTexture == null)
			throw new RuntimeException("projectile type must not be null");
		else
			this.projectileTexture = projectileTexture;
		this.maxRange = this.range = range;
		this.damage = damage;
		this.startEffect = startEffect;
		this.endEffect = endEffect;

		if (startEffect != null)
			GameManager.get().getWorld().addEntity(startEffect);

		setTargetPosition(targetPos.x, targetPos.y, targetPos.z);
		updatePosition();
		setPosition();
	}

	/**
	 * Initialize heading. Used if heading changes
	 */
	protected void updatePosition() {
		delta.set(getPosX() - targetPos.x, getPosY() - targetPos.y);
		float angle = (float) (Math.atan2(delta.y, delta.x)) + (float) (Math.PI);
		rotationAngle = (float) ((angle * 180 / Math.PI) + 45 + 90);
		change.set((float) (SPEED * Math.cos(angle)), (float) (SPEED * Math.sin(angle)), 0);
	}

	/**
	 * Set the location of the target
	 * 
	 * @param xPos
	 *            target x position
	 * @param yPos
	 *            target y position
	 * @param zPos
	 *            target y position
	 */
	public void setTargetPosition(float xPos, float yPos, float zPos) {
		targetPos.set(xPos, yPos, zPos);
	}

	/**
	 * Each frame the position is set/updated
	 */
	protected void setPosition() {
		setPosX(getPosX() + change.x);
		setPosY(getPosY() + change.y);

		if (range < SPEED || rangeReached) {
			GameManager.get().getWorld().removeEntity(this);
		} else {
			range -= SPEED;
		}
	}

	@Override
	public float rotationAngle() {
		return rotationAngle;
	}

	protected int projectileEffectTimer;
	protected int projectileCurrentSpriteIndexCount;

	/*
	 * Loops through texture array and sets sprite every frame. Looking into using
	 * AnimationFactory as animation controller
	 */
	protected void animate() {
		if (animate) {
			projectileEffectTimer++;
			if (loopAnimation) {
				if (projectileEffectTimer % 4 == 0) {
					setTexture(projectileTexture.textures()[projectileCurrentSpriteIndexCount]);
					if (projectileCurrentSpriteIndexCount == projectileTexture.textures().length - 1)
						projectileCurrentSpriteIndexCount = 0;
					else {
						projectileCurrentSpriteIndexCount++;
					}
				}
			}
		}
	}

	@Override
	public void onTick(long time) {
		animate();
		setPosition();

		Box3D newPos = getBox3D();
		newPos.setX(this.getPosX());
		newPos.setY(this.getPosY());

		Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();

		for (AbstractEntity entity : entities.values()) {
			if (!targetClass.isInstance(entity)) {
				continue;
			}
			if (newPos.overlaps(entity.getBox3D())) {
				((MortalEntity) entity).damage(damage);
				if (endEffect != null)
					GameManager.get().getWorld().addEntity(endEffect);
				rangeReached = true;
				setPosition();

			}
		}
	}

	/**
	 * Returns max range
	 * 
	 * @return maxRange
	 */
	public float getRange() {
		return maxRange;
	}

	/**
	 * Returns Damage value
	 * 
	 * @return damage
	 */
	public float getDamage() {
		return damage;
	}

	/**
	 * Return target class
	 * 
	 * @return targetClass
	 */
	public Class<?> getTargetClass() {
		return targetClass;
	}

	/**
	 * Get the start effect
	 * 
	 * @return startEffect
	 */
	public Effect getStartEffect() {
		return startEffect;
	}

	/**
	 * Get the end effect
	 * 
	 * @return endEffect
	 */
	public Effect getEndEffect() {
		return endEffect;
	}


}
