package com.deco2800.potatoes.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Window;

/**
 * Describes a single GUI. More should be added here when needed
 *
 * Gui's each have a Window, this parameter should be initialized within the constructor of the Gui element.
 * The constructor should also take the game Stage, and then add the window to the Stage once initialization is complete.
 */
public interface Gui {

    /**
     * @return the internal window element
     */
    Window getWindow();

    /**
     * Redraws this gui element
     * @param deltaTime deltaTime for animations/effects that are relative to game speed.
     */
    void render(float deltaTime);

    /**
     * Hide's this Gui element. Fadeout effects can be implemented on a case-by-case basis.
     */
    void hide();

    /**
     * Show's this Gui element. Fadein effects can be implemented on a case-by-case basis.
     */
    void show();

    /**
     * @return if this element is hidden
     */
    boolean isHidden();
}
