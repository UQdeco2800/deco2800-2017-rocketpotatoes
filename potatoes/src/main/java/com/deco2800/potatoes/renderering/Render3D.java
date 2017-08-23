package com.deco2800.potatoes.renderering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.ExplosionProjectile;
import com.deco2800.potatoes.entities.HasProgress;
import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.entities.trees.AbstractTree;
import com.deco2800.potatoes.entities.trees.ResourceTree;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.MultiplayerManager;
import com.deco2800.potatoes.managers.TextureManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A simple isometric renderer for DECO2800 games
 * 
 * @Author Tim Hadwen
 */
public class Render3D implements Renderer {

	BitmapFont font;
	SpriteBatch renderBatch;

	private static final Logger LOGGER = LoggerFactory.getLogger(Render3D.class);

	/**
	 * Renders onto a batch, given a renderables with entities It is expected that
	 * AbstractWorld contains some entities and a Map to read tiles from
	 * 
	 * @param batch
	 *            Batch to render onto
	 */
	@Override
	public void render(SpriteBatch batch) {
		this.renderBatch = batch;
		if (font == null) {
			font = new BitmapFont();
			font.getData().setScale(1.0f);
		}
		Map<Integer, AbstractEntity> renderables = GameManager.get().getWorld().getEntities();

		int tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get("tilewidth");
		int tileHeight = (int) GameManager.get().getWorld().getMap().getProperties().get("tileheight");

		/* Tree map so we sort our entities properly */
		SortedMap<AbstractEntity, Integer> entities = new TreeMap<>(new Comparator<AbstractEntity>() {
			@Override
			public int compare(AbstractEntity abstractEntity, AbstractEntity t1) {
				int val = abstractEntity.compareTo(t1);
				//System.out.println(abstractEntity+" "+t1);
				if (abstractEntity instanceof ExplosionProjectile) {
					val = -1;
				}
				// Hacky fix so TreeMap doesn't throw away duplicate values. I.e. Renderables in
				// the exact same location
				// Since TreeMap's think when the comparator result is 0 the objects are
				// duplicates, we just make that
				// impossible to occur.
				if (val == 0) {
					val = 1;
				}

				return val;
			}
		});

		/* Gets a list of all entities in the renderables */
		for (Map.Entry<Integer, AbstractEntity> e : renderables.entrySet()) {
			entities.put(e.getValue(), e.getKey());
		}

		batch.begin();

		//drawTextureBetween("Lightning",0, 0, 1, 1);

		/* Render each entity (backwards) in order to retain objects at the front */
		for (Map.Entry<AbstractEntity, Integer> e : entities.entrySet()) {
			AbstractEntity entity = e.getKey();

			String textureString = entity.getTexture();
			TextureManager reg = (TextureManager) GameManager.get().getManager(TextureManager.class);
			Texture tex = reg.getTexture(textureString);

			Vector2 isoPosition = worldToScreenCoordinates(entity.getPosX(), entity.getPosY());

			// We want to keep the aspect ratio of the image so...
			float aspect = (float) (tex.getWidth()) / (float) (tileWidth);

			// old method of draw:
			// batch.draw(tex, isoPosition.x, isoPosition.y,
			// tileWidth*entity.getXRenderLength(),
			//(tex.getHeight()/aspect)*entity.getYRenderLength());

			// NEW: changed the render method to allow for sprite rotation.

			batch.draw(tex, isoPosition.x, isoPosition.y, (tileWidth * entity.getXRenderLength()) / 2,
					(tileHeight * entity.getYRenderLength()) / 2, tileWidth * entity.getXRenderLength(),
					(tex.getHeight() / aspect) * entity.getYRenderLength(), 1, 1, 0 - entity.rotateAngle(), 0, 0,
					tex.getWidth(), tex.getHeight(), false, false);
		}

		for (Map.Entry<AbstractEntity, Integer> e : entities.entrySet()) {
			AbstractEntity entity = e.getKey();

			Vector2 isoPosition = worldToScreenCoordinates(entity.getPosX(), entity.getPosY());

			if (entity instanceof HasProgress && ((HasProgress) entity).showProgress()) {
				font.setColor(Color.RED);
				font.getData().setScale(1.0f);
				font.draw(batch, String.format("%d%%", ((HasProgress) entity).getProgress()),
						isoPosition.x + tileWidth / 2 - 10, isoPosition.y + 60);
			}
			/*
			 * Construction percentage displayed in yellow
			 */
			if (entity instanceof AbstractTree && ((AbstractTree) entity).getConstructionLeft() > 0) {
				font.setColor(Color.YELLOW);
				font.getData().setScale(1.0f);
				font.draw(batch, String.format("%d%%", 100 - ((AbstractTree) entity).getConstructionLeft()),
						isoPosition.x + tileWidth / 2 - 10, isoPosition.y + 60);
			}

			/*
			 * Display resource collected for Resource Tree
			 */
			if (entity instanceof ResourceTree && ((ResourceTree) entity).getResourceAmount() > 0) {
				font.setColor(Color.GREEN);
				font.getData().setScale(1.0f);
				font.draw(batch, String.format("%s", ((ResourceTree) entity).resourceCount),
						isoPosition.x + tileWidth / 2 - 7, isoPosition.y + 65);
			}

			/**************************/
			MultiplayerManager m = (MultiplayerManager) GameManager.get().getManager(MultiplayerManager.class);
			if (entity instanceof Player && m.isMultiplayer()) {
				font.setColor(Color.WHITE);
				font.getData().setScale(1.3f);
				if (m.getID() == e.getValue()) {
					font.setColor(Color.BLUE);
				}
				font.draw(batch, String.format("%s", m.getClients().get(e.getValue())),
						isoPosition.x + tileWidth / 2 - 10, isoPosition.y + 70);

			}
		}

		// /*
		// Timmy approves this commented out code. Shut up sonar!
		// Leaving this here.
		// It renders the rendering order onto entites so you can see what gets rendered
		// when
		//
		// */s
		//for (int index = 0; index < entities.size(); index++) {
		//Renderable entity = entities.get(index);
		//float cartX = entity.getPosX();
		//float cartY = (worldWidth-1) - entity.getPosY();
		//
		//float isoX = baseX + ((cartX - cartY) / 2.0f * tileWidth);
		//float isoY = baseY + ((cartX + cartY) / 2.0f) * tileHeight;
		//
		//font.draw(batch, String.format("%d", index), isoX + 32, isoY + 32);
		// }

		batch.end();

	}

