package com.deco2800.potatoes.renderering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.HasProgress;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.TextureManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
            font.getData().setScale(0.8f);
        }
        Map<Integer, AbstractEntity> renderables = GameManager.get().getWorld().getEntities();
        int worldLength = GameManager.get().getWorld().getLength();
        int worldWidth = GameManager.get().getWorld().getWidth();

        int tileWidth = (int)GameManager.get().getWorld().getMap().getProperties().get("tilewidth");
        int tileHeight = (int)GameManager.get().getWorld().getMap().getProperties().get("tileheight");

        float baseX = tileWidth*(worldWidth/2.0f - 0.5f); // bad

        float baseY = -tileHeight/2*worldLength + tileHeight/2f; // good

        List<AbstractEntity> entities = new ArrayList<>();

        /* Gets a list of all entities in the renderables */
        for (AbstractEntity r : renderables.values()) {
            if (r instanceof AbstractEntity) {
                entities.add((AbstractEntity)r);
            }
        }

        /* Sort these so that the objects don't render in the wrong order */
        Collections.sort(entities);

        batch.begin();

        /* Render each entity (backwards) in order to retain objects at the front */
        for (int index = 0; index < entities.size(); index++) {
            AbstractEntity entity = entities.get(index);

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

        for (int index = 0; index < entities.size(); index++) {
            AbstractEntity entity = entities.get(index);

            float cartX = entity.getPosX();
            float cartY = (worldWidth-1) - entity.getPosY();

            float isoX = baseX + ((cartX - cartY) / 2.0f * tileWidth);
            float isoY = baseY + ((cartX + cartY) / 2.0f) * tileHeight;


            if (entity instanceof HasProgress && ((HasProgress) entity).showProgress()) {
                font.draw(batch, String.format("%d%%", ((HasProgress) entity).getProgress()), isoX + tileWidth/2 - 5, isoY + 50);
            }
        }

//        /*
//        Timmy approves this commented out code. Shut up sonar!
//        Leaving this here.
//        It renders the rendering order onto entites so you can see what gets rendered when
//
//         */
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
