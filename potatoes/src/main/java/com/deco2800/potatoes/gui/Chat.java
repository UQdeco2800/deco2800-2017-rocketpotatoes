package com.deco2800.potatoes.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class ChatGui implements Gui {
    private boolean hidden;
    private Window window;

    /**
     * Initializes this ChatGui. Creating the window and all associated elements attached to it
     */
    public ChatGui(Stage stage) {
        hidden = false;
        //window = new Window
    }

    /**
     * @return the internal window element
     */
    @Override
    public Window getWindow() {
        return window;
    }

    /**
     * Redraws this gui element
     *
     * @param deltaTime deltaTime for animations/effects that are relative to game speed.
     */
    @Override
    public void render(float deltaTime) {

    }

    /**
     * Hide's this Gui element. Fadeout effects can be implemented on a case-by-case basis.
     */
    @Override
    public void hide() {
        hidden = true;
    }

    /**
     * Show's this Gui element. Fadein effects can be implemented on a case-by-case basis.
     */
    @Override
    public void show() {
        hidden = false;
    }
}
