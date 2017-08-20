package com.deco2800.potatoes.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class ChatGui extends Gui {
    private Skin uiSkin;
    private Table table;
    private ScrollPane chatContainer;
    private List<Label> textArea;

    /**
     * Initializes this ChatGui
     */
    public ChatGui(Stage stage) {
        hidden = false;
        // Make window, with the given skin
        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));

        table = new Table(uiSkin);
        textArea = new List<>(uiSkin);
        Label t = new Label("Hey!", uiSkin);
        t.setName("[TOM]");
        t.setColor(Color.RED);
        t.set
        textArea.setItems(t);
        textArea.setSelectedIndex(-1);
        chatContainer = new ScrollPane(textArea, uiSkin);
        chatContainer.setForceScroll(false, true);
        chatContainer.setScrollBarPositions(false, true);
        chatContainer.setFadeScrollBars(false);
        chatContainer.pack();

        resetGui(stage);
        stage.addActor(table);  
    }

    /**
     * Adjusts this gui's position to correct for any resize event.
     *
     * @param stage
     */
    @Override
    public void resize(Stage stage) {
        super.resize(stage);

        resetGui(stage);
    }

    private void resetGui(Stage stage) {
        table.reset();
        //table.setDebug(true);
        table.add(chatContainer).width(stage.getWidth() * 0.3f).height(stage.getHeight() * 0.3f);
        table.getColor().a = 0.8f;
        table.setPosition(0, 0);
        table.pack();
    }
}
