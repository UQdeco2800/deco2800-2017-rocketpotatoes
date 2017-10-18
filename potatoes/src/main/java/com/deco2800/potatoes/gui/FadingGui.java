package com.deco2800.potatoes.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.managers.*;
import com.deco2800.potatoes.renderering.Render3D;
import com.deco2800.potatoes.renderering.particles.ParticleEmitter;
import com.deco2800.potatoes.renderering.particles.types.BasicParticleType;
import com.deco2800.potatoes.renderering.particles.types.BuoyantParticleType;
import com.deco2800.potatoes.renderering.particles.types.ParticleType;

//import java.awt.*;

public class FadingGui extends Gui implements Tickable{

    private TreeState treeState;
    private int timer;
    private Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
    private Image image;
    private int startTime;
    private Table container;

    public FadingGui(TreeState treeState, int timer, Stage stage) {
        this.treeState = treeState;
        this.timer = timer;
        this.startTime = timer;
        container = new Table();
        initializeGui(stage);

    }

    private void initializeGui(Stage stage){
        TextureManager textureManager = GameManager.get().getManager(TextureManager
                .class);
        Label unlockedLabel = new Label("Unlocked " + treeState.getTree().getName(),
                uiSkin);
        image =  new Image(new TextureRegionDrawable(new TextureRegion(textureManager
                .getTexture(treeState.getTree().getTexture()))));

        float x = (stage.getWidth() - image.getImageWidth())/2;
        float y = (stage.getHeight() - image.getImageHeight())/2;


        ParticleManager p = GameManager.get().getManager(ParticleManager.class);
        ParticleType particle =  new BasicParticleType(100000, 500.0f,
                0.0f, 1024, Color.RED, 5, 5);
        particle.speed = 0.9f;

        Vector2 pos = Render3D.worldToScreenCoordinates(x,y, 0);
        int tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get("tilewidth");
        int tileHeight = (int) GameManager.get().getWorld().getMap().getProperties().get("tileheight");
        p.addParticleEmitter(1.0f, new ParticleEmitter(pos.x + tileWidth / 2, pos.y + tileHeight / 2,
                particle));


        container.add(unlockedLabel);
        container.row();
        container.add(image).size(100, 100);
        stage.addActor(container);
        container.setPosition(x, y);
    }

    @Override
    public void onTick (long time){
        timer --;
        /*if (timer < 1) {
            GameManager.get().getManager(GuiManager.class).removeFadingGui(this);
            return;
        }*/
        float opacity = (float)timer / startTime;
        container.setColor(4,4,4,opacity);
        container.setPosition(container.getX(), container.getY()+1);
    }

    @Override
    public String toString() {
        return "Fading Gui: "+treeState.toString();
    }
}
