package com.deco2800.potatoes.entities;

public abstract class Projectile extends AbstractEntity implements Tickable{

	public Projectile() {
		//empty for serialization
	}


	public Projectile(float posX, float posY, float posZ, String texture) {
		super(posX, posY, posZ, 0.4f, 0.4f, 0.4f, 0.4f, 0.4f, true,texture);
	}

	public Projectile(float posX, float posY, float posZ,float xLength, float yLength, float zLength, float xRenderLength, float yRenderLength, String texture) {
		super(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, true, texture);
	}
	
	public abstract float getDamage();

}
