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
import com.deco2800.potatoes.managers.SoundManager;
import com.deco2800.potatoes.renderering.Renderable;

public class GameMenuGui extends Gui {
    // Buttons
    private Skin uiSkin;
    private Button quitButton;
    private Button duckSoundButton;
    private Button resetButton;
    private Button selectButton;

    public GameMenuGui(Stage stage) {
        hidden = false;

        // Make window, with the given skin
        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
        window = new Window("Menu", uiSkin);

		// Make our buttons
        quitButton = new TextButton("Quit", uiSkin);
        duckSoundButton = new TextButton("Play Duck Sound", uiSkin);
        resetButton = new TextButton("Reset", uiSkin);
        selectButton = new TextButton("Select a Unit", uiSkin);

		/* Add a programatic listener to the quit button */
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.exit(0);
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
                // Ayy broken
            }
        });

		/* Add all buttons to the menu
		*   Note: this is left to right order
		* */
        window.add(quitButton);
        window.add(duckSoundButton);
        window.add(resetButton);
        window.add(selectButton);
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
