package com.deco2800.potatoes.entities.effects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.TextureManager;
import com.deco2800.potatoes.util.Box3D;

public abstract class Effect extends AbstractEntity implements Tickable {

	protected float DAMAGE = 0;

	public Effect() {

	}

	public Effect(float posX, float posY, float posZ, float xLength, float yLength, float zLength, float xRenderLength,
			float yRenderLength, String texture) {
		super(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, true, texture);
	}

	public void drawEffect(SpriteBatch batch) {

	}

	@Override
	public void onTick(long time) {
		Box3D newPos = getBox3D();
		newPos.setX(this.getPosX());
		newPos.setY(this.getPosY());

//		Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();
//		// Check surroundings
//		for (AbstractEntity entity : entities.values()) {
//
//			if (newPos.overlaps(entity.getBox3D())) {
//				System.out.println(entity.toString());
//				try {
//					((MortalEntity) entity).damage(DAMAGE);
//				} catch (Exception e) {
//
//				}
//
//				break;
//			}
//
//		}
	}
	
	public float rotation(float xPos, float yPos, float fxPos, float fyPos) {
		Vector2 startPos = worldToScreenCoordinates(xPos, yPos, 0);
		Vector2 endPos = worldToScreenCoordinates(fxPos, fyPos, 0);
		float l = endPos.x - startPos.x;
		float h = endPos.y - startPos.y;
		float rotation = (float) (Math.atan2(l, h) * 180 / Math.PI) - 90;
		return rotation;
	}
	
	public void drawTextureBetween(SpriteBatch batch, String texture, float xPos, float yPos, float fxPos,
			float fyPos) {
		int tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get("tilewidth");
		int tileHeight = (int) GameManager.get().getWorld().getMap().getProperties().get("tileheight");

		TextureManager reg = (TextureManager) GameManager.get().getManager(TextureManager.class);
		Texture tex = reg.getTexture(this.getTexture());

		float lWidth = tex.getWidth();
		float lHeight = tex.getHeight();

		Vector2 startPos = worldToScreenCoordinates(xPos, yPos, 0);
		Vector2 endPos = worldToScreenCoordinates(fxPos, fyPos, 0);

		float l = endPos.x - startPos.x;
		float h = endPos.y - startPos.y;

		float lX = startPos.x - (lWidth - tileWidth) / 2;
		float lY = 0 - startPos.y - (lHeight - tileHeight) / 2;

		float originX = tex.getWidth() / 2;
		float originY = tex.getHeight() / 2;

		float lScaleX = (float) (Math.sqrt(l * l + h * h));
		float lScaleY = 0.4f;

		int srcX = 0;
		int srcY = 0;
		int srcWidth = tex.getWidth();
		int srcHeight = tex.getHeight();
		batch.draw(tex, lX, lY, originX, originY, lWidth, lHeight, lScaleX, lScaleY, rotation(xPos, yPos, fxPos, fyPos),
				srcX, srcY, srcWidth, srcHeight, false, false);

	}

	public abstract float getDamage();


}
