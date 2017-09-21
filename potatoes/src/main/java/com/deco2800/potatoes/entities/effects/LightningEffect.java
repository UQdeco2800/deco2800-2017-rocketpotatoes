package com.deco2800.potatoes.entities.effects;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.Box3D;
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

	public LightningEffect(Class<?> targetClass, Vector3 startPos, Vector3 targetPos, float damage, float range) {
		super(targetClass, targetPos, 5f, 5f, 0, 1f, 1f, damage, range, EffectType.LIGHTNING);

		this.startPos = startPos;
		this.targetPos = targetPos;

		animate = false;

		// TODO: figure out why inverses
		// this.xPos = yPos;
		// this.yPos = xPos;
		// this.fxPos = fyPos;
		// this.fyPos = fxPos;

		pos = calculatePositions(startPos.y, startPos.x, targetPos.y, targetPos.x);
	}

	public float[][] calculatePositions(float xPos, float yPos, float fxPos, float fyPos) {
		float lengthX = fxPos - xPos;
		float lengthY = fyPos - yPos;

		float magnitude = (float) Math.sqrt(lengthX * lengthX + lengthY * lengthY);

		segments = (int) Math.ceil(magnitude / segmentStep);// 8
		float[][] positions = new float[segments + 1][2];

		Random random = new Random();

		float segmentSize = (float) (1.0 / segments);// 0.125
		float segmentsDone = 0;

		for (int i = 0; i < segments + 1; i++) {
			float randx = (float) ((random.nextFloat() - 0.5) * 2f) * ((segmentSize * magnitude) / 2) * distanceDeltaX;// add
																														// limit
			float randy = (float) ((random.nextFloat() - 0.5) * 2f) * ((segmentSize * magnitude) / 2) * distanceDeltaY;// add
																														// limit

			float x = (float) (xPos + segmentsDone * lengthX
					+ Math.abs(Math.sin(Math.toRadians(WorldUtil.rotation(xPos, yPos, fxPos, fyPos) - 45))) * randx);
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

	public void drawEffect(SpriteBatch batch) {
		if (!staticStrike) {
			pos = calculatePositions(startPos.y, startPos.x, targetPos.y, targetPos.x);
		}
		for (int x = 0; x < pos.length - 1; x++) {
			drawTextureBetween(batch, getTexture(), pos[x][0], pos[x][1], pos[x + 1][0], pos[x + 1][1]);
		}

		Box3D newPos = getBox3D();
		newPos.setX(targetPos.x);
		newPos.setY(targetPos.y);
		//
		// Map<Integer, AbstractEntity> entities =
		// GameManager.get().getWorld().getEntities();
		//
		// float radiusOfImpact = 0.5f;
		// for (AbstractEntity entity : entities.values()) {
		// if (entity.distance(newPos.getX(), newPos.getY(), 0) <= radiusOfImpact) {
		// try {
		// ((MortalEntity) entity).damage(damage);
		// } catch (Exception e) {
		// //LOGGER.error(e.getMessage());
		// }
		// break;
		// }
		// }

		// ExplosionEffect expEffect = new ExplosionEffect(EnemyEntity.class,
		// newPos.getX(), newPos.getY(), 0, damage,
		// range);

		// ExplosionEffect expEffect = new ExplosionEffect(EnemyEntity.class, 0, 0, 0,
		// damage, range);
		// GameManager.get().getWorld().addEntity(expEffect);

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
