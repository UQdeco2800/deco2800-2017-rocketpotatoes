package com.deco2800.potatoes.renderering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.*;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.entities.animation.Animated;
import com.deco2800.potatoes.entities.health.HasProgress;
import com.deco2800.potatoes.entities.health.HasProgressBar;
import com.deco2800.potatoes.entities.health.ProgressBar;
import com.deco2800.potatoes.entities.trees.AbstractTree;
import com.deco2800.potatoes.entities.trees.ResourceTree;
import com.deco2800.potatoes.managers.CameraManager;
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
				// System.out.println(abstractEntity+" "+t1);
				if (abstractEntity instanceof Effect) {
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

		// drawTextureBetween("lightning",0, 0, 1, 1);

		/* Render each entity (backwards) in order to retain objects at the front */
		for (Map.Entry<AbstractEntity, Integer> e : entities.entrySet()) {
			AbstractEntity entity = e.getKey();

			TextureManager reg = GameManager.get().getManager(TextureManager.class);
			Texture tex;
			if (e.getKey() instanceof Animated) {
				// Animations should probably be changed to TextureRegion for performance
				tex = reg.getTexture(((Animated) e.getKey()).getAnimation().getFrame());
			} else {
				tex = reg.getTexture(entity.getTexture());
			}

			Vector2 isoPosition = worldToScreenCoordinates(entity.getPosX(), entity.getPosY());

			// We want to keep the aspect ratio of the image so...
			float aspect = (float) (tex.getWidth()) / (float) (tileWidth);

			// old method of draw:
			// batch.draw(tex, isoPosition.x, isoPosition.y,
			// tileWidth*entity.getXRenderLength(),
			// (tex.getHeight()/aspect)*entity.getYRenderLength());

			// NEW: changed the render method to allow for sprite rotation.

			batch.draw(tex,
					// x, y
					isoPosition.x, isoPosition.y,
					// originX, originY
					(tileWidth * entity.getXRenderLength()) / 2,
					(tileHeight * entity.getYRenderLength()) / 2,
					// width, height
					tileWidth * entity.getXRenderLength(),
					(tex.getHeight() / aspect) * entity.getYRenderLength(),
					// scaleX, scaleY, rotation
					1, 1, 0 - entity.rotateAngle(),
					// srcX, srcY
					0, 0,
					// srcWidth, srcHeight
					tex.getWidth(), tex.getHeight(),
					// flipX, flipY
					false, false);
		}

		for (Map.Entry<AbstractEntity, Integer> e : entities.entrySet()) {
			AbstractEntity entity = e.getKey();

			Vector2 isoPosition = worldToScreenCoordinates(entity.getPosX(), entity.getPosY());

			if (entity instanceof HasProgressBar && ((HasProgress) entity).showProgress()) {
				TextureManager reg = GameManager.get()
					.getManager(TextureManager.class);

				ProgressBar progressBar = ((HasProgressBar) entity).getProgressBar();
				Texture barTexture = reg.getTexture((progressBar.getTexture()));

				// sets colour palette
				batch.setColor(progressBar.getColour(((HasProgress) entity).getProgressRatio()));

				// draws the progress bar
				Texture entityTexture = reg.getTexture(entity.getTexture());
				float aspect = (float) (entityTexture.getWidth()) / (float) (tileWidth);

				float barRatio = ((HasProgress) entity).getProgressRatio();
				float maxBarWidth = tileWidth * entity.getXRenderLength()
					* progressBar.getWidthScale();
				float barWidth = maxBarWidth * barRatio;
				float barBackgroundWidth = maxBarWidth * (1 - barRatio);

				// x co-ordinate,
				// finds the overlap length of the bar and moves it half as much left
				float barX = isoPosition.x - (tileWidth * entity.getXRenderLength()
						* (progressBar.getWidthScale() - 1) / 2);
				// y co-ordinate
				// If height is specified, use it, otherwise estimate the right height
				float barY = isoPosition.y + (entityTexture.getHeight() / aspect * entity.getYRenderLength());
				float endX = barX + barWidth;
				// We haven't implemented rounded corners, but when we do:
				// float greyBarX = endX + endWidth;

				//draw half of bar that represents current health
				batch.draw(barTexture,
						// x, y
						barX, barY,
						// width, height
						barWidth, maxBarWidth / 8,
						// srcX, srcY
						0, 0,
						// srcWidth, srcHeight
						(int) (barTexture.getWidth() * barRatio), barTexture.getHeight(),
						// flipX, flipY
						false, false);

				//draw shadow half of bar that represents health lost
				batch.setColor(0.5f, 0.5f, 0.5f, 1f);
				batch.draw(barTexture,
						// x, y
						endX, barY,
						// width, height
						barBackgroundWidth, maxBarWidth / 8,
						// srcX, srcY
						(int) (barTexture.getWidth() * barRatio), 0,
						// srcWidth, srcHeight
						(int) (barTexture.getWidth() * (1 - barRatio)), barTexture.getHeight(),
						// flipX, flipY
						false, false);

				// reset the batch colour
				batch.setColor(Color.WHITE);

				/* display font (used for debugging)
				 * font.setColor(Color.RED); font.getData().setScale(1.0f); font.draw(batch,
				 * String.format("%d", ((HasProgress) entity).getProgress()), isoPosition.x +
				 * tileWidth / 2 - 10, isoPosition.y + 60);
				 */
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
			if (entity instanceof ResourceTree && ((ResourceTree) entity).getGatherCount() > 0) {
				font.setColor(Color.GREEN);
				font.getData().setScale(1.0f);
				font.draw(batch, String.format("%s", ((ResourceTree) entity).getGatherCount()),
						isoPosition.x + tileWidth / 2 - 7, isoPosition.y + 65);
			}

			/**************************/
			MultiplayerManager m = GameManager.get().getManager(MultiplayerManager.class);
			if (entity instanceof Player && m.isMultiplayer()) {
				font.setColor(Color.WHITE);
				font.getData().setScale(1.3f);
				if (m.getID() == e.getValue()) {
					font.setColor(Color.BLUE);
				}
				font.draw(batch, String.format("%s", m.getClients().get(e.getValue())),
						isoPosition.x + tileWidth / 2 - 10, isoPosition.y + 70);
			}
			
			if(entity instanceof Effect) {
				((Effect)entity).drawEffect(batch); 
			}
		}

		// /*
		// Timmy approves this commented out code. Shut up sonar!
		// Leaving this here.
		// It renders the rendering order onto entites so you can see what gets rendered
		// when
		//
		// */s
		// for (int index = 0; index < entities.size(); index++) {
		// Renderable entity = entities.get(index);
		// float cartX = entity.getPosX();
		// float cartY = (worldWidth-1) - entity.getPosY();
		//
		// float isoX = baseX + ((cartX - cartY) / 2.0f * tileWidth);
		// float isoY = baseY + ((cartX + cartY) / 2.0f) * tileHeight;
		//
		// font.draw(batch, String.format("%d", index), isoX + 32, isoY + 32);
		// }

		batch.end();

	}

	private void renderProgress(SpriteBatch batch, AbstractEntity entity) {
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

	public static Vector3 screenToWorldCoordiates(float x, float y, float z) {
		return GameManager.get().getManager(CameraManager.class).getCamera()
				.unproject(new Vector3(x, y, z));
	}

	public static Vector2 worldPosToTile(float x, float y) {
		float projX = 0, projY = 0;

		float tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get("tilewidth");
		float tileHeight = (int) GameManager.get().getWorld().getMap().getProperties().get("tileheight");

		projX = x / tileWidth;
		projY = -(y - tileHeight / 2f) / tileHeight + projX;
		projX -= projY - projX;

		return new Vector2(projX, projY);
	}
}
