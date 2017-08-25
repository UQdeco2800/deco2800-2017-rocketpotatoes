package com.deco2800.potatoes.entities.trees;

import java.util.Optional;

import com.deco2800.potatoes.entities.*;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.WorldUtil;

/**
 * Represents a projectile shot from a tree, may be generalised to all entities
 * later
 */
public class TreeProjectileShootEvent extends TimeEvent<AbstractTree> {

	/**
	 * Default constructor for serialization
	 */
	public TreeProjectileShootEvent() {
	}

	/**
	 * @param shootDelay
	 *            the delay between shots
	 */
	public TreeProjectileShootEvent(int shootDelay) {
		setDoReset(true);
		setResetAmount(shootDelay);
		reset();
	}

	/**
	 * Temporary action for testing
	 */
	@Override
	public void action(AbstractTree tree) {
		Optional<AbstractEntity> target = WorldUtil.getClosestEntityOfClass(Squirrel.class, tree.getPosX(),
				tree.getPosY());

		if (!target.isPresent() || tree.distance(target.get()) > tree.getUpgradeStats().getRange()) {
			return;
		}

		//Added custom damages to projectiles

		GameManager.get().getWorld().addEntity(new HomingProjectile(tree.getPosX(), tree.getPosY(), tree.getPosZ(),
				target, tree.getUpgradeStats().getRange(),1));


	}

	@Override
	public TimeEvent<AbstractTree> copy() {
		return new TreeProjectileShootEvent(getResetAmount());
	}

}
