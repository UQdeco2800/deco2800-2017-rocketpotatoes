package com.deco2800.potatoes.util;

import com.deco2800.moos.managers.GameManager;
import com.deco2800.moos.renderers.Renderable;
import com.deco2800.moos.worlds.WorldEntity;

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
	public static Optional<WorldEntity> closestEntityToPosition(float x, float y, float delta) {
		WorldEntity result = null;
		double distance = Double.MAX_VALUE;
		for (Renderable r : GameManager.get().getWorld().getEntities()) {
			double tempDistance = Math.sqrt(Math.pow((r.getPosX() - x), 2) + Math.pow((r.getPosY() - y), 2));

			if (tempDistance < distance) {
				// Closer than current closest
				distance = tempDistance;
				result = (WorldEntity) r;
			}
		}
		if (distance < delta){
			return Optional.of(result);
		} else {
			return Optional.empty();
		}
	}
}
