package com.deco2800.potatoes.entities.effects;

import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.TextureManager;
import com.deco2800.potatoes.util.Box3D;

public class ExplosionEffect extends Effect {

	private static float effectWidth = 1;
	private static float effectHeight = 1;
	private static EffectType effectType = EffectType.EXPLOSION;
	protected static transient String TEXTURE = "explosion1";

	public ExplosionEffect() {
		// empty for serialization

	}

	// public void drawEffect(SpriteBatch batch) {
	// //drawTextureBetween(batch, "lightning",0,0,0.005f,0.005f);
	// }

	public ExplosionEffect(Class<?> targetClass, float posX, float posY, float posZ, float damage, float range) {
		// 0-12, 0+3
//		super(targetClass, 0 - pos(posX, posY)[0], 0 + pos(posX, posY)[1], 0, effectWidth, effectHeight, 0, effectWidth,
//				effectHeight, damage, range, effectType);
//////		super(targetClass, new Vector3(posX - (effectWidth / 2), posY - (effectHeight / 2), 0), effectWidth + 3, effectHeight + 3, 0, effectWidth,
//////				effectHeight, damage, range, EffectType.EXPLOSION);
		// super(targetClass, 0, 0, 0, 0, 0, 0, 0, 0, damage, range, effectType);

		animate = true;
		// System.out.println(0+pos(posX,posY)[0]);
		// // super(targetClass, 0f, 0f, 0,effectWidth, effectHeight, 0,
		// effectWidth, effectHeight, damage, range, effectType);
		// super(targetClass, posX , posY , 0, effectWidth, effectHeight , 0,
		// effectWidth,
		// effectHeight, damage, range, effectType);
		loopAnimation = false;
		// System.out.println(getCenterOffset(effectHeight));
	}

	private static float[] pos(float posX, float posY) {
		int tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get("tilewidth");
		int tileHeight = (int) GameManager.get().getWorld().getMap().getProperties().get("tileheight");

		TextureManager reg = (TextureManager) GameManager.get().getManager(TextureManager.class);
		Texture tex = reg.getTexture(effectType.toString()+"0");

		float lWidth = tex.getWidth() * effectWidth;
		float lHeight = tex.getHeight() * effectHeight;

		Vector2 startPos = worldToScreenCoordinates(posX, posY, 0);

		float originX = tex.getWidth() / 2;
		float originY = tex.getHeight() / 2;

		float tr = (float) (0.5f * Math.sqrt(lWidth * lWidth + lHeight * lHeight));

		float lX = startPos.x - lWidth / 2 - originX;
		float lY = 0 - startPos.y - (lHeight - tileHeight) / 2;

		//System.out.println(worldPosToTile(lX, lY).y);

		float lScaleX = 1f;
		float lScaleY = 1f;

		int srcX = 0;
		int srcY = 0;
		int srcWidth = tex.getWidth();
		int srcHeight = tex.getHeight();
		// System.out.println(worldPosToTile(lX, lY).x+" "+worldPosToTile(lX, lY).y);
		//System.out.println(worldPosToTile(tr, 0).x + " " + worldPosToTile(lX, lY).y);
		float[] p = { worldPosToTile(tr, 0).x, worldPosToTile(lX, lY).y };

		return p;
	}

	@Override
	public void onTick(long time) {
//		// super.onTick(time);
//
//		Box3D newPos = getBox3D();
//		newPos.setX(this.getPosX());
//		newPos.setY(this.getPosY());
//
//		Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();
//
//		for (AbstractEntity entity : entities.values()) {
//			// if (!targetClass.isInstance(entity)) {
//			// continue;
//			// }
//			if (newPos.overlaps(entity.getBox3D())) {
//				//System.out.println(entity);
//				if (entity instanceof Player) {
//					System.out.println("player col");
//				}
//				// ((MortalEntity) entity).damage(damage);
//			}
//		}
	}

}
