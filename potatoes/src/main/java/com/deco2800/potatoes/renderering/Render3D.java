package com.deco2800.potatoes.renderering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.HasProgress;
import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.MultiplayerManager;
import com.deco2800.potatoes.managers.TextureManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.text.html.parser.Entity;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * A simple isometric renderer for DECO2800 games
 * @Author Tim Hadwen
 */
public class Render3D implements Renderer {

    BitmapFont font;

    private static final Logger LOGGER = LoggerFactory.getLogger(Render3D.class);

    /**
     * Renders onto a batch, given a renderables with entities
     * It is expected that AbstractWorld contains some entities and a Map to read tiles from
     * @param batch Batch to render onto
     */
    @Override
    public void render(SpriteBatch batch) {
        if (font == null) {
            font = new BitmapFont();
            font.getData().setScale(1.0f);
        }
        Map<Integer, AbstractEntity> renderables = GameManager.get().getWorld().getEntities();
        int worldLength = GameManager.get().getWorld().getLength();
        int worldWidth = GameManager.get().getWorld().getWidth();

        int tileWidth = (int)GameManager.get().getWorld().getMap().getProperties().get("tilewidth");
        int tileHeight = (int)GameManager.get().getWorld().getMap().getProperties().get("tileheight");

        float baseX = tileWidth*(worldWidth/2.0f - 0.5f); // bad

        float baseY = -tileHeight/2*worldLength + tileHeight/2f; // good

        /* Tree map so we sort our entities properly */
        SortedMap<AbstractEntity, Integer> entities = new TreeMap<>();

        /* Gets a list of all entities in the renderables */
        for (Map.Entry<Integer, AbstractEntity> e : renderables.entrySet()) {
            entities.put(e.getValue(), e.getKey());
        }

             batch.begin();

        /* Render each entity (backwards) in order to retain objects at the front */
        for (Map.Entry<AbstractEntity, Integer> e : entities.entrySet()) {
            AbstractEntity entity = e.getKey();

            String textureString = entity.getTexture();
            TextureManager reg = (TextureManager) GameManager.get().getManager(TextureManager.class);
            Texture tex = reg.getTexture(textureString);

            float cartX = entity.getPosX();
            float cartY = (worldWidth-1) - entity.getPosY();

            float isoX = baseX + ((cartX - cartY) / 2.0f * tileWidth);
            float isoY = baseY + ((cartX + cartY) / 2.0f) * tileHeight;

            // We want to keep the aspect ratio of the image so...
            float aspect = (float)(tex.getWidth())/(float)(tileWidth);

            batch.draw(tex, isoX, isoY, tileWidth*entity.getXRenderLength(),
                    (tex.getHeight()/aspect)*entity.getYRenderLength());
        }

        for (Map.Entry<AbstractEntity, Integer> e : entities.entrySet()) {
            AbstractEntity entity = e.getKey();

            float cartX = entity.getPosX();
            float cartY = (worldWidth-1) - entity.getPosY();

            float isoX = baseX + ((cartX - cartY) / 2.0f * tileWidth);
            float isoY = baseY + ((cartX + cartY) / 2.0f) * tileHeight;


            if (entity instanceof HasProgress && ((HasProgress) entity).showProgress()) {
                font.setColor(Color.RED);
                font.getData().setScale(1.0f);
                font.draw(batch, String.format("%d%%", ((HasProgress) entity).getProgress()), isoX + tileWidth/2 - 10, isoY + 60);
            }

            MultiplayerManager m = (MultiplayerManager) GameManager.get().getManager(MultiplayerManager.class);
            if (entity instanceof Player && m.isMultiplayer()) {
                font.setColor(Color.WHITE);
                font.getData().setScale(1.3f);
                if (m.getID() == e.getValue()) { font.setColor(Color.BLUE); }
                font.draw(batch, String.format("%s", m.getClients().get(e.getValue())), isoX + tileWidth/2 - 10, isoY + 70);

            }
        }

//        /*
//        Timmy approves this commented out code. Shut up sonar!
//        Leaving this here.
//        It renders the rendering order onto entites so you can see what gets rendered when
//
//         */s
//        for (int index = 0; index < entities.size(); index++) {
//            Renderable entity = entities.get(index);
//            float cartX = entity.getPosX();
//            float cartY = (worldWidth-1) - entity.getPosY();
//
//            float isoX = baseX + ((cartX - cartY) / 2.0f * tileWidth);
//            float isoY = baseY + ((cartX + cartY) / 2.0f) * tileHeight;
//
//            font.draw(batch, String.format("%d", index), isoX + 32, isoY + 32);
//        }

        batch.end();

    }

    /**
     * Returns the correct tile renderer for the given rendering engine
     * @param batch The current sprite batch
     * @return A TiledMapRenderer for the current engine
     */
    @Override
    public BatchTiledMapRenderer getTileRenderer(SpriteBatch batch) {
        return new IsometricTiledMapRenderer(GameManager.get().getWorld().getMap(), 1, batch);
    }
}
