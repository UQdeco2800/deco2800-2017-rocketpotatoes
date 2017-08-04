package com.deco2800.potatoes.entities;

public class Projectile extends AbstractEntity{

	public Projectile(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
			float xRenderLength, float yRenderLength, boolean centered) {
		super(posX, posY, posZ, 0.2f, 0.2f, 0.2f, 0.2f, 0.2f);
		
		this.setTexture("projectile");
	}

}
