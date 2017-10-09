package com.deco2800.potatoes.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.deco2800.potatoes.RocketPotatoes;
import com.deco2800.potatoes.cheats.CheatList;
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
	private ProgressBarManager progressBarManager;

	private SpriteBatch batch;

	private double tickrate = 1;

	private int maxShopRange;

	float minimumZoom = 1.0f;
	float maximumZoom = 4.0f;
	float zoomSpeed = 0.2f;

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

        GameManager.get().getManager(GameTimeManager.class);

		/* Setup camera */
		cameraManager = GameManager.get().getManager(CameraManager.class);
		cameraManager.setCamera(new OrthographicCamera(1920, 1080));
		cameraManager.getCamera().zoom += 1;
		
		/* Setup progress bar manager. */
		progressBarManager = GameManager.get().getManager(ProgressBarManager.class);

        /* Set up cheat codes. */
        CheatList.addCheats();

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
		maxShopRange = guiManager.getGui(TreeShopGui.class).getMaxRange();

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
		
		// Make our TutorialGui
		guiManager.addGui(new TutorialGui(guiManager.getStage(), this));
        
		/* Setup inputs */
		setupInputHandling();

        // Sets the world to the initial world, forest world
        GameManager.get().getManager(WorldManager.class).setWorld(WorldType.FOREST_WORLD);

		/* Move camera to center */
		cameraManager.getCamera().position.x = GameManager.get().getWorld().getWidth() * 32;
		cameraManager.getCamera().position.y = 0;

		/*
		 * Create a new render batch. At this stage we only want one but perhaps we need
		 * more for HUDs etc
		 */
		batch = new SpriteBatch();
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
		inputManager.addScrollListener(new ZoomHandler());

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
			GameManager.get().getWorld().addEntity(new ProjectileTree(8.5f, 8.5f));
			GameManager.get().getWorld().addEntity(new GoalPotate(15.5f, 10.5f));

			//add an enemy gate to game world
			GameManager.get().getWorld().addEntity(new EnemyGate(24.5f,24.5f));

			//add enemy waves
			GameManager.get().getManager(WaveManager.class).addWave(new EnemyWave(1, 0, 0,0, 750, 1));
			GameManager.get().getManager(WaveManager.class).addWave(new EnemyWave()); // pause wave
			GameManager.get().getManager(WaveManager.class).addWave(new EnemyWave(0, 1, 0,0, 900, 2));
			GameManager.get().getManager(WaveManager.class).addWave(new EnemyWave(1, 1, 1,1, 1050, 3));


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
				playerManager.setPlayer(5.5f, 10.5f);
				GameManager.get().getWorld().addEntity(playerManager.getPlayer());
			}
			GameManager.get().getManager(ParticleManager.class);
		}
		
		//show the tutorial menu
		guiManager.getGui(TutorialGui.class).show();
	}

	private void addDamageTree() {
		GameManager.get().getWorld().addEntity(new DamageTree(16.5f, 11.5f));
		GameManager.get().getWorld().addEntity(new DamageTree(14.5f, 11.5f, new AcornTreeType()));
		GameManager.get().getWorld().addEntity(new DamageTree(15.5f, 11.5f, new IceTreeType()));
		GameManager.get().getWorld().addEntity(new DamageTree(13.5f, 11.5f, new FireTreeType()));
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



		GameManager.get().getWorld().addEntity(new ResourceEntity(10.5f, 20.5f, seedResource));
		GameManager.get().getWorld().addEntity(new ResourceEntity(10.5f, 18.5f, foodResource));
		GameManager.get().getWorld().addEntity(new ResourceEntity(10.5f, 16.5f, woodResource));

		GameManager.get().getWorld().addEntity(new ResourceEntity(9.5f, 20.5f, tumbleweedResource));
		GameManager.get().getWorld().addEntity(new ResourceEntity(9.5f, 18.5f, cactusThornResource));
		GameManager.get().getWorld().addEntity(new ResourceEntity(9.5f, 16.5f, pricklyPearResource));

		GameManager.get().getWorld().addEntity(new ResourceEntity(8.5f, 20.5f, snowBallResource));
		GameManager.get().getWorld().addEntity(new ResourceEntity(8.5f, 18.5f, sealSkinResource));
		GameManager.get().getWorld().addEntity(new ResourceEntity(8.5f, 16.5f, iceCrystalResource));


		GameManager.get().getWorld().addEntity(new ResourceEntity(7.5f, 20.5f, coalResource));
		GameManager.get().getWorld().addEntity(new ResourceEntity(7.5f, 18.5f, bonesThornResource));
		GameManager.get().getWorld().addEntity(new ResourceEntity(7.5f, 16.5f, obsidianResource));


		GameManager.get().getWorld().addEntity(new ResourceEntity(6.5f, 20.5f, fishMeatResource));
		GameManager.get().getWorld().addEntity(new ResourceEntity(6.5f, 18.5f, pearlResource));
		GameManager.get().getWorld().addEntity(new ResourceEntity(6.5f, 16.5f, treasureResource));




	}

	private void initialisePortal() {
		GameManager.get().getWorld().addEntity(new BasePortal(14.5f, 17.5f, 100));
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

	/**
	 * Get the current state of waves from WaveManager and update the WavesGui accordingly.
	 */
	private void updateWaveGUI() {
		int timeToWaveEnd;
		int timeToNextWave;
		int currentIndex = GameManager.get().getManager(WaveManager.class).getWaveIndex();	//position of current wave in queue
		int totalWaves = GameManager.get().getManager(WaveManager.class).getWaves().size();	//total waves in queue
		Gui waveGUI = guiManager.getGui(WavesGui.class);
		if (waveGUI instanceof WavesGui) {
			//Display progress through total waves
			EnemyWave activeWave = GameManager.get().getManager(WaveManager.class).getActiveWave();
			((WavesGui) waveGUI).getWaveGuiWindow().getTitleLabel().setText("wave: " + (currentIndex+1) + "/" + totalWaves);
			if (activeWave != null) {
				//if a wave is currently active show time left until it finishes spawning enemies
				timeToWaveEnd = activeWave.getTimeToEnd();
				((WavesGui) waveGUI).getWaveStatusLabel().setText("Time left in wave: ");
				((WavesGui) waveGUI).getWaveTimeLabel().setText("" + timeToWaveEnd/75);
			} else {
				//No active waves: display if there are more waves and if so how long until it starts
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

	/**
	 * Renderer thread Must update all displayed elements using a Renderer
	 */
	@Override
	public void render(float delta) {
		/**
		 * We only tick/render the game if we're actually playing. Lets us seperate main
		 * menu and such from the game
		 */

		/*
		 * Tickrate = 100Hz
		 */

		if (!GameManager.get().isPaused()) {
			tickGame((int)(delta * 1000 * tickrate));
		}

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

			renderer.render(batch);

		updateWaveGUI();
		updateRespawnGUI();
		renderGUI(batch);

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

		float newZoom;

		@Override
		public void notifyKeyDown(int keycode) {
			OrthographicCamera c = cameraManager.getCamera();
			int speed = 10;

			if (keycode == Input.Keys.EQUALS) {
				newZoom = c.zoom + zoomSpeed;
				if (newZoom < maximumZoom) {
					c.zoom = newZoom;
				} else {
					c.zoom = maximumZoom;
				}
			} else if (keycode == Input.Keys.MINUS) {
				newZoom = c.zoom + zoomSpeed;
				if (c.zoom > minimumZoom) {
					c.zoom = newZoom;
				} else {
					c.zoom = minimumZoom;
				}
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
				guiManager.getGui(PauseMenuGui.class).show();
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

	private class ZoomHandler implements ScrollObserver {

		float newZoom;

		@Override
		public void notifyScrolled(int amount) {
			OrthographicCamera c = cameraManager.getCamera();
			if (amount == 1) {
				newZoom = c.zoom + zoomSpeed * amount;
				if (newZoom < maximumZoom) {
					c.zoom = newZoom;
				} else {
					c.zoom = maximumZoom;
				}
			}
			if (amount == -1) {
				newZoom = c.zoom + zoomSpeed * amount;
				if (c.zoom > minimumZoom) {
					c.zoom = newZoom;
				} else {
					c.zoom = minimumZoom;
				}
			}
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
		if (tickrate < 0) {
			tickrate = 0;
		}
		this.tickrate = tickrate;
	}

}
