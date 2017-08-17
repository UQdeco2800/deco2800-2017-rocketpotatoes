package com.deco2800.potatoes.entities;

public abstract class Projectile extends AbstractEntity implements Tickable{

	public Projectile() {
		//empty for serialization
	}


	public Projectile(float posX, float posY, float posZ, String texture) {
		super(posX, posY, posZ, 0.4f, 0.4f, 0.4f, 0.4f, 0.4f, texture);
	}
	
	public abstract float getDamage();

}
