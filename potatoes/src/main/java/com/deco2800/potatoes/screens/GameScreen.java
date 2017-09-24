package com.deco2800.potatoes.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.deco2800.potatoes.RocketPotatoes;
import com.deco2800.potatoes.entities.*;
import com.deco2800.potatoes.entities.enemies.*;
import com.deco2800.potatoes.entities.health.HasProgress;
import com.deco2800.potatoes.entities.portals.BasePortal;
import com.deco2800.potatoes.entities.resources.FoodResource;
import com.deco2800.potatoes.entities.resources.*;
import com.deco2800.potatoes.entities.trees.AcornTreeType;
import com.deco2800.potatoes.entities.trees.DamageTree;
import com.deco2800.potatoes.entities.trees.FireTreeType;
import com.deco2800.potatoes.entities.trees.IceTreeType;
import com.deco2800.potatoes.entities.trees.ProjectileTree;
import com.deco2800.potatoes.gui.*;
import com.deco2800.potatoes.handlers.MouseHandler;
import com.deco2800.potatoes.managers.*;
import com.deco2800.potatoes.observers.KeyDownObserver;
import com.deco2800.potatoes.observers.ScrollObserver;
import com.deco2800.potatoes.renderering.Render3D;
import com.deco2800.potatoes.renderering.Renderable;
import com.deco2800.potatoes.renderering.Renderer;

import com.deco2800.potatoes.waves.EnemyWave;
import com.deco2800.potatoes.worlds.WorldType;
import com.deco2800.potatoes.worlds.terrain.Terrain;

import java.io.IOException;
import java.util.Map;

/**
 * Handles the creation of the world and rendering.
 *
 */
public class GameScreen implements Screen {

	// References to the 'game' object which handles our screens/
	private RocketPotatoes game;
	/**
	 * Set the renderer. 3D is for Isometric worlds 2D is for Side Scrolling worlds
	 * Check the documentation for each renderer to see how it handles WorldEntity
	 * coordinates
	 */
	private Renderer renderer = new Render3D();

	// Managers tracked here for ease of use. Should be initialized from the
	// GameManager.get().getManager(...) though!
	private SoundManager soundManager;
	private MouseHandler mouseHandler;
	private PlayerManager playerManager;
	private MultiplayerManager multiplayerManager;
	private GuiManager guiManager;
	private CameraManager cameraManager;
	private TextureManager textureManager;
	private InputManager inputManager;
	private WaveManager waveManager;

	private long lastGameTick = 0;
	private boolean playing = true;
	private double tickrate = 10;
	// Would be nice to move this somewhere else
	private TiledDrawable background;

	private int maxShopRange;
	/**
	 * Start's a multiplayer game
	 *
	 * @param game
	 *            game instance
	 * @param name
	 *            name to join with
	 * @param IP
	 *            IP to connect to, (ignored if isHost is true (will connect to
	 *            127.0.0.1))
	 * @param port
	 *            port to connect/host on
	 * @param isHost
	 *            is this client a host (i.e. start a server then connect to it)
	 */
	public GameScreen(RocketPotatoes game, String name, String IP, int port, boolean isHost)
			throws IllegalStateException, IllegalArgumentException, IOException {
		this.game = game;
		setupGame();
		
		// setup multiplayer
		if (isHost) {
			multiplayerManager.createHost(port);
			// Loopback for host's connection to itself
			multiplayerManager.joinGame(name, "127.0.0.1", port);
		} else {
			multiplayerManager.joinGame(name, IP, port);
		}

		initializeGame();
		
	}

	/**
	 * Start's a singleplayer game
	 *
	 * @param game
	 *            game instance
	 */
	public GameScreen(RocketPotatoes game) {
		this.game = game;
		setupGame();
		initializeGame();
	}

