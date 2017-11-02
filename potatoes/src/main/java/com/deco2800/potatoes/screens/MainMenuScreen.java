package com.deco2800.potatoes.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.deco2800.potatoes.RocketPotatoes;
import com.deco2800.potatoes.gui.MainMenuGui;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.SoundManager;
import com.deco2800.potatoes.managers.TextureManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * "Call to Adventure" Kevin MacLeod (incompetech.com)
 * Licensed under Creative Commons: By Attribution 3.0 License
 * http://creativecommons.org/licenses/by/3.0/
 */

/**
 * Main menu screen implemetation. Handles the logic/display for the main menu, and other adjacent menus (e.g. options).
 * Also holds the logic for starting a game, (e.g. singleplayer, multiplayer, loaded, etc.)
 */
public class MainMenuScreen implements Screen {
    private RocketPotatoes game;

    private static final Logger LOGGER = LoggerFactory.getLogger(MainMenuScreen.class);

    private SpriteBatch batch;
    private Stage stage;

    private MainMenuGui mainMenuGui;
    private OrthographicCamera camera;
    private SoundManager soundManager;
    private TextureManager textureManager;


    public MainMenuScreen(RocketPotatoes game) {
        this.game = game;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        // game screen background

        soundManager = GameManager.get().getManager(SoundManager.class);
        textureManager = GameManager.get().getManager(TextureManager.class);
        TextureManager.loadTextures();
        stage = new Stage(new ScreenViewport());

        soundManager.playMusic("Call to Adventure.mp3");

        setupGui();

        // Setup input handling
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    /**
     * Creates GUI.
     */
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

        Gdx.graphics.setTitle("DECO2800 " + this.getClass().getCanonicalName() +  " - FPS: " +
                Gdx.graphics.getFramesPerSecond());

        // Draw/update gui
        stage.act();
        stage.getBatch().begin();
        Texture t = textureManager.getTexture("backgroundMainMenu");
        stage.getBatch().draw(t, 0 - (int)(t.getWidth() / 2.0) + (int)(stage.getWidth() / 2.0),  0 - (int)(t.getHeight() / 2.0) + (int)(stage.getHeight() / 2.0), t.getWidth(), t.getHeight());


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
//not implemented
    }


    /**
     * @see ApplicationListener#pause()
     */
    @Override
    public void pause() {
//not implemented
    }

    /**
     * @see ApplicationListener#resume()
     */
    @Override
    public void resume() {
//not implemented
    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {
//not implemented
    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
//not implemented
    }

    /**
     * Start a singleplayer game.
     */
    public void startSinglePlayer() {
        soundManager.stopMusic();
        game.setScreen(new GameScreen(game));
    }

    /**
     * Sets the sound effects volume (v) in SoundManager. (from 0 to 1)
     * @param v
     */
    public void setEffectsVolume(float v){
        soundManager.setEffectsVolume(v);
    }

    /**
     * Returns the current sound effects volume from SoundManager.
     * @return float from 0 to 1.
     */
    public float getEffectsVolume(){
        return soundManager.getEffectsVolume();
    }

    /**
     * Sets the music volume (v) in SoundManager. (from 0 to 1)
     * @param v
     */
    public void setMusicVolume(float v){
        soundManager.setMusicVolume(v);
    }

    /**
     * Returns the current music volume from SoundManager.
     * @return float from 0 to 1.
     */
    public float getMusicVolume(){
        return soundManager.getMusicVolume();
    }

    /**
     * Plays a blip sound.
     */
    public void menuBlipSound(){
        soundManager.playSound("menu_blip.wav");
    }

}
