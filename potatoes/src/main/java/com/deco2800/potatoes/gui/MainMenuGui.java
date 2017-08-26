package com.deco2800.potatoes.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.potatoes.screens.MainMenuScreen;

public class MainMenuGui extends Gui {
    private MainMenuScreen mainMenuScreen;

    private Stage stage;
    private Skin uiSkin;

    // Root table for this entire element
    private Table root;
    private VerticalGroup primaryButtons;
    private TextButton startButton;
    private TextButton optionsButton;
    private TextButton quitButton;

    private VerticalGroup startButtonGroup;
    private TextButton singleplayerButton;
    private TextButton multiplayerButton;
    private TextButton startBackButton;

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
    private Label optionsMasterVolumeLabel;
    private Slider optionsMasterVolumeSlider;
    private Label optionsMusicVolumeLabel;
    private Slider optionsMusicVolumeSlider;
    private CheckBox optionsFullscreenCheckbox;
    private CheckBox optionsColourblindCheckbox;
    private TextButton optionsBackButton;

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

        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));

        // State 1
        startButton = new TextButton("Start Game", uiSkin);
        optionsButton = new TextButton("Options", uiSkin);
        quitButton = new TextButton("Quit", uiSkin);

        primaryButtons = new VerticalGroup();
        primaryButtons.addActor(startButton);
        primaryButtons.addActor(optionsButton);
        primaryButtons.addActor(quitButton);

        // Start state
        singleplayerButton = new TextButton("Singleplayer Game", uiSkin);
        multiplayerButton = new TextButton("Multiplayer Game", uiSkin);
        startBackButton = new TextButton("Back", uiSkin);

        startButtonGroup = new VerticalGroup();
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
        optionsMasterVolumeLabel = new Label("Master Volume", uiSkin);
        optionsMasterVolumeSlider = new Slider(0f,1f,0.01f,false, uiSkin);
        optionsMusicVolumeLabel = new Label("Music Volume", uiSkin);
        optionsMusicVolumeSlider = new Slider(0f,1f,0.01f,false, uiSkin);
        optionsFullscreenCheckbox = new CheckBox("Fullscreen", uiSkin);
        optionsColourblindCheckbox = new CheckBox("Colour Blind", uiSkin);
        optionsBackButton = new TextButton("Back", uiSkin);

        optionsButtonGroup = new VerticalGroup();
        optionsButtonGroup.addActor(optionsMasterVolumeLabel);
        optionsButtonGroup.addActor(optionsMasterVolumeSlider);
        optionsButtonGroup.addActor(optionsMusicVolumeLabel);
        optionsButtonGroup.addActor(optionsMusicVolumeSlider);
        optionsButtonGroup.addActor(optionsFullscreenCheckbox);
        optionsButtonGroup.addActor(optionsColourblindCheckbox);
        optionsButtonGroup.addActor(optionsBackButton);

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
                MainMenuScreen.menuBlipSound();
                state = States.START_GAME;
                resetGui(stage);
            }
        });

        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MainMenuScreen.menuBlipSound();
                state = States.OPTIONS;
                resetGui(stage);
            }
        });

        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MainMenuScreen.menuBlipSound();
                // Todo nicer exit?
                System.exit(0);
            }
        });

        // Start state
        singleplayerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MainMenuScreen.menuBlipSound();
                // TODO loading etc
                mainMenuScreen.startSinglePlayer();
            }
        });

        multiplayerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MainMenuScreen.menuBlipSound();
                state = States.START_MULTIPLAYER;
                resetGui(stage);
            }
        });

        startBackButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MainMenuScreen.menuBlipSound();
                state = States.PRIMARY;
                resetGui(stage);
            }
        });

        // Multiplayer start state

        multiplayerClientButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MainMenuScreen.menuBlipSound();
                state = States.MULTIPLAYER_CLIENT;
                resetGui(stage);
            }
        });

        multiplayerHostButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MainMenuScreen.menuBlipSound();
                state = States.MULTIPLAYER_HOST;
                resetGui(stage);
            }
        });

        multiplayerBackButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MainMenuScreen.menuBlipSound();
                state = States.START_GAME;
                resetGui(stage);
            }
        });


        // Multiplayer Client state

        multiplayerClientConnectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MainMenuScreen.menuBlipSound();
                mainMenuScreen.startMultiplayer(multiplayerClientName.getText(),
                        multiplayerClientIpAddConnection.getText(),1337, false);
            }
        });

        multiplayerClientBackButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MainMenuScreen.menuBlipSound();
                state = States.START_MULTIPLAYER;
                resetGui(stage);
            }
        });

        // Multiplayer Host state

        multiplayerHostConnectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MainMenuScreen.menuBlipSound();
                mainMenuScreen.startMultiplayer(multiplayerHostName.getText(),
                        MainMenuScreen.multiplayerHostAddress(),1337, true);
            }
        });

        multiplayerHostBackButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MainMenuScreen.menuBlipSound();
                state = States.START_MULTIPLAYER;
                resetGui(stage);
            }
        });

        // Options State

        optionsMasterVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainMenuScreen.setMasterVolume(optionsMasterVolumeSlider.getValue());
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
               MainMenuScreen.menuBlipSound();
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
        //root.debugAll();
        root.center();
        root.setWidth(stage.getWidth());
        root.setHeight(stage.getHeight());
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
