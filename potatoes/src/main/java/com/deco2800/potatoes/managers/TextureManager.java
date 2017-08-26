package com.deco2800.potatoes.managers;

import com.badlogic.gdx.graphics.Texture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Texture manager acts as a cache between the file system and the renderers.
 * This allows all textures to be read into memory at the start of the game saving
 * file reads from being completed during rendering.
 *
 * With this in mind don't load textures you're not going to use.
 * Textures that are not used should probably (at some point) be removed
 * from the list and then read from disk when needed again using some type
 * of reference counting
 * @Author Tim Hadwen
 */
public class TextureManager extends Manager {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextureManager.class);

    /**
     * A HashMap of all textures with string keys
     */
    private Map<String, Texture> textureMap = new HashMap<String, Texture>();

    /**
     * Constructor
     * Currently loads up all the textures but probably shouldn't/doesn't
     * need to.
     */
    public TextureManager() {
    	saveTexture("grass", "resources/placeholderassets/grass.png");
        saveTexture("grass2", "resources/placeholderassets/grass2.png");
        saveTexture("tree", "resources/placeholderassets/spacman.png");
        saveTexture("real_tree", "resources/placeholderassets/tree.png");
        saveTexture("seed_resource_tree", "resources/placeholderassets/basicResourceTree-01.png");
        saveTexture("food_resource_tree", "resources/placeholderassets/pinkResourcetree-01.png");
        saveTexture("ground_1", "resources/placeholderassets/ground-1.png");
        saveTexture("spacman", "resources/placeholderassets/spacman.png");
        saveTexture("spacman_red", "resources/placeholderassets/spacman_red.png");
        saveTexture("spacman_blue", "resources/placeholderassets/spacman_blue.png");
        saveTexture("spacman_blue_2", "resources/placeholderassets/spacman_blue_2.png");
        saveTexture("spacman_green", "resources/placeholderassets/spacman_green.png");
        saveTexture("spacman_ded", "resources/placeholderassets/spacman_ded.png");
        saveTexture("selected", "resources/placeholderassets/selected.png");
        saveTexture("selected_black", "resources/placeholderassets/selected_black.png");
        saveTexture("tree_selected", "resources/placeholderassets/tree_selected.png");
		saveTexture("ground_1", "resources/placeholderassets/ground-1.png");
		saveTexture("squirrel", "resources/placeholderassets/squirrel.png");
        saveTexture("squirrel2", "resources/placeholderassets/squirrelFaceRight.png");
		saveTexture("tower", "resources/placeholderassets/tower.png");
		saveTexture("potate", "resources/placeholderassets/potate.png");
		saveTexture("projectile", "resources/placeholderassets/projectile.png");
        saveTexture("progress_bar","resources/placeholderassets/progress_bar.png");
		saveTexture("seed", "resources/placeholderassets/seed.png");
		saveTexture("food", "resources/placeholderassets/food.png");
		saveTexture("aoe1","resources/placeholderassets/aoe1.png");
        saveTexture("aoe2","resources/placeholderassets/aoe2.png");
        saveTexture("aoe3","resources/placeholderassets/aoe3.png");
        saveTexture("proj1","resources/placeholderassets/proj1.png");
        saveTexture("Lightning","resources/placeholderassets/Lightning.png");
        saveTexture("ring","resources/placeholderassets/ring.png");
        saveTexture("highlight_tile","resources/tiles/highlight_tile.png");
        saveTexture("tankBear", "resources/placeholderassets/tankBear.png");
        saveTexture("speedySquirrel","resources/placeholderassets/speedySquirrel.png");
        saveTexture("screen_background", "resources/placeholderassets/screen_background.png");


    }

    /**
     * Gets a texture object for a given string id
     * @param id Texture identifier
     * @return Texture for given id
     */
    public Texture getTexture(String id) {
        if (textureMap.containsKey(id)) {
            return textureMap.get(id);
        } else {
            return textureMap.get("spacman_ded");
        }

    }

    /**
     * Saves a texture with a given id
     * @param id Texture id
     * @param filename Filename within the assets folder
     */
    public void saveTexture(String id, String filename) {
        LOGGER.info("Saving texture" + id + " with Filename " + filename);
        if (!textureMap.containsKey(id)) {
            textureMap.put(id, new Texture(filename));
        }
    }
}
