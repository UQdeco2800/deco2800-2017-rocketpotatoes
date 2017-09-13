package com.deco2800.potatoes.entities.projectiles;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.managers.GameManager;

public class Projectile extends AbstractEntity implements Tickable {

	protected float range;
	protected float damage;

	protected final static transient String TEXTURE = "rocket1";
	protected String projectileType = "rocket";
	protected int textureArrayLength = 3;
	protected String[] textureArray = new String[textureArrayLength];

	protected float goalX;
	protected float goalY;
	protected float goalZ;
	protected float changeX;
	protected float changeY;
	protected float changeZ;

	protected Class<?> targetClass;

	protected boolean maxRange;

	protected float rotationAngle = 0;

	protected final float speed = 0.2f;


	public Projectile() {
		// empty for serialization
	}

	// not used
	public Projectile(float posX, float posY, float posZ, String texture) {
		super(posX, posY, posZ, 1f, 2f, 0.4f, 0.4f, 0.4f, true,texture);
	}

	// not used
	public Projectile(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
			float xRenderLength, float yRenderLength, String texture) {

		super(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, true, texture);
	}

	public Projectile(float posX, float posY, float posZ, float xRenderLength, float yRenderLength, String texture) {
		super(posX, posY, posZ, 0.4f, 0.4f, 0.4f, xRenderLength, yRenderLength, true, texture);
	}


	public Projectile(Class<?> targetClass, float posX, float posY, float posZ, float targetPosX, float targetPosY,
			float targetPosZ, float range, float damage, float xRenderLength, float yRenderLength) {
		super(posX, posY, posZ, 0.4f, 0.4f, 0.4f, xRenderLength, yRenderLength, true, TEXTURE);

		for (int t = 0; t < textureArrayLength; t++) {
			textureArray[t] = projectileType + Integer.toString(t + 1);
		}

		this.damage = damage;
		setTargetPosition(targetPosX, targetPosY, targetPosZ);
		// this.aoeDAMAGE = aoeDAMAGE;
		this.range = damage;
		this.targetClass = targetClass;

		float deltaX = getPosX() - goalX;
		float deltaY = getPosY() - goalY;

		float angle = (float) (Math.atan2(deltaY, deltaX)) + (float) (Math.PI);

		changeX = (float) (speed * Math.cos(angle));
		changeY = (float) (speed * Math.sin(angle));
		// TODO: add changeZ

		rotationAngle = (int) ((angle * 180 / Math.PI) + 45 + 90);
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
		changeX = (float) (speed * Math.cos(angle));
		changeY = (float) (speed * Math.sin(angle));

		setPosX(getPosX() + changeX);
		setPosY(getPosY() + changeY);

		if (range <= 0 || maxRange) {
			GameManager.get().getWorld().removeEntity(this);
		}else {
			range -= speed;
		}

		// System.out.println(range + " " +speed);

	}

	@Override
	public float rotateAngle() {
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
		return range;
	}

	private int projectileEffectTimer;
	private int projectileCurrentSpriteIndexCount;

	public void animate() {
		projectileEffectTimer++;
		if (projectileEffectTimer % 4 == 0) {
			if (projectileType.equalsIgnoreCase("rocket")) {
				setTexture(textureArray[projectileCurrentSpriteIndexCount]);
				if (projectileCurrentSpriteIndexCount == textureArrayLength - 1)
					projectileCurrentSpriteIndexCount = 0;
				else {
					projectileCurrentSpriteIndexCount++;
				}
			} else if (projectileType.equalsIgnoreCase("chilli")) {

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

	}
}
