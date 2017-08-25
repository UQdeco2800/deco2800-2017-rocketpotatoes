package com.deco2800.potatoes.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
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
    private TextButton multiplayerConnectButton;
    private TextButton multiplayerHostButton;
    private TextButton multiplayerBackButton;

    private VerticalGroup optionsButtonGroup;
    private Label optionsVolumeLabel;
    private Slider optionsVolumeSlider;
    private TextButton optionsBackButton;

    // State indicator
    private enum States {
        PRIMARY,
        START_GAME,
        START_MULTIPLAYER,
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
        multiplayerConnectButton = new TextButton("Connect", uiSkin);
        multiplayerHostButton = new TextButton("Host", uiSkin);
        multiplayerBackButton = new TextButton("Back", uiSkin);

        startMultiplayerButtonGroup = new VerticalGroup();
        startMultiplayerButtonGroup.addActor(multiplayerConnectButton);
        startMultiplayerButtonGroup.addActor(multiplayerHostButton);
        startMultiplayerButtonGroup.addActor(multiplayerBackButton);

        // Options State
        optionsVolumeLabel = new Label("Volume", uiSkin);
        optionsVolumeSlider = new Slider(0f,1f,0.01f,false, uiSkin);
        optionsBackButton = new TextButton("Back", uiSkin);

        optionsButtonGroup = new VerticalGroup();
        optionsButtonGroup.addActor(optionsVolumeLabel);
        optionsButtonGroup.addActor(optionsVolumeSlider);
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
                state = States.START_GAME;
                resetGui(stage);
            }
        });

        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // TODO
                state = States.OPTIONS;
                resetGui(stage);
            }
        });

        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Todo nicer exit?
                System.exit(0);
            }
        });

        // Start state
        singleplayerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // TODO loading etc
                mainMenuScreen.startSinglePlayer();
            }
        });

        multiplayerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                state = States.START_MULTIPLAYER;
                resetGui(stage);
            }
        });

        startBackButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                state = States.PRIMARY;
                resetGui(stage);
            }
        });

        // Multiplayer start state

        multiplayerConnectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainMenuScreen.startMultiplayer("Bob", "127.0.0.1",1337, false);
            }
        });

        multiplayerHostButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainMenuScreen.startMultiplayer("Fred", "",1337, true);
            }
        });

        multiplayerBackButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                state = States.START_GAME;
                resetGui(stage);
            }
        });

        optionsVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainMenuScreen.setVolume(optionsVolumeSlider.getValue());
            }
        });

        optionsBackButton.addListener(new ChangeListener() {
           @Override
           public void changed(ChangeEvent event, Actor actor) {
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
