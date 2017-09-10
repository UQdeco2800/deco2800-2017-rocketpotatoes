package com.deco2800.potatoes.entities.projectiles;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Tickable;

public abstract class Projectile extends AbstractEntity implements Tickable {

	protected static float RANGE;
	protected static float DAMAGE;

	protected final static transient String TEXTURE = "rocket1";
	protected String projectileType = "rocket";
	protected String[] textureArray=new String[3];

	protected float goalX;
	protected float goalY;
	protected float goalZ;
	protected float changeX;
	protected float changeY;
	protected float changeZ;

	protected Class<?> targetClass;

	protected boolean maxRange;

	protected int rotationAngle = 0;

	protected final float speed = 0.2f;

	public Projectile() {

	}

	public Projectile(float posX, float posY, float posZ, String texture) {
		super(posX, posY, posZ, 1f, 2f, 0.4f, 0.4f, 0.4f, true, texture);
	}

	public Projectile(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
			float xRenderLength, float yRenderLength, String texture) {
		super(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, true, texture);
	}

	public Projectile(float posX, float posY, float posZ, float xRenderLength, float yRenderLength, String texture) {
		super(posX, posY, posZ, 0.4f, 0.4f, 0.4f, xRenderLength, yRenderLength, true, texture);
	}

	public abstract float getDamage();

	public abstract void animate();

}
