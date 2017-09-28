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
import com.deco2800.potatoes.entities.projectiles.Projectile;
import com.deco2800.potatoes.entities.trees.ResourceTree;
import com.deco2800.potatoes.gui.DebugModeGui;
import com.deco2800.potatoes.gui.TreeShopGui;
import com.deco2800.potatoes.managers.*;
import com.deco2800.potatoes.worlds.World;
import com.deco2800.potatoes.worlds.terrain.Terrain;
import jdk.nashorn.internal.runtime.Debug;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A simple isometric renderer for DECO2800 games
 *
 * @Author Tim Hadwen, Dion Lao, Tazman Schmidt
 */
public class Render3D implements Renderer {

	BitmapFont font;

	private static final Logger LOGGER = LoggerFactory.getLogger(Render3D.class);

	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch;
	private SortedMap<AbstractEntity, Integer> rendEntities;


	/**
	 * Renders onto a batch, given a renderables with entities It is expected that
	 * World contains some entities and a Map to read tiles from
	 *
	 * @param batch
	 *            Batch to render onto
	 */
	@Override
	public void render(SpriteBatch batch) {
		//IMPORTANT: each subroutine opens and closes the batch itself

		// Created here because constructor is run in tests, TODO should be moved to constructor though
		if (shapeRenderer == null) {
			shapeRenderer = new ShapeRenderer();
		}

		this.batch = batch;

		//get entities sorted back to front, for drawing order //TODO only rend entities on screen or close to edges
		getRenderedEntitiesSorted();

		//get shading colour for day night cycle
		Color shading = GameManager.get().getManager(GameTimeManager.class).getColour();


		batch.setColor(shading);		// set world shading
		renderMap();					// rend tiles TODO render is offset
		renderCursor();					//		highlighted cursor
		renderShadows();				// 		CollisionMasks of entities as shadows
		renderEntities();				// 		entities normal
		renderProjectiles();			// 		entities projectile
		renderEffects();				// 		effect entities

		GameManager.get().getManager(ParticleManager.class).draw(batch);	//rend particles

		// text & displays
		batch.setColor(Color.WHITE);	// clear shading
		renderTreeResources();			// rend tree resource count
		renderMultiplayerName();		// 		mutiplayer names
		renderProgressBars();			// 		progress bars

		// tree shop radial menu
		GameManager.get().getManager(GuiManager.class).getGui(TreeShopGui.class).render();
		// TODO does this render for other players ???
		// TODO planting does not match up with mouse cursor, depending on part of the tile clicked

		//if DebugGui is shown ...
		if (!GameManager.get().getManager(GuiManager.class).getGui(DebugModeGui.class).isHidden()) {
			renderCollisionMasks(); 	// rend collisionMask outlines of entities
			renderPathFinderNodes();	// rend nodes in PathManager TODO
		}
	}

