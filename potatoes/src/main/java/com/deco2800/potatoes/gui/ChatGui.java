package com.deco2800.potatoes.gui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class ChatGui extends HideableGui implements Gui {
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

}
