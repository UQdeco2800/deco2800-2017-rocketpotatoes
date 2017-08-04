package com.deco2800.potatoes.entities;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.util.Box3D;

public abstract class EnemyEntity extends AbstractEntity {

	public EnemyEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength, String texture) {
		this(posX, posY, posZ, xLength, yLength, zLength, xLength, yLength, false, texture);
	}

	public EnemyEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
			float xRenderLength, float yRenderLength, boolean centered, String texture) {
		super(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, centered, texture);
	}


}
