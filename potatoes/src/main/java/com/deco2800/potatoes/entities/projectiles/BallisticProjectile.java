package com.deco2800.potatoes.entities.projectiles;

import java.util.Map;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.effects.AOEEffect;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.Box3D;

public class BallisticProjectile extends Projectile {


	// /**
	// * Width and height of AOE sprite. NOTE: (height < width) to give isometric
	// * illusion
	// *
	// * @param AOE_width
	// * Width of AOE sprite
	// * @param AOE_height
	// * Height of AOE sprite
	// */

	public BallisticProjectile() {

	}

	/**
	 * Creates a new Ballistic Projectile. Ballistic Projectiles do not change
	 * direction once fired. The initial direction is based on the direction to the
	 * closest entity
	 *
	 * @param posX
	 *            x start position
	 * @param posY
	 *            y start position
	 * @param posZ
	 *            z start position
	 * @param fPosX
	 *            target x position
	 * @param fPosY
	 *            target y position
	 * @param fPosZ
	 *            target z position
	 * @param RANGE
	 *            Projectile range
	 * @param DAMAGE
	 *            Projectile hit damage
	 * @param aoeDAMAGE
	 *            AOE damage
	 */

	public BallisticProjectile(Class<?> targetClass, float posX, float posY, float posZ, float targetPosX,
			float targetPosY, float targetPosZ, float range, float damage, float aoeDAMAGE) {
		super(targetClass, posX, posY, posZ, targetPosX, targetPosY, targetPosZ, range, damage, 1.4f, 1.4f);

	}

	@Override
	public void onTick(long time) {


		updatePosition();
		animate();


		Box3D newPos = getBox3D();
		newPos.setX(this.getPosX());
		newPos.setY(this.getPosY());

		Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();

		float AOE_width = 5f;
		float AOE_height = 2f;
		
		for (AbstractEntity entity : entities.values()) {
			if (targetClass.isInstance(entity) && newPos.overlaps(entity.getBox3D())) {
				((MortalEntity) entity).damage(damage);
				AOEEffect exp = new AOEEffect(goalX - (AOE_width / 2), goalY - (AOE_height / 2), 0, AOE_width,
						AOE_height, 0, AOE_width, AOE_height, 1);
				GameManager.get().getWorld().addEntity(exp);
				maxRange = true;
				updatePosition();
				break;

			}

		}

	}
}