	public void drawTextureBetween(String texture, float xPos, float yPos, float fxPos, float fyPos) {
		int tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get("tilewidth");
		int tileHeight = (int) GameManager.get().getWorld().getMap().getProperties().get("tileheight");

		TextureManager reg = (TextureManager) GameManager.get().getManager(TextureManager.class);
		Texture tex = reg.getTexture(texture);

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

		renderBatch.draw(tex, lX, lY, originX, originY, lWidth, lHeight, lScaleX, lScaleY, rotation, srcX, srcY,
				srcWidth, srcHeight, false, false);

	}

	/**
	 * Returns the correct tile renderer for the given rendering engine
	 * 
	 * @param batch
	 *            The current sprite batch
	 * @return A TiledMapRenderer for the current engine
	 */
	@Override
	public BatchTiledMapRenderer getTileRenderer(SpriteBatch batch) {
		return new IsometricTiledMapRenderer(GameManager.get().getWorld().getMap(), 1, batch);
	}

	/**
	 * Transforms world coordinates to screen coordinates for rendering.
	 * 
	 * @param x
	 *            x coord in the world
	 * @param y
	 *            y coord in the world
	 * @return a Vector2 with the screen coordinates
	 */
	public static Vector2 worldToScreenCoordinates(float x, float y) {
		int worldLength = GameManager.get().getWorld().getLength();
		int worldWidth = GameManager.get().getWorld().getWidth();

		int tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get("tilewidth");
		int tileHeight = (int) GameManager.get().getWorld().getMap().getProperties().get("tileheight");

		float baseX = tileWidth * (worldWidth / 2.0f - 0.5f); // bad
		float baseY = -tileHeight / 2 * worldLength + tileHeight / 2f; // good

		float cartX = x;
		float cartY = (worldWidth - 1) - y;

		float isoX = baseX + ((cartX - cartY) / 2.0f * tileWidth);
		float isoY = baseY + ((cartX + cartY) / 2.0f) * tileHeight;

		return new Vector2(isoX, isoY);
	}

	/**
	 * Transforms world coordinates to screen coordinates for rendering.
	 * 
	 * @param p
	 *            Vector2 with the world coords
	 * @return a Vector2 with the screen coordinates
	 */
	public static Vector2 worldToScreenCoordinates(Vector2 p) {
		return worldToScreenCoordinates(p.x, p.y);
	}

    public static Vector2 screenToWorldCoordiates(float x, float y) {
		float projX = 0, projY = 0;

		float tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get("tilewidth");
		float tileHeight = (int) GameManager.get().getWorld().getMap().getProperties().get("tileheight");

		projX = x / tileWidth;
		projY = -(y - tileHeight / 2f) / tileHeight + projX;
		projX -= projY - projX;

		return new Vector2(projX, projY);
	}
}
