package com.deco2800.potatoes.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.potatoes.entities.Selectable;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.GuiManager;
import com.deco2800.potatoes.managers.SoundManager;
import com.deco2800.potatoes.renderering.Renderable;
import com.deco2800.potatoes.screens.GameScreen;

public class GameMenuGui extends Gui {
    private GameScreen screen;

    // Buttons
    private Skin uiSkin;
    private Button quitButton;
    private Button duckSoundButton;
    private Button resetButton;
    private Button selectButton;
    private Button godModeButton;
    private Window window;

    public GameMenuGui(Stage stage, GameScreen screen) {
        hidden = false;
        this.screen = screen;

        // Make window, with the given skin
        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
        window = new Window("Menu", uiSkin);

		// Make our buttons
        quitButton = new TextButton("Pause", uiSkin);
        duckSoundButton = new TextButton("Play Duck Sound", uiSkin);
        resetButton = new TextButton("Exit to Main", uiSkin);
        selectButton = new TextButton("Select a Unit", uiSkin);
        godModeButton = new TextButton("God",uiSkin);

           /* Listener to godMode button */
        godModeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //Sound added just to alert a change in game state
                screen.menuBlipSound();
                ((DebugModeGui) ((GuiManager)GameManager.get().getManager(GuiManager.class)).getGui(DebugModeGui.class)).show();

            }
        });

		/* Add a programatic listener to the quit button */
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen.menuBlipSound();
                ((PauseMenuGui) ((GuiManager)GameManager.get().getManager(GuiManager.class)).getGui(PauseMenuGui.class)).show();

            }
        });

		/* Add a handler to play a sound */
        duckSoundButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((SoundManager)GameManager.get().getManager(SoundManager.class)).playSound("quack.wav");
            }
        });

		/* Add listener for peon button */
        selectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for (Renderable r : GameManager.get().getWorld().getEntities().values()) {
                    if (r instanceof Selectable) {
                        if (((Selectable) r).isSelected()) {
                            // Did this ever work?
                        }
                    }
                }
            }
        });

		/* Listener for reset button */
        resetButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen.exitToMenu();
            }
        });

		/* Add all buttons to the menu
		*   Note: this is left to right order
		* */
        window.add(resetButton);
        window.add(quitButton);
        window.add(duckSoundButton);
        window.add(selectButton);
        window.add(godModeButton);
        window.pack();
        window.setMovable(false); // So it doesn't fly around the screen
        window.setPosition(0, stage.getHeight()); // Place it in the top left of the screen

        // Finally add our Gui window to the stage
        stage.addActor(window);
    }

    /**
     * Adjusts this gui's position to correct for any resize event.
     *
     * @param stage
     */
    @Override
    public void resize(Stage stage) {
        super.resize(stage);
        window.setPosition(0, stage.getHeight());
    }
}
