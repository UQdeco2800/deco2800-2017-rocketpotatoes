package com.deco2800.potatoes.entities.projectiles;

import java.util.Map;
import java.util.Optional;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.effects.ExplosionEffect;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.Box3D;
import com.deco2800.potatoes.util.WorldUtil;

public class HomingProjectile extends Projectile {

	public HomingProjectile() {

	}

	/**
	 * Creates a new Homing Projectile. Homing Projectiles changes direction once
	 * fired. The initial direction is based on the direction to the closest entity
	 * and follows it.
	 *
	 * @param posX
	 *            x start position
	 * @param posY
	 *            y start position
	 * @param posZ
	 *            z start position
	 * @param targetPosX
	 *            target x position
	 * @param targetPosY
	 *            target y position
	 * @param targetPosZ
	 *            target z position
	 * @param range
	 *            Projectile range
	 * @param DAMAGE
	 *            Projectile damage
	 */
	public HomingProjectile(Class<?> targetClass, float posX, float posY, float posZ, float targetPosX,
			float targetPosY, float targetPosZ, float range, float damage, String projectileType) {
		super(targetClass, posX, posY, posZ, targetPosX, targetPosY, targetPosZ, range, damage, 1.4f, 1.4f);
	}

	@Override
	public void onTick(long time) {
		Optional<AbstractEntity> targetEntity = WorldUtil.getClosestEntityOfClass(targetClass, this.goalX, this.goalY);
		if (targetEntity != null)
			setTargetPosition(targetEntity.get().getPosX(), targetEntity.get().getPosY(),
					targetEntity.get().getPosZ());
		
		animate();
		updatePosition();

		Box3D newPos = getBox3D();
		newPos.setX(this.getPosX());
		newPos.setY(this.getPosY());

		Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();

		for (AbstractEntity entity : entities.values()) {
			if (targetClass.isInstance(entity)) {
				if (newPos.overlaps(entity.getBox3D())) {
					((MortalEntity) entity).damage(range);
					ExplosionEffect expEffect = new ExplosionEffect(goalX, goalY, goalZ, 5f, 5f, 0, 1f, 1f);
					GameManager.get().getWorld().addEntity(expEffect);
					maxRange=true;
					updatePosition();
				}

			}
		}
		
	}
}
