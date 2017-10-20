package com.deco2800.potatoes.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.managers.*;

public class FadingGui extends Gui implements Tickable{

    private TreeState treeState;
    private int timer;
    private Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
    private Image image;
    private int startTime;
    private int latestTick;
    private Table container;

    public FadingGui(TreeState treeState, int timer, Stage stage) {
        this.treeState = treeState;
        this.timer = timer;
        this.startTime = timer;
        this.latestTick = timer;
        container = new Table();
        initializeGui(stage);

    }

    public int getTimer() {
        return timer;
    }

    /**
     * Adds the gui to center of screen
     */
    private void initializeGui(Stage stage){
        TextureManager textureManager = GameManager.get().getManager(TextureManager
                .class);
        Label unlockedLabel = new Label("Unlocked " + treeState.getTree().getName(),
                uiSkin);
        image =  new Image(new TextureRegionDrawable(new TextureRegion(textureManager
                .getTexture(treeState.getTree().getTexture()))));

        float x = (stage.getWidth() - image.getImageWidth())/2;
        float y = (stage.getHeight() - image.getImageHeight())/2;

        container.add(unlockedLabel);
        container.row();
        container.add(image).size(100, 100);
        stage.addActor(container);
        container.setPosition(x, y);
    }

    /**
     * Fades the image until it's gone and then delete it
     * @param time Current game tick
     */
    @Override
    public void onTick (long time){
        timer -= time-latestTick;
        float opacity = (float)timer / startTime;
        container.setColor(4,4,4,opacity);
        container.setPosition(container.getX(), container.getY()+1);
        latestTick = timer;
    }

    @Override
    public String toString() {
        return "Fading Gui: "+treeState.toString();
    }
}