	/**
	 * @return a list of entities sorted in render order (back to front)
	 */
	private void getRenderedEntitiesSorted() {
		//TODO only rend entities on screen or close to edges
		Map<Integer, AbstractEntity> renderables = GameManager.get().getWorld().getEntities();

		/* Tree map so we sort our entities properly */
		rendEntities = new TreeMap<>(new Comparator<AbstractEntity>() {
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
			rendEntities.put(e.getValue(), e.getKey());
		}
	}

	/**
	 * Renders the tiles of the Map	*/
	private void renderMap() {
		TextureManager textureManager = GameManager.get().getManager(TextureManager.class);
		World world = GameManager.get().getWorld();

		int tileWidth = (int) world.getMap().getProperties().get("tilewidth");
		int tileHeight = (int) world.getMap().getProperties().get("tileheight");

		/* Render the tiles first */
		batch.begin();
		BatchTiledMapRenderer tileRenderer = getTileRenderer(batch);
		tileRenderer.setView(GameManager.get().getManager(CameraManager.class).getCamera());

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
	}

	/**
	 * Renders the cursor highlight the the treeShop radial menu */
	private void renderCursor() {
		World world = GameManager.get().getWorld();
		TreeShopGui treeShopGui = GameManager.get().getManager(GuiManager.class).getGui(TreeShopGui.class);

		//convert screen coords to game coords
		Vector3 coords = Render3D.screenToWorldCoordiates(GameManager.get().getManager(InputManager.class).getMouseX(),
				GameManager.get().getManager(InputManager.class).getMouseY(), 0);
		Vector2 tileCoords = Render3D.worldPosToTile(coords.x, coords.y);

		float tileX = Math.round(tileCoords.x);
		float tileY = Math.round(tileCoords.y) - 1;

		//find terrain at tile
		Terrain terrain = world.getTerrain((int)tileX, (int)tileY);
		String terrainText = terrain.getTexture();

		//send distance to treeShopGui
		float distance = GameManager.get().getManager(PlayerManager.class).distanceFromPlayer(tileX,tileY);
		treeShopGui.setPlantable(distance < treeShopGui.getMaxRange()
				&& terrain.isPlantable() && !terrainText.equals("void"));

		//if on the map
		if (!(terrainText.equals("void") || terrainText.equals("water_tile_1"))) {

			//make box using game coords
			Box2D cursor = new Box2D(tileX + 0.5f, tileY + 0.5f, 1, 1);

			// start drawing
			Gdx.gl.glEnable(GL20.GL_BLEND);
			ShapeRenderer shapeRenderer = new ShapeRenderer();

			//pick colour based on treeShop's discretion
			Color colour;
			if (treeShopGui.getPlantable()) {
				colour = new Color(0, 1, 0, 0.3f); //green
			} else {
				colour = new Color(1, 0, 0, 0.3f); //red
			}

			//draw fill
			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			shapeRenderer.setColor(colour);
			cursor.renderShape(shapeRenderer);
			shapeRenderer.end();

			//draw outline
			shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			shapeRenderer.setColor(new Color(0.5f, 0.5f, 0.5f, 0.5f));
			cursor.renderShapeOutline(shapeRenderer);
			shapeRenderer.end();

			//stop drawing
			Gdx.gl.glDisable(GL20.GL_BLEND);
		}
	}

	/**
	 * Renders non-Projectile, non-Effect entities.
	 * Does not consider rotate images */
	private void renderEntities() {
		TextureManager texMan = GameManager.get().getManager(TextureManager.class);

		int tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get("tilewidth");

		batch.begin();
		for (Map.Entry<AbstractEntity, Integer> entity : rendEntities.entrySet()) {
			AbstractEntity e = entity.getKey();

			// skip projectiles & effects
			if (e instanceof Projectile || e instanceof Effect) continue;

			// get texture
			Texture tex;
			if (e instanceof Animated) {
				// TODO Animations should probably be changed to TextureRegion for performance
				tex = texMan.getTexture(((Animated) e).getAnimation().getFrame());
			} else {
				tex = texMan.getTexture(e.getTexture());
			}

			Vector2 isoPosition = worldToScreenCoordinates(e.getPosX(), e.getPosY(), e.getPosZ());

			// We want to keep the aspect ratio of the image so...
			float aspect = (float) tex.getWidth() / (float) tileWidth;

			float offsetX, offsetY;
			offsetX = tileWidth * e.getXRenderLength() / 2 - aspect * e.getXRenderOffset();
			offsetY = tileWidth * e.getXRenderLength() / 4 - aspect * e.getYRenderOffset();

			batch.draw(tex,
					isoPosition.x - offsetX, isoPosition.y - offsetY,		// x, y
					tileWidth * e.getXRenderLength(), 						// width
					tex.getHeight() / aspect * e.getYRenderLength());		// height
		}
		batch.end();
	}

	/**
	 * Renders Projectile entities */
	private void renderProjectiles() {
		TextureManager texMan = GameManager.get().getManager(TextureManager.class);

		int tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get("tilewidth");

		batch.begin();
		for (Map.Entry<AbstractEntity, Integer> entity : rendEntities.entrySet()) {
			AbstractEntity e = entity.getKey();

			// skip projectiles & effects
			if (!(e instanceof Projectile)) continue;

			// get texture
			Texture tex;
			if (e instanceof Animated) {
				// TODO Animations should probably be changed to TextureRegion for performance
				tex = texMan.getTexture(((Animated) e).getAnimation().getFrame());
			} else {
				tex = texMan.getTexture(e.getTexture());
			}

			Vector2 isoPosition = worldToScreenCoordinates(e.getPosX(), e.getPosY(), e.getPosZ());

			// We want to keep the aspect ratio of the image so...
			float aspect = (float) tex.getWidth() / (float) tileWidth;

			float offsetX, offsetY;
			offsetX = tileWidth * e.getXRenderLength() / 2 - aspect * e.getXRenderOffset();
			offsetY = tileWidth * e.getXRenderLength() / 2 - aspect * e.getYRenderOffset();


			batch.draw(tex,
					isoPosition.x - offsetX, isoPosition.y - offsetY,		// x, y
					offsetX, offsetY, 										// originX, originY
					tileWidth * e.getXRenderLength(), 						// width
					tex.getHeight() / aspect * e.getYRenderLength(),		// height
					1, 1, - e.rotationAngle(), 0, 0,						// scaleX, scaleY, rotation,  srcX, srcY
					tex.getWidth(), tex.getHeight(), false, false);			// srcWidth, srcHeight, flipX, flipY

		}
		batch.end();
	}

	/**
	 * Renders progress bars above entities */
	private void renderProgressBars() {
		int tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get("tilewidth");

		Color currentShade = batch.getColor();

		batch.begin();
		for (Map.Entry<AbstractEntity, Integer> entity : rendEntities.entrySet()) {
			AbstractEntity e = entity.getKey();

			if (e instanceof HasProgressBar && ((HasProgress) e).showProgress()) {

				Vector2 isoPosition = worldToScreenCoordinates(e.getPosX(), e.getPosY(), e.getPosZ());

				ProgressBar PROGRESS_BAR = ((HasProgressBar) e).getProgressBar();
				// Allow entities to return null if they don't want to display their progress bar
				if (PROGRESS_BAR != null) {
					TextureManager reg = GameManager.get().getManager(TextureManager.class);

					Texture barTexture = reg.getTexture(PROGRESS_BAR.getTexture());

					// sets colour palette
					batch.setColor(PROGRESS_BAR.getColour(((HasProgress) e).getProgressRatio()));

					// draws the progress bar
					Texture entityTexture = reg.getTexture(e.getTexture());
					float aspect = (float) entityTexture.getWidth() / (float) tileWidth;

					float barRatio = ((HasProgress) e).getProgressRatio();
					float maxBarWidth = tileWidth * e.getXRenderLength() * PROGRESS_BAR.getWidthScale();
					float barWidth = maxBarWidth * barRatio;
					float barBackgroundWidth = maxBarWidth * (1 - barRatio);

					// x co-ordinate,
					// finds the overlap length of the bar and moves it half as much left
					float barX = isoPosition.x
							- tileWidth * e.getXRenderLength() * (PROGRESS_BAR.getWidthScale() - 1) / 2;
					// y co-ordinate
					// If height is specified, use it, otherwise estimate the right height
					float barY = isoPosition.y + entityTexture.getHeight() / aspect * e.getYRenderLength();
					float endX = barX + barWidth;
					// We haven't implemented rounded corners, but when we do:
					// float greyBarX = endX + endWidth;

					// draw half of bar that represents current health
					batch.draw(barTexture, barX, barY,                        // texture, x, y
							barWidth, maxBarWidth / 8, 0, 0,                // width, height srcX, srcY
							(int) (barTexture.getWidth() * barRatio),        // srcWidth
							barTexture.getHeight(),                            // srcHeight
							false, false);                                    // flipX, flipY

					// draw shadow half of bar that represents health lost
					batch.setColor(0.5f, 0.5f, 0.5f, 1f);
					batch.draw(barTexture, endX, barY,                            // texture, x, y
							barBackgroundWidth, maxBarWidth / 8,                // width, height
							(int) (barTexture.getWidth() * barRatio), 0,        // srcX, srcY
							(int) (barTexture.getWidth() * (1 - barRatio)),        // srcWidth
							barTexture.getHeight(),                                // srcHeight
							false, false);                                        // flipX, flipY

					// reset the batch colour
					batch.setColor(Color.WHITE);
				}
			}
		}
		batch.end();

		batch.setColor(currentShade);
	}

	/**
	 * Renders tree resource count */
	private void renderTreeResources(){

		//initialise font
		if (font == null) {
			font = new BitmapFont();
		}
		font.getData().setScale(1.0f);
		font.setColor(Color.GREEN);

		int tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get("tilewidth");

		batch.begin();
		for (Map.Entry<AbstractEntity, Integer> entity : rendEntities.entrySet()) {
			AbstractEntity e = entity.getKey();

			if (e instanceof ResourceTree && ((ResourceTree) e).getGatherCount() > 0) {

				Vector2 isoPosition = worldToScreenCoordinates(e.getPosX(), e.getPosY(), e.getPosZ());

				font.draw(batch, String.format("%s", ((ResourceTree) e).getGatherCount()),
						isoPosition.x + tileWidth / 2 - 7, isoPosition.y + 65);
			}
		}
		batch.end();
	}

	/**
	 * Renders names of players if multiplayer is on */
	private void renderMultiplayerName() {
		//draw multiplayer names
		MultiplayerManager m = GameManager.get().getManager(MultiplayerManager.class);

		if (!m.isMultiplayer()) return;

		font.getData().setScale(1.3f);

		int tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get("tilewidth");

		batch.begin();
		for (Map.Entry<AbstractEntity, Integer> entity : rendEntities.entrySet()) {
			AbstractEntity e = entity.getKey();

			if (e instanceof Player) {

				//font colour
				if (m.getID() == entity.getValue()) {
					font.setColor(Color.BLUE);
				} else {
					font.setColor(Color.WHITE);
				}

				Vector2 isoPosition = worldToScreenCoordinates(e.getPosX(), e.getPosY(), e.getPosZ());

				font.draw(batch, String.format("%s", "<name>"),  //TODO player name: m.getClients().get(e.getValue())
						isoPosition.x + tileWidth / 2 - 10, isoPosition.y + 70);
			}
		}

		return;
	}

	/**
	 * Renders 'Effect' rendEntities */
	private void renderEffects() {
		batch.begin();

		for (Map.Entry<AbstractEntity, Integer> entity : rendEntities.entrySet()) {
			AbstractEntity e = entity.getKey();

			if (e instanceof Effect) {
				((Effect) e).drawEffect(batch);
			}
		}
		batch.end();
	}

	/**
	 * Renders the CollisionMasks of entities as shadows */
	private void renderShadows() {
		//camera used for translation
		OrthographicCamera camera = GameManager.get().getManager(CameraManager.class).getCamera();

		//start drawing & set fill transparent grey
		Gdx.gl.glEnable(GL20.GL_BLEND);
		ShapeRenderer shapeRenderer = new ShapeRenderer();
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(new Color(0, 0, 0, 0.3f));

		//Loop through entities
		for (Map.Entry<AbstractEntity, Integer> entity : rendEntities.entrySet()) {

			CollisionMask shadow = entity.getKey().getShadow();

			if (shadow != null)
				shadow.renderShape(shapeRenderer);
		}

		//stop drawing
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	/**
	 * Renders the collisionMasks of entities */
	private void renderCollisionMasks() {

		batch.begin();
		for (AbstractEntity e : GameManager.get().getWorld().getEntities().values()) {
			e.getMask().renderHighlight(batch);
		}
		batch.end();

	}

	/**
	 * Renders the nodes in PathManager */
	private void renderPathFinderNodes() {
		PathManager pathMan = GameManager.get().getManager(PathManager.class);

		// TODO does PathManger store nodes as expected?
		/*
		batch.begin();
		for (Point2D node : pathMan.getNodes) {
			Point2D.render...()
		}
		batch.end();
		*/
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
