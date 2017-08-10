package com.deco2800.potatoes.entities.trees;

import java.util.Optional;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.BallisticProjectile;
import com.deco2800.potatoes.entities.Squirrel;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.WorldUtil;

/**
 * Represents a projectile shot from a tree
 */
public class TreeProjectileShootEvent extends TimeEvent {
	
	private AbstractTree tree;
	private float range;
	
	/**
	 * @param tree the tree the projectile originated from
	 * @param shootDelay the delay between shots
	 * @param range the range of the shot
	 */
	public TreeProjectileShootEvent(AbstractTree tree, int shootDelay, float range /* Projectile projectile, ProjectileDetails details */) {
		this.tree = tree;
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
        Optional<AbstractEntity> target = WorldUtil.getClosestEntityOfClass(Squirrel.class, tree.getPosX(), 
        		tree.getPosY());

        if(!target.isPresent()||target.get().distance(this.tree)>range) {
            return;
        }
        System.out.println("FiRiNg Mi LaZoRs");

        GameManager.get().getWorld().addEntity(new BallisticProjectile(tree.getPosX(), tree.getPosY(), tree.getPosZ(),
        			target.get().getPosX(), target.get().getPosY(), tree.getPosZ(), range));
	}

}
