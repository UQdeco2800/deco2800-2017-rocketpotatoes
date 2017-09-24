package com.deco2800.potatoes.entities.trees;

import java.util.Optional;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.entities.effects.AOEEffect;
import com.deco2800.potatoes.entities.enemies.EnemyEntity;
import com.deco2800.potatoes.entities.projectiles.BallisticProjectile;
import com.deco2800.potatoes.entities.projectiles.HomingProjectile;
import com.deco2800.potatoes.entities.projectiles.Projectile;
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
		// default constructer
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

	@Override
	public void action(AbstractTree tree) {
		Optional<AbstractEntity> target1 = WorldUtil.getClosestEntityOfClass(EnemyEntity.class, tree.getPosX(),
				tree.getPosY());
		if (target1.isPresent() && (tree.distance(target1.get()) <= tree.getUpgradeStats().getAttackRange())) {
			GameManager.get().getWorld().addEntity(new BallisticProjectile(target1.get().getClass(),
					new Vector3(tree.getPosX() + 0.5f, tree.getPosY() + 0.5f, tree.getPosZ()),
					new Vector3(target1.get().getPosX(), target1.get().getPosY(), target1.get().getPosZ()),
					tree.getUpgradeStats().getAttackRange(), 1, Projectile.ProjectileTexture.ROCKET, null,
					new AOEEffect(target1.getClass(),
							new Vector3(target1.get().getPosX(), target1.get().getPosY(), target1.get().getPosZ()), 100,
							1),"", Projectile.ShootingStyles.BALLISTICPROJECTILE));

//			GameManager.get().getWorld().addEntity(new HomingProjectile(target1.get().getClass(),
//					new Vector3(tree.getPosX() + 0.5f, tree.getPosY() + 0.5f, tree.getPosZ()),
//					new Vector3(target1.get().getPosX(), target1.get().getPosY(), target1.get().getPosZ()),
//					tree.getUpgradeStats().getAttackRange(), 100, Projectile.ProjectileTexture.ROCKET, null,
//					null,"", Projectile.ShootingStyles.HOMINGPROJECTILE));

		}

	}

	@Override
	public TimeEvent<AbstractTree> copy() {
		return new TreeProjectileShootEvent(getResetAmount());
	}
}
