package com.deco2800.potatoes;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.deco2800.potatoes.entities.*;
import com.deco2800.potatoes.entities.Enemies.*;
import com.deco2800.potatoes.gui.GameMenuGui;
import com.deco2800.potatoes.handlers.MouseHandler;
import com.deco2800.potatoes.managers.*;
import com.deco2800.potatoes.observers.KeyDownObserver;
import com.deco2800.potatoes.observers.ScrollObserver;
import com.deco2800.potatoes.renderering.Render3D;
import com.deco2800.potatoes.renderering.Renderable;
import com.deco2800.potatoes.renderering.Renderer;
import com.deco2800.potatoes.worlds.InitialWorld;

import java.util.Map;
import java.util.Random;

/**
 * Handles the creation of the world and rendering.
 *
 */
public class RocketPotatoes extends ApplicationAdapter implements ApplicationListener {

	/**
	 * Set the renderer.
	 * 3D is for Isometric worlds
	 * 2D is for Side Scrolling worlds
	 * Check the documentation for each renderer to see how it handles WorldEntity coordinates
	 */
	private Renderer renderer = new Render3D();

	// Managers tracked here for ease of use. Should be initialized from the GameManager.get().getManager(...) though!
	private SoundManager soundManager;
	private MouseHandler mouseHandler;
	private PlayerManager playerManager;
	private MultiplayerManager multiplayerManager;
	private GuiManager guiManager;
	private CameraManager cameraManager;

	private long lastGameTick = 0;
	private boolean playing = true;


	/**
	 * Creates the required objects for window, gui and such. Also calls initializeGame().
	 */
	@Override
	public void create () {
		
		/*
		 * Forces the GameManager to load the TextureManager, and load textures.
		 */
		GameManager.get().getManager(TextureManager.class);

		/**
		 *	Setup managers etc.
		 */
		
		/* Create a sound manager for the whole game */
		soundManager = (SoundManager) GameManager.get().getManager(SoundManager.class);

		/* Create a mouse handler for the game */
		mouseHandler = new MouseHandler();

		/* Create a multiplayer manager for the game */
		multiplayerManager = (MultiplayerManager)GameManager.get().getManager(MultiplayerManager.class);

		/* Create a player manager. */
		playerManager = (PlayerManager)GameManager.get().getManager(PlayerManager.class);

		/* Setup camera */
		cameraManager = (CameraManager)GameManager.get().getManager(CameraManager.class);
		cameraManager.setCamera(new OrthographicCamera(1920, 1080));

		/**
		 * GuiManager, which contains all our Gui specific properties/logic. Creates our stage etc.
		 */

		guiManager = (GuiManager)GameManager.get().getManager(GuiManager.class);
		guiManager.setStage(new Stage(new ScreenViewport()));

		// Make our GameMenuGui
		guiManager.addGui(new GameMenuGui(guiManager.getStage()));

		/* Setup inputs */
		setupInputHandling();

		/* Init game TODO move? */
		initializeGame();
	}

	private void setupInputHandling() {

		/**
		 * Setup inputs for the buttons and the game itself
		 */
		/* Setup an Input Multiplexer so that input can be handled by both the UI and the game */
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(guiManager.getStage()); // Add the UI as a processor

		InputManager input = (InputManager) GameManager.get().getManager(InputManager.class);
		input.addKeyDownListener(new CameraHandler());
		input.addScrollListener(new ScrollTester());
		
		MouseHandler mouseHandler = new MouseHandler();
		input.addTouchDownListener(mouseHandler);
		input.addTouchDraggedListener(mouseHandler);
		inputMultiplexer.addProcessor(input);

		Gdx.input.setInputProcessor(inputMultiplexer);

	}

