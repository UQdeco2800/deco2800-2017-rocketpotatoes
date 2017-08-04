package com.deco2800.potatoes.entities;

public class Projectile extends AbstractEntity{
	
	private final static String TEXTURE = "projectile";

	public Projectile(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 0.4f, 0.4f, 0.4f, 0.4f, 0.4f, TEXTURE);
	}

}
