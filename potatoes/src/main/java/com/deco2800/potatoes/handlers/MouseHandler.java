package com.deco2800.potatoes.handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Clickable;
import com.deco2800.potatoes.gui.TreeShopGui;
import com.deco2800.potatoes.managers.CameraManager;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.GuiManager;
import com.deco2800.potatoes.observers.MouseMovedObserver;
import com.deco2800.potatoes.observers.TouchDownObserver;
import com.deco2800.potatoes.observers.TouchDraggedObserver;
import com.deco2800.potatoes.renderering.Render3D;
import com.deco2800.potatoes.util.WorldUtil;
import com.deco2800.potatoes.worlds.World;

import java.util.Optional;

/**
 * Really crappy mouse handler for the game
 */
public class MouseHandler implements TouchDownObserver, TouchDraggedObserver, MouseMovedObserver {
	private int originX;
	private int originY;
	private TreeShopGui treeShop;

	/**
	 * Constructor for the mouse handler
	 */
	public MouseHandler() {
		treeShop = GameManager.get().getManager(GuiManager.class).getGui(TreeShopGui.class);
	}

	/**
	 * Currently only handles objects on height 0
	 * 
	 * @param x
	 * @param y
	 * @param button
	 */
	public void handleMouseClick(float x, float y, int button) {
		Vector2 coords = Render3D.worldPosToTile(x, y);

		Optional<AbstractEntity> closest = WorldUtil.closestEntityToPosition(coords.x, coords.y, 2f);
		if (closest.isPresent() && closest.get() instanceof Clickable) {
			((Clickable) closest.get()).onClick();
			return;
		} else {
			World world = GameManager.get().getWorld();
			if (world instanceof World) {
				world.deSelectAll();
			}
		}
		treeShop.initShop(originX,originY);
	}

	@Override
	public void notifyTouchDown(int screenX, int screenY, int pointer, int button) {
		originX = screenX;
		originY = screenY;

		Vector3 worldCoords = Render3D.screenToWorldCoordiates(screenX, screenY);
		handleMouseClick(worldCoords.x, worldCoords.y, button);
		
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
	
	/**
	 * Get the camera manager
	 * 
	 * @return CameraManager
	 */
	private CameraManager getCameraManager() {
		return GameManager.get().getManager(CameraManager.class);
	}

	@Override
	public void notifyMouseMoved(int screenX, int screenY) {
		treeShop.checkMouseOver(screenX, screenY);
	}
}
