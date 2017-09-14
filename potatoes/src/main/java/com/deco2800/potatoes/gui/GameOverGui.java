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

public class GameOverGui extends Gui {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameOverGui.class);

    private GameScreen screen;
    private Stage stage;

    // Buttons
    private Skin uiSkin;
    private Drawable pauseMenuDrawable;
    private Label gameOverLabel;

    private Table table;

    public GameOverGui(Stage stage, GameScreen screen) {
        this.screen = screen;
        this.stage = stage;

        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
        table = new Table(uiSkin);

        gameOverLabel = new Label("GAME OVER",uiSkin);

        table.setBackground(pauseMenuDrawable);
        table.add(gameOverLabel);
        table.setVisible(false);
        table.setWidth(500);
        table.setHeight(450);
        table.center();
        table.setPosition(stage.getWidth()/2,stage.getHeight()/2, center);

        stage.addActor(table);

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