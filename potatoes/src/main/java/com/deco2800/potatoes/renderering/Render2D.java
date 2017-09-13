package com.deco2800.potatoes.renderering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.TextureManager;

import java.util.Collection;

/**
 * Render2D is a 2D renderer for side scrolling games.
 * It impliments the Renderer interface in order to talk to LibGDX
 * @Author Tim Hadwen
 */
public class Render2D implements Renderer {

    OrthogonalTiledMapRenderer tiledMapRenderer;

    /**
     * A multiplier to convert from 3D worlds into 2D worlds.
     * (x,y) position * COORDINATEMULTIPLIER = Screen position
     */
    private int COORDINATEMULTIPLIER = 64;

    /**
     * Simply renders things onto the viewport in 2D
     * @param batch Batch to render onto
     */
    @Override
    public void render(SpriteBatch batch) {
        Collection<AbstractEntity> renderables = GameManager.get().getWorld().getEntities().values();

        batch.begin();

        for (AbstractEntity e : renderables) {
            TextureManager reg = GameManager.get().getManager(TextureManager.class);
            Texture tex = reg.getTexture(e.getTexture());
            batch.draw(tex, e.getPosY()*COORDINATEMULTIPLIER, e.getPosZ()*COORDINATEMULTIPLIER, e.getXRenderLength()*COORDINATEMULTIPLIER, e.getYRenderLength()*COORDINATEMULTIPLIER);

        }

        batch.end();
    }

    /**
     * Returns the correct tile renderer for this rendering engine
     * @param batch The current sprite batch
     * @return TiledMapRenderer for the 2D engine
     */
    @Override
    public BatchTiledMapRenderer getTileRenderer(SpriteBatch batch) {
        return new OrthogonalTiledMapRenderer(GameManager.get().getWorld().getMap(), 1, batch);
    }
}
