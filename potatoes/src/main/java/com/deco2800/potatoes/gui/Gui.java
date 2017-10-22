package com.deco2800.potatoes.gui;

import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Describes a single GUI. More should be added here when needed.
 */
public class Gui {
    protected boolean hidden;

    /**
     * Redraws this gui element
     * @param deltaTime deltaTime for animations/effects that are relative to game speed.
     */
    public void render(float deltaTime) {
        // Nothin
    }

    /**
     * Hide's this Gui element. Fadeout effects can be implemented on a case-by-case basis.
     */
    public void hide() {
        hidden = true;
    }

    /**
     * Show's this Gui element. Fadein effects can be implemented on a case-by-case basis.
     */
    public void show() {
        hidden = false;
    }

    /**
     * @return if this element is hidden
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Adjusts this gui's position to correct for any resize event.
     * @param stage
     */
    public void resize(Stage stage) {
    }
}
