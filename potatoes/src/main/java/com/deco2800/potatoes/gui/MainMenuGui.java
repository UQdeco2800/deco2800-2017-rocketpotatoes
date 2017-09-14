package com.deco2800.potatoes.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.TextureManager;
import com.deco2800.potatoes.screens.MainMenuScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainMenuGui extends Gui {
    private MainMenuScreen mainMenuScreen;

    private static final Logger LOGGER = LoggerFactory.getLogger(MainMenuGui.class);

    private Stage stage;
    private Skin uiSkin;

    // Root table for this entire element
    private Table root;
    private HorizontalGroup primaryButtons;
    private Drawable startDrawable;
    private Drawable optionsDrawable;
    private Drawable exitDrawable;
    private ImageButton startButton;
    private ImageButton optionsButton;
    private ImageButton exitButton;

    private HorizontalGroup startButtonGroup;
    private Drawable singleplayerDrawable;
    private Drawable multiplayerDrawable;
    private Drawable startBackDrawable;
    private ImageButton singleplayerButton;
    private ImageButton multiplayerButton;
    private ImageButton startBackButton;

    private VerticalGroup startMultiplayerButtonGroup;
    private TextButton multiplayerClientButton;
    private TextButton multiplayerHostButton;
    private TextButton multiplayerBackButton;

    private VerticalGroup multiplayerClientButtonGroup;
    private TextField multiplayerClientName;
    private TextField multiplayerClientIpAddConnection;
    private TextButton multiplayerClientConnectButton;
    private TextButton multiplayerClientBackButton;

    private VerticalGroup multiplayerHostButtonGroup;
    private Label multiplayerHostIpAddress;
    private TextField multiplayerHostName;
    private TextButton multiplayerHostConnectButton;
    private TextButton multiplayerHostBackButton;

    private VerticalGroup optionsButtonGroup;
    private Label optionsEffectsVolumeLabel;
    private Slider optionsEffectsVolumeSlider;
    private Label optionsMusicVolumeLabel;
    private Slider optionsMusicVolumeSlider;
    private CheckBox optionsFullscreenCheckbox;
    private CheckBox optionsColourblindCheckbox;
    private TextButton optionsBackButton;

    private Dialog failedMultiplayerConnection;

    // State indicator
    private enum States {
        PRIMARY,
        START_GAME,
        START_MULTIPLAYER,
        MULTIPLAYER_CLIENT,
        MULTIPLAYER_HOST,
        OPTIONS
    }

    private States state = States.PRIMARY;


    public MainMenuGui(Stage stage, MainMenuScreen screen) {
        this.stage = stage;
        this.mainMenuScreen = screen;

        uiSkin = new Skin(Gdx.files.internal("menu/uiskin.json"));
        // State 1
        // Make drawables from textures
        startDrawable = new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("startMainMenu")));
        optionsDrawable = new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("optionsMainMenu")));
        exitDrawable = new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("exitMainMenu")));
        startButton = new ImageButton(startDrawable);
        optionsButton = new ImageButton(optionsDrawable);
        exitButton = new ImageButton(exitDrawable);

        primaryButtons = new HorizontalGroup();
        primaryButtons.addActor(startButton);
        primaryButtons.addActor(optionsButton);
        primaryButtons.addActor(exitButton);

        // Start state
        singleplayerDrawable = new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("singleplayerMainMenu")));
        multiplayerDrawable = new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("multiplayerMainMenu")));
        startBackDrawable = new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("backMainMenu")));
        singleplayerButton = new ImageButton(singleplayerDrawable);
        multiplayerButton = new ImageButton(multiplayerDrawable);
        startBackButton = new ImageButton(startBackDrawable);

        startButtonGroup = new HorizontalGroup();
        startButtonGroup.addActor(singleplayerButton);
        startButtonGroup.addActor(multiplayerButton);
        startButtonGroup.addActor(startBackButton);

        // Start Multiplayer state
        multiplayerClientButton = new TextButton("Client", uiSkin);
        multiplayerHostButton = new TextButton("Host", uiSkin);
        multiplayerBackButton = new TextButton("Back", uiSkin);

        startMultiplayerButtonGroup = new VerticalGroup();
        startMultiplayerButtonGroup.addActor(multiplayerClientButton);
        startMultiplayerButtonGroup.addActor(multiplayerHostButton);
        startMultiplayerButtonGroup.addActor(multiplayerBackButton);

        // Multiplayer Client state
        multiplayerClientName = new TextField("Client Name", uiSkin);
        multiplayerClientIpAddConnection = new TextField(MainMenuScreen.multiplayerHostAddress(), uiSkin);
        multiplayerClientConnectButton = new TextButton("Connect", uiSkin);
        multiplayerClientBackButton = new TextButton("Back", uiSkin);

        multiplayerClientButtonGroup = new VerticalGroup();
        multiplayerClientButtonGroup.addActor(multiplayerClientName);
        multiplayerClientButtonGroup.addActor(multiplayerClientIpAddConnection);
        multiplayerClientButtonGroup.addActor(multiplayerClientConnectButton);
        multiplayerClientButtonGroup.addActor(multiplayerClientBackButton);

        // Multiplayer Host state
        multiplayerHostIpAddress = new Label("Host IP:  " + MainMenuScreen.multiplayerHostAddress(), uiSkin);
        multiplayerHostName = new TextField("Host Name", uiSkin);
        multiplayerHostConnectButton = new TextButton("Connect", uiSkin);
        multiplayerHostBackButton = new TextButton("Back", uiSkin);

        multiplayerHostButtonGroup = new VerticalGroup();
        multiplayerHostButtonGroup.addActor(multiplayerHostIpAddress);
        multiplayerHostButtonGroup.addActor(multiplayerHostName);
        multiplayerHostButtonGroup.addActor( multiplayerHostConnectButton);
        multiplayerHostButtonGroup.addActor(multiplayerHostBackButton);

        // Options State
        optionsEffectsVolumeLabel = new Label("SFX Volume", uiSkin);
        optionsEffectsVolumeSlider = new Slider(0f,1f,0.01f,false, uiSkin);
        optionsMusicVolumeLabel = new Label("Music Volume", uiSkin);
        optionsMusicVolumeSlider = new Slider(0f,1f,0.01f,false, uiSkin);
        optionsFullscreenCheckbox = new CheckBox("Fullscreen", uiSkin);
        optionsColourblindCheckbox = new CheckBox("Colour Blind", uiSkin);
        optionsBackButton = new TextButton("Back", uiSkin);

        optionsButtonGroup = new VerticalGroup();
        optionsButtonGroup.addActor(optionsEffectsVolumeLabel);
        optionsButtonGroup.addActor(optionsEffectsVolumeSlider);
        optionsButtonGroup.addActor(optionsMusicVolumeLabel);
        optionsButtonGroup.addActor(optionsMusicVolumeSlider);
        optionsButtonGroup.addActor(optionsFullscreenCheckbox);
        optionsButtonGroup.addActor(optionsColourblindCheckbox);
        optionsButtonGroup.addActor(optionsBackButton);
        optionsEffectsVolumeSlider.setValue(mainMenuScreen.getEffectsVolume());
        optionsMusicVolumeSlider.setValue(mainMenuScreen.getMusicVolume());

        // Dialog
        failedMultiplayerConnection = new Dialog("Failed to connect to host.", uiSkin);

        setupListeners();

        root = new Table(uiSkin);

        resetGui(stage);
        stage.addActor(root);
    }

    private void setupListeners() {
        // Primary state

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
                // Todo nicer exit?
                System.exit(0);
            }
        });

        // Start state
        singleplayerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainMenuScreen.menuBlipSound();
                // TODO loading etc
                mainMenuScreen.startSinglePlayer();
            }
        });

        multiplayerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainMenuScreen.menuBlipSound();
                state = States.START_MULTIPLAYER;
                resetGui(stage);
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

        // Multiplayer start state

        multiplayerClientButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainMenuScreen.menuBlipSound();
                state = States.MULTIPLAYER_CLIENT;
                resetGui(stage);
            }
        });

        multiplayerHostButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainMenuScreen.menuBlipSound();
                state = States.MULTIPLAYER_HOST;
                resetGui(stage);
            }
        });

        multiplayerBackButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainMenuScreen.menuBlipSound();
                state = States.START_GAME;
                resetGui(stage);
            }
        });


        // Multiplayer Client state

        multiplayerClientConnectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainMenuScreen.menuBlipSound();
                mainMenuScreen.startMultiplayer(multiplayerClientName.getText(),
                        multiplayerClientIpAddConnection.getText(),1337, false);
                //Todo handle failed connection
                //if (failedConnection) {
                    // show Dialog
                //}
            }
        });

        multiplayerClientBackButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainMenuScreen.menuBlipSound();
                state = States.START_MULTIPLAYER;
                resetGui(stage);
            }
        });

        // Multiplayer Host state

        multiplayerHostConnectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainMenuScreen.menuBlipSound();
                mainMenuScreen.startMultiplayer(multiplayerHostName.getText(),
                        MainMenuScreen.multiplayerHostAddress(),1337, true);
            }
        });

        multiplayerHostBackButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainMenuScreen.menuBlipSound();
                state = States.START_MULTIPLAYER;
                resetGui(stage);
            }
        });

        // Options State

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

        optionsFullscreenCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // TODO
            }
        });

        optionsColourblindCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // TODO
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
        root.setPosition(0, 0);

        switch (state) {
            case PRIMARY:
                root.add(primaryButtons).expandX().center();
                break;
            case START_GAME:
                root.add(startButtonGroup).expandX().center();
                break;
            case START_MULTIPLAYER:
                root.add(startMultiplayerButtonGroup).expandX().center();
                break;
            case MULTIPLAYER_CLIENT:
                root.add(multiplayerClientButtonGroup).expandX().center();
                break;
            case MULTIPLAYER_HOST:
                root.add(multiplayerHostButtonGroup).expandX().center();
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
     *
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
}
