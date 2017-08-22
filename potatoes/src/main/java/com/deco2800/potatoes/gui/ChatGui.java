package com.deco2800.potatoes.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ArraySelection;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Cullable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

public class ChatGui extends Gui {
    private Skin uiSkin;
    private Table table;
    private ScrollPane chatContainer;
    private ChatList textArea;

    //2private Table
    private TextField textField;
    private Button sendButton;

    /**
     * Initializes this ChatGui
     */
    public ChatGui(Stage stage) {
        hidden = false;

        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));

        // Our table contains our entire chat gui
        table = new Table(uiSkin);

        // List of messages, with custom draw method so we have label specific
        textArea = new ChatList(uiSkin);

        // Scrollpane containing the list of messages
        chatContainer = new ScrollPane(textArea, uiSkin);
        chatContainer.setForceScroll(false, true);
        chatContainer.setScrollBarPositions(false, true);
        chatContainer.setFadeScrollBars(true);
        chatContainer.pack();

        // Field where chat is entered
        textField = new TextField("", uiSkin);

        // Button to press when chat is complete
        sendButton = new Button(uiSkin);
        sendButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!textField.getText().equals("")) {
                    addMessage("Button", textField.getText(), Color.WHITE);
                    textField.setText("");
                    stage.setKeyboardFocus(null);
                }
            }
        });

        resetGui(stage);
        stage.addActor(table);
    }
    private int num = 0;
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
        //stable.setDebug(true);

        table.add(chatContainer).width(stage.getWidth() * 0.4f).height(stage.getHeight() * 0.4f);
        table.row();
        table.add(textField).width(stage.getWidth() * 0.4f - 30.0f).align(Align.left);
        table.add(sendButton).width(30.0f).height(30.0f).pad(0).padLeft(-30.0f);

        table.getColor().a = 0.3f;
        table.setPosition(0, 0);
        table.pack();
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
        l.setName("[" + name + "]");
        l.setText(text);
        l.setColor(color);

        textArea.addItem(l);

        // Force scroll to bottom (TODO disable if manually scrolled up?)
        chatContainer.layout();
        chatContainer.scrollTo(0, 0, 0, 0);
    }

    /** Tweaked list element so we can colour specific elements, and don't listen for click events
     * Note: I've just ripped 90% of this off the libgdx implementation
     * Also Note: The style and font size needs to be the same as the Labels contained within or this will break
     */
    private class ChatList extends Widget implements Cullable {
        private com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle style;
        final Array<Label> items = new Array();
        private Rectangle cullingArea;
        private float prefWidth, prefHeight;
        private float itemHeight;
        private float textOffsetX, textOffsetY;

        public ChatList(Skin skin) {
            this(skin.get(com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle.class));
        }

        public ChatList(Skin skin, String styleName) {
            this(skin.get(styleName, com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle.class));
        }

        public ChatList(com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle style) {
            setStyle(style);
            setSize(getPrefWidth(), getPrefHeight());
        }

        public void setStyle(com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle style) {
            if (style == null) throw new IllegalArgumentException("style cannot be null.");
            this.style = style;
            invalidateHierarchy();
        }

        public void layout() {
            final BitmapFont font = style.font;
            final Drawable selectedDrawable = style.selection;

            itemHeight = font.getCapHeight() - font.getDescent() * 2;
            itemHeight += selectedDrawable.getTopHeight() + selectedDrawable.getBottomHeight();

            textOffsetX = selectedDrawable.getLeftWidth();
            textOffsetY = selectedDrawable.getTopHeight() - font.getDescent();

            prefWidth = 0;
            Pool<GlyphLayout> layoutPool = Pools.get(GlyphLayout.class);
            GlyphLayout layout = layoutPool.obtain();
            for (int i = 0; i < items.size; i++) {
                layout.setText(font, toString(items.get(i)));
                prefWidth = Math.max(layout.width, prefWidth);
            }
            layoutPool.free(layout);
            prefWidth += selectedDrawable.getLeftWidth() + selectedDrawable.getRightWidth();
            prefHeight = items.size * itemHeight;

            Drawable background = style.background;
            if (background != null) {
                prefWidth += background.getLeftWidth() + background.getRightWidth();
                prefHeight += background.getTopHeight() + background.getBottomHeight();
            }
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            validate();

            BitmapFont font = style.font;

            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

            float x = getX(), y = getY(), width = getWidth(), height = getHeight();
            float itemY = height;

            Drawable background = style.background;
            if (background != null) {
                background.draw(batch, x, y, width, height);
                float leftWidth = background.getLeftWidth();
                x += leftWidth;
                itemY -= background.getTopHeight();
                width -= leftWidth + background.getRightWidth();
            }

            for (int i = 0; i < items.size; i++) {
                if (cullingArea == null || (itemY - itemHeight <= cullingArea.y + cullingArea.height && itemY >= cullingArea.y)) {
                    Label item = items.get(i);
                    font.setColor(item.getColor().r, item.getColor().g, item.getColor().b, item.getColor().a);
                    drawItem(batch, font, i, item, x + textOffsetX, y + itemY - textOffsetY);
                } else if (itemY < cullingArea.y) {
                    break;
                }
                itemY -= itemHeight;
            }
        }

        protected GlyphLayout drawItem(Batch batch, BitmapFont font, int index, Label item, float x, float y) {
            return font.draw(batch, toString(item), x, y);
        }

        /**
         * Add's a single label item
         * @param l the label to be added
         */
        public void addItem(Label l) {
            if (l == null) throw new IllegalArgumentException("new item cannot be null.");
            float oldPrefWidth = getPrefWidth(), oldPrefHeight = getPrefHeight();

            items.add(l);

            invalidate();
            if (oldPrefWidth != getPrefWidth() || oldPrefHeight != getPrefHeight()) invalidateHierarchy();
        }

        public void setItems(Label... newItems) {
            if (newItems == null) throw new IllegalArgumentException("newItems cannot be null.");
            float oldPrefWidth = getPrefWidth(), oldPrefHeight = getPrefHeight();

            items.clear();
            items.addAll(newItems);

            invalidate();
            if (oldPrefWidth != getPrefWidth() || oldPrefHeight != getPrefHeight()) invalidateHierarchy();
        }

        /**
         * Sets the items visible in the list, clearing the selection if it is no longer valid. If a selection is
         * {@link ArraySelection#getRequired()}, the first item is selected.
         */
        public void setItems(Array newItems) {
            if (newItems == null) throw new IllegalArgumentException("newItems cannot be null.");
            float oldPrefWidth = getPrefWidth(), oldPrefHeight = getPrefHeight();

            items.clear();
            items.addAll(newItems);

            invalidate();
            if (oldPrefWidth != getPrefWidth() || oldPrefHeight != getPrefHeight()) invalidateHierarchy();
        }

        public void clearItems() {
            if (items.size == 0) return;
            items.clear();
            invalidateHierarchy();
        }

        /**
         * Returns the internal items array. If modified, {@link #setItems(Array)} must be called to reflect the changes.
         */
        public Array<Label> getItems() {
            return items;
        }

        public float getItemHeight() {
            return itemHeight;
        }

        public float getPrefWidth() {
            validate();
            return prefWidth;
        }

        public float getPrefHeight() {
            validate();
            return prefHeight;
        }

        protected String toString(Label obj) {
            return obj.toString();
        }

        public void setCullingArea(Rectangle cullingArea) {
            this.cullingArea = cullingArea;
        }
    }
}