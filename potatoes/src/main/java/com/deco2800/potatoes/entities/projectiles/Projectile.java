package com.deco2800.potatoes.entities.projectiles;

import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.collisions.CollisionMask;
import com.deco2800.potatoes.collisions.Box2D;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.util.Box3D;

public class Projectile extends AbstractEntity implements Tickable {

	protected static final float SPEED = 0.2f;

	protected ProjectileType projectileType;
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

	public enum ProjectileType {
		ROCKET {
			public String toString() {
				return "rocket";
			}

			public String[] textures() {
				return new String[] { "rocket1", "rocket2", "rocket3" };
			}

		},
		CHILLI {
			public String toString() {
				return "chilli";
			}

			public String[] textures() {
				return new String[] { "chilli1", "chilli2", "chilli3" };
			}
		},
		LEAVES {
			public String toString() {
				return "leaves";
			}

			public String[] textures() {
				return new String[] { "leaves1", "leaves2", "leaves3" };
			}
		},
		ACORN {
			public String toString() {
				return "acorn";
			}

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

	// currently used in Player, will probably need to change out later.
    public Projectile(Class<?> targetClass, Vector3 startPos, Vector3 targetPos, float range, float damage,
			ProjectileType projectileType, Effect startEffect, Effect endEffect, String Directions) {
        super(new Box2D(startPos.x, startPos.y, xLength + 1f, yLength + 1f), xRenderLength, yRenderLength,
                projectileType.textures()[0]);

		if (targetClass != null)
			this.targetClass = targetClass;
		else
			this.targetClass = MortalEntity.class;

		if (projectileType == null)
			throw new RuntimeException("projectile type must not be null");
		else
			this.projectileType = projectileType;

		this.range = damage;
		this.damage = damage;
		this.startEffect = startEffect;
		this.endEffect = endEffect;

		if (startEffect != null)
			GameManager.get().getWorld().addEntity(startEffect);

		updatePosition();
		/**
		 * Shoots enemies base on their player directions
		 */
		if (Directions.equalsIgnoreCase("w")) {
			setTargetPosition(startPos.x - 5, startPos.y - 5, startPos.z);
			// setTargetPosition(TargetPosX, TargetPosY, startPos.z);
			updatePosition();
			setPosition();
		} else if (Directions.equalsIgnoreCase("e")) {
			setTargetPosition(startPos.x + 5, startPos.y + 5, startPos.z);
			updatePosition();
			setPosition();
			// setTargetPosition(TargetPosX, TargetPosY, startPos.z);
		} else if (Directions.equalsIgnoreCase("n")) {
			setTargetPosition(startPos.x + 15, startPos.y - 15, startPos.z);
			updatePosition();
			setPosition();
		} else if (Directions.equalsIgnoreCase("s")) {
			setTargetPosition(startPos.x - 15, startPos.y + 15, startPos.z);
			updatePosition();
			setPosition();
		} else if (Directions.equalsIgnoreCase("ne")) {
			setTargetPosition(startPos.x + 15, startPos.y + 1, startPos.z);
			updatePosition();
			setPosition();
		} else if (Directions.equalsIgnoreCase("nw")) {
			setTargetPosition(startPos.x - 15, startPos.y - 200, startPos.z);
			updatePosition();
			setPosition();
		} else if (Directions.equalsIgnoreCase("se")) {
			setTargetPosition(startPos.x + 20, startPos.y + 200, startPos.z);
			updatePosition();
			setPosition();
		} else if (Directions.equalsIgnoreCase("sw")) {
			setTargetPosition(startPos.x - 200, startPos.y - 20, startPos.z);
			updatePosition();
			setPosition();
		}

		/**
		 * Shoots enemies based on their last position
		 */
		// setTargetPosition(TargetPosX,TargetPosY,0f);
		// updatePosition();
		// setPosition();
	}

	public Projectile(Class<?> targetClass, Vector3 startPos, Vector3 targetPos, float range, float damage,
			ProjectileType projectileType, Effect startEffect, Effect endEffect) {
        this(targetClass, startPos, targetPos, range, damage, projectileType, startEffect, endEffect, "");

        // TODO -- look at the other constructor -- this block of code is commented out there
		setTargetPosition(targetPos.x, targetPos.y, targetPos.z);
		updatePosition();
		setPosition();
	}

	public void setTargetPosition(float xPos, float yPos, float zPos) {
		targetPos.set(xPos, yPos, zPos);
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
	 * every frame the position is set
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

	/**
	 * Returns Range value
	 */
	public float getRange() {
		return maxRange;
	}

	/**
	 * Returns Damage value
	 */
	public float getDamage() {
		return damage;
	}

	protected int projectileEffectTimer;
	protected int projectileCurrentSpriteIndexCount;

	protected void animate() {
		if (animate) {
			projectileEffectTimer++;
			if (loopAnimation) {
				if (projectileEffectTimer % 4 == 0) {
					setTexture(projectileType.textures()[projectileCurrentSpriteIndexCount]);
					if (projectileCurrentSpriteIndexCount == projectileType.textures().length - 1)
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

		CollisionMask newPos = getMask();
		newPos.setX(this.getPosX());
		newPos.setY(this.getPosY());

		Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();

		for (AbstractEntity entity : entities.values()) {
			if (!targetClass.isInstance(entity)) {
				continue;
			}
			if (newPos.overlaps(entity.getMask())) {
				((MortalEntity) entity).damage(damage);
				if (endEffect != null)
					GameManager.get().getWorld().addEntity(endEffect);
				rangeReached = true;
				setPosition();
			}
		}
	}
}
