package com.deco2800.potatoes.managers;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.deco2800.potatoes.gui.Gui;

import java.util.ArrayList;
import java.util.List;

public class GuiManager extends Manager {
    private List<Gui> gui;
    private Stage stage;

    /**
     * Initialize the basic GuiManager. Just creates the internal gui storage, and the internal stage
     */
    public GuiManager() {
        gui = new ArrayList<>();
        stage = new Stage(new ScreenViewport());
    }

    /**
     * Add's a gui element to be tracked by the GuiManager. Duplicates are not well supported
     * TODO better support for duplicates
     * @param g the gui element
     */
    public void addGui(Gui g) {
        gui.add(g);
    }

    /**
     * Get's the Gui of the given type, if it exists. Doesn't work for duplicates.
     * If the Gui doesn't exist in the manager, then this returns null.
     * @param type the type e.g. 'Type.class'
     * @return the gui element, or null
     */
    public Gui getGui(Class<?> type) {
        /* Find gui */
        for (Gui g : gui) {
            if (g.getClass() == type) {
                return g;
            }
        }

		/* If it doesn't exist, we return null. TODO log this? error? */

        return null;
    }

    /**
     * @return the internal stage variable
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Resizes the stage appropriately and also
     * call's resize(...) on all Gui's. Allowing them to appropriately adjust to the new window size.
     */
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

        for (Gui c : gui) {
            c.resize(stage);
        }
    }
}
