package com.deco2800.potatoes.renderering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.deco2800.potatoes.collisions.*;
import com.deco2800.potatoes.entities.*;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.entities.animation.Animated;
import com.deco2800.potatoes.entities.health.HasProgress;
import com.deco2800.potatoes.entities.health.HasProgressBar;
import com.deco2800.potatoes.entities.health.ProgressBar;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.entities.trees.ResourceTree;
import com.deco2800.potatoes.gui.DebugModeGui;
import com.deco2800.potatoes.gui.TreeShopGui;
import com.deco2800.potatoes.managers.CameraManager;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.GameTimeManager;
import com.deco2800.potatoes.managers.GuiManager;
import com.deco2800.potatoes.managers.InputManager;
import com.deco2800.potatoes.managers.MultiplayerManager;
import com.deco2800.potatoes.managers.ParticleManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.managers.TextureManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.worlds.World;
import com.deco2800.potatoes.worlds.terrain.Terrain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A simple isometric renderer for DECO2800 games
 *
 * @Author Tim Hadwen, Dion Lao
 */
public class Render3D implements Renderer {

	BitmapFont font;

	private static final Logger LOGGER = LoggerFactory.getLogger(Render3D.class);

	/**
	 * Renders onto a batch, given a renderables with entities It is expected that
	 * World contains some entities and a Map to read tiles from
	 *
	 * @param batch
	 *            Batch to render onto
	 */
	@Override
	public void render(SpriteBatch batch) {
		renderMap(batch);

		//loops through all entities, renders their CollisionMasks as shadows TODO add opt in for entities
		renderHitBoxShadows();


		batch.setColor(GameManager.get().getManager(GameTimeManager.class).getColour());

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

			Vector2 isoPosition = worldToScreenCoordinates(entity.getPosX(), entity.getPosY(), entity.getPosZ());

			// We want to keep the aspect ratio of the image so...
			float aspect = (float) tex.getWidth() / (float) tileWidth;

			batch.draw(tex,
					// x, y
					isoPosition.x, isoPosition.y,
					// originX, originY
					tileWidth * entity.getXRenderLength() / 2, tileHeight * entity.getYRenderLength() / 2,
					// width, height
					tileWidth * entity.getXRenderLength(), tex.getHeight() / aspect * entity.getYRenderLength(),
					// scaleX, scaleY, rotation
					1, 1, 0 - entity.rotationAngle(),
					// srcX, srcY
					0, 0,
					// srcWidth, srcHeight
					tex.getWidth(), tex.getHeight(),
					// flipX, flipY
					false, false);
		}

		for (Map.Entry<AbstractEntity, Integer> e : entities.entrySet()) {
			AbstractEntity entity = e.getKey();

			Vector2 isoPosition = worldToScreenCoordinates(entity.getPosX(), entity.getPosY(), entity.getPosZ());

			if (entity instanceof HasProgressBar && ((HasProgress) entity).showProgress()) {
				ProgressBar PROGRESS_BAR = ((HasProgressBar) entity).getProgressBar();
				// Allow entities to return null if they don't want to display their progress
				// bar
				if (PROGRESS_BAR != null) {
					TextureManager reg = GameManager.get().getManager(TextureManager.class);

					Texture barTexture = reg.getTexture(PROGRESS_BAR.getTexture());

					// sets colour palette
					batch.setColor(PROGRESS_BAR.getColour(((HasProgress) entity).getProgressRatio()));

					// draws the progress bar
					Texture entityTexture = reg.getTexture(entity.getTexture());
					float aspect = (float) entityTexture.getWidth() / (float) tileWidth;

					float barRatio = ((HasProgress) entity).getProgressRatio();
					float maxBarWidth = tileWidth * entity.getXRenderLength() * PROGRESS_BAR.getWidthScale();
					float barWidth = maxBarWidth * barRatio;
					float barBackgroundWidth = maxBarWidth * (1 - barRatio);

					// x co-ordinate,
					// finds the overlap length of the bar and moves it half as much left
					float barX = isoPosition.x
							- tileWidth * entity.getXRenderLength() * (PROGRESS_BAR.getWidthScale() - 1) / 2;
					// y co-ordinate
					// If height is specified, use it, otherwise estimate the right height
					float barY = isoPosition.y + entityTexture.getHeight() / aspect * entity.getYRenderLength();
					float endX = barX + barWidth;
					// We haven't implemented rounded corners, but when we do:
					// float greyBarX = endX + endWidth;

					// draw half of bar that represents current health
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

					// draw shadow half of bar that represents health lost
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

					/*
					 * display font (used for debugging) font.setColor(Color.RED);
					 * font.getData().setScale(1.0f); font.draw(batch, String.format("%d",
					 * ((HasProgress) entity).getProgress()), isoPosition.x + tileWidth / 2 - 10,
					 * isoPosition.y + 60);
					 */
				}
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

			if (entity instanceof Effect) {
				((Effect) entity).drawEffect(batch);
			}
		}

		batch.end();

		// TODO: add render for projectile's separately
		GameManager.get().getManager(ParticleManager.class).draw(batch);

		//if DebugGui is shown ...
		if (!GameManager.get().getManager(GuiManager.class).getGui(DebugModeGui.class).isHidden()) {
			renderCollisionMasks(batch); 	// outline the CollisionMasks of entities
			renderPathingNodes(batch);		// show all nodes in PathManager
		}

		GameManager.get().getManager(GuiManager.class).getGui(TreeShopGui.class).render();
	}

