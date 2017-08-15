package com.deco2800.potatoes.managers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.renderering.Render3D;

public class CameraManager extends Manager{

	// Unused
	private OrthographicCamera camera;

	private AbstractEntity target;

	/**
	 * Set's an AbstractEntity as the camera target. Pass in null for no target.
	 * @param target
	 */
	public void setTarget(AbstractEntity target) {
		this.target = target;
	}

	/**
	 * @return returns if a target is set
	 */
	public boolean hasTarget() {
		return target != null;
	}

	/**
	 * Moves the camera to center on the target, if the camera has no target this method does nothing
	 */
	public void centerOnTarget() {
		if (camera != null) {
			if (hasTarget()) {
				Vector2 isoPosition = Render3D.worldToScreenCoordinates(target.getPosX(), target.getPosY());

				this.camera.position.x = isoPosition.x;
				this.camera.position.y = isoPosition.y;
			}
		}
	}

	/**
	 * Set's the internal camera
	 * @param camera
	 */
	public void setCamera(OrthographicCamera camera) {
		this.camera = camera;
	}

	/**
	 * @return the internal camera object. Null if none is set yet
	 */
	public OrthographicCamera getCamera() {
		return camera;
	}

}
