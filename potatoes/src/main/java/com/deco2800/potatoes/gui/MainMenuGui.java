package com.deco2800.potatoes.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.managers.PlayerManager.PlayerType;
import com.deco2800.potatoes.managers.TextureManager;
import com.deco2800.potatoes.screens.MainMenuScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainMenuGui extends Gui {
    private MainMenuScreen mainMenuScreen;

    private static final Logger LOGGER = LoggerFactory.getLogger(MainMenuGui.class);

    private TextureManager textureManager;

    private Stage stage;
    private Skin uiSkin;

    // Standard sizings and spacing for widgets in the main menu.
    private int buttonHeight;
    private int buttonWidth;
    private float buttonSpacing;

    // Root table for this entire element
    private Table root;

    // Elements on opening screen.
    private Table primaryButtons;
    private TextButton startButton;
    private TextButton optionsButton;
    private TextButton exitButton;

    // Elements in start menu.
    private Table startButtonGroup;
    private Table startCharacterSelectTable;
    private Image startCharacterImage;
    private SelectBox<String> startCharacterSelect;
    private TextButton singleplayerButton;
    private TextButton startBackButton;

    // Elements in options menu.
    private Table optionsButtonGroup;
    private Table effectsButtonGroup;
    private Table musicButtonGroup;
    private Label optionsEffectsVolumeLabel;
    private Slider optionsEffectsVolumeSlider;
    private Label optionsMusicVolumeLabel;
    private Slider optionsMusicVolumeSlider;
    private TextButton optionsBackButton;

    // State indicator
    private enum States {
        PRIMARY,
        START_GAME,
        OPTIONS
    }

    private States state = States.PRIMARY;

    public MainMenuGui(Stage stage, MainMenuScreen screen) {
        this.stage = stage;
        this.mainMenuScreen = screen;

        uiSkin = new Skin(Gdx.files.internal("menu/uiskin.json"));
        textureManager = GameManager.get().getManager(TextureManager.class);

        buttonHeight = (int) stage.getHeight()/10;
        buttonWidth = (int) stage.getWidth()/7;
        buttonSpacing = (float) stage.getWidth()/20;

        // State 1
        startButton = new TextButton("Start", uiSkin);
        optionsButton = new TextButton("Options", uiSkin);
        exitButton = new TextButton("Exit", uiSkin);

        primaryButtons = new Table();
        primaryButtons.add(startButton).width(buttonWidth).height(buttonHeight).space(buttonSpacing);
        primaryButtons.add(optionsButton).width(buttonWidth).height(buttonHeight).space(buttonSpacing);
        primaryButtons.add(exitButton).width(buttonWidth).height(buttonHeight).space(buttonSpacing).right();

        // Start state
        startCharacterImage = new Image(new TextureRegion(textureManager.getTexture("caveman_idle_SW_1")));
        startCharacterSelect = new SelectBox<String>(uiSkin);
        startCharacterSelect.setItems(capitalisePlayerTypes(PlayerType.names()));
        singleplayerButton = new TextButton("Start Game", uiSkin);
        startBackButton = new TextButton("Back", uiSkin);

        startButtonGroup = new Table();
        startCharacterSelectTable = new Table();
        startCharacterSelectTable.add(startCharacterImage).size(125, 125);
        startCharacterSelectTable.add(startCharacterSelect).width(buttonWidth - 50).height(buttonHeight/2);
        startButtonGroup.add(startCharacterSelectTable);
        startButtonGroup.add(singleplayerButton).width(buttonWidth).height(buttonHeight).space(buttonSpacing);
        startButtonGroup.add(startBackButton).width(buttonWidth).height(buttonHeight).space(buttonSpacing).right();
        startCharacterSelect.setSelected(GameManager.get().getManager(PlayerManager.class).getPlayerType().name());

        // Options State
        optionsEffectsVolumeLabel = new Label("SFX Volume", uiSkin);
        optionsEffectsVolumeSlider = new Slider(0f,1f,0.01f,false, uiSkin);
        optionsMusicVolumeLabel = new Label("Music Volume", uiSkin);
        optionsMusicVolumeSlider = new Slider(0f,1f,0.01f,false, uiSkin);
        optionsBackButton = new TextButton("Back", uiSkin);

        optionsButtonGroup = new Table();
        effectsButtonGroup = new Table();
        musicButtonGroup = new Table();
        effectsButtonGroup.add(optionsEffectsVolumeLabel);
        effectsButtonGroup.row();
        effectsButtonGroup.add(optionsEffectsVolumeSlider);
        musicButtonGroup.add(optionsMusicVolumeLabel);
        musicButtonGroup.row();
        musicButtonGroup.add(optionsMusicVolumeSlider);
        optionsButtonGroup.add(effectsButtonGroup).space(buttonSpacing);
        optionsButtonGroup.add(musicButtonGroup).space(buttonSpacing);
        optionsButtonGroup.add(optionsBackButton).width(buttonWidth).height(buttonHeight).space(buttonSpacing).right();
        optionsEffectsVolumeSlider.setValue(mainMenuScreen.getEffectsVolume());
        optionsMusicVolumeSlider.setValue(mainMenuScreen.getMusicVolume());
        setupListeners();

        root = new Table(uiSkin);

        resetGui(stage);
        stage.addActor(root);
    }

    /* Setup the listeners for the widgets for all states of the screen. */
    private void setupListeners() {

        // Primary State

        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainMenuScreen.menuBlipSound();
                state = States.START_GAME;
                resetGui(stage);
            }
        });

        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainMenuScreen.menuBlipSound();
                state = States.OPTIONS;
                resetGui(stage);
            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainMenuScreen.menuBlipSound();
                System.exit(0);
            }
        });

        // Start State

        startCharacterSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.get().getManager(PlayerManager.class).setPlayerType(PlayerType.valueOf(startCharacterSelect.getSelected().toUpperCase()));
                startCharacterImage.setDrawable(new TextureRegionDrawable(new TextureRegion(textureManager.getTexture(startCharacterSelect.getSelected().toLowerCase()+"_idle_SW_1"))));
            }
        });

        singleplayerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainMenuScreen.menuBlipSound();
                mainMenuScreen.startSinglePlayer();
            }
        });

        startBackButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainMenuScreen.menuBlipSound();
                state = States.PRIMARY;
                resetGui(stage);
            }
        });

        optionsEffectsVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainMenuScreen.setEffectsVolume(optionsEffectsVolumeSlider.getValue());
            }
        });

        optionsMusicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainMenuScreen.setMusicVolume(optionsMusicVolumeSlider.getValue());
            }
        });

        optionsBackButton.addListener(new ChangeListener() {
           @Override
           public void changed(ChangeEvent event, Actor actor) {
               mainMenuScreen.menuBlipSound();
               state = States.PRIMARY;
               resetGui(stage);
           }
        });
    }

    /**
     * Hide's this Gui element. Fadeout effects can be implemented on a case-by-case basis.
     */
    @Override
    public void hide() {
        super.hide();

        root.remove();
    }

    /**
     * Show's this Gui element. Fadein effects can be implemented on a case-by-case basis.
     */
    @Override
    public void show() {
        super.show();

        stage.addActor(root);
    }

    private void resetGui(Stage stage) {
        root.reset();
        root.center();
        root.setWidth(stage.getWidth());
        root.setHeight(stage.getHeight()/2);
        root.setPosition(0, -45);

        switch (state) {
            case PRIMARY:
                root.add(primaryButtons).expandX().center();
                break;
            case START_GAME:
                root.add(startButtonGroup).expandX().center();
                break;
            case OPTIONS:
                root.add(optionsButtonGroup).expandX().center();
                break;
            default:
                LOGGER.error("Failed to find main menu state.");
                break;
        }
    }

    /**
     * Adjusts this gui's position to correct for any resize event.
     * @param stage
     */
    @Override
    public void resize(Stage stage) {
        super.resize(stage);

        resetGui(stage);
    }

    /**
     *
     */
    public void addSingleplayerStartListener(EventListener e) {
        singleplayerButton.addListener(e);
    }

    /* Makes the list of player types display nice. */
    private static Array<String> capitalisePlayerTypes(Array<String> playerTypes) {
        Array<String> capitalisedPayerTypes = new Array<> ();
        for (String lowerCaseName : playerTypes) {
            String tempStr = lowerCaseName.substring(0, 1).toUpperCase();
            String nameCapitalised = tempStr + lowerCaseName.substring(1);
            capitalisedPayerTypes.add(nameCapitalised);
        }
        return capitalisedPayerTypes;
    }

}
