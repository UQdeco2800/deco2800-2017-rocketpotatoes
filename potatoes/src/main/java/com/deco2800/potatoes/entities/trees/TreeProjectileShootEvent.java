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
	int shootOnce = 0;
	@Override
	public void action(AbstractTree tree) {
		Optional<AbstractEntity> target1 = WorldUtil.getClosestEntityOfClass(Squirrel.class, tree.getPosX(),
				tree.getPosY());
		Optional<AbstractEntity> target2 = WorldUtil.getClosestEntityOfClass(TankEnemy.class, tree.getPosX(),
				tree.getPosY());

		if(target1.isPresent()&&(tree.distance(target1.get()) <= tree.getUpgradeStats().getRange())) {
			GameManager.get().getWorld().addEntity(new BallisticProjectile(tree.getPosX(), tree.getPosY(), tree.getPosZ(),
					target1,tree.getUpgradeStats().getRange(),10));
		}
		if(target2.isPresent()&&(tree.distance(target2.get()) <= tree.getUpgradeStats().getRange())) {
			GameManager.get().getWorld().addEntity(new BallisticProjectile(tree.getPosX(), tree.getPosY(), tree.getPosZ(),
					target2,tree.getUpgradeStats().getRange(),10));
		}
		
//		if (!target1.isPresent() || tree.distance(target1.get()) > tree.getUpgradeStats().getRange()) {
//			return;
//		}
//		if (!target2.isPresent() || tree.distance(target2.get()) > tree.getUpgradeStats().getRange()) {
//			return;
//		}

		// Added custom damages to projectiles

//		GameManager.get().getWorld().addEntity(new HomingProjectile(tree.getPosX(), tree.getPosY(), tree.getPosZ(),
//				target1, tree.getUpgradeStats().getRange(),1));

//
//		if(shootOnce <=0){
//			shootOnce++;
		
//			GameManager.get().getWorld().addEntity(new BallisticProjectile(tree.getPosX(), tree.getPosY(), tree.getPosZ(),
//					target2,tree.getUpgradeStats().getRange(),10));
		//}




//					target1,tree.getUpgradeStats().getRange(),0);

//		GameManager.get().getWorld().addEntity(new HomingProjectile(tree.getPosX(), tree.getPosY(), tree.getPosZ(),
//				target1, tree.getUpgradeStats().getRange(),1));
	}

	@Override
	public TimeEvent<AbstractTree> copy() {
		return new TreeProjectileShootEvent(getResetAmount());
	}

}