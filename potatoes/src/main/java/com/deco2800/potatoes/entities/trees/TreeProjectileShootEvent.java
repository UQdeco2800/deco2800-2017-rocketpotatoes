package com.deco2800.potatoes.entities.trees;

import java.util.Optional;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.BallisticProjectile;
import com.deco2800.potatoes.entities.Squirrel;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.Box3D;
import com.deco2800.potatoes.util.WorldUtil;

/**
 * Represents a projectile shot from a tree
 */
public class TreeProjectileShootEvent extends TimeEvent {

	private Box3D sourcePosition = new Box3D();
	private float range = 0;

	/**
	 * Default constructor for serialization
	 */
	public TreeProjectileShootEvent() {
	}
	
	/**
	 * @param tree
	 *            the tree the projectile originated from
	 * @param shootDelay
	 *            the delay between shots
	 * @param range
	 *            the range of the shot
	 */
	public TreeProjectileShootEvent(Box3D sourcePosition, int shootDelay,
			float range /* Projectile projectile, ProjectileDetails details */) {
		this.sourcePosition = sourcePosition;
		this.range = range;
		setDoReset(true);
		setResetAmount(shootDelay);
		reset();
	}

	/**
	 * Temporary action for testing
	 */
	@Override
	public void action() {
		Optional<AbstractEntity> target = WorldUtil.getClosestEntityOfClass(Squirrel.class, sourcePosition.getX(),
				sourcePosition.getY());

		if (!target.isPresent() || sourcePosition.distance(target.get().getBox3D()) > range) {
			return;
		}
		//System.out.println("FiRiNg Mi LaZoRs");

		GameManager.get().getWorld().addEntity(new BallisticProjectile(sourcePosition.getX(), sourcePosition.getY(),
				sourcePosition.getZ(), target.get().getPosX(), target.get().getPosY(), sourcePosition.getZ(), range));
	}

}
