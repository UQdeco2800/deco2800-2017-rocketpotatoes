package com.deco2800.potatoes.gui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.potatoes.entities.Resource;
import com.deco2800.potatoes.managers.*;
import com.deco2800.potatoes.screens.GameScreen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Set;

import static com.badlogic.gdx.utils.Align.center;
import static com.badlogic.gdx.utils.Align.left;

public class DebugModeGui extends Gui {
    private static final Logger LOGGER = LoggerFactory.getLogger(DebugModeGui.class);

    private GameScreen screen;
    private Stage stage;

    // Buttons
    private Skin uiSkin;
    private VerticalGroup debugButtonGroup;
    private Label debugOn;
    private TextButton resetButton;
    private TextButton addResourcesButton;
    private TextButton spawnButton;
    private TextButton immortalButton;
    private TextButton exitButton;
    private Table table;

    // State indicator
    private enum States {
        DEBUGON,
        DEBUGOFF
    }

    private States state = States.DEBUGON;

    public DebugModeGui(Stage stage, GameScreen screen){
        this.screen = screen;
        this.stage = stage;
        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
        table = new Table(uiSkin);

        // actors initialisation
        debugOn = new Label("Debug Options",uiSkin);
        resetButton = new TextButton("Reset", uiSkin);
        addResourcesButton = new TextButton("Add Resources", uiSkin);
        spawnButton = new TextButton("Spawn", uiSkin);
        immortalButton = new TextButton("Immortality", uiSkin);
        exitButton = new TextButton("Exit Debug", uiSkin);

        // adding actors
        debugButtonGroup = new VerticalGroup();
        debugButtonGroup.addActor(debugOn);
        debugButtonGroup.addActor(resetButton);
        debugButtonGroup.addActor(addResourcesButton);
        debugButtonGroup.addActor(spawnButton);
        debugButtonGroup.addActor(immortalButton);
        debugButtonGroup.addActor(exitButton);
        table.add(debugButtonGroup);

        setupListeners();

        resetGui(stage);
        table.setVisible(false);
        //resetGui(stage);

        stage.addActor(table);
    }

    private void resetGui(Stage stage) {

        table.reset();
        table.left();
        table.setPosition(0,stage.getHeight()/2, left);
        table.add(debugButtonGroup);
        //table.setDebug(true);

    }

    private void setupListeners() {

        /* Listener for the exit debug button. */
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hide();
            }
        });

        /* Listener for the immortality button */
        immortalButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen.menuBlipSound();
                ((PlayerManager) GameManager.get().getManager(PlayerManager.class)).getPlayer().heal(200);
                ((PlayerManager) GameManager.get().getManager(PlayerManager.class)).getPlayer().addDamageScaling(0);
            }
        });

        /* Listener for the add resources button */
        addResourcesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Set<Resource> rsc = ((PlayerManager)GameManager.get().getManager(PlayerManager.class)).getPlayer().getInventory().getInventoryResources();
                for (Resource rscname: rsc) {
                    ((PlayerManager) GameManager.get().getManager(PlayerManager.class)).getPlayer().getInventory().updateQuantity(rscname, 10);
                }
            }
        });
    }

    public void show() {
        table.setVisible(true);
        stage.addActor(table);
    }

    public void hide() {
        table.setVisible(false);
    }
}
