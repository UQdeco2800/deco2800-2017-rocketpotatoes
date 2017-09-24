package com.deco2800.potatoes.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;

/**
 * WaveGui provides a gui displaying the status of the enemy waves
 * GameMenuGUI was used as a template and is following the same style
 * which is likely a placeholder style.
 *
 * @author craig
 */
public class WaveGui extends Gui {

    // Gui elements
    private Skin uiSkin;
    private Label waveStatusLabel;
    private Label waveTimeLabel;
    private Window window;

    /**
     * Construct a new WaveGui element for the current stage.
     *
     * @param stage the stage to place the WaveGui.
     */
    public WaveGui(Stage stage) {
        hidden = false;

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
        window.setPosition(stage.getWidth()/2, stage.getHeight());

        stage.addActor(window);

    }

    /**
     * @return this gui's Window
     */
    public Window getWaveGuiWindow() { return window; }

    /**
     * @return the Label describing the status of waves at the moment
     */
    public Label getWaveStatusLabel() { return waveStatusLabel; }

    /**
     * @return The Label indicating the time of the current wave stage
     */
    public Label getWaveTimeLabel() { return waveTimeLabel; }

}
