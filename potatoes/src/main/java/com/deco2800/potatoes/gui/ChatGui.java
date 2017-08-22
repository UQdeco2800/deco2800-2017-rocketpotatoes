package com.deco2800.potatoes.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.GuiManager;
import com.deco2800.potatoes.managers.InputManager;
import com.deco2800.potatoes.managers.MultiplayerManager;
import com.deco2800.potatoes.observers.KeyDownObserver;

public class ChatGui extends Gui {
    private Stage stage;

    private Skin uiSkin;
    private Table table;
    private ScrollPane chatContainer;
    private Table textList;

    private TextField textField;
    private Button sendButton;

    /**
     * Initializes this ChatGui
     */
    public ChatGui(Stage stage) {
        hidden = false;
        this.stage = stage;

        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));

        // Our table contains our entire chat gui
        table = new Table(uiSkin);

        // List of messages
        textList = new Table();
        textList.align(Align.topLeft);

        // Scrollpane containing the list of messages
        chatContainer = new ScrollPane(textList, uiSkin);
        chatContainer.setForceScroll(false, true);
        chatContainer.setScrollingDisabled(true, false);
        chatContainer.setScrollBarPositions(false, true);
        chatContainer.setFadeScrollBars(true);
        chatContainer.pack();

        // Field where chat is entered

        textField = new TextField("", uiSkin);
        textField.setMaxLength(1024);

        // Button to press when chat is complete
        sendButton = new Button(uiSkin);
        sendButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sendMessage();
            }
        });

        textField.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    sendMessage();
                    return true;
                }

                if (keycode == Input.Keys.ESCAPE) {
                    stage.setKeyboardFocus(null);
                    return true;
                }
                return false;
            }
        });

        // Key listener to focus on textfield when playing
        ((InputManager)GameManager.get().getManager(InputManager.class)).addKeyDownListener(new KeyDownObserver() {
            @Override
            public void notifyKeyDown(int keycode) {
                if (!hidden) {
                    if (keycode == Input.Keys.ENTER) {
                        stage.setKeyboardFocus(textField);
                    }
                }
            }
        });

        resetGui(stage);
        stage.addActor(table);
    }

    /**
     * Hide's this Gui element. Fadeout effects can be implemented on a case-by-case basis.
     */
    @Override
    public void hide() {
        super.hide();

        table.remove();
    }

    /**
     * Show's this Gui element. Fadein effects can be implemented on a case-by-case basis.
     */
    @Override
    public void show() {
        super.show();

        ((GuiManager) GameManager.get().getManager(GuiManager.class)).getStage().addActor(table);
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

        // TODO refine these measurements
        table.add(chatContainer).width(stage.getWidth() * 0.4f).height(stage.getHeight() * 0.4f);
        table.row();
        table.add(textField).width(stage.getWidth() * 0.4f - 30.0f).align(Align.left);
        table.add(sendButton).width(30.0f).height(30.0f).pad(0).padLeft(-30.0f);

        table.getColor().a = 0.3f;
        table.setPosition(0, 0);
        table.pack();

        // Realign messages
        for (Cell l : textList.getCells()) {
            l.width(stage.getWidth() * 0.4f - 10.0f);
        }
        // Automatically adjust alpha to counter the parent table
        textList.getColor().a = 1.0f / table.getColor().a;
    }

    /**
     * Add's a message to the chat box.
     *
     * @param name  the name/sender of the message (e.g. "System", "Bob", "Jimmy"
     * @param text  the contents of the message (e.g. "tomgr is cool"
     * @param color the gdx defined colour
     */
    public void addMessage(String name, String text, Color color) {
        Label l = new Label("", uiSkin);
        l.setText("[" + name + "]: " + text);
        l.setColor(color);
        l.setHeight(100.0f);
        l.setWrap(true);

        textList.add(l).width(stage.getWidth() * 0.4f - 10.0f).align(Align.left);
        textList.row();
        textList.layout();

        // Force scroll to bottom (TODO disable if manually scrolled up?)
        chatContainer.layout();
        chatContainer.scrollTo(0, 0, 0, 0);
    }

    private void sendMessage() {
        if (!textField.getText().equals("")) {
            MultiplayerManager m = (MultiplayerManager) GameManager.get().getManager(MultiplayerManager.class);

            if (m.isMultiplayer()) {
                m.broadcastMessage(textField.getText());
            }

            //addMessage("Button", textField.getText(), Color.WHITE);
            textField.setText("");

            // Reset keyboard focus to game window
            stage.setKeyboardFocus(null);
        }
    }
}