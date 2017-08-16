package com.deco2800.potatoes.handlers;

import java.util.Optional;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Clickable;
import com.deco2800.potatoes.entities.Tower;
import com.deco2800.potatoes.managers.CameraManager;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.observers.TouchDownObserver;
import com.deco2800.potatoes.observers.TouchDraggedObserver;
import com.deco2800.potatoes.util.WorldUtil;
import com.deco2800.potatoes.worlds.AbstractWorld;
import com.deco2800.potatoes.worlds.InitialWorld;

/**
 * Really crappy mouse handler for the game
 */
public class MouseHandler implements TouchDownObserver, TouchDraggedObserver {
	private int originX;
	private int originY;

	/**
	 * Constructor for the mouse handler
	 */
	public MouseHandler() {
	}

	/**
	 * Currently only handles objects on height 0
	 * 
	 * @param x
	 * @param y
	 */
	public void handleMouseClick(float x, float y) {

		float projX = 0, projY = 0;

		float tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get("tilewidth");
		float tileHeight = (int) GameManager.get().getWorld().getMap().getProperties().get("tileheight");

		projX = x / tileWidth;
		projY = -(y - tileHeight / 2f) / tileHeight + projX;
		projX -= projY - projX;

		Optional<AbstractEntity> closest = WorldUtil.closestEntityToPosition(projX, projY, 2f);
		if (closest.isPresent() && closest.get() instanceof Clickable) {
			((Clickable) closest.get()).onClick();
		} else {
			AbstractWorld world = GameManager.get().getWorld();
			if (world instanceof InitialWorld) {
				((InitialWorld) (world)).deSelectAll();
			}
		}
		// Build Testing
		GameManager.get().getWorld().addEntity(new Tower(Math.round(projX), Math.round(projY), 0));
	}

	@Override
	public void notifyTouchDown(int screenX, int screenY, int pointer, int button) {
		originX = screenX;
		originY = screenY;

		Vector3 worldCoords = getCameraManager().getCamera().unproject(new Vector3(screenX, screenY, 0));
		handleMouseClick(worldCoords.x, worldCoords.y);
	}

	@Override
	public void notifyTouchDragged(int screenX, int screenY, int pointer) {
		OrthographicCamera c = getCameraManager().getCamera();

		originX -= screenX;
		originY -= screenY;

		// invert the y axis
		originY = -originY;

		originX += getCameraManager().getCamera().position.x;
		originY += getCameraManager().getCamera().position.y;

		c.translate(originX - c.position.x, originY - c.position.y);

		originX = screenX;
		originY = screenY;
	}

	private CameraManager getCameraManager() {
		return (CameraManager) GameManager.get().getManager(CameraManager.class);
	}
}