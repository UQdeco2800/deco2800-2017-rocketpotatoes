package com.deco2800.potatoes.renderering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.potatoes.collisions.*;
import com.deco2800.potatoes.entities.*;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.entities.enemies.EnemyEntity;
import com.deco2800.potatoes.entities.animation.Animated;
import com.deco2800.potatoes.entities.health.HasProgress;
import com.deco2800.potatoes.entities.health.HasProgressBar;
import com.deco2800.potatoes.entities.health.ProgressBar;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.entities.portals.BasePortal;
import com.deco2800.potatoes.entities.projectiles.Projectile;
import com.deco2800.potatoes.entities.trees.ResourceTree;
import com.deco2800.potatoes.gui.DebugModeGui;
import com.deco2800.potatoes.gui.TreeShopGui;
import com.deco2800.potatoes.managers.*;
import com.deco2800.potatoes.worlds.World;
import com.deco2800.potatoes.worlds.terrain.Terrain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
	private SpriteBatch hudBatch = new SpriteBatch();;
	private SpriteCache cache;
	private SpriteCacheBatch spriteCacheBatch;
	private BatchTiledMapRenderer tiledMap;
	private SortedMap<AbstractEntity, Integer> rendEntities;

	private int tileWidth;
	private int tileHeight;
	private static final String TILE_WIDTH =  "tilewidth";
	private static final String TILE_HEIGHT =  "tileheight";

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

		// Created here because constructor is run in tests
		if (shapeRenderer == null) {
			shapeRenderer = new ShapeRenderer();
		}
		if (cache == null) {
			cache = new SpriteCache(8191, true);
		}
		if (spriteCacheBatch == null) {
			spriteCacheBatch = new SpriteCacheBatch();
		}

		this.batch = batch;
		
		World world = GameManager.get().getWorld();
		this.tileWidth = (int) world.getMap().getProperties().get(TILE_WIDTH);
		this.tileHeight = (int) world.getMap().getProperties().get(TILE_HEIGHT);

		//get entities sorted back to front, for drawing order
		getRenderedEntitiesSorted();

		//get shading colour for day night cycle
		Color shading = GameManager.get().getManager(GameTimeManager.class).getColour();


		batch.setColor(shading);		// 		set world shading
		renderMap();					// 		rend tiles
		renderCursor();					//		highlighted cursor, communicates with treeShopGui
		renderShadows();				// 		CollisionMasks of entities as shadows
		renderEntities();				// 		entities normal
		renderProjectiles();			// 		entities projectile
		renderEffects();				// 		effect entities

		GameManager.get().getManager(ParticleManager.class).draw(batch);	//rend particles

		// text & displays
		batch.setColor(Color.WHITE);	// clear shading
		renderTreeResources();			// rend tree resource count
		renderProgressBars();			// 		progress bars

		// tree shop radial menu
		GameManager.get().getManager(GuiManager.class).getGui(TreeShopGui.class).render();

		//if DebugGui is shown ...
		if (!GameManager.get().getManager(GuiManager.class).getGui(DebugModeGui.class).isHidden()) {
			renderCollisionMasks(); 	// rend collisionMask outlines of entities
			renderPathFinderNodes();	// rend nodes in PathManager
		}
	}

	/**
	 * @return a list of entities sorted in render order (back to front)
	 */
	private void getRenderedEntitiesSorted() {
		Map<Integer, AbstractEntity> renderables = GameManager.get().getWorld().getEntities();

		/* Tree map so we sort our entities properly */
		rendEntities = new TreeMap<>(new Comparator<AbstractEntity>() {
			@Override
			public int compare(AbstractEntity abstractEntity, AbstractEntity t1) {
				int val = abstractEntity.compareTo(t1);
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
		BatchTiledMapRenderer tileRenderer = getTileRenderer(batch);

		batch.begin();
		// within the screen, but down rounded to the nearest tile
		Vector2 waterCoords = new Vector2(
				tileWidth * ((float) Math.floor(tileRenderer.getViewBounds().x / tileWidth - 1) - 0.5f *
						WorldManager.WORLD_SIZE),tileHeight * ((float) Math.floor(tileRenderer.getViewBounds().y /
				tileHeight - 1) - WorldManager.WORLD_SIZE));
		// draw with screen corner and width a little bit more than the screen
		TextureRegionDrawable background = GameManager.get().getWorld().getBackgroundArray()[Math.round(100 *
				GameManager.get()
				.getManager(GameTimeManager.class).getCurrentTime()) % GameManager.get().getWorld().getBackgroundArray
				().length];
		batch.draw(background.getRegion(), waterCoords.x, waterCoords.y);
		batch.draw(background.getRegion(), waterCoords.x - tileWidth / 2, waterCoords.y - tileHeight / 2);
		batch.end();

		tileRenderer.setMap(GameManager.get().getWorld().getMap());

		tileRenderer.setView(GameManager.get().getManager(CameraManager.class).getCamera());
		tileRenderer.render();
	}

	/**
	 * Renders the cursor highlight the the treeShop radial menu */
	private void renderCursor() {
		World world = GameManager.get().getWorld();
		TreeShopGui treeShopGui = GameManager.get().getManager(GuiManager.class).getGui(TreeShopGui.class);

		//convert screen coords to game coords
		Vector3 coords = Render3D.screenToWorldCoordiates(GameManager.get().getManager(InputManager.class).getMouseX(),
				GameManager.get().getManager(InputManager.class).getMouseY());
		Vector2 tileCoords = Render3D.worldPosToTile(coords.x, coords.y);

		float tileX = (int) tileCoords.x;
		float tileY = (int) tileCoords.y;

		//find terrain at tile
		Terrain terrain = world.getTerrain((int)tileX, (int)tileY);
		String terrainText = terrain.getTexture();

		//send distance to treeShopGui
		float distance = GameManager.get().getManager(PlayerManager.class).distanceFromPlayer(tileX,tileY);
		treeShopGui.setPlantable(distance < treeShopGui.getMaxRange()
				&& terrain.isPlantable() && !"void".equals(terrainText));

		//if on the map
		if (!("void".equals(terrainText) || "water_tile_1".equals(terrainText))) {

			//make box using game coords
			Box2D cursor = new Box2D(tileX+ 1 , tileY, 1, 1);

			// start drawing
			Gdx.gl.glEnable(GL20.GL_BLEND);

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


		batch.begin();
		for (Map.Entry<AbstractEntity, Integer> entity : rendEntities.entrySet()) {
			AbstractEntity e = entity.getKey();

			// skip projectiles & effects
			if (e instanceof Projectile || e instanceof Effect)
				continue;

			// get texture
			Texture tex = texMan.getTexture(e.getTexture());

			Vector2 isoPosition = worldToScreenCoordinates(e.getPosX(), e.getPosY(), e.getPosZ());

			// We want to keep the aspect ratio of the image so...
			float aspect = (float) tex.getWidth() / (float) tileWidth;

			float offsetX;
			float offsetY;
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

		batch.begin();
		for (Map.Entry<AbstractEntity, Integer> entity : rendEntities.entrySet()) {
			AbstractEntity e = entity.getKey();

			// skip projectiles & effects
			if (!(e instanceof Projectile)&&!(e instanceof Effect))
				continue;

			// get texture
			Texture tex;
			if (e instanceof Animated) {
				tex = texMan.getTexture(((Animated) e).getAnimation().getFrame());
			} else {
				tex = texMan.getTexture(e.getTexture());
			}

			Vector2 isoPosition = worldToScreenCoordinates(e.getPosX(), e.getPosY(), e.getPosZ());

			// We want to keep the aspect ratio of the image so...
			float aspect = (float) tex.getWidth() / (float) tileWidth;

			float offsetX;
			float offsetY;
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
		ProgressBarManager progressValues = GameManager.get().getManager(ProgressBarManager.class);
		TextureManager reg = GameManager.get().getManager(TextureManager.class);

		Color currentShade = batch.getColor();
		
		Player player = GameManager.get().getManager(PlayerManager.class).getPlayer();
		BasePortal portal = null;
		
		batch.begin();
		
		for (Map.Entry<AbstractEntity, Integer> entity : rendEntities.entrySet()) {
			AbstractEntity e = entity.getKey();

			// Progress Bars for players.
			if (e.equals(GameManager.get().getManager(PlayerManager.class).getPlayer())) {
				continue;
			}
			
			// Progress Bar for Goal Potato.
			if (!progressValues.showPotatoProgress() && e instanceof BasePortal) {
				continue;
			}

			// Progress Bars for allies [Trees, Portals].
			if (!progressValues.showAlliesProgress() && !(e instanceof EnemyEntity)
					&& !e.equals(GameManager.get().getManager(PlayerManager.class).getPlayer()) 
							&& !(e instanceof BasePortal)) {
				continue;
			}
			// Progress Bars for enemy entities.
			if (!progressValues.showEnemiesProgress() && (e instanceof EnemyEntity)) {
				continue;
			}

			if (e instanceof BasePortal) {
				portal = (BasePortal) e;
			}
			
			if (e instanceof HasProgressBar && ((HasProgress) e).showProgress()) {

				Vector2 isoPosition = worldToScreenCoordinates(e.getPosX(), e.getPosY(), e.getPosZ());

				ProgressBar progressBar = ((HasProgressBar) e).getProgressBar();
	
				// Allow entities to return null if they don't want to display their progress bar
				if (progressBar != null) {

					Texture barTexture = reg.getTexture(progressBar.getTexture());
					
					// sets colour palette
					batch.setColor(progressBar.getColour(((HasProgress) e).getProgressRatio()));

					// draws the progress bar
					Texture entityTexture = reg.getTexture(e.getTexture());
					float aspect = (float) entityTexture.getWidth() / (float) tileWidth;

					float barRatio = ((HasProgress) e).getProgressRatio();
					float maxBarWidth = tileWidth * e.getXRenderLength() * progressBar.getWidthScale();
					float barWidth = maxBarWidth * barRatio;
					float barBackgroundWidth = maxBarWidth * (1 - barRatio);

					// x co-ordinate,
					// finds the overlap length of the bar and moves it half as much left
					float barX = isoPosition.x
							- tileWidth * e.getXRenderLength() * (progressBar.getWidthScale()) / 2;
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


				}
			}

		}
		batch.end();
		hudBatch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		hudBatch.begin();
		// Draw player health HUD and progress bar.
		if (player != null && progressValues.showPlayerProgress()) {
			// Get texture
			ProgressBar progressBar = player.getProgressBar();
			Texture iconTexture = reg.getTexture(progressBar.getLayoutTexture());
			Texture barTexture =  reg.getTexture(progressBar.getTexture());
			// Render the player health HUD
			hudBatch.setColor(Color.WHITE);
			hudBatch.draw(iconTexture, 25+75, Gdx.graphics.getHeight()-134/2-75 + 60, 638/2, 134/2, 0, 0, iconTexture.getWidth(), iconTexture.getHeight(), false, false);
			
			// Draw the player HealthBar
			float barRatio = player.getProgressRatio();
			float maxBarWidth = 638/2.55f;
			float barWidth = maxBarWidth * barRatio;
			float barBackgroundWidth = maxBarWidth * (1 - barRatio);
			float barX = 93+75;
			float barY = Gdx.graphics.getHeight()-134/2-73.5f + 60;
			float endX = barX + barWidth;
			// Draw amount of health left.
			hudBatch.draw(barTexture, barX, barY,                        // texture, x, y
					barWidth, maxBarWidth / 8, 0, 0,                // width, height srcX, srcY
					(int) (barTexture.getWidth() * barRatio),        // srcWidth
					barTexture.getHeight(),                            // srcHeight
					false, false);                                    // flipX, flipY
			// Draw amount of health lost.
			hudBatch.setColor(0.5f, 0.5f, 0.5f, 1f);
			hudBatch.draw(barTexture, endX, barY,                            // texture, x, y
					barBackgroundWidth, maxBarWidth / 8,                // width, height
					(int) (barTexture.getWidth() * barRatio), 0,        // srcX, srcY
					(int) (barTexture.getWidth() * (1 - barRatio)),        // srcWidth
					barTexture.getHeight(),                                // srcHeight
					false, false);  
		}
		// portal
		if (portal != null && progressValues.showPotatoProgress()) {
			// Get texture
			ProgressBar progressBar = portal.getProgressBar();
			Texture iconTexture = reg.getTexture(progressBar.getLayoutTexture());
			Texture barTexture =  reg.getTexture(progressBar.getTexture());
			// Render the player health HUD
			hudBatch.setColor(Color.WHITE);
			hudBatch.draw(iconTexture, Gdx.graphics.getWidth() - (25+75)-75 - 638/2.55f, 
					Gdx.graphics.getHeight()-134/2-75 + 60, 638/2, 134/2, 0, 0, iconTexture.getWidth(),
					iconTexture.getHeight(), false, false);
			
			// Draw the player HealthBar
			float barRatio = portal.getProgressRatio();
			float maxBarWidth = 638/2.55f;
			float barWidth = maxBarWidth * barRatio;
			float barBackgroundWidth = maxBarWidth * (1 - barRatio);
			float barX = Gdx.graphics.getWidth() - (93+80) - 638/2.55f;
			float barY = Gdx.graphics.getHeight()-134/2-73.5f + 60;
			float endX = barX + barWidth;
			// Draw amount of health left.
			hudBatch.draw(barTexture, barX, barY,                        // texture, x, y
					barWidth, maxBarWidth / 8, 0, 0,                // width, height srcX, srcY
					(int) (barTexture.getWidth() * barRatio),        // srcWidth
					barTexture.getHeight(),                            // srcHeight
					false, false);                                    // flipX, flipY
			// Draw amount of health lost.
			hudBatch.setColor(0.5f, 0.5f, 0.5f, 1f);
			hudBatch.draw(barTexture, endX, barY,                            // texture, x, y
					barBackgroundWidth, maxBarWidth / 8,                // width, height
					(int) (barTexture.getWidth() * barRatio), 0,        // srcX, srcY
					(int) (barTexture.getWidth() * (1 - barRatio)),        // srcWidth
					barTexture.getHeight(),                                // srcHeight
					false, false);  
		}
		hudBatch.end();
		batch.setColor(currentShade);
	}

	/**
	 * Renders tree resource count */
	private void renderTreeResources(){

		//initialise font
		if (font == null) {
			font = new BitmapFont();
		}
		font.getData().setScale(3.0f);
		font.setColor(Color.GREEN);


		batch.begin();
		for (Map.Entry<AbstractEntity, Integer> entity : rendEntities.entrySet()) {
			AbstractEntity e = entity.getKey();

			if (e instanceof ResourceTree && ((ResourceTree) e).getGatherCount() > 0) {

				Vector2 isoPosition = worldToScreenCoordinates(e.getPosX(), e.getPosY(), e.getPosZ());

				font.draw(batch, String.format("%s", ((ResourceTree) e).getGatherCount()),
						isoPosition.x, isoPosition.y + 100);
			}
		}
		batch.end();
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

		//start drawing & set fill transparent grey
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(new Color(0, 0, 0, 0.3f));

		//Loop through entities
		for (Map.Entry<AbstractEntity, Integer> entity : rendEntities.entrySet()) {

			AbstractEntity e = entity.getKey();

			if (e.hasShadow()) {
				Shape2D shadow = e.getShadow();
				shadow.renderShape(shapeRenderer);
			}
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
		if (tiledMap == null) {
			tiledMap = new IsometricTiledMapRenderer(GameManager.get().getWorld().getMap(), 1, batch);
		}
		return tiledMap;
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
		Vector3 world = Render3D.screenToWorldCoordiates(x, y);

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

		int tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get(TILE_WIDTH);
		int tileHeight = (int) GameManager.get().getWorld().getMap().getProperties().get(TILE_HEIGHT);

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

	/**
	 * Transforms screen coordinates to world coordinates
	 *
	 * @param x		The x coordinate on the screen
	 * @param y		The y coordinate on the screen
	 * @return		A Vector3 with the corresponding x and y world coordinates
	 */
	public static Vector3 screenToWorldCoordiates(float x, float y) {
		return GameManager.get().getManager(CameraManager.class).getCamera().unproject(new Vector3(x, y, 0));
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

		float tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get(TILE_WIDTH);
		float tileHeight = (int) GameManager.get().getWorld().getMap().getProperties().get(TILE_HEIGHT);

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
		float projX = x;
		float projY = y;

		float tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get(TILE_WIDTH);
		float tileHeight = (int) GameManager.get().getWorld().getMap().getProperties().get(TILE_HEIGHT);


		projX = (projY + projX) / 2;
		float worldY = (projY - projX) * -tileHeight + tileHeight / 2f;
		float worldX = projX * tileWidth;

		return new Vector2(worldX, worldY);

	}

	/**
	 * Hacky class for converting between a SpriteBatch and SpriteCache
	 */
	private class SpriteCacheBatch extends SpriteBatch {
		public List<Integer> cacheIds = new ArrayList<>();

		public SpriteCacheBatch() {
			// Do nothing
		}

		@Override
		public void begin() {
			cache.beginCache();
		}

		@Override
		public void end() {
			cacheIds.add(cache.endCache());
		}

		@Override
		public void setColor(Color tint) {
			cache.setColor(tint);
		}

		@Override
		public void setColor(float r, float g, float b, float a) {
			cache.setColor(r, g, b, a);
		}

		@Override
		public void setColor(float color) {
			cache.setColor(color);
		}

		@Override
		public Color getColor() {
			return cache.getColor();
		}

		@Override
		public void draw(Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {
			cache.add(texture, x, y, originX, originY, width, height, scaleX, scaleY, rotation, srcX, srcY, srcWidth,
					srcHeight, flipX, flipY);
		}

		@Override
		public void draw(Texture texture, float x, float y, float width, float height, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {
			cache.add(texture, x, y, width, height, srcX, srcY, srcWidth, srcHeight, flipX, flipY);
		}

		@Override
		public void draw(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight) {
			cache.add(texture, x, y, srcX, srcY, srcWidth, srcHeight);
		}

		@Override
		public void draw(Texture texture, float x, float y, float width, float height, float u, float v, float u2, float v2) {
			cache.add(new TextureRegion(texture, u, v, u2, v2), x, y, width, height);
		}

		@Override
		public void draw(Texture texture, float x, float y) {
			cache.add(texture, x, y);
		}

		@Override
		public void draw(Texture texture, float x, float y, float width, float height) {
			cache.add(new TextureRegion(texture), x, y, width, height);
		}

		@Override
		public void draw(Texture texture, float[] spriteVertices, int offset, int count) {
			cache.add(texture, spriteVertices, offset, count);
		}

		@Override
		public void draw(TextureRegion region, float x, float y) {
			cache.add(region, x, y);
		}

		@Override
		public void draw(TextureRegion region, float x, float y, float width, float height) {
			cache.add(region, x, y, width, height);
		}

		@Override
		public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {
			cache.add(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
		}

		@Override
		public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, boolean clockwise) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void draw(TextureRegion region, float width, float height, Affine2 transform) {
			throw new UnsupportedOperationException();
		}
	}
}
