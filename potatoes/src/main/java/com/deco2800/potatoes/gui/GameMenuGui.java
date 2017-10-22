package com.deco2800.potatoes.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.potatoes.entities.Selectable;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.GuiManager;
import com.deco2800.potatoes.renderering.Renderable;
import com.deco2800.potatoes.screens.GameScreen;

public class GameMenuGui extends Gui {
    private GameScreen screen;

    // Buttons
    private Skin uiSkin;
    private Button pauseMenuButton;
    private Button selectButton;
    private Window window;

    public GameMenuGui(Stage stage, GameScreen screen) {
        hidden = false;
        this.screen = screen;

        // Make window, with the given skin
        uiSkin = new Skin(Gdx.files.internal("menu/uiskin.json"));
        window = new Window("Menu", uiSkin);

		// Make our buttons
        pauseMenuButton = new TextButton("Menu", uiSkin);
        selectButton = new TextButton("Select a Unit", uiSkin);

		/* Add a listener to the pause menu button */
        pauseMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen.menuBlipSound();
                GameManager.get().getManager(GuiManager.class).getGui(PauseMenuGui.class).toggle();
                GameManager.get().getManager(GuiManager.class).getGui(TreeShopGui.class).closeShop();
            }
        });

		/* Add listener for peon button */
        selectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for (Renderable r : GameManager.get().getWorld().getEntities().values()) {
                    if (r instanceof Selectable && ((Selectable) r).isSelected()) {
                        // Did this ever work?

                    }
                }
            }
        });

		/* Add all buttons to the menu
		*   Note: this is left to right order
		* */
        window.add(pauseMenuButton);
        window.pack();
        window.setMovable(false); // So it doesn't fly around the screen
        window.setPosition(0, stage.getHeight()); // Place it in the top left of the screen

        window.setMovable(true);

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
