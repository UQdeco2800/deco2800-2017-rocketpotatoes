package com.deco2800.potatoes.entities.effects;

import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.Box3D;

public class LightningEffect extends Effect {

	private float lifetime = 1f;
	private float segmentStep = 2f;
	private int segments;

	float xPos;
	float yPos;
	float fxPos;
	float fyPos;

	float distanceDeltaX = 1f;
	float distanceDeltaY = 1f;

	float[][] pos = null;

	boolean staticStrike = true;

	public LightningEffect(float xPos, float yPos, float fxPos, float fyPos) {
		super(fyPos, fxPos, 0, 5f, 5f, 0, 1f, 1f, "lightning");
		DAMAGE = 0.1f;

		// TODO: figure out why inverses
		this.xPos = yPos;
		this.yPos = xPos;
		this.fxPos = fyPos;
		this.fyPos = fxPos;

		pos = positions(this.xPos, this.yPos, this.fxPos, this.fyPos);
	}

	public float[][] positions(float xPos, float yPos, float fxPos, float fyPos) {

		float lengthX = (fxPos - xPos);
		float lengthY = (fyPos - yPos);

		float magnitude = (float) Math.sqrt(lengthX * lengthX + lengthY * lengthY);

		segments = (int) Math.ceil(magnitude / segmentStep);// 8
		float[][] pos = new float[segments + 1][2];

		Random random = new Random();

		float segmentSize = (float) (1.0 / segments);// 0.125
		float segmentsDone = 0;

		for (int i = 0; i < segments + 1; i++) {
			float randx = ((float) ((random.nextFloat() - 0.5) * 2f) * ((segmentSize * magnitude) / 2)
					* distanceDeltaX);// add limit
			float randy = ((float) ((random.nextFloat() - 0.5) * 2f) * ((segmentSize * magnitude) / 2)
					* distanceDeltaY);// add limit

			float x = (float) (xPos + segmentsDone * lengthX
					+ Math.abs(Math.sin(Math.toRadians(rotation(xPos, yPos, fxPos, fyPos)))) * randx);
			float y = (float) (yPos + segmentsDone * lengthY
					+ Math.abs(Math.cos(Math.toRadians(rotation(xPos, yPos, fxPos, fyPos)))) * randy);

			if (i == 0) {
				pos[i][0] = xPos;
				pos[i][1] = yPos;
			} else if (i == segments) {
				pos[i][0] = fxPos;
				pos[i][1] = fyPos;
			} else {
				pos[i][0] = x;
				pos[i][1] = y;
			}
			segmentsDone += segmentSize;
		}

		// Arrays.sort(pos, new Comparator<float[]>() {
		// @Override
		// public int compare(float[] o1, float[] o2) {
		// return Float.compare(o2[1], o1[1]);
		// }
		// });
		return pos;

	}

	public void drawEffect(SpriteBatch batch) {
		if (!staticStrike) {
			pos = positions(this.xPos, this.yPos, this.fxPos, this.fyPos);
		}
		for (int x = 0; x < pos.length - 1; x++) {
			drawTextureBetween(batch, getTexture(), pos[x][0], pos[x][1], pos[x + 1][0], pos[x + 1][1]);
		}

		Box3D newPos = getBox3D();
		newPos.setX(fyPos);
		newPos.setY(fxPos);

		Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();

		float radiusOfImpact = 0.5f;
		for (AbstractEntity entity : entities.values()) {
			if (entity.distance(newPos.getX(), newPos.getY(), 0) <= radiusOfImpact) {
				try {
					((MortalEntity) entity).damage(DAMAGE);
				} catch (Exception e) {

				}
				break;
			}
		}

		ExplosionEffect expEffect = new ExplosionEffect(newPos.getX(), newPos.getY(), 0, 5f, 5f, 0, 1f, 1f);
		GameManager.get().getWorld().addEntity(expEffect);

	}

	@Override
	public void onTick(long time) {
		lifetime -= 0.05;
		if (lifetime <= 0) {
			GameManager.get().getWorld().removeEntity(this);
		}
	}

	@Override
	public float getDamage() {
		return DAMAGE;
	}

}
