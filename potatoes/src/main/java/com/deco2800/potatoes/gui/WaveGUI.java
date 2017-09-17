package com.deco2800.potatoes.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.potatoes.entities.Selectable;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.GuiManager;
import com.deco2800.potatoes.renderering.Renderable;
import com.deco2800.potatoes.screens.GameScreen;

/*
    Used GameMenuGUI as a template
* */
public class WaveGUI extends Gui {
    private GameScreen screen;

    // Buttons
    private Skin uiSkin;
    private Label waveStatusLabel;
    private Label waveTimeLabel;
    private Window window;

    public WaveGUI(Stage stage, GameScreen screen) {
        hidden = false;
        this.screen = screen;

        // Make window with skin
        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
        window = new Window("Wave info", uiSkin);

        // Make buttons & labels
        waveStatusLabel = new Label("Time until wave ends", uiSkin);
        waveTimeLabel = new Label("0", uiSkin);

        //window

        window.add(waveStatusLabel);
        window.add(waveTimeLabel);
        window.pack();
        window.setMovable(false);
        window.setPosition(stage.getWidth(), 0);

        stage.addActor(window);

    }


    public Label getWaveStatusLabel() { return waveStatusLabel; }

    public Label getWaveTimeLabel() { return waveTimeLabel; }

}
