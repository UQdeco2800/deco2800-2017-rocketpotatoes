package com.deco2800.potatoes.managers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.potatoes.entities.AbstractEntity;

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
		return;
		if (camera != null) {
			if (hasTarget()) {
				//System.out.println(target.getPosX() + " : " + target.getPosY());
				Vector2 xVector = new Vector2(
						1,
						0);

				// Rotate x unit vector 60 degrees
				xVector.rotate(60);

				Vector2 yVector = new Vector2(
						0,
						1);

				// Rotate y unit vector 30 degrees
				yVector.rotate(30);

				xVector.x *= target.getPosX() * 32;
				xVector.y *= target.getPosX() * 32;

				yVector.x *= target.getPosY() * 32;
				yVector.y *= target.getPosY() * 32;

				Vector2 newPos = new Vector2(0, 0);
				newPos.add(xVector).add(yVector);


				System.out.println(newPos.x + " : " + newPos.y);

				camera.position.x = newPos.x;
				camera.position.y = newPos.y;

				//camera.project()
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