	/**
	 * Sets up everything needed for the game, but doesn't initialize any game
	 * specific things just yet
	 */
	private void setupGame() {
		/*
		 * Forces the GameManager to load the TextureManager, and load textures.
		 */
		textureManager = GameManager.get().getManager(TextureManager.class);
		
		// Creates the object for the repeated background texture
		// TODO this will need to be changed once proper texture is added
		TextureRegion waterTexture = textureManager.getTextureRegion("w1");
		waterTexture.setRegionX(waterTexture.getRegionX() + 2);
		waterTexture.setRegionY(waterTexture.getRegionY() + 2);
		background = new TiledDrawable(textureManager.getTextureRegion("w1"));
		/**
		 * Setup managers etc.
		 */

		/* Create a sound manager for the whole game */
		soundManager = GameManager.get().getManager(SoundManager.class);

		/* Create a mouse handler for the game */
		mouseHandler = new MouseHandler();

		/* Create a multiplayer manager for the game */
		multiplayerManager = GameManager.get().getManager(MultiplayerManager.class);

		/* Create a player manager. */
		playerManager = GameManager.get().getManager(PlayerManager.class);

        /* Create an enemy wave manager */
        waveManager = GameManager.get().getManager(WaveManager.class);

		/* Setup camera */
		cameraManager = GameManager.get().getManager(CameraManager.class);
		cameraManager.setCamera(new OrthographicCamera(1920, 1080));

		/**
		 * GuiManager, which contains all our Gui specific properties/logic. Creates our
		 * stage etc.
		 */

		guiManager = GameManager.get().getManager(GuiManager.class);
		guiManager.setStage(new Stage(new ScreenViewport()));

		// Deselect all gui elements if we click anywhere in the game world
		guiManager.getStage().getRoot().addCaptureListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (!(event.getTarget() instanceof TextField)) {
					guiManager.getStage().setKeyboardFocus(null);
				}
				return false;
			}
		});

		// Make our GameMenuGui
		guiManager.addGui(new GameMenuGui(guiManager.getStage(), this));

		// Make our PauseMenuGui
		guiManager.addGui(new PauseMenuGui(guiManager.getStage(), this));

		// Make our DebugMenuGui
		guiManager.addGui(new DebugModeGui(guiManager.getStage(), this));

		// Make our chat window
		guiManager.addGui(new ChatGui(guiManager.getStage()));

		// Add test TreeShop Gui
		guiManager.addGui(new TreeShopGui(guiManager.getStage()));
		maxShopRange = ((TreeShopGui)guiManager.getGui(TreeShopGui.class)).getMaxRange();

        // Make our chat window
        guiManager.addGui(new ChatGui(guiManager.getStage()));

        // Make our inventory window
        guiManager.addGui(new InventoryGui(guiManager.getStage()));
        
        // Add World Change Gui
        guiManager.addGui(new WorldChangeGui(guiManager.getStage(), this));

        //Make our game over window
		guiManager.addGui(new GameOverGui(guiManager.getStage(),this));

		guiManager.addGui(new WavesGui(guiManager.getStage()));

		guiManager.addGui(new RespawnGui(guiManager.getStage(),this));
        
		/* Setup inputs */
		setupInputHandling();

        // Sets the world to the initial world, forest world
        GameManager.get().getManager(WorldManager.class).setWorld(WorldType.FOREST_WORLD);

		/* Move camera to center */
		cameraManager.getCamera().position.x = GameManager.get().getWorld().getWidth() * 32;
		cameraManager.getCamera().position.y = 0;
	}

	private void setupInputHandling() {

		/**
		 * Setup inputs for the buttons and the game itself
		 */
		/*
		 * Setup an Input Multiplexer so that input can be handled by both the UI and
		 * the game
		 */
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(guiManager.getStage()); // Add the UI as a processor

		inputManager = GameManager.get().getManager(InputManager.class);
		inputManager.addKeyDownListener(new CameraHandler());
		inputManager.addKeyDownListener(new PauseHandler());
		inputManager.addScrollListener(new ScrollTester());

		//testing Game over screen
		inputManager.addKeyDownListener(new GameOverHandler());

		MouseHandler mouseHandler = new MouseHandler();
		inputManager.addTouchDownListener(mouseHandler);
		inputManager.addTouchDraggedListener(mouseHandler);
		inputManager.addMouseMovedListener(mouseHandler);
		inputMultiplexer.addProcessor(inputManager);

		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	/**
	 * Initializes everything needed to actually play the game Can be used to
	 * `reset` the state of the game
	 *
	 * TODO this logic should be state-machined'd (i.e. Main menu <-> Playing <->
	 * Paused. With every state having TODO it's own menu(s), initialization etc.
	 * And when we setup custom transition logic.
	 */
	private void initializeGame() {

		if (!multiplayerManager.isMultiplayer()) {
			guiManager.getGui(ChatGui.class).hide();
		}

		GameManager.get().getManager(EventManager.class).unregisterAll();


		MultiplayerManager m = multiplayerManager;
		if (m.isMaster() || !m.isMultiplayer()) {
			GameManager.get().getWorld().addEntity(new ProjectileTree(8, 8));
			GameManager.get().getWorld().addEntity(new GoalPotate(15, 10));

			//add an enemy gate to game world
			GameManager.get().getWorld().addEntity(new EnemyGate(24,24));

			//add enemy waves
			GameManager.get().getManager(WaveManager.class).addWave(new EnemyWave(1, 0, 0,0, 750));
			GameManager.get().getManager(WaveManager.class).addWave(new EnemyWave(0, 1, 0,0, 750));
			GameManager.get().getManager(WaveManager.class).addWave(new EnemyWave(1, 1, 1,1, 750));

			initialiseResources();
			initialisePortal();
			addDamageTree();

			if (!multiplayerManager.isMultiplayer()) {
			/*
			 * TODO bug! currently reseting the game while having a key held down will then
			 * notify the new player with the keyUp TODO event, which will result it in
			 * moving without pressing a key. This is something a bit difficult to fix as
			 * TODO so I'm just going to leave it for now since fixing it is a bit of a
			 * hassle
			 */

				// Make our player
				playerManager.setPlayer(5, 10);
				GameManager.get().getWorld().addEntity(playerManager.getPlayer());
			}
			GameManager.get().getManager(ParticleManager.class);
		}
	}

	private void addDamageTree() {
		GameManager.get().getWorld().addEntity(new DamageTree(16, 11));
		GameManager.get().getWorld().addEntity(new DamageTree(14, 11, new AcornTreeType()));
		GameManager.get().getWorld().addEntity(new DamageTree(15, 11, new IceTreeType()));
		GameManager.get().getWorld().addEntity(new DamageTree(13, 11, new FireTreeType()));
	}

	private void initialiseResources() {

		SeedResource seedResource = new SeedResource();
		FoodResource foodResource = new FoodResource();
		WoodResource woodResource = new WoodResource();
		TumbleweedResource tumbleweedResource = new TumbleweedResource();
		CactusThornResource cactusThornResource = new CactusThornResource();
		PricklyPearResource pricklyPearResource = new PricklyPearResource();

		SnowBallResource snowBallResource = new SnowBallResource();
		SealSkinResource sealSkinResource = new SealSkinResource();
		IceCrystalResource iceCrystalResource = new IceCrystalResource();

		CoalResource coalResource = new CoalResource();
		BonesResource bonesThornResource = new BonesResource();
		ObsidianResource obsidianResource = new ObsidianResource();

		FishMeatResource fishMeatResource = new FishMeatResource();
		PearlResource pearlResource = new PearlResource();
		TreasureResource treasureResource = new TreasureResource();



		GameManager.get().getWorld().addEntity(new ResourceEntity(10, 20, seedResource));
		GameManager.get().getWorld().addEntity(new ResourceEntity(10, 18, foodResource));
		GameManager.get().getWorld().addEntity(new ResourceEntity(10, 16, woodResource));

		GameManager.get().getWorld().addEntity(new ResourceEntity(9, 20, tumbleweedResource));
		GameManager.get().getWorld().addEntity(new ResourceEntity(9, 18, cactusThornResource));
		GameManager.get().getWorld().addEntity(new ResourceEntity(9, 16, pricklyPearResource));

		GameManager.get().getWorld().addEntity(new ResourceEntity(8, 20, snowBallResource));
		GameManager.get().getWorld().addEntity(new ResourceEntity(8, 18, sealSkinResource));
		GameManager.get().getWorld().addEntity(new ResourceEntity(8, 16, iceCrystalResource));


		GameManager.get().getWorld().addEntity(new ResourceEntity(7, 20, coalResource));
		GameManager.get().getWorld().addEntity(new ResourceEntity(7, 18, bonesThornResource));
		GameManager.get().getWorld().addEntity(new ResourceEntity(7, 16, obsidianResource));


		GameManager.get().getWorld().addEntity(new ResourceEntity(6, 20, fishMeatResource));
		GameManager.get().getWorld().addEntity(new ResourceEntity(6, 18, pearlResource));
		GameManager.get().getWorld().addEntity(new ResourceEntity(6, 16, treasureResource));




	}

	private void initialisePortal() {
		GameManager.get().getWorld().addEntity(new BasePortal(14, 17, 100));


	}

	private void tickGame(long timeDelta) {
		/*
		 * broken! window.removeActor(peonButton); boolean somethingSelected = false;
		 */

		// Tick our player
		if (multiplayerManager.isMultiplayer() && !multiplayerManager.isMaster()) {
			playerManager.getPlayer().onTick(timeDelta);
		}

		// Tick other stuff maybe
		for (Renderable e : GameManager.get().getWorld().getEntities().values()) {
			if (e instanceof Tickable &&(!multiplayerManager.isMultiplayer() || multiplayerManager.isMaster())) {
				((Tickable) e).onTick(timeDelta);

			}

			/*
			 * broken! if (e instanceof Selectable) { if (((Selectable) e).isSelected()) {
			 * peonButton = ((Selectable) e).getButton(); somethingSelected = true; } }
			 */

		}

		// Broadcast updates if we're master TODO only when needed.
		if (multiplayerManager.isMultiplayer() && multiplayerManager.isMaster()) {
			for (Map.Entry<Integer, AbstractEntity> e : GameManager.get().getWorld().getEntities().entrySet()) {
				// But don't broadcast our player yet
				if (e.getKey() != multiplayerManager.getID()) {
					multiplayerManager.broadcastEntityUpdatePosition(e.getKey());

					// TODO only when needed Maybe attach to the HasProgress interface itself?
					if (e.getValue() instanceof HasProgress) {
						multiplayerManager.broadcastEntityUpdateProgress(e.getKey());
					}
				}
			}
		}

		// Broadcast our player updating pos TODO only when needed.
		multiplayerManager.broadcastPlayerUpdatePosition();

		/*
		 * broken! if (!somethingSelected) { peonButton = uiPeonButton; }
		 * window.add(peonButton);
		 */

		// Tick CameraManager, maybe want to make managers tickable??
		cameraManager.centerOnTarget(timeDelta);
		// Ticks all tickable managers, currently events, waves, particles
		GameManager.get().onTick(timeDelta);
    }

	private void renderGUI(SpriteBatch batch) {

		// Update window title
		Gdx.graphics.setTitle(
				"DECO2800 " + this.getClass().getCanonicalName() + " - FPS: " + Gdx.graphics.getFramesPerSecond());

		// Render GUI elements
		guiManager.getStage().act();
		guiManager.getStage().draw();

	}

	private void renderGameGUI(SpriteBatch batch) {
		guiManager.getStage().act();
		guiManager.getStage().draw();
	}

	//Is it bad to be setting the status label text on every tick? Might want to think this through
	private void updateWaveGUI() {
		// Update WaveGui time
		int timeToWaveEnd;
		int timeToNextWave;
		int currentIndex = GameManager.get().getManager(WaveManager.class).getWaveIndex();
		int totalWaves = GameManager.get().getManager(WaveManager.class).getWaves().size();
		Gui waveGUI = guiManager.getGui(WavesGui.class);
		if (waveGUI instanceof WavesGui) {
			EnemyWave activeWave = GameManager.get().getManager(WaveManager.class).getActiveWave();
			((WavesGui) waveGUI).getWaveGuiWindow().getTitleLabel().setText("wave: " + (currentIndex+1) + "/" + totalWaves);
			if (activeWave != null) {
				timeToWaveEnd = activeWave.getTimeToEnd();
				((WavesGui) waveGUI).getWaveStatusLabel().setText("Time left in wave: ");
				((WavesGui) waveGUI).getWaveTimeLabel().setText("" + timeToWaveEnd/75);
			} else {
				if (GameManager.get().getManager(WaveManager.class).areWavesCompleted()) {
					((WavesGui) waveGUI).getWaveStatusLabel().setText("No more waves.");
					((WavesGui) waveGUI).getWaveTimeLabel().setText("");
				} else {
					timeToNextWave = GameManager.get().getManager(WaveManager.class).getTimeBeforeNextWave();
					((WavesGui) waveGUI).getWaveStatusLabel().setText("Time to next wave: ");
					((WavesGui) waveGUI).getWaveTimeLabel().setText("" + timeToNextWave / 75);
				}
			}
		}
	}

	//TODO: better implementation?
	private void updateRespawnGUI(){

		Gui respawnGui = guiManager.getGui(RespawnGui.class);

		if(playerManager.getPlayer().isDead()) {
			int count = ((RespawnGui) respawnGui).returnCount();
			int display =(int) Math.round(count/1000.0) + 1;
			((RespawnGui) respawnGui).returnTimer().setText(Integer.toString(display));
		}

	}

	private void renderGame(SpriteBatch batch) {
		int tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get("tilewidth");
		int tileHeight = (int) GameManager.get().getWorld().getMap().getProperties().get("tileheight");
		
		/* Render the tiles first */
		BatchTiledMapRenderer tileRenderer = renderer.getTileRenderer(batch);
		tileRenderer.setView(cameraManager.getCamera());

		batch.begin();
		// within the screen, but down rounded to the nearest tile
		Vector2 waterCoords = new Vector2(
				tileWidth * (float) Math.floor(tileRenderer.getViewBounds().x / tileWidth - 1),
				tileHeight * (float) Math.floor(tileRenderer.getViewBounds().y / tileHeight - 1));
		// draw with screen corner and width a little bit more than the screen
		background.draw(batch, waterCoords.x, waterCoords.y, tileRenderer.getViewBounds().width + tileWidth * 4,
				tileRenderer.getViewBounds().height + tileHeight * 4);
		background.draw(batch, waterCoords.x - tileWidth / 2, waterCoords.y - tileHeight / 2,
				tileRenderer.getViewBounds().width + tileWidth * 4,
				tileRenderer.getViewBounds().height + tileHeight * 4);
		batch.end();

		tileRenderer.render();

		/* Draw highlight on current tile we have selected */
		batch.begin();
		// Convert our mouse coordinates to world, where we then convert them to a tile
		// [x, y], then back to screen

		Vector3 coords = Render3D.screenToWorldCoordiates(inputManager.getMouseX(), inputManager.getMouseY(), 0);
		Vector2 tileCoords = Render3D.worldPosToTile(coords.x, coords.y);

		float tileX = (int) (Math.floor(tileCoords.x));
		float tileY = (int) (Math.floor(tileCoords.y));

		Vector2 realCoords = Render3D.worldToScreenCoordinates(tileX, tileY, 0);
		
		float distance = playerManager.distanceFromPlayer(tileX,tileY);
		Terrain terrain = GameManager.get().getWorld().getTerrain((int)tileX, (int)tileY);
		TreeShopGui treeShopGui = (TreeShopGui)GameManager.get().getManager(GuiManager.class).getGui(TreeShopGui.class);
		treeShopGui.setPlantable(distance < maxShopRange && terrain.isPlantable() && !terrain.getTexture().equals("void"));
		if (terrain.getTexture().equals("void")) {
			// Do nothing
		} else if (treeShopGui.getPlantable())
			batch.draw(textureManager.getTexture("highlight_tile"), realCoords.x, realCoords.y);
		else 
			batch.draw(textureManager.getTexture("highlight_tile_invalid"), realCoords.x, realCoords.y);
			
		
		batch.end();

		// Render entities etc.
		renderer.render(batch);


		// TODO: add render for projectile's separately
		GameManager.get().getManager(ParticleManager.class).draw(batch);
		((TreeShopGui)GameManager.get().getManager(GuiManager.class).getGui(TreeShopGui.class)).render();
		
	}

	/**
	 * Renderer thread Must update all displayed elements using a Renderer
	 */
	@Override
	public void render(float delta) {
		/**
		 * We only tick/render the game if we're actually playing. Lets us seperate main
		 * menu and such from the game TODO We may lose/gain a tick or part of a tick
		 * when we pause/unpause?
		 */
		/*
		 * Tickrate = 100Hz
		 */

		if (playing) {
			// Stop the first tick lasting years
			if (lastGameTick != 0) {
				long timeDelta = TimeUtils.millis() - lastGameTick;
				if (timeDelta > tickrate) {

					// Tick game, a bit a weird place to have it though.
					tickGame(timeDelta);
					lastGameTick = TimeUtils.millis();
				}
			} else {
				lastGameTick = TimeUtils.millis();
			}
		}

		/*
		 * Create a new render batch. At this stage we only want one but perhaps we need
		 * more for HUDs etc
		 */
		SpriteBatch batch = new SpriteBatch();

		/*
		 * Update the input handlers
		 */
		// handleInput();

		/*
		 * Update the camera
		 */
		cameraManager.getCamera().update();
		batch.setProjectionMatrix(cameraManager.getCamera().combined);

		/*
		 * Clear the entire display as we are using lazy rendering
		 */
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// TODO way to render game so it appears paused (could just be a flag)
		if (playing) {
			renderGame(batch);
		}

		updateWaveGUI();
		updateRespawnGUI();
		renderGUI(batch);

		batch.dispose();
	}

	/**
	 * Resizes the viewport
	 * 
	 * @param width
	 * @param height
	 */
	@Override
	public void resize(int width, int height) {
		cameraManager.getCamera().viewportWidth = width;
		cameraManager.getCamera().viewportHeight = height;
		cameraManager.getCamera().update();

		// Tell the gui manager to resize appropriately
		guiManager.resize(width, height);
	}

	/**
	 * @see ApplicationListener#pause()
	 */
	@Override
	public void pause() {

	}

	/**
	 * @see ApplicationListener#resume()
	 */
	@Override
	public void resume() {

	}

	/**
	 * Called when this screen becomes the current screen for a {@link Game}.
	 */
	@Override
	public void show() {

	}

	/**
	 * Called when this screen is no longer the current screen for a {@link Game}.
	 */
	@Override
	public void hide() {

	}

	/**
	 * Disposes of assets etc when the rendering system is stopped.
	 */
	@Override
	public void dispose() {
		// Don't need this at the moment
	}

	public void exitToMenu() {
		GameManager.get().clearManagers();
		game.setScreen(new MainMenuScreen(game));
		dispose();
	}

	private class CameraHandler implements KeyDownObserver {

		@Override
		public void notifyKeyDown(int keycode) {
			OrthographicCamera c = cameraManager.getCamera();
			int speed = 10;

			if (keycode == Input.Keys.EQUALS) {
				if (c.zoom > 0.1) {
					c.zoom -= 0.1;
				}
			} else if (keycode == Input.Keys.MINUS) {
				c.zoom += 0.1;

			} else if (keycode == Input.Keys.UP) {
				c.translate(0, 1 * speed * c.zoom, 0);
			} else if (keycode == Input.Keys.DOWN) {
				c.translate(0, -1 * speed * c.zoom, 0);
			} else if (keycode == Input.Keys.LEFT) {
				c.translate(-1 * speed * c.zoom, 0, 0);
			} else if (keycode == Input.Keys.RIGHT) {
				c.translate(1 * speed * c.zoom, 0, 0);
			}
		}
	}

	private class PauseHandler implements KeyDownObserver {
		@Override
		public void notifyKeyDown(int keycode) {
			if (keycode == Input.Keys.ESCAPE) {
				// Pause the Game
				// ToDo
				// Show the Pause Menu
				((PauseMenuGui) guiManager.getGui(PauseMenuGui.class)).show();
			}
		}
	}
	
	private class GameOverHandler implements KeyDownObserver {
		@Override
		public void notifyKeyDown(int keycode) {
			if (keycode == Input.Keys.G) {
				guiManager.getGui(GameOverGui.class).show();
				GameManager.get().getWorld()
						.removeEntity(GameManager.get().getManager(PlayerManager.class).getPlayer());
			}
		}
	}

	private class ScrollTester implements ScrollObserver {

		@Override
		public void notifyScrolled(int amount) {
			// System.out.println(amount);
		}

	}

	/**
	 * Sets the sound effects volume (v) in SoundManager. (from 0 to 1)
	 * 
	 * @param v
	 */
	public void setEffectsVolume(float v) {
		soundManager.setEffectsVolume(v);
	}

	/**
	 * Returns the current sound effects volume from SoundManager.
	 * 
	 * @return float from 0 to 1.
	 */
	public float getEffectsVolume() {
		return soundManager.getEffectsVolume();
	}

	/**
	 * Sets the music volume (v) in SoundManager. (from 0 to 1)
	 * 
	 * @param v
	 */
	public void setMusicVolume(float v) {
		soundManager.setMusicVolume(v);
	}

	/**
	 * Returns the current music volume from SoundManager.
	 * 
	 * @return float from 0 to 1.
	 */
	public float getMusicVolume() {
		return soundManager.getMusicVolume();
	}

	/**
	 * Plays a blip sound.
	 */
	public void menuBlipSound() {
		soundManager.playSound("menu_blip.wav");
	}

	public double getTickrate() {
		return this.tickrate;
	}

	public void setTickrate(double tickrate) {
		this.tickrate = tickrate;
	}

}
