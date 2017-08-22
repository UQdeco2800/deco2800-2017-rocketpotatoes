package com.deco2800.potatoes.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class MainMenuGui extends Gui {
    private Stage stage;
    private Skin uiSkin;

    // Root table for this entire element
    private Table root;
    private VerticalGroup primaryButtons;
    private TextButton startButton;
    private TextButton optionsButton;
    private TextButton quitButton;

    public MainMenuGui(Stage stage) {
        this.stage = stage;
        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));

        startButton = new TextButton("Start Game", uiSkin);
        optionsButton = new TextButton("Options", uiSkin);
        quitButton = new TextButton("Quit", uiSkin);

        primaryButtons = new VerticalGroup();
        primaryButtons.addActor(startButton);
        primaryButtons.addActor(optionsButton);
        primaryButtons.addActor(quitButton);

        root = new Table(uiSkin);

        resetGui(stage);
        stage.addActor(root);
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
        root.debugAll();
        root.add(primaryButtons);
        root.setWidth(stage.getWidth());
        root.setHeight(stage.getHeight());
        root.setPosition(0, 0);
        root.layout();
        root.pack();
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
}
