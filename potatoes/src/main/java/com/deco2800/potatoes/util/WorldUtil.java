package com.deco2800.potatoes.util;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.renderering.Renderable;

import java.util.Optional;

/**
 * A utility class for the World instances
 * Created by timhadwen on 23/7/17.
 */
public class WorldUtil {

	/**
	 * Finds the closest entity to a position within a delta
	 * @param world
	 * @param x
	 * @param y
	 * @param delta
	 * @return Optional of WorldEntity
	 */
	public static Optional<AbstractEntity> closestEntityToPosition(float x, float y, float delta) {
		AbstractEntity result = null;
		double distance = Double.MAX_VALUE;
		for (Renderable r : GameManager.get().getWorld().getEntities()) {
			double tempDistance = Math.sqrt(Math.pow((r.getPosX() - x), 2) + Math.pow((r.getPosY() - y), 2));

			if (tempDistance < distance) {
				// Closer than current closest
				distance = tempDistance;
				result = (AbstractEntity) r;
			}
		}
		if (distance < delta){
			return Optional.of(result);
		} else {
			return Optional.empty();
		}
	}
}
