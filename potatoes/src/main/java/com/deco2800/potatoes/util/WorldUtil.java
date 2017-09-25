package com.deco2800.potatoes.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.renderering.Render3D;
import com.deco2800.potatoes.renderering.Renderable;

/**
 * A utility class for the World instances
 * Created by timhadwen on 23/7/17.
 */
public class WorldUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(WorldUtil.class);

	/**
	 * Finds the closest entity to a position within a delta
	 * @param world
	 * @param x
	 * @param y
	 * @param delta
	 * @return Optional of AbstractEntity
	 */
	public static Optional<AbstractEntity> closestEntityToPosition(float x, float y, float delta) {
		AbstractEntity ret = null;
		double distance = Double.MAX_VALUE;
		for (Renderable e : GameManager.get().getWorld().getEntities().values()) {
			double tmp_distance = distance(x, y, e.getPosX(), e.getPosY());

			if (tmp_distance < distance) {
				// Closer than current closest
				distance = tmp_distance;
				ret = (AbstractEntity) e;
			}
		}
		if (distance < delta){
			LOGGER.info("Closest is " + ret);
			return Optional.of(ret);
		} else {
			LOGGER.info("Nothing is that close");
			return Optional.empty();
		}
	}

	public static List<AbstractEntity> getEntitiesOfClass(Collection<AbstractEntity> entities, Class<?> c) {
		List<AbstractEntity> classEntities = new ArrayList<>();
		for (AbstractEntity w : entities) {
			if (c.isAssignableFrom(w.getClass())) {
				classEntities.add(w);
			}
		}
		return classEntities;
	}

	public static Optional<AbstractEntity> getClosestEntityOfClass(Class<?> c, float x, float y) {
		List<AbstractEntity> entities = WorldUtil.getEntitiesOfClass(GameManager.get().getWorld().getEntities().values(), c);

		AbstractEntity closest = null;
		float dist = Float.MAX_VALUE;
		for (AbstractEntity e : entities) {
			float tmpDistance = distance(x, y, e.getPosX(), e.getPosY());
			if (closest == null || dist > tmpDistance) {
				dist = tmpDistance;
				closest = e;
			}
		}

		if (closest == null) {
			return Optional.empty();
		} else {
			return Optional.of(closest);
		}
	}

	public static Optional<AbstractEntity> getEntityAtPosition(float x, float y) {
		
		for (Renderable e : GameManager.get().getWorld().getEntities().values()) {
			if (Math.abs(e.getPosX() - x) < 1f && Math.abs(e.getPosY() - y) < 1f) {
				return Optional.of((AbstractEntity)e);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Returns the distance between the point (x1,y1) and (x2,y2)
	 */
	public static float distance(float x1, float y1, float x2, float y2) {
		return (float)Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}
	
	/**
	 * Returns the angle (in degrees) between the point (x1,y1) and (x2,y2) Example:
	 * rotation(0,0,1,1) = 45 deg
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return rotation in degrees
	 */
	public static float rotation(float x1, float y1, float x2, float y2) {
		Vector2 startPos = Render3D.worldToScreenCoordinates(x1, y1, 0);
		Vector2 endPos = Render3D.worldToScreenCoordinates(x2, y2, 0);
		float l = endPos.x - startPos.x;
		float h = endPos.y - startPos.y;
		// float rotation removed due to direct return
		return (float) (Math.atan2(l, h) * 180 / Math.PI) - 45;
	}
}
