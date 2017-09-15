package com.deco2800.potatoes.entities.projectiles;

import java.util.Map;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.Box3D;

public class Projectile extends AbstractEntity implements Tickable {

	protected static final transient String TEXTURE = "rocket1";
	protected String projectileType = "rocket";
	protected int textureArrayLength = 3;
	protected String[] textureArray;

	protected float goalX;
	protected float goalY;
	protected float goalZ;
	protected float changeX;
	protected float changeY;
	protected float changeZ;

	protected static float xRenderLength = 1.4f;
	protected static float yRenderLength = 1.4f;
	protected static float xLength = 0.4f;
	protected static float yLength = 0.4f;
	protected static float zLength = 0.4f;

	protected Class<?> targetClass;
	protected boolean maxRange;
	protected float range;
	protected float damage;
	protected float rotationAngle = 0;
	protected static final float SPEED = 0.2f;

	protected Effect startEffect;
	protected Effect endEffect;

	public Projectile() {
		textureArray = new String[textureArrayLength];
		// empty for serialization
	}

	public Projectile(Class<?> targetClass, float posX, float posY, float posZ, float targetPosX, float targetPosY,
			float targetPosZ, float range, float damage, String projectileType, Effect startEffect, Effect endEffect) {
		super(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, true, TEXTURE);
		textureArray = new String[textureArrayLength];
		if (projectileType != "" && projectileType != null)
			this.projectileType = projectileType;

		for (int t = 0; t < textureArrayLength; t++) {
			textureArray[t] = this.projectileType + Integer.toString(t + 1);
		}

		if (targetClass != null)
			this.targetClass = targetClass;
		else
			this.targetClass = MortalEntity.class;

		this.range = damage;
		this.damage = damage;
		this.startEffect = startEffect;
		this.endEffect = endEffect;

		if (startEffect != null)
			GameManager.get().getWorld().addEntity(startEffect);

		updatePosition();
		setTargetPosition(targetPosX, targetPosY, targetPosZ);
	}

	public void setTargetPosition(float xPos, float yPos, float zPos) {
		this.goalX = xPos;
		this.goalY = yPos;
		this.goalZ = zPos;
	}

	public void updatePosition() {
		float deltaX = getPosX() - this.goalX;
		float deltaY = getPosY() - this.goalY;
		float angle = (float) (Math.atan2(deltaY, deltaX)) + (float) (Math.PI);
		rotationAngle = (int) ((angle * 180 / Math.PI) + 45 + 90);
		changeX = (float) (SPEED * Math.cos(angle));
		changeY = (float) (SPEED * Math.sin(angle));

		setPosX(getPosX() + changeX);
		setPosY(getPosY() + changeY);

		if (range <= 0 || maxRange) {
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
		return range;
	}

	/**
	 * Returns Damage value
	 */
	public float getDamage() {
		return damage;
	}

	private int projectileEffectTimer;
	private int projectileCurrentSpriteIndexCount;

	public void animate() {
		projectileEffectTimer++;
		if (projectileEffectTimer % 4 == 0) {
			if ("rocket".equalsIgnoreCase(projectileType)) {
				setTexture(textureArray[projectileCurrentSpriteIndexCount]);
				if (projectileCurrentSpriteIndexCount == textureArrayLength - 1)
					projectileCurrentSpriteIndexCount = 0;
				else {
					projectileCurrentSpriteIndexCount++;
				}
			} else if ("chilli".equalsIgnoreCase(projectileType)) {

				setTexture(textureArray[projectileCurrentSpriteIndexCount]);
				if (projectileCurrentSpriteIndexCount == textureArrayLength - 1)
					projectileCurrentSpriteIndexCount = 0;
				else {
					projectileCurrentSpriteIndexCount++;
				}
			}
		}
	}

	@Override
	public void onTick(long time) {
		animate();
		updatePosition();

		Box3D newPos = getBox3D();
		newPos.setX(this.getPosX());
		newPos.setY(this.getPosY());

		Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();

		for (AbstractEntity entity : entities.values()) {
			if (!targetClass.isInstance(entity)) {
				continue;
			}
			if (newPos.overlaps(entity.getBox3D())) {
				((MortalEntity) entity).damage(range);
				if (endEffect != null)
					GameManager.get().getWorld().addEntity(endEffect);
				maxRange = true;
				updatePosition();
				break;
			}
		}
	}
}
