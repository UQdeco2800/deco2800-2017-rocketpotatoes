package com.deco2800.potatoes;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.deco2800.potatoes.entities.*;
import com.deco2800.potatoes.managers.*;
import com.deco2800.potatoes.observers.KeyDownObserver;
import com.deco2800.potatoes.observers.ScrollObserver;
import com.deco2800.potatoes.renderering.Render3D;
import com.deco2800.potatoes.renderering.Renderable;
import com.deco2800.potatoes.renderering.Renderer;
import com.deco2800.potatoes.handlers.MouseHandler;
import com.deco2800.potatoes.util.Box3D;
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

	/**
	 * Create a camera for panning and zooming.
	 * Camera must be updated every render cycle.
	 */

	private SoundManager soundManager;
	private MouseHandler mouseHandler;
	private PlayerManager playerManager;
	private MultiplayerManager multiplayerManager;

	private Stage stage;
	private Window window;
	private Button peonButton;

	private long lastGameTick = 0;

	/**
	 * Creates the required objects for the game to start.
	 * Called when the game first starts
	 */
	@Override
	public void create () {
		
		/*
		 * Forces the GameManager to load the TextureManager, and load textures.
		 */
		GameManager.get().getManager(TextureManager.class);

		


		/**
		 *	Set up new stuff for this game
		 */
		/* Create an example world for the engine */
		GameManager.get().setWorld(new InitialWorld());
		
		/* Create a sound manager for the whole game */
		soundManager = (SoundManager) GameManager.get().getManager(SoundManager.class);

		/* Create a mouse handler for the game */
		mouseHandler = new MouseHandler();

		/* Create a multiplayer manager for the game */
		multiplayerManager = new MultiplayerManager();

		/*
		//TODO TESTING REMOVE !!
		//multiplayerManager.createHost(1337);
		//multiplayerManager.joinGame("Tom", "127.0.0.1", 1337);
		//multiplayerManager.broadcastMessage("Hey everybody!");

		Random random = new Random();

		MultiplayerManager m = multiplayerManager;
		if (m.isMultiplayer() && m.isMaster()) {
			for (int i = 0; i < 5; i++) {
				m.broadcastNewEntity(new Squirrel(
						10 + random.nextFloat() * 10, 10 + random.nextFloat() * 10, 0));
			}

			//m.broadcastNewEntity(new Peon(7, 7, 0));
			m.broadcastNewEntity(new Tower(8, 8, 0));
			//m.broadcastNewEntity(new GoalPotate(15, 10, 0));
		}
		*/

		/* Create a player manager. */
		playerManager = (PlayerManager)GameManager.get().getManager(PlayerManager.class);

		if (!multiplayerManager.isMultiplayer()) {
			// Make our player
			playerManager.setPlayer(new Player(5, 10, 0));
			GameManager.get().getWorld().addEntity(playerManager.getPlayer());
		}

		/**
		 * Setup the game itself
		 */
		/* Setup the camera and move it to the center of the world */
		GameManager.get().setCamera(new OrthographicCamera(1920, 1080));
		GameManager.get().getCamera().translate(GameManager.get().getWorld().getWidth()*32, 0);

		/**
		 * Setup GUI
		 */
		stage = new Stage(new ScreenViewport());
		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		window = new Window("Menu", skin);

		/* Add a quit button to the menu */
		Button button = new TextButton("Quit", skin);

		/* Add another button to the menu */
		Button anotherButton = new TextButton("Play Duck Sound", skin);

		/* Add another button to the menu */
		peonButton = new TextButton("Select a Unit", skin);

		/* Add a programatic listener to the quit button */
		button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.exit(0);
			}
		});

		/* Add a handler to play a sound */
		anotherButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				soundManager.playSound("quack.wav");
			}
		});

		/* Add listener for peon button */
		peonButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				for (Renderable r : GameManager.get().getWorld().getEntities().values()) {
					if (r instanceof Selectable) {
						if (((Selectable) r).isSelected()) {

						}
					}
				}
			}
		});

		/* Add all buttons to the menu */
		window.add(button);
		window.add(anotherButton);
		window.add(peonButton);
		window.pack();
		window.setMovable(false); // So it doesn't fly around the screen
		window.setPosition(0, stage.getHeight()); // Place it in the top left of the screen

		/* Add the window to the stage */
		stage.addActor(window);


		/**
		 * Setup inputs for the buttons and the game itself
		 */
		/* Setup an Input Multiplexer so that input can be handled by both the UI and the game */
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(stage); // Add the UI as a processor

		InputManager input = (InputManager) GameManager.get().getManager(InputManager.class);
		input.addKeyDownListener(new CameraHandler());
		input.addScrollListener(new ScrollTester());
		inputMultiplexer.addProcessor(input);
		
        /*
         * Set up some input handlers for panning with dragging.
         */
		inputMultiplexer.addProcessor(new InputAdapter() {

			int originX;
			int originY;

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				originX = screenX;
				originY = screenY;


				Vector3 worldCoords = GameManager.get().getCamera().unproject(new Vector3(screenX, screenY, 0));
				mouseHandler.handleMouseClick(worldCoords.x, worldCoords.y);

				return true;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {

				originX -= screenX;
				originY -= screenY;

				// invert the y axis
				originY = -originY;

				originX += GameManager.get().getCamera().position.x;
				originY += GameManager.get().getCamera().position.y;

				GameManager.get().getCamera().translate(originX - GameManager.get().getCamera().position.x, originY - GameManager.get().getCamera().position.y);

				originX = screenX;
				originY = screenY;

				return true;
			}
		});

		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	/**
	 * Renderer thread
	 * Must update all displayed elements using a Renderer
	 */
	@Override
	public void render () {

		/*
		 * Tickrate = 100Hz
		 */
		long timeDelta = TimeUtils.millis() - lastGameTick;
		if(timeDelta > 10) {
			window.removeActor(peonButton);
			boolean somethingSelected = false;


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
				lastGameTick = TimeUtils.millis();

				if (e instanceof Selectable) {
					if (((Selectable) e).isSelected()) {
						peonButton = ((Selectable) e).getButton();
						somethingSelected = true;
					}
				}

			}

			// Broadcast updates if we're master
			if (multiplayerManager.isMultiplayer() && multiplayerManager.isMaster()) {
				for (Map.Entry<Integer, AbstractEntity> e : GameManager.get().getWorld().getEntities().entrySet()) {
					// But don't broadcast our player yet
					if (e.getKey() != multiplayerManager.getID()) {
						multiplayerManager.broadcastEntityUpdate(e.getValue(), e.getKey());
					}
				}
			}

			// Broadcast our player updating
			multiplayerManager.broadcastEntityUpdate( playerManager.getPlayer(), multiplayerManager.getID());

			if (!somethingSelected) {
				peonButton = new TextButton("Select a Unit", new Skin(Gdx.files.internal("uiskin.json")));
			}
			window.add(peonButton);


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
		GameManager.get().getCamera().update();
		batch.setProjectionMatrix(GameManager.get().getCamera().combined);

        /*
         * Clear the entire display as we are using lazy rendering
         */
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /* Render the tiles first */
		BatchTiledMapRenderer tileRenderer = renderer.getTileRenderer(batch);
		tileRenderer.setView(GameManager.get().getCamera());
		tileRenderer.render();

		/*
         * Use the selected renderer to render objects onto the map
         */
		renderer.render(batch);

		/* Dispose of the spritebatch to not have memory leaks */
		Gdx.graphics.setTitle("DECO2800 " + this.getClass().getCanonicalName() +  " - FPS: "+ Gdx.graphics.getFramesPerSecond());

		stage.act();
		stage.draw();

		batch.dispose();
	}


	/**
	 * Resizes the viewport
	 * @param width
	 * @param height
	 */
	@Override
	public void resize(int width, int height) {
		GameManager.get().getCamera().viewportWidth = width;
		GameManager.get().getCamera().viewportHeight = height;
		GameManager.get().getCamera().update();

		stage.getViewport().update(width, height, true);
		window.setPosition(0, stage.getHeight());
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
			int speed = 10;

			if (keycode == Input.Keys.EQUALS) {
				if (GameManager.get().getCamera().zoom > 0.1) {
					GameManager.get().getCamera().zoom -= 0.1;
				}
			} else if (keycode == Input.Keys.MINUS) {
				GameManager.get().getCamera().zoom += 0.1;

			} else if (keycode == Input.Keys.UP) {
				GameManager.get().getCamera().translate(0, 1 * speed * GameManager.get().getCamera().zoom, 0);
			} else if (keycode == Input.Keys.DOWN) {
				GameManager.get().getCamera().translate(0, -1 * speed * GameManager.get().getCamera().zoom, 0);
			} else if (keycode == Input.Keys.LEFT) {
				GameManager.get().getCamera().translate(-1 * speed * GameManager.get().getCamera().zoom, 0, 0);
			} else if (keycode == Input.Keys.RIGHT) {
				GameManager.get().getCamera().translate(1 * speed * GameManager.get().getCamera().zoom, 0, 0);
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