	private void renderMap(SpriteBatch batch) {
		TextureManager textureManager = GameManager.get().getManager(TextureManager.class);
		World world = GameManager.get().getWorld();

		int tileWidth = (int) world.getMap().getProperties().get("tilewidth");
		int tileHeight = (int) world.getMap().getProperties().get("tileheight");

		/* Render the tiles first */
		BatchTiledMapRenderer tileRenderer = getTileRenderer(batch);
		tileRenderer.setView(GameManager.get().getManager(CameraManager.class).getCamera());

		batch.setColor(GameManager.get().getManager(GameTimeManager.class).getColour());

		batch.begin();
		// within the screen, but down rounded to the nearest tile
		Vector2 waterCoords = new Vector2(
				tileWidth * (float) Math.floor(tileRenderer.getViewBounds().x / tileWidth - 1),
				tileHeight * (float) Math.floor(tileRenderer.getViewBounds().y / tileHeight - 1));
		// draw with screen corner and width a little bit more than the screen
		TiledDrawable background = GameManager.get().getManager(WorldManager.class).getBackground();
		background.draw(batch, waterCoords.x, waterCoords.y, tileRenderer
						.getViewBounds().width +
						tileWidth * 4,
				tileRenderer.getViewBounds().height + tileHeight * 4);
		background.draw(batch, waterCoords.x - tileWidth / 2, waterCoords.y - tileHeight / 2,
				tileRenderer.getViewBounds().width + tileWidth * 4,
				tileRenderer.getViewBounds().height + tileHeight * 4);
		batch.end();

		tileRenderer.render();

		/* Draw highlight on current tile we have selected */
		batch.begin();
		// Resets the colour for tile highlights
		batch.setColor(Color.WHITE);
		// Convert our mouse coordinates to world, where we then convert them to a tile
		// [x, y], then back to screen

		Vector3 coords = Render3D.screenToWorldCoordiates(GameManager.get().getManager(InputManager.class).getMouseX(),
				GameManager.get().getManager(InputManager.class).getMouseY(), 0);
		Vector2 tileCoords = Render3D.worldPosToTile(coords.x, coords.y);

		float tileX = (int) Math.floor(tileCoords.x);
		float tileY = (int) Math.floor(tileCoords.y);

		Vector2 realCoords = Render3D.worldToScreenCoordinates(tileX, tileY, 0);

		float distance = GameManager.get().getManager(PlayerManager.class).distanceFromPlayer(tileX,tileY);
		Terrain terrain = world.getTerrain((int)tileX, (int)tileY);
		TreeShopGui treeShopGui = GameManager.get().getManager(GuiManager.class).getGui(TreeShopGui.class);
		treeShopGui.setPlantable(distance < treeShopGui.getMaxRange() && terrain.isPlantable() && !terrain.getTexture
				().equals("void"));
		if (terrain.getTexture().equals("void")) {
			// Do nothing
		} else {
			if (treeShopGui.getPlantable())
				batch.draw(textureManager.getTexture("highlight_tile"), realCoords.x, realCoords.y);
			else
				batch.draw(textureManager.getTexture("highlight_tile_invalid"), realCoords.x, realCoords.y);
		}


		batch.end();
	}

