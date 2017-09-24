package com.deco2800.potatoes.entities.projectiles;

import com.deco2800.potatoes.collisions.CollisionMask;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Tickable;

public abstract class Projectile extends AbstractEntity implements Tickable{

	public Projectile() {
		// empty for serialization
	}

	public Projectile(CollisionMask mask, String texture) {
        super(mask, 0.4f, 0.4f, texture);
	}

    public Projectile(CollisionMask mask, float xRenderLength, float yRenderLength, String texture) {
        super(mask, xRenderLength, yRenderLength, texture);
    }
	
	public abstract float getDamage();
	
}
