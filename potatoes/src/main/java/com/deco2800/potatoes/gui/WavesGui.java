package com.deco2800.potatoes.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

/**
 * WavesGui provides a gui displaying the status of the enemy waves
 * GameMenuGUI was used as a template and is following the same style
 * which is likely a placeholder style.
 *
 * @author craig
 */
public class WavesGui extends Gui {

    // Gui elements
    private Skin uiSkin;
    private Label waveStatusLabel;
    private Label waveTimeLabel;
    private Label waveTotalAmountLabel;
    private Label waveEnemiesLabel;
    private Window window;
    private Window enemyAmountWindow;

    /**
     * Construct a new WavesGui element for the current stage.
     *
     * @param stage the stage to place the WavesGui.
     */
    public WavesGui(Stage stage) {
        hidden = false;

        // Make window with skin
        uiSkin = new Skin(Gdx.files.internal("menu/uiskin.json"));
        window = new Window("Wave info", uiSkin);

        //add a window for amount of enemies
        enemyAmountWindow = new Window(" Enemies in the forest ", uiSkin);
        enemyAmountWindow.setWidth(30);
        
        // Make buttons & labels
        waveStatusLabel = new Label("Time until wave ends", uiSkin);
        waveTimeLabel = new Label("0", uiSkin);
        waveTotalAmountLabel = new Label("Total Enemies: ",uiSkin);
        waveEnemiesLabel = new Label("0", uiSkin);


        //window
        window.add(waveStatusLabel);
        window.add(waveTimeLabel);
        window.pack();
        window.setMovable(true);
        window.setPosition(stage.getWidth()/2, stage.getHeight());
        
        enemyAmountWindow.add(waveTotalAmountLabel);
        enemyAmountWindow.add(waveEnemiesLabel);
        enemyAmountWindow.pack();
        enemyAmountWindow.setMovable(true);
        enemyAmountWindow.setPosition(stage.getWidth()/2.8f, stage.getHeight());

        stage.addActor(window);
        stage.addActor(enemyAmountWindow);
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
    
    /**
     * @return the label indicating the total enemy amount of the current wave  
     */
    public Label getWaveTotalAmountLabel() { return waveTotalAmountLabel;}
    
    /**
     * @return the label indicating the enemy amount of the current wave stage 
     */
    public Label getWaveEnemiesLabel() { return waveEnemiesLabel; }
 
}
