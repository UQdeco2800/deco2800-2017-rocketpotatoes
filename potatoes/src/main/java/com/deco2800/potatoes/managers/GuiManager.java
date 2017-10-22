package com.deco2800.potatoes.managers;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.potatoes.gui.FadingGui;
import com.deco2800.potatoes.gui.Gui;

import java.util.ArrayList;
import java.util.List;

public class GuiManager extends Manager {
    private List<Gui> gui;
    private Stage stage;
    private List<FadingGui> fadingGui;

    /**
     * Initialize the basic GuiManager. Just creates the internal gui storage
     */
    public GuiManager() {
        gui = new ArrayList<>();
        fadingGui = new ArrayList<>();
    }

    /**
     * Set's the internal stage to operate on
     * @param stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Add's a gui element to be tracked by the GuiManager. Duplicates are not well supported
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
    @SuppressWarnings("unchecked")
	public <G extends Gui> G getGui(Class<G> type) {
        /* Find gui */
        for (Gui g : gui) {
            if (g.getClass() == type) {
                return (G) g;
            }
        }
        

        return null;
    }

    public void addFadingGui(FadingGui fadingGui) {
        this.fadingGui.add(fadingGui);
    }

    public void removeFadingGui(FadingGui fadingGui) {
        this.fadingGui.remove(fadingGui);
    }

    public void tickFadingGuis(long tick) {
        for (FadingGui gui : fadingGui) {
            if (gui.getTimer() < 0) {
                fadingGui.remove(gui);
                return;
            }
            gui.onTick(tick);
        }
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
        if (stage != null) {
            stage.getViewport().update(width, height, true);

            for (Gui c : gui) {
                c.resize(stage);
            }
        }
    }
    
}
