package com.deco2800.potatoes.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.deco2800.potatoes.RocketPotatoes;
import com.deco2800.potatoes.gui.MainMenuGui;
import com.deco2800.potatoes.gui.OptionsMenuGui;
import com.deco2800.potatoes.handlers.MouseHandler;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.InputManager;
import com.deco2800.potatoes.managers.SoundManager;
import com.deco2800.potatoes.managers.TextureManager;

/**
 * Main menu screen implemetation. Handles the logic/display for the main menu, and other adjacent menus (e.g. options).
 * Also holds the logic for starting a game, (e.g. singleplayer, multiplayer, loaded, etc.)
 *
 * TODO make this nicer (i.e. use dispose) Probably has tiny memory leaks
 */
public class MainMenuScreen implements Screen {
    private RocketPotatoes game;

    private SpriteBatch batch;
    private Stage stage;

    private MainMenuGui mainMenuGui;
    private OptionsMenuGui optionsMenuGui;
    private OrthographicCamera camera;
    private TextureManager texturemanager;


    public MainMenuScreen(RocketPotatoes game) {
        this.game = game;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        // TODO config?
        camera.setToOrtho(false, 1920, 1080);
        //game screen background
        texturemanager=(TextureManager)GameManager.get().getManager(TextureManager.class);

        stage = new Stage(new ScreenViewport());

        setupGui();

        // Setup input handling
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    private void setupGui() {
        mainMenuGui = new MainMenuGui(stage, this);
    }


    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.graphics.setTitle("DECO2800 " + this.getClass().getCanonicalName() +  " - FPS: "+ Gdx.graphics.getFramesPerSecond());

        // Draw/update gui
        stage.act();
        stage.getBatch().begin();
        stage.getBatch().draw(texturemanager.getTexture("screen_background"), 0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        stage.getBatch().end();
        
        stage.draw();

    }

    /**
     * @param width
     * @param height
     * @see ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();

        stage.getViewport().update(width, height, true);

        mainMenuGui.resize(stage);
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {


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
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {

    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {

    }

    public void startSinglePlayer() {
        game.setScreen(new GameScreen(game));
    }

    public void startMultiplayer(String name, String IP, int port, boolean isHost) {
        try {
            game.setScreen(new GameScreen(game, name, IP, port, isHost));
        }
        catch (Exception ex) {
            // TODO handle this stuff yo
            ex.printStackTrace();
            System.exit(-1);
        }
    }

    public void setMasterVolume(float v){
        ((SoundManager)GameManager.get().getManager(SoundManager.class)).setMasterVolume(v);
    }

    public void setMusicVolume(float v){
        ((SoundManager)GameManager.get().getManager(SoundManager.class)).setMusicVolume(v);
    }

}
