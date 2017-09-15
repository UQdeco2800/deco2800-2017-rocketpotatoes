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
 * <p>
 * With this in mind don't load textures you're not going to use.
 * Textures that are not used should probably (at some point) be removed
 * from the list and then read from disk when needed again using some type
 * of reference counting
 *
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
        saveTexture("w1", "resources/placeholderassets/w1.png");
        saveTexture("tree", "resources/trees/Basic.png");
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
        saveTexture("squirrel", "resources/placeholderassets/squirrel.png");
        saveTexture("squirrel_right", "resources/placeholderassets/squirrel_right.png");
        saveTexture("tower", "resources/placeholderassets/tower.png");
        saveTexture("potate", "resources/placeholderassets/potate.png");
        saveTexture("projectile", "resources/placeholderassets/projectile.png");
        saveTexture("europeanhattedsquirrel", "resources/placeholderassets/europeanhattedsquirrel.png");
        saveTexture("pronograde", "resources/placeholderassets/pronograde.png");
        saveTexture("progress_bar", "resources/placeholderassets/progress_bar.png");
        saveTexture("seed", "resources/placeholderassets/seed.png");
        saveTexture("food", "resources/placeholderassets/food.png");
        saveTexture("ring", "resources/placeholderassets/ring.png");
        saveTexture("highlight_tile", "resources/tiles/highlight_tile.png");
        saveTexture("tankBear", "resources/placeholderassets/tankBear.png");
        saveTexture("speedyRaccoon", "resources/placeholderassets/raccoon.png");
        saveTexture("healthbar", "resources/healthproperties/Full_Health_Bar.png");
        saveTexture("greybar", "resources/healthproperties/greyBar.png");

        // Projectiles
        for (int i = 1; i < 4; i++) {
            saveTexture("aoe" + i, "resources/projectiles/aoe" + i + ".png");
            saveTexture("rocket" + i, "resources/projectiles/rocket" + i + ".png");
            saveTexture("exp" + i, "resources/projectiles/explosion" + i + ".png");
            saveTexture("chilli" + i, "resources/projectiles/chilliproj" + i + ".png");
        }
        saveTexture("lightning", "resources/projectiles/lightning.png");

        // Particles
        saveTexture("snowflake", "resources/particles/snowflake.png");

        // GUI
        saveTexture("tree_shop", "resources/menu/tree_menu.png");
        saveTexture("backgroundMainMenu", "resources/menu/backgroundMainMenu.png");
        saveTexture("gameOverScreen", "resources/healthproperties/gameOverScreen.png");
        saveTexture("resumePauseMenu", "resources/menu/resumePauseMenu.png");
        saveTexture("optionsPauseMenu", "resources/menu/optionsPauseMenu.png");
        saveTexture("exitPauseMenu", "resources/menu/exitPauseMenu.png");
        saveTexture("backgroundPauseMenu", "resources/menu/backgroundPauseMenu.png");
        saveTexture("startMainMenu", "resources/menu/startMainMenu.png");
        saveTexture("optionsMainMenu", "resources/menu/optionsMainMenu.png");
        saveTexture("exitMainMenu", "resources/menu/exitMainMenu.png");
        saveTexture("singleplayerMainMenu", "resources/menu/singleplayerMainMenu.png");
        saveTexture("multiplayerMainMenu", "resources/menu/multiplayerMainMenu.png");
        saveTexture("backMainMenu", "resources/menu/backMainMenu.png");


        // Tree growing animation, should maybe be moved to TextureRegion later
        for (int i = 1; i < 8; i++) {
            saveTexture("basictree_grow" + i, "resources/trees/Grow" + i + ".png");
        }
//        //damage tree:lightning
        for (int i = 1; i < 10; i++) {
            saveTexture("lightning_tree" + i, "resources/trees/lightning" + i + ".png");
            saveTexture("lightning_being_damaged" + i, "resources/trees/lightningBeingDamaged" + i + ".png");
            saveTexture("lightning_damaged" + i, "resources/trees/lightningDamaged" + i + ".png");
            if (i < 5)
                saveTexture("fire_tree"+i,"resources/trees/fire" + i + ".png");
            if (i < 9)
            	saveTexture("lightning_damaged_being_damaged" + i, "resources/trees/lightningDamagedBeingDamaged" + i + ".png");
            if (i < 8) {
                saveTexture("lightning_dead" + i, "resources/trees/lightningDead" + i + ".png");
            }
        }
        //damage tree:ice
        saveTexture("ice_basic_tree", "resources/trees/iceBasicTree.png");
        //damage tree:acorn
        saveTexture("acorn_tree", "resources/trees/tree-acorn.png");
        // Enemies
        saveTexture("swipe1", "resources/enemies/swipe1.png");
        saveTexture("swipe2", "resources/enemies/swipe2.png");
        saveTexture("swipe3", "resources/enemies/swipe3.png");
        saveTexture("swipe4", "resources/enemies/swipe4.png");
        saveTexture("swipe5", "resources/enemies/swipe5.png");
        saveTexture("empty", "resources/enemies/empty.png");
        saveTexture("DamagedGroundTemp1", "resources/enemies/DamagedGroundTemp1.png");
        saveTexture("DamagedGroundTemp2", "resources/enemies/DamagedGroundTemp2.png");
        saveTexture("DamagedGroundTemp3", "resources/enemies/DamagedGroundTemp3.png");
       //Portals 
        saveTexture("desert_portal", "resources/portals/Desert_Portal.png");
        saveTexture("iceland_portal", "resources/portals/Iceland_Portal.png");
        saveTexture("volcano_portal", "resources/portals/Volcano_Portal.png");

        saveTexture("flash_red_left","resources/placeholderassets/spacman_blue_2_1.png");
        saveTexture("flash_red_right","resources/placeholderassets/spacman_blue_damage_1.png");

    }

    /**
     * Gets a texture object for a given string id
     *
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
     *
     * @param id       Texture id
     * @param filename Filename within the assets folder
     */
    public void saveTexture(String id, String filename) {
        LOGGER.info("Saving texture" + id + " with Filename " + filename);
        if (!textureMap.containsKey(id)) {
            textureMap.put(id, new Texture(filename));
        }
    }
}
