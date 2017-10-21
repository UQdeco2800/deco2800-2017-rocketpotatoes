package com.deco2800.potatoes.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.potatoes.managers.*;
import com.deco2800.potatoes.screens.GameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.badlogic.gdx.utils.Align.center;

/**
 * Respawn Gui
 * A text that shows the timer left until the player respawns
 *
 *@author Goh Zen Hao
 */
public class RespawnGui extends Gui{

    private static final Logger LOGGER = LoggerFactory.getLogger(RespawnGui.class);

    private GameScreen screen;
    private Stage stage;

    // Buttons
    private Skin uiSkin;
    private Label deadLabel;
    private Label respawnLabel;
    private Label timer;
    private Table table;

    private int count = 1;


    public RespawnGui(Stage stage, GameScreen screen) {

        this.screen = screen;
        this.stage = stage;

        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
        table = new Table(uiSkin);

        deadLabel = new Label("YOU ARE DEAD. ", uiSkin);

        respawnLabel = new Label("Respawning in ", uiSkin);

        timer = new Label("5",uiSkin);

        //table

        deadLabel.setColor(Color.RED);
        respawnLabel.setColor(Color.RED);
        timer.setColor(Color.RED);
        deadLabel.setFontScale(1.2f);
        respawnLabel.setFontScale(1.2f);
        timer.setFontScale(1.4f);
        table.add(deadLabel);
        table.add(respawnLabel);
        table.add(timer);
        table.setVisible(false);
        table.setHeight(200);
        table.setWidth(500);
        table.setPosition(stage.getWidth()/2,stage.getHeight()/2,center);

        stage.addActor(table);

    }


    public Label returnTimer(){
        return this.timer;
    }

    public int returnCount(){
        return this.count;
    }

    public void setCount(int count){
        this.count = count;
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
