package com.deco2800.potatoes.handlers;

import com.deco2800.moos.entities.AbstractEntity;
import com.deco2800.moos.managers.GameManager;
import com.deco2800.moos.worlds.AbstractWorld;
import com.deco2800.potatoes.InitialWorld;
import com.deco2800.potatoes.entities.Clickable;
import com.deco2800.potatoes.util.WorldUtil;

import java.util.Optional;

/**
 * Really crappy mouse handler for the game
 */
public class MouseHandler {

	/**
	 * Constructor for the mouse handler
	 */
	public MouseHandler() {
	}

	/**
	 * Currently only handles objects on height 0
	 * @param x
	 * @param y
	 */
	public void handleMouseClick(float x, float y) {
		System.out.printf("Clicked at %f %f\n\r", x, y);

		float projX = 0 , projY = 0;

		projX = x/64f;
		projY = -(y - 32f / 2f) / 32f + projX;
		projX -= projY - projX;

		Optional<AbstractEntity> closest = WorldUtil.closestEntityToPosition(projX, projY, 2f);
		if (closest.isPresent() &&  closest.get() instanceof Clickable) {
			((Clickable) closest.get()).onClick();
		} else {
			AbstractWorld world = GameManager.get().getWorld();
			if (world instanceof InitialWorld) {
				((InitialWorld)(world)).deSelectAll();
			}
		}
	}
}