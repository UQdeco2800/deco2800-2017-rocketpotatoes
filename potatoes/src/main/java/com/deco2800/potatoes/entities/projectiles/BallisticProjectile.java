package com.deco2800.potatoes.entities.projectiles;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.effects.AOEEffect;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.Box3D;

public class BallisticProjectile extends Projectile {

	private final static transient String TEXTURE = "rocket1";
	private static float DAMAGE = 10;
	private static float aoeDAMAGE = 1;
	private static float RANGE;
	private int rocketEffectTimer;
	private int rocketCurrentSpriteIndexCount;
	private String[] rocketSpriteArray = { "rocket1", "rocket2", "rocket3" };
	// private String[] expSpriteArray = {"exp1","exp2","exp3"};
	// private int expEffectTimer = 0;
	// private int expCurrentSpriteIndexCount = 0;

	/**
	 * Width and height of AOE sprite. NOTE: (height < width) to give isometric
	 * illusion
	 *
	 * @param AOE_width
	 *            Width of AOE sprite
	 * @param AOE_height
	 *            Height of AOE sprite
	 */
	private final static float AOE_width = 5f;
	private final static float AOE_height = 2f;

	private float goalX;
	private float goalY;
	private float goalZ;

	private int rotateAngle = 0;

	private final float speed = 0.2f;
	private Optional<AbstractEntity> mainTarget;
	private float changeX;
	private float changeY;
	private float changeZ;

	private boolean maxRange = false;

	private Class<?> targetClass;

	public BallisticProjectile() {
		// empty for serialization
		rotateAngle = 0;
		DAMAGE = 1;
		aoeDAMAGE = 1;
		maxRange = false;
	}

	/**
	 * Creates a new Ballistic Projectile. Ballistic Projectiles do not change
	 * direction once fired. The initial direction is based on the direction to the
	 * closest entity
	 *
	 * @param posX
	 *            x start position
	 * @param posY
	 *            y start position
	 * @param posZ
	 *            z start position
	 * @param target
	 *            Entity target object
	 * @param RANGE
	 *            Projectile range
	 * @param DAMAGE
	 *            Projectile damage
	 * @param aoeDAMAGE
	 *            AOE damage
	 */
	public BallisticProjectile(Class<?> targetClass, float posX, float posY, float posZ,
			Optional<AbstractEntity> target, float RANGE, float DAMAGE, float aoeDAMAGE) {
		super(posX, posY, posZ, 1, 2, TEXTURE);
		this.DAMAGE = DAMAGE;
		this.mainTarget = target;
		this.goalX = target.get().getPosX();
		this.goalY = target.get().getPosY();
		this.goalZ = target.get().getPosZ();
		this.aoeDAMAGE = aoeDAMAGE;
		this.RANGE = RANGE;
		this.targetClass = targetClass;

		float deltaX = getPosX() - goalX;
		float deltaY = getPosY() - goalY;

		float angle = (float) (Math.atan2(deltaY, deltaX)) + (float) (Math.PI);

		changeX = (float) (speed * Math.cos(angle));
		changeY = (float) (speed * Math.sin(angle));
		// TODO: add changeZ

		rotateAngle = (int) ((angle * 180 / Math.PI) + 45 + 90);

	}

	/**
	 * Creates a new Ballistic Projectile. Ballistic Projectiles do not change
	 * direction once fired. The initial direction is based on the direction to the
	 * closest entity
	 *
	 * @param posX
	 *            x start position
	 * @param posY
	 *            y start position
	 * @param posZ
	 *            z start position
	 * @param fPosX
	 *            target x position
	 * @param fPosY
	 *            target y position
	 * @param fPosZ
	 *            target z position
	 * @param RANGE
	 *            Projectile range
	 * @param DAMAGE
	 *            Projectile hit damage
	 * @param aoeDAMAGE
	 *            AOE damage
	 */
	public BallisticProjectile(float posX, float posY, float posZ, float fPosX, float fPosY,
			float fPosZ, float RANGE, float DAMAGE, float aoeDAMAGE) {
		super(posX, posY, posZ, TEXTURE);
		this.DAMAGE = DAMAGE;
		this.aoeDAMAGE = aoeDAMAGE;
		this.goalX = fPosX;
		this.goalY = fPosY;
		this.goalZ = fPosZ;

		this.RANGE = RANGE;

		float deltaX = getPosX() - goalX;
		float deltaY = getPosY() - goalY;
		float deltaZ = getPosZ() - goalZ;

		float angle = (float) (Math.atan2(deltaY, deltaX)) + (float) (Math.PI);

		changeX = (float) (speed * Math.cos(angle));
		changeY = (float) (speed * Math.sin(angle));
		// TODO: add changeZ

		rotateAngle = (int) ((angle * 180 / Math.PI) + 45 + 90);
	}

	public int rotateAngle() {
		return rotateAngle;
	}

	@Override
	public void onTick(long time) {

		updatePos();
		rocketEffectTimer++;
		if (rocketEffectTimer % 4 == 0) {
			setTexture(rocketSpriteArray[rocketCurrentSpriteIndexCount]);
			if (rocketCurrentSpriteIndexCount == rocketSpriteArray.length - 1)
				rocketCurrentSpriteIndexCount = 0;
			else
				rocketCurrentSpriteIndexCount++;
		}

		Box3D newPos = getBox3D();
		newPos.setX(this.getPosX());
		newPos.setY(this.getPosY());

		Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();
		// Check surroundings
		for (AbstractEntity entity : entities.values()) {
			if (targetClass.isInstance(entity)) {
				if (newPos.overlaps(entity.getBox3D())) {
					((MortalEntity) entity).damage(DAMAGE);
					AOEEffect exp = new AOEEffect(goalX - (AOE_width / 2), goalY - (AOE_height / 2), 0,
							AOE_width, AOE_height, 0, AOE_width, AOE_height, aoeDAMAGE);
					GameManager.get().getWorld().addEntity(exp);
					GameManager.get().getWorld().removeEntity(this);
				}

			}
		}
		if (maxRange) {
			GameManager.get().getWorld().removeEntity(this);
		}
	}

	public void updatePos() {
		maxRange = false;
		if (RANGE < speed) {
			setPosX(goalX);
			setPosY(goalY);
			setPosZ(goalZ);
			maxRange = true;
		} else {
			setPosX(getPosX() + changeX);
			setPosY(getPosY() + changeY);
		}
		RANGE -= speed;
	}

	/**
	 * Returns Range value
	 */
	public float getRange() {
		return RANGE;
	}

	/**
	 * Returns Damage value
	 */
	@Override
	public float getDamage() {
		return DAMAGE;
	}

	/**
	 * Returns AOE Damage value
	 */
	public float getAOEDamage() {
		return aoeDAMAGE;
	}

}