	/**
	 * Initializes everything needed to actually play the game
	 * Can be used to `reset` the state of the game
	 *
	 * TODO this logic should be state-machined'd (i.e. Main Menu <-> Playing <-> Paused. With every state having
	 * TODO it's own menu(s), initialization etc. And when we setup custom transition logic.
	 */
	private void initializeGame() {

		/* Create an example world for the engine */
		GameManager.get().setWorld(new InitialWorld());

		/* Move camera to center */
		cameraManager.getCamera().position.x = GameManager.get().getWorld().getWidth() * 32;
		cameraManager.getCamera().position.y = 0;


		// TODO clean up testing stuff

		//TODO TESTING REMOVE !!
		// Magic testing code
		/*
		try {
			try {
				System.out.println("Starting client");
				multiplayerManager.joinGame("Tom 2", "127.0.0.1", 1337);
				while (!multiplayerManager.isClientReady()) ;
				System.out.println("Started client");
			} catch (IOException ex) {
				System.out.println("No server to connect to");
				System.out.println("Starting server");
				multiplayerManager.createHost(1337);

				// Wait until server is ready
				while (!multiplayerManager.isServerReady()) ;
				System.out.println("Started server");
				try {
					System.out.println("Starting client");
					multiplayerManager.joinGame("Tom", "127.0.0.1", 1337);
					while (!multiplayerManager.isClientReady()) ;
					System.out.println("Started client");
				} catch (IOException ex2) {
					System.exit(-1);
				}
			}
		}
		catch (Exception ex) {
			// rest in peace
			ex.printStackTrace();
			System.exit(-1);
		}
		*/


		Random random = new Random();

		MultiplayerManager m = multiplayerManager;
		if (m.isMaster() || !m.isMultiplayer()) {
//			for (int i = 0; i < 5; i++) {
//				GameManager.get().getWorld().addEntity(new Squirrel(
//						10 + random.nextFloat() * 10, 10 + random.nextFloat() * 10, 0));
//			}



			GameManager.get().getWorld().addEntity(new Peon(7, 7, 0));
			GameManager.get().getWorld().addEntity(new Tower(8, 8, 0));
			GameManager.get().getWorld().addEntity(new GoalPotate(15, 10, 0));
		}



		if (!multiplayerManager.isMultiplayer()) {
			/* TODO bug! currently reseting the game while having a key held down will then notify the new player with the keyUp
		   TODO event, which will result it in moving without pressing a key. This is something a bit difficult to fix as
		   TODO so I'm just going to leave it for now since fixing it is a bit of a hassle
		 	*/

			// Make our player
			playerManager.setPlayer(new Player(5, 10, 0));
			GameManager.get().getWorld().addEntity(playerManager.getPlayer());
			//Testing a new enemy
			GameManager.get().getWorld().addEntity(new TestEnemy(
					10 + random.nextFloat() * 10, 10 + random.nextFloat() * 10, 0));
		}
	}

	private void tickGame(long timeDelta) {
		/* broken!
		window.removeActor(peonButton);
		boolean somethingSelected = false;
		*/

		// Tick our player
		if (multiplayerManager.isMultiplayer() && !multiplayerManager.isMaster()) {
			playerManager.getPlayer().onTick(timeDelta);
		}

		// Tick other stuff maybe
		for (Renderable e : GameManager.get().getWorld().getEntities().values()) {
			if (e instanceof Tickable) {
				// Only tick elements if we're singleplayer or master
				if (!multiplayerManager.isMultiplayer() || multiplayerManager.isMaster()) {
					((Tickable) e).onTick(timeDelta);
				}
			}


			/* broken!
			if (e instanceof Selectable) {
				if (((Selectable) e).isSelected()) {
					peonButton = ((Selectable) e).getButton();
					somethingSelected = true;
				}
			}
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


		/* broken!
		if (!somethingSelected) {
			peonButton = uiPeonButton;
		}
		window.add(peonButton);
		*/

		// Tick CameraManager, maybe want to make managers tickable??
		cameraManager.centerOnTarget(timeDelta);
	}

	/**
	 * Pauses the game, does nothing if the game is already paused
	 * TODO temp
	 */
	public void pauseGame() {
		playing = false;
	}

	/**
	 * Resumes the game, does nothing if the game is already playing
	 * TODO temp
	 */
	public void resumeGame() {
		playing = true;
	}

	private void renderGUI(SpriteBatch batch) {

		// Update window title
		Gdx.graphics.setTitle("DECO2800 " + this.getClass().getCanonicalName() +  " - FPS: "+ Gdx.graphics.getFramesPerSecond());

		// Render GUI elements
		guiManager.getStage().act();
		guiManager.getStage().draw();
	}

	private void renderGame(SpriteBatch batch) {

        /* Render the tiles first */
		BatchTiledMapRenderer tileRenderer = renderer.getTileRenderer(batch);
		tileRenderer.setView(cameraManager.getCamera());
		tileRenderer.render();

		// Render entities etc.
		renderer.render(batch);
	}

	/**
	 * Renderer thread
	 * Must update all displayed elements using a Renderer
	 */
	@Override
	public void render () {
		/**
		 * We only tick/render the game if we're actually playing. Lets us seperate main menu and such from the game
		 * TODO We may lose/gain a tick or part of a tick when we pause/unpause?
		 */
		/*
		 * Tickrate = 100Hz
		 */
		if (playing) {
			long timeDelta = TimeUtils.millis() - lastGameTick;
			if (timeDelta > 10) {

				// Tick game, a bit a weird place to have it though.
				tickGame(timeDelta);
				lastGameTick = TimeUtils.millis();
			}
		}

        /*
         * Create a new render batch.
         * At this stage we only want one but perhaps we need more for HUDs etc
         */
		SpriteBatch batch = new SpriteBatch();

		        /*
         * Update the input handlers
         */
		//handleInput();

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

		renderGUI(batch);

		batch.dispose();
	}


	/**
	 * Resizes the viewport
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
	 * Disposes of assets etc when the rendering system is stopped.
	 */
	@Override
	public void dispose () {
		// Don't need this at the moment
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
	
	
	private class ScrollTester implements ScrollObserver {

		@Override
		public void notifyScrolled(int amount) {
			System.out.println(amount);			
		}
		
	}
	
	
}
