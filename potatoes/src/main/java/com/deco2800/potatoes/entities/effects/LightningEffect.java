package com.deco2800.potatoes.entities.effects;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import com.deco2800.potatoes.collisions.Shape2D;
import com.deco2800.potatoes.collisions.Circle2D;

import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.WorldUtil;

public class LightningEffect extends Effect {

	private float lifetime = 0.4f;
	private float segmentStep = 1f;
	private int segments;

	protected Vector3 startPos;
	protected Vector3 targetPos;

	float distanceDeltaX = 1f;
	float distanceDeltaY = 1f;

	float[][] pos = null;

	boolean staticStrike = true;

	public LightningEffect(Class<?> targetClass, Vector3 startPos, Vector3 targetPos, float damage, float range,
			EffectTexture texture) {
		super(targetClass, new Circle2D(targetPos.x, targetPos.y, 1f), 1f, 1f, damage, range, texture);

		this.startPos = startPos;
		this.targetPos = targetPos;

		animate = false;

		// NOTE: inverses are due to change in cartesian mapping x<->y
		pos = calculatePositions(startPos.y, startPos.x, targetPos.y, targetPos.x);
	}

	/**
	 * Returns nodes along a straight line that are offset perpendicular to the
	 * direction of the line; creating the illusion of a lightning bolt
	 * 
	 * @param xPos
	 *            start x pos
	 * @param yPos
	 *            start y pos
	 * @param fxPos
	 *            end x pos
	 * @param fyPos
	 *            end y pos
	 * @return
	 */
	public float[][] calculatePositions(float xPos, float yPos, float fxPos, float fyPos) {
		float lengthX = fxPos - xPos;
		float lengthY = fyPos - yPos;

		float magnitude = (float) Math.sqrt(lengthX * lengthX + lengthY * lengthY);

		// the number of divisions in the line
		segments = (int) Math.ceil(magnitude / segmentStep);// 8
		float[][] positions = new float[segments + 1][2];

		Random random = new Random();

		// segments as a decimal
		float segmentSize = (float) (1.0 / segments);// 0.125
		float segmentsDone = 0;

		for (int i = 0; i < segments + 1; i++) {
			// the random x offset to add to the nodes
			float randx = (float) ((random.nextFloat() - 0.5) * 2f) * (segmentSize * magnitude / 2) * distanceDeltaX;
			// the random y offset to add to the nodes
			float randy = (float) ((random.nextFloat() - 0.5) * 2f) * (segmentSize * magnitude / 2) * distanceDeltaY;

			// the x pos of the node on the line
			float x = (float) (xPos + segmentsDone * lengthX
					+ Math.abs(Math.sin(Math.toRadians(WorldUtil.rotation(xPos, yPos, fxPos, fyPos) - 45))) * randx);
			// the y pos of the node on the line
			float y = (float) (yPos + segmentsDone * lengthY
					+ Math.abs(Math.cos(Math.toRadians(WorldUtil.rotation(xPos, yPos, fxPos, fyPos) - 45))) * randy);

			if (i == 0) {
				positions[i][0] = xPos;
				positions[i][1] = yPos;
			} else if (i == segments) {
				positions[i][0] = fxPos;
				positions[i][1] = fyPos;
			} else {
				positions[i][0] = x;
				positions[i][1] = y;
			}
			segmentsDone += segmentSize;
		}
		return positions;
	}

	@Override
	public void drawEffect(SpriteBatch batch) {
		if (!staticStrike) {
			pos = calculatePositions(startPos.y, startPos.x, targetPos.y, targetPos.x);
		}
		// draw staight line between each node and the next
		for (int x = 0; x < pos.length - 1; x++) {
			drawTextureBetween(batch, getTexture(), pos[x][0], pos[x][1], pos[x + 1][0], pos[x + 1][1]);
		}
	}

	@Override
	public void onTick(long time) {
		super.onTick(time);
		lifetime -= 0.05;
		if (lifetime <= 0) {
			GameManager.get().getWorld().removeEntity(this);
		}
	}

}
