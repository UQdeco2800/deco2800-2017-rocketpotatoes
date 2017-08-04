package com.deco2800.potatoes.entities;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.util.Box3D;

public abstract class EnemyEntity extends AbstractEntity {

	public EnemyEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength) {
		this(posX, posY, posZ, xLength, yLength, zLength, xLength, yLength, false);
	}

	public EnemyEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
			float xRenderLength, float yRenderLength, boolean centered) {
		super(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, centered);
	}

	public EnemyEntity(Box3D position, float xRenderLength, float yRenderLength, boolean centered) {
		super(position, xRenderLength, yRenderLength, centered);

	}

}
