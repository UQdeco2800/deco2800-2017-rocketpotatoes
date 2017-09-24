package com.deco2800.potatoes.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.TextureManager;
import com.deco2800.potatoes.screens.GameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.badlogic.gdx.utils.Align.center;

public class PauseMenuGui extends Gui {

    private static final Logger LOGGER = LoggerFactory.getLogger(PauseMenuGui.class);

    private GameScreen screen;
    private Stage stage;

    private TextureManager textureManager;

    // Buttons
    private Skin uiSkin;
    private Drawable resumeDrawable;
    private Drawable optionsDrawable;
    private Drawable saveDrawable;
    private Drawable exitDrawable;
    private Drawable pauseMenuDrawable;
    private VerticalGroup pauseButtonGroup;
    private ImageButton resumeButton;
    private ImageButton optionsButton;
    private ImageButton saveButton;
    private ImageButton exitButton;
    private Table table;

    private VerticalGroup optionsButtonGroup;
    private Label optionsMenuLabel;
    private Label optionsEffectsVolumeLabel;
    private Slider optionsEffectsVolumeSlider;
    private Label optionsMusicVolumeLabel;
    private Slider optionsMusicVolumeSlider;
    private TextButton optionsBackButton;

    // State indicator
    private enum States {
        PAUSE,
        OPTIONS
    }

    private States state = States.PAUSE;

    public PauseMenuGui(Stage stage, GameScreen screen) {
        this.screen = screen;
        this.stage = stage;

        textureManager = GameManager.get().getManager(TextureManager.class);

        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
        table = new Table(uiSkin);

        // Make drawables from textures
        resumeDrawable = new TextureRegionDrawable(new TextureRegion(textureManager.getTexture("resumePauseMenu")));
        optionsDrawable = new TextureRegionDrawable(new TextureRegion(textureManager.getTexture("optionsPauseMenu")));
        saveDrawable = new TextureRegionDrawable(new TextureRegion(textureManager.getTexture("savePauseMenu")));
        exitDrawable = new TextureRegionDrawable(new TextureRegion(textureManager.getTexture("exitPauseMenu")));
        pauseMenuDrawable = new TextureRegionDrawable(new TextureRegion(textureManager.getTexture("backgroundPauseMenu")));

        // Pause State
        resumeButton = new ImageButton(resumeDrawable);
        optionsButton = new ImageButton(optionsDrawable);
        saveButton = new ImageButton(saveDrawable);
        exitButton = new ImageButton(exitDrawable);

        pauseButtonGroup = new VerticalGroup();
        pauseButtonGroup.addActor(resumeButton);
        pauseButtonGroup.addActor(optionsButton);
        pauseButtonGroup.addActor(saveButton);
        pauseButtonGroup.addActor(exitButton);
        pauseButtonGroup.space(30);

        // Options State
        optionsMenuLabel = new Label("Options", uiSkin);
        optionsEffectsVolumeLabel = new Label("SFX Volume", uiSkin);
        optionsEffectsVolumeSlider = new Slider(0f,1f,0.01f,false, uiSkin);
        optionsMusicVolumeLabel = new Label("Music Volume", uiSkin);
        optionsMusicVolumeSlider = new Slider(0f,1f,0.01f,false, uiSkin);
        optionsBackButton = new TextButton("Back", uiSkin);

        optionsButtonGroup = new VerticalGroup();
        optionsButtonGroup.addActor(optionsMenuLabel);
        optionsButtonGroup.addActor(optionsEffectsVolumeLabel);
        optionsButtonGroup.addActor(optionsEffectsVolumeSlider);
        optionsButtonGroup.addActor(optionsMusicVolumeLabel);
        optionsButtonGroup.addActor(optionsMusicVolumeSlider);
        optionsButtonGroup.addActor(optionsBackButton);
        optionsEffectsVolumeSlider.setValue(screen.getEffectsVolume());
        optionsMusicVolumeSlider.setValue(screen.getMusicVolume());
        optionsButtonGroup.space(20);

        setupListeners();

        resetGui(stage);

        table.setBackground(pauseMenuDrawable);
        table.add(pauseButtonGroup);
        table.setVisible(false);
        table.setWidth(500);
        table.setHeight(450);
        table.center();
        table.setPosition(stage.getWidth()/2,stage.getHeight()/2, center);

        stage.addActor(table);

    }

    private void setupListeners() {

        /* Listener for the resume button. */
        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // ToDo: restart game state.
                screen.menuBlipSound();
                hide();
            }
        });

        /* Listener for the options button. */
        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen.menuBlipSound();
                state = States.OPTIONS;
                resetGui(stage);
            }
        });

        /* Listener for the save button. */
        saveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //TODO
            }
        });

        /* Listener for the exit button. */
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen.menuBlipSound();
                screen.exitToMenu();
            }
        });

        // Options State

        optionsEffectsVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen.setEffectsVolume(optionsEffectsVolumeSlider.getValue());
            }
        });

        optionsMusicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen.setMusicVolume(optionsMusicVolumeSlider.getValue());
            }
        });

        optionsBackButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen.menuBlipSound();
                state = States.PAUSE;
                resetGui(stage);
            }
        });
    }

    private void resetGui(Stage stage) {
        table.reset();
        table.setWidth(500);
        table.setHeight(450);
        table.center();
        table.setPosition(stage.getWidth()/2,stage.getHeight()/2, center);

        switch (state) {
            case PAUSE:
                table.add(pauseButtonGroup).expandX().center();
                break;
            case OPTIONS:
                table.add(optionsButtonGroup).expandX().center();
                break;
            default:
                LOGGER.error("Failed to find pause menu state.");
                break;
        }
    }

    /**
     * Adjusts this gui's position to correct for any resize event.
     *
     * @param stage
     */
    @Override
    public void resize(Stage stage) {
        super.resize(stage);

        resetGui(stage);
    }

    @Override
	public void show() {
        table.setVisible(true);

        stage.addActor(table);
    }

    @Override
	public void hide() {
        table.setVisible(false);
    }


}