	//TODO comment stuff dingus
	private void renderHitBoxShadows() {
		//camera used for translation
		OrthographicCamera camera = GameManager.get().getManager(CameraManager.class).getCamera();

		//start drawing & set fill transparent grey
		Gdx.gl.glEnable(GL20.GL_BLEND);
		ShapeRenderer shapeRenderer = new ShapeRenderer();
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(new Color(0, 0, 0, 0.3f));


		Vector2 screenWorldCoords;
		float rt2 = (float) Math.sqrt(2);

		for (AbstractEntity e : GameManager.get().getWorld().getEntities().values()) {
			CollisionMask shadow = e.getMask();

			if (shadow instanceof Box2D) {

				Box2D box = (Box2D) shadow;

				//calculate orthagonal corners of box
				screenWorldCoords = worldToScreenCoordinates(
						box.getX() + box.getXLength()/2, box.getY() + box.getYLength()/2, 0);
				Vector3 c1 = camera.project(new Vector3(screenWorldCoords.x, screenWorldCoords.y, 0));

				screenWorldCoords = worldToScreenCoordinates(
						box.getX() - box.getXLength()/2, box.getY() + box.getYLength()/2, 0);
				Vector3 c2 = camera.project(new Vector3(screenWorldCoords.x, screenWorldCoords.y, 0));

				Vector3 c3 = new Vector3(c2.x * 2 - c1.x, c1.y, 0);

				Vector3 c4 = new Vector3(c2.x, c1.y * 2 - c2.y, 0); //c4 is c2 reflected on y

				//use 2 triangles to get diamond shape
				shapeRenderer.triangle(c1.x, c1.y, c2.x, c2.y, c3.x, c3.y);
				shapeRenderer.triangle(c1.x, c1.y, c4.x, c4.y, c3.x, c3.y);


			} else if (shadow instanceof Circle2D) {
				Circle2D circ = (Circle2D) shadow;

				//calculate orthagonal corners of bounding box
				screenWorldCoords = worldToScreenCoordinates(
						circ.getX() + circ.getRadius(), circ.getY() + circ.getRadius(), 0);
				Vector3 c1 = camera.project(new Vector3(screenWorldCoords.x, screenWorldCoords.y, 0));

				screenWorldCoords = worldToScreenCoordinates(
						circ.getX() - circ.getRadius(), circ.getY() + circ.getRadius(), 0);
				Vector3 c2 = camera.project(new Vector3(screenWorldCoords.x, screenWorldCoords.y, 0));

				Vector3 c3 = new Vector3(c2.x * 2 - c1.x, c1.y, 0);

				Vector3 c4 = new Vector3(c2.x, c1.y * 2 - c2.y, 0); //c4 is c2 reflected on y

				//use 2 triangles to get diamond shape TODO remove square bounding box
				shapeRenderer.triangle(c1.x, c1.y, c2.x, c2.y, c3.x, c3.y);
				shapeRenderer.triangle(c1.x, c1.y, c4.x, c4.y, c3.x, c3.y);

				//render ellipse
				shapeRenderer.ellipse( c2.x - (c2.x - c3.x)/rt2, c1.y - (c1.y - c4.y)/rt2,
						rt2 * (c1.x - c2.x), rt2  * (c2.y - c1.y));

			}
		}

		//stop drawing
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	//use to render the collisionMasks of entities
	private void renderCollisionMasks(SpriteBatch batch) {


		TextureManager texMan = GameManager.get().getManager(TextureManager.class);

		Texture pntHighlight = texMan.getTexture("Point2D_highlight");
		Texture cirHighlight = texMan.getTexture("Circle2D_highlight");
		Texture boxHighlight = texMan.getTexture("Box2D_highlight");

		int tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get("tilewidth");
		int tileHeight = (int) GameManager.get().getWorld().getMap().getProperties().get("tileheight");

		batch.begin();
		for (AbstractEntity e : GameManager.get().getWorld().getEntities().values()) {

			CollisionMask shadow = e.getMask();

			Texture tex;
			if (shadow instanceof Box2D) {
				tex = boxHighlight;
			} else if (shadow instanceof Circle2D) {
				tex = cirHighlight;
			} else { //if (shadow instanceof Point2D) {
				tex = pntHighlight;
			}


			Vector2 isoPosition = worldToScreenCoordinates(e.getPosX(), e.getPosY(), 0);

			// We want to keep the aspect ratio of the image so...
			float aspect = (float) tex.getWidth() / (float) tileWidth;

			batch.draw(tex,
					// x, y
					isoPosition.x, isoPosition.y,
					// originX, originY
					tileWidth * e.getXRenderLength() / 2, tileHeight * e.getYRenderLength() / 2,
					// width, height
					tileWidth * e.getXRenderLength(), tex.getHeight() / aspect * e.getYRenderLength(),
					// scaleX, scaleY, rotation
					1, 1, 0 - e.rotationAngle(),
					// srcX, srcY
					0, 0,
					// srcWidth, srcHeight
					tex.getWidth(), tex.getHeight(),
					// flipX, flipY
					false, false);

		}
		batch.end();

	}

	//use to render the nodes in PathManager
	private void renderPathingNodes(SpriteBatch batch) {
		TextureManager texMan = GameManager.get().getManager(TextureManager.class);
		Texture pntHighlight = texMan.getTexture("Point2D_highlight");

		batch.begin();

		//for each node
		//batch.draw

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
	 * Transforms screen(gui) coordinates to tile coordinates. Reverses tileToScreen.
	 * 
	 * @param x
	 *            x coordinate in screen
	 * @param y
	 *            y coordinate in screen
	 * @return a Vector2 with tile coordinates
	 */
	public static Vector2 screenToTile(float x, float y) {
		Vector3 world = Render3D.screenToWorldCoordiates(x, y, 1);
		
		return Render3D.worldPosToTile(world.x, world.y);
	}

	/**
	 * Transforms tile coordinates to screen(gui) coordinates. Reverses screenToTile.
	 * 
	 * @param stage
	 *            stage that the screen is on
	 * @param x
	 *            x coordinate for tile
	 * @param y
	 *            y coordinate for tile
	 * @return a Vector3 for screen (gui) coordinates
	 */
	public static Vector2 tileToScreen(Stage stage, float x, float y) {
		OrthographicCamera camera = GameManager.get().getManager(CameraManager.class).getCamera();
		Vector2 screenWorldCoords = worldToScreenCoordinates(x, y, 0);
		Vector3 screenCoords = camera.project(new Vector3(screenWorldCoords.x, screenWorldCoords.y, 0));

		screenCoords.y = stage.getHeight() - screenCoords.y;

		return new Vector2(screenCoords.x, screenCoords.y);
	}

	/**
	 * Converts world coords to screen(gui) coordinates. Reverses ScreenToWorldCoordinates.
	 * 
	 * @param stage
	 *            stage that the screen is on
	 * @param x
	 *            x coord in world
	 * @param y
	 *            y coord in world
	 * @param z
	 *            z coord in world
	 * @return a Vector3 screen(gui) coordinate
	 */
	public static Vector3 worldToGuiScreenCoordinates(Stage stage, float x, float y, float z) {
		Vector3 screen = GameManager.get().getManager(CameraManager.class).getCamera()
				.project(new Vector3(x, y - Gdx.graphics.getHeight() + 1, z));
		screen.y = -screen.y;
		
		return screen;
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
	public static Vector2 worldToScreenCoordinates(float x, float y, float z) {
		int worldLength = GameManager.get().getWorld().getLength();
		int worldWidth = GameManager.get().getWorld().getWidth();

		int tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get("tilewidth");
		int tileHeight = (int) GameManager.get().getWorld().getMap().getProperties().get("tileheight");

		// X and Y offset for our isometric world (centered)
		float baseX = tileWidth * (worldWidth - 1) / 2f;
		float baseY = -tileHeight * (worldLength - 1) / 2f; // screen y is inverted

		float cartX = x;
		float cartY = worldWidth - 1 - y; // screen y is from the bottom corner

		float isoX = baseX + (cartX - cartY) / 2.0f * tileWidth;
		float isoY = baseY + (cartX + cartY) / 2.0f * tileHeight;

		// scaled to length of tile side
		float zScale = new Vector2(tileWidth, tileHeight).scl(0.5f).len();

		return new Vector2(isoX, isoY + z * zScale);
	}

	/**
	 * Transforms world coordinates to screen coordinates for rendering.
	 *
	 * @param p
	 *            Vector2 with the world coords
	 * @return a Vector2 with the screen coordinates
	 */
	public static Vector2 worldToScreenCoordinates(Vector3 p) {
		return worldToScreenCoordinates(p.x, p.y, p.z);
	}

	public static Vector3 screenToWorldCoordiates(float x, float y, float z) {
		return GameManager.get().getManager(CameraManager.class).getCamera().unproject(new Vector3(x, y, z));
	}

	/**
	 * Converts world to tile coordinates. Reverses tileToWorldPos.
	 * @param x x coord in world
	 * @param y y coord in world
	 * @return a Vector2 for tile coordinates
	 */
	public static Vector2 worldPosToTile(float x, float y) {
		float projX;
		float projY;

		float tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get("tilewidth");
		float tileHeight = (int) GameManager.get().getWorld().getMap().getProperties().get("tileheight");

		projX = x / tileWidth;
		projY = -(y - tileHeight / 2f) / tileHeight + projX;
		projX -= projY - projX;
		
		return new Vector2(projX, projY);
	}

	/**
	 * Converts tile to world coordinates. Reverses worldPostoTile
	 * @param x x coord in tile
	 * @param y y coord in tile
	 * @return a Vector2 of world coordinates
	 */
	public static Vector2 tileToWorldPos(float x, float y) {
		float projX = x, projY = y;

		float tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get("tilewidth");
		float tileHeight = (int) GameManager.get().getWorld().getMap().getProperties().get("tileheight");


		projX = (projY + projX) / 2;
		y = (projY - projX) * -tileHeight + tileHeight / 2f;
		x = projX * tileWidth;

		return new Vector2(x, y);

	}
}
