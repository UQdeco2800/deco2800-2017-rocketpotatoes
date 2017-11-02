package com.deco2800.potatoes.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.deco2800.potatoes.RocketPotatoes;
import com.deco2800.potatoes.cheats.CheatList;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.trees.*;
import com.deco2800.potatoes.gui.*;
import com.deco2800.potatoes.handlers.MouseHandler;
import com.deco2800.potatoes.managers.*;
import com.deco2800.potatoes.observers.KeyDownObserver;
import com.deco2800.potatoes.observers.ScrollObserver;
import com.deco2800.potatoes.renderering.Render3D;
import com.deco2800.potatoes.renderering.Renderable;
import com.deco2800.potatoes.renderering.Renderer;
import com.deco2800.potatoes.waves.EnemyWave;
import com.deco2800.potatoes.worlds.*;
import com.deco2800.potatoes.worlds.terrain.Terrain;

/* "Feral Chase" Kevin MacLeod (incompetech.com)
 * Licensed under Creative Commons: By Attribution 3.0 License
 * http://creativecommons.org/licenses/by/3.0/
 */


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
	private SoundManager soundManager;
	private MouseHandler mouseHandler;
	private PlayerManager playerManager;
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

		// Add test TreeShop Gui
		guiManager.addGui(new TreeShopGui(guiManager.getStage()));
		maxShopRange = guiManager.getGui(TreeShopGui.class).getMaxRange();

        // Make our inventory window
        guiManager.addGui(new InventoryGui(guiManager.getStage()));
        
        // Add World Change Gui
        guiManager.addGui(new WorldChangeGui(guiManager.getStage(), this));

        //Make our game over window
		guiManager.addGui(new GameOverGui(guiManager.getStage(),this));

		// Make our wave information window
		guiManager.addGui(new WavesGui(guiManager.getStage()));

		guiManager.addGui(new RespawnGui(guiManager.getStage(),this));
		
		// Make our TutorialGui
		guiManager.addGui(new TutorialGui(guiManager.getStage(), this));
        
		/* Setup inputs */
		setupInputHandling();

        // Sets the world to the initial world, forest world
        GameManager.get().getManager(WorldManager.class).setWorld(ForestWorld.get());

		/* Move camera to center */
		cameraManager.getCamera().position.x = GameManager.get().getWorld().getWidth() * 32;
		cameraManager.getCamera().position.y = 0;

		/*
		 * Create a new render batch. At this stage we only want one but perhaps we need
		 * more for HUDs etc
		 */
		batch = new SpriteBatch();

		// Start music
		soundManager.playMusic("Feral Chase.mp3");
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

		MouseHandler mouseHandlers = new MouseHandler();
		inputManager.addTouchDownListener(mouseHandlers);
		inputManager.addTouchDraggedListener(mouseHandlers);
		inputManager.addMouseMovedListener(mouseHandlers);
		inputMultiplexer.addProcessor(inputManager);

		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	/**
	 * Initializes everything needed to actually play the game Can be used to
	 * `reset` the state of the game
	 */
	private void initializeGame() {
		GameManager.get().getManager(EventManager.class).unregisterAll();

		GameManager.get().getManager(WaveManager.class).regularGame(WaveManager.EASY);
			
		/* Randomly generate trees in each world */
		AbstractTree[] forestTrees = {new SeedTree(0, 0), new PineTree(0, 0), new FoodTree(0, 0), new DamageTree(0, 0, new AcornTreeType()), new DefenseTree(0, 0)};
		randomlyGenerateTrees(GameManager.get().getManager(WorldManager.class).getWorld(ForestWorld.get()), forestTrees);

		AbstractTree[] desertTrees = {new SeedTree(0, 0), new PineTree(0, 0), new FoodTree(0, 0), new DamageTree(0, 0, new CactusTreeType())};
		randomlyGenerateTrees(GameManager.get().getManager(WorldManager.class).getWorld(DesertWorld.get()), desertTrees);

		AbstractTree[] iceTrees = {new SeedTree(0, 0), new PineTree(0, 0), new FoodTree(0, 0), new DamageTree(0, 0, new IceTreeType())};
		randomlyGenerateTrees(GameManager.get().getManager(WorldManager.class).getWorld(IceWorld.get()), iceTrees);

		AbstractTree[] oceanTrees = {new SeedTree(0, 0), new PineTree(0, 0), new FoodTree(0, 0), new DamageTree(0, 0, new LightningTreeType())};
		randomlyGenerateTrees(GameManager.get().getManager(WorldManager.class).getWorld(OceanWorld.get()), oceanTrees);

		AbstractTree[] volcanoTrees = {new FoodTree(0, 0), new PineTree(0, 0), new DamageTree(0, 0, new FireTreeType())};
		randomlyGenerateTrees(GameManager.get().getManager(WorldManager.class).getWorld(VolcanoWorld.get()), volcanoTrees);
		// Make our player
		int targetX = (int) (GameManager.get().getWorld().getLength() / 2 - 5f);
		int targetY = (int) (GameManager.get().getWorld().getWidth() / 2 - 5f);

		while (GameManager.get().getWorld().getTerrain(targetX, targetY).getMoveScale() == 0) {
			targetX += GameManager.get().getRandom().nextInt() % 4 - 2;
			targetY += GameManager.get().getRandom().nextInt() % 4 - 2;
		}

		playerManager.setPlayer(targetX, targetY);
		GameManager.get().getWorld().addEntity(playerManager.getPlayer());

		GameManager.get().getManager(ParticleManager.class);

		//show the tutorial menu
		guiManager.getGui(TutorialGui.class).show();
	}
	
	/**
	 * Randomly generates trees in a specified world. Currently only spawns
	 * resource trees.
	 * 
	 * @param world 		the world to randomly generate trees in
	 * @param trees	 	the trees allowed to generate
	 */
	private void randomlyGenerateTrees(com.deco2800.potatoes.worlds.World world, AbstractTree[] trees) {
		// locations to add the trees
	    	int xPos;
	    	int yPos;

	    	// The amount of each tree to generate
    		int amount = 100/trees.length;

	    	// Terrain to add the tree to
	    	Terrain terrain;

	    	boolean oldTreeSpread = true;

	    	// Iterate over the trees
	    	for (int i = 0; i < trees.length; i ++) {
	    		for (int j = 0; j < amount; j++) {
	    			if (oldTreeSpread) {// Generate random location
					    xPos = (int) (Math.random() * WorldManager.WORLD_SIZE * 0.7) + (int)(WorldManager.WORLD_SIZE
							    * 0.15);
					    yPos = (int) (Math.random() * WorldManager.WORLD_SIZE * 0.7) + (int)(WorldManager.WORLD_SIZE
							    * 0.15);
					    terrain = world.getTerrain(xPos, yPos);

					    // Only add a tree if it is on grass
					    if (terrain.isPlantable()) {
						    AbstractTree newTree = trees[i].createCopy();
						    newTree.setPosX(xPos);
						    newTree.setPosY(yPos);
						    world.addEntity(newTree);
					    }
				    } else {
	    				world.addToPlantable(trees[i].createCopy());
				    }
	    		}
	    	}

	}
	
	private void addResourceTrees() {
		GameManager.get().getWorld().addEntity(new SeedTree(20f, 20f));
		GameManager.get().getWorld().addEntity(new FoodTree(22f, 20f));
		GameManager.get().getWorld().addEntity(new PineTree(24f, 20f));
	}

	private void addDamageTree() {
		GameManager.get().getWorld().addEntity(new DamageTree(16.5f, 11.5f, new LightningTreeType()));
		GameManager.get().getWorld().addEntity(new DamageTree(14.5f, 11.5f, new AcornTreeType()));
		GameManager.get().getWorld().addEntity(new DamageTree(15.5f, 11.5f, new IceTreeType()));
		GameManager.get().getWorld().addEntity(new DamageTree(13.5f, 11.5f, new FireTreeType()));
		GameManager.get().getWorld().addEntity(new DamageTree(12.5f, 11.5f, new CactusTreeType()));
		
		GameManager.get().getWorld().addEntity(new DefenseTree(10.5f, 11.5f));
	}

	private void tickGame(long timeDelta) {
		// Tick other stuff maybe
		for (Renderable e : GameManager.get().getWorld().getEntities().values()) {
			if (e instanceof Tickable) {
				((Tickable) e).onTick(timeDelta);
			}
		}

		// Tick CameraManager, maybe want to make managers tickable??
		cameraManager.centerOnTarget(timeDelta);

		// Ticks all tickable managers, currently events, waves, particles
		GameManager.get().onTick(timeDelta);
		
		guiManager.tickFadingGuis(timeDelta);
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
		int currentIndex = GameManager.get().getManager(WaveManager.class).getNonPauseWaveIndex();	//position of current wave in queue
		int totalEnemies = GameManager.get().getManager(
				WaveManager.class).getActiveWave().getTotalEnemies();//total enemies in game
		Gui waveGui = guiManager.getGui(WavesGui.class);
		if (waveGui instanceof WavesGui) {
			//Display progress through total waves
			EnemyWave activeWave = GameManager.get().getManager(WaveManager.class).getActiveWave();
			((WavesGui) waveGui).getWaveGuiWindow().getTitleLabel().setText("wave: " + (currentIndex));
			if (activeWave != null) {
				//if a wave is currently active show time left until it finishes spawning enemies
				timeToWaveEnd = activeWave.getTimeToEnd();
				if (activeWave.isPauseWave()) {
					((WavesGui) waveGui).getWaveStatusLabel().setText("Time to next wave: ");
				} else {
					((WavesGui) waveGui).getWaveStatusLabel().setText("Time left in wave: ");
				}
				((WavesGui) waveGui).getWaveTimeLabel().setText(Integer.toString(timeToWaveEnd/75));
				((WavesGui) waveGui).getWaveEnemiesLabel().setText(Integer.toString(totalEnemies));
			} else {
				//No active waves: display if there are more waves and if so how long until it starts
				if (GameManager.get().getManager(WaveManager.class).areWavesCompleted()) {
					((WavesGui) waveGui).getWaveStatusLabel().setText("No more waves.");
					((WavesGui) waveGui).getWaveTimeLabel().setText("");
				}
			}
		}
	}
	
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
		//note for sonar
	}

	/**
	 * @see ApplicationListener#resume()
	 */
	@Override
	public void resume() {
		// Don't need this at the moment
	}

	/**
	 * Called when this screen becomes the current screen for a {@link Game}.
	 */
	@Override
	public void show() {
		// Don't need this at the moment
	}

	/**
	 * Called when this screen is no longer the current screen for a {@link Game}.
	 */
	@Override
	public void hide() {
		// Don't need this at the moment
	}

	/**
	 * Disposes of assets etc when the rendering system is stopped.
	 */
	@Override
	public void dispose() {
		// Don't need this at the moment
	}

	public void exitToMenu() {
		soundManager.stopMusic();
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
