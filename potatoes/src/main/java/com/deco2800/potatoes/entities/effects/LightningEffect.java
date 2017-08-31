package com.deco2800.potatoes.entities.effects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.TextureManager;

public class LightningEffect extends Effect {

	private float lifetime = 0.25f;

	float xPos = 0;
	float yPos = 0;
	float fxPos = 1;
	float fyPos = 1;

	public LightningEffect(float startX, float startY, float endX, float endY) {
		setTexture("Lightning");
		this.xPos = startX;
		this.yPos = startY;
		this.fxPos = endX;
		this.fyPos = endY;
	}

	public void drawEffect(SpriteBatch batch) {

		int tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get("tilewidth");
		int tileHeight = (int) GameManager.get().getWorld().getMap().getProperties().get("tileheight");

		TextureManager reg = (TextureManager) GameManager.get().getManager(TextureManager.class);
		Texture tex = reg.getTexture(this.getTexture());

		float lWidth = tex.getWidth();
		float lHeight = tex.getHeight();

		Vector2 startPos = worldToScreenCoordinates(xPos, yPos);

		Vector2 endPos = worldToScreenCoordinates(fxPos, fyPos);

		float l = endPos.x - startPos.x;
		float h = endPos.y - startPos.y;

		float lX = startPos.x - (lWidth - tileWidth) / 2;
		float lY = 0 - startPos.y - (lHeight - tileHeight) / 2;

		float originX = tex.getWidth() / 2;
		float originY = tex.getHeight() / 2;

		float lScaleX = (float) (Math.sqrt(l * l + h * h));
		float lScaleY = 0.4f;

		float rotation = (float) (Math.atan2(l, h) * 180 / Math.PI) - 90;

		int srcX = 0;
		int srcY = 0;
		int srcWidth = tex.getWidth();
		int srcHeight = tex.getHeight();

		batch.draw(tex, lX, lY, originX, originY, lWidth, lHeight, lScaleX, lScaleY, rotation, srcX, srcY, srcWidth,
				srcHeight, false, false);

	}

	@Override
	public void onTick(long time) {
		lifetime -= 0.05;
		if (lifetime <= 0)
			GameManager.get().getWorld().removeEntity(this);
	}

	@Override
	public float getDamage() {
		// TODO Auto-generated method stub
		return 0;
	}

}
