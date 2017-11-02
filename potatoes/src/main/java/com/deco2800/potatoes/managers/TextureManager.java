package com.deco2800.potatoes.managers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.deco2800.potatoes.entities.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
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
    private static Map<String, TextureRegion> textureMap = new HashMap<String, TextureRegion>();

    /**
     * Loads all the textures.
     */
    public static void loadTextures() {
        saveTexture("tree", "resources/trees/Basic.png");
        saveTexture("spacman", "resources/placeholderassets/spacman.png");
        saveTexture("spacman_red", "resources/placeholderassets/spacman_red.png");
        saveTexture("spacman_blue", "resources/placeholderassets/spacman_blue.png");
        saveTexture("spacman_blue_2", "resources/placeholderassets/spacman_blue_2.png");
        saveTexture("spacman_green", "resources/placeholderassets/spacman_green.png");
        saveTexture("spacman_ded", "resources/placeholderassets/spacman_ded.png");
        saveTexture("selected", "resources/placeholderassets/selected.png");
        saveTexture("selected_black", "resources/placeholderassets/selected_black.png");
        saveTexture("squirrel", "resources/placeholderassets/squirrel.png");
        saveTexture("squirrel_right", "resources/placeholderassets/squirrel_right.png");
        saveTexture("potate", "resources/placeholderassets/potate.png");
        saveTexture("projectile", "resources/placeholderassets/projectile.png");
        saveTexture("europeanhattedsquirrel", "resources/placeholderassets/europeanhattedsquirrel.png");
        saveTexture("pronograde", "resources/placeholderassets/pronograde.png");  //Moose placeholder
        saveTexture("progress_bar", "resources/placeholderassets/progress_bar.png");
        saveTexture("ring", "resources/placeholderassets/ring.png");
        saveTexture("tankBear", "resources/enemies/bear/bear_SW.png");
        saveTexture("speedyRaccoon", "resources/placeholderassets/raccoon.png");
        saveTexture("enemyGate", "resources/placeholderassets/enemyGate.png");
        saveTexture("enemyCave_SE", "resources/enemies/enemyCave_SE.png");
        saveTexture("enemyCave_S", "resources/enemies/enemyCave_S.png");
        saveTexture("enemyCave_N", "resources/enemies/enemyCave_N.png");
        saveTexture("enemyCave_W", "resources/enemies/enemyCave_W.png");
        saveTexture("enemyCave_E", "resources/enemies/enemyCave_E.png");
        saveTexture("enemyCave_SW", "resources/enemies/enemyCave_SW.png");
        saveTexture("enemyCave_WS", "resources/enemies/enemyCave_WS.png");
        saveTexture("healthBarBlue", "resources/healthproperties/health-bar-blue.png");
        saveTexture("healthBarRed", "resources/healthproperties/health-bar-red.png");
        saveTexture("healthBarGreen", "resources/healthproperties/health-bar-green.png");
        saveTexture("caveIcon", "resources/healthproperties/icon-healthbar-cav.png");
        saveTexture("wizardIcon", "resources/healthproperties/icon-healthbar-wiz.png");
        saveTexture("archerIcon", "resources/healthproperties/icon-healthbar-arc.png");
        saveTexture("portalIcon", "resources/healthproperties/icon-healthbar-potato.png");
        saveTexture("nicer_terrain", "resources/placeholderassets/nicer_terrain.png");
        saveTexture("acorn1", "resources/Projectiles/acorn1.png");
        saveTexture("leaves1", "resources/Projectiles/leaves1.png");
        saveTexture("leaves","resources/Projectiles/leaves.png");
        saveTexture("acorn","resources/Projectiles/acorn.png");
        saveTexture("axe","resources/Projectiles/axe1.png");

        saveTexture("arrow","resources/Projectiles/arrow1.png");
        saveTexture("water","resources/Projectiles/water.png");
        saveTexture("ice","resources/Projectiles/icetreeProjectiles/ice1.png");
        saveTexture("fire","resources/Projectiles/fire.png");
        saveFromSpriteSheet("nicer_terrain", new String[][] {{"ground_1", "grass", "w1"}});
        

        /* Seed Resource Tree Sprites */
        saveTexture("seed_resource_tree", "resources/resourceTrees/seedTree.png"); // Default Sprite
        
        // Seed Grow Animation
        for (int i = 1; i < 44; i++) {
        		saveTexture("seedtree_grow_" + i, "resources/resourceTrees/seed/grow/seedtree_grow_" + String.format("%04d", i) + ".png");
        }
        
        // Seed Produce Animation
        for (int i = 1; i < 32; i++) {
        		saveTexture("seedtree_produce_" + i, "resources/resourceTrees/seed/produce/seedtree_produce_" + String.format("%04d", i) + ".png");
        }
        
        /* Food Resource Tree Sprites */
        saveTexture("food_resource_tree", "resources/resourceTrees/foodTree.png"); // Default Sprite
        
        // Food Grow Animation
        for (int i = 1; i < 51; i++) {
        		saveTexture("foodtree_grow_" + i, "resources/resourceTrees/food/grow/foodtree_grow_" + String.format("%04d", i) + ".png");
        }
        
        // Food Produce Animation
        for (int i = 1; i < 36; i++) {
        		saveTexture("foodtree_produce_" + i, "resources/resourceTrees/food/produce/foodtree_produce_" + String.format("%04d", i) + ".png");
        }
        
        /* Pine Resource Tree Sprites */
        saveTexture("pine_resource_tree", "resources/resourceTrees/pineTree.png"); // Default Sprite
        
        // Pine Grow Animation
        for (int i = 1; i < 44; i++) {
    			saveTexture("pinetree_grow_" + i, "resources/resourceTrees/pine/grow/pinetree_grow_" + String.format("%04d", i) + ".png");
        }
        
        // Pine Produce Animation
        for (int i = 1; i < 36; i++) {
        		saveTexture("pinetree_produce_" + i, "resources/resourceTrees/pine/produce/pinetree_produce_" + String.format("%04d", i) + ".png");
        }

        // Projectiles TODO: fix
        for (int i = 1; i < 4; i++) {
            saveTexture("aoe" + i, "resources/Projectiles/aoe" + i + ".png");
            saveTexture("rocket" + i, "resources/Projectiles/rocket" + i + ".png");
            saveTexture("explosion" + i, "resources/Projectiles/explosion" + i + ".png");
            saveTexture("chilli" + i, "resources/Projectiles/chilliproj" + i + ".png");

        }
        for (int i = 1; i < 6; i++) {
            saveTexture("start" + i, "resources/Projectiles/start" + i + ".png");
        }
        for (int i = 1; i < 5; i++) {
            saveTexture("leaves" + i, "resources/Projectiles/leaves" + i + ".png");
        }
        for (int i = 1; i < 5; i++) {
            saveTexture("mines" + i, "resources/Projectiles/mine" + i + ".png");
        }

        // Tree Attack Textures
        saveTexture("lightningDesert", "resources/Projectiles/lightningDesert.png");
        saveTexture("lightningFire", "resources/Projectiles/lightningFire.png");
        saveTexture("lightningForrest", "resources/Projectiles/lightningForrest.png");
        saveTexture("lightningIce", "resources/Projectiles/lightningIce.png");
        saveTexture("lightningWater", "resources/Projectiles/lightningWater.png");

        // Particles
        saveTexture("snowflake", "resources/particles/snowflake.png");
        saveTexture("duck", "resources/particles/duck.png");

        // GUI
        saveTexture("tree_shop", "resources/menu/tree_menu.png");
        saveTexture("backgroundMainMenu", "resources/menu/tutorial/backgroundMainMenu.png");
        saveTexture("gameOverScreen", "resources/healthproperties/Game-OverV2.11.png");
        saveTexture("gameOverRestart", "resources/healthproperties/restartButton.png");
        saveTexture("gameOverExit", "resources/healthproperties/exitButton.png");
        saveTexture("resumePauseMenu", "resources/menu/pauseMenu/resumePauseMenu.png");
        saveTexture("optionsPauseMenu", "resources/menu/pauseMenu/optionsPauseMenu.png");
        saveTexture("savePauseMenu", "resources/menu/pauseMenu/savePauseMenu.png");
        saveTexture("exitPauseMenu", "resources/menu/pauseMenu/exitPauseMenu.png");
        saveTexture("tutorialPauseMenu", "resources/menu/pauseMenu/tutorialPauseMenu.png");
        saveTexture("backgroundOptionsMenu", "resources/menu/pauseMenu/backgroundOptionsMenu.png");

        saveTexture("startMainMenu", "resources/menu/startMainMenu.png");
        saveTexture("optionsMainMenu", "resources/menu/optionsMainMenu.png");
        saveTexture("exitMainMenu", "resources/menu/exitMainMenu.png");
        saveTexture("singleplayerMainMenu", "resources/menu/singleplayerMainMenu.png");
        saveTexture("multiplayerMainMenu", "resources/menu/multiplayerMainMenu.png");
        saveTexture("backMainMenu", "resources/menu/backMainMenu.png");
        saveTexture("clientMainMenu", "resources/menu/clientMainMenu.png");
        saveTexture("hostMainMenu", "resources/menu/hostMainMenu.png");
        saveTexture("connectMainMenu", "resources/menu/connectMainMenu.png");
        saveTexture("menuButtonPlaceholder", "resources/placeholderassets/menuButtonPlaceholder.png");

        // Tutorial Title Buttons
        saveTexture("initialGameplayButton", "resources/menu/tutorial/initialGameplayButton.png");
        saveTexture("treesButton", "resources/menu/tutorial/treesButton.png");
        saveTexture("enemiesButton", "resources/menu/tutorial/enemiesButton.png");
        saveTexture("healthButton", "resources/menu/tutorial/healthButton.png");
        saveTexture("portalsButton", "resources/menu/tutorial/portalsButton.png");
        saveTexture("controlsButton", "resources/menu/tutorial/controlsButton.png");
        saveTexture("optionsBackButton", "resources/menu/pauseMenu/optionsBackButton.png");

        // Tutorial
        saveTexture("tutorial1", "resources/menu/tutorial/Tutorial1.png");
        saveTexture("tutorial2", "resources/menu/tutorial/Tutorial2.png");
        saveTexture("tutorial3", "resources/menu/tutorial/Tutorial3.png");
        saveTexture("tutorial4", "resources/menu/tutorial/Tutorial4.png");
        saveTexture("tutorial5", "resources/menu/tutorial/Tutorial5.png");
        saveTexture("tutorial6", "resources/menu/tutorial/Tutorial6.png");
        saveTexture("tutorial7", "resources/menu/tutorial/Tutorial7.png");
        saveTexture("controls", "resources/menu/tutorial/Controls.png");

        // Tree growing animation, should maybe be moved to TextureRegion later
        for (int i = 1; i < 8; i++) {
            saveTexture("basictree_grow" + i, "resources/trees/Grow" + i + ".png");
        }
//        //damage tree:lightning
        for (int i = 1; i < 10; i++) {
            saveTexture("lightning_tree" + i, "resources/trees/lightning" + i + ".png");
            saveTexture("lightning_being_damaged" + i, "resources/trees/lightningBeingDamaged" + i + ".png");
            saveTexture("lightning_damaged" + i, "resources/trees/lightningDamaged" + i + ".png");
            if (i < 5) {
                saveTexture("fire_tree" + i, "resources/trees/fire" + i + ".png");
                saveTexture("ice-break" + i, "resources/trees/ICE-break" + i + ".png");
            }

            if (i < 9) {
                saveTexture("lightning_damaged_being_damaged" + i, "resources/trees/lightningDamagedBeingDamaged" + i + ".png");
                saveTexture("ice_being_damaged" + i, "resources/trees/ICE-beingDamaged" + i + ".png");
            }
            if (i < 8) {
                saveTexture("lightning_dead" + i, "resources/trees/lightningDead" + i + ".png");
                saveTexture("ice_tree" + i, "resources/trees/ice" + i + ".png");
            }
            if (i < 4) {
                saveTexture("acorn_tree" + i, "resources/trees/acorn" + i + ".png");
                saveTexture("acorn_tree_damaged" + i, "resources/trees/acornDamaged" + i + ".png");
            }
            if (i < 6) {
                saveTexture("acorn_tree_damage_being_damaged" + i, "resources/trees/acornDamagedBeingDamaged" + i + ".png");

            }
            saveTexture("acorn_tree_dead" + i, "resources/trees/acornDead" + i + ".png");

        }


        saveTexture("cactusTree", "resources/trees/cactusTree.png");
        saveTexture("coralTree", "resources/trees/coralTree.png");
        saveTexture("defenseTree", "resources/trees/defenseTree.png");
        //damage tree:ice

        //damage tree:acorn


        //enemies
        saveTexture("swipe1", "resources/enemies/swipe1.png");
        saveTexture("swipe2", "resources/enemies/swipe2.png");
        saveTexture("swipe3", "resources/enemies/swipe3.png");
        saveTexture("swipe4", "resources/enemies/swipe4.png");
        saveTexture("swipe5", "resources/enemies/swipe5.png");
        saveTexture("empty", "resources/enemies/empty.png");
        saveTexture("DamagedGroundTemp1", "resources/enemies/DamagedGroundTemp1.png");
        saveTexture("DamagedGroundTemp2", "resources/enemies/DamagedGroundTemp2.png");
        saveTexture("DamagedGroundTemp3", "resources/enemies/DamagedGroundTemp3.png");
        saveTexture("TankFootstepTemp1", "resources/enemies/TankFootstepTemp1.png");
        saveTexture("TankFootstepTemp2", "resources/enemies/TankFootstepTemp2.png");
        saveTexture("TankFootstepTemp3", "resources/enemies/TankFootstepTemp3.png");
        saveTexture("Healing1", "resources/enemies/Healing1.png");
        saveTexture("Healing2", "resources/enemies/Healing2.png");
        saveTexture("Healing3", "resources/enemies/Healing3.png");
        //Squirrel
        saveTexture("squirrel_E", "resources/enemies/squirrel/idle/squirrel_E.png");
        saveTexture("squirrel_N", "resources/enemies/squirrel/idle/squirrel_N.png");
        saveTexture("squirrel_NE", "resources/enemies/squirrel/idle/squirrel_NE.png");
        saveTexture("squirrel_NW", "resources/enemies/squirrel/idle/squirrel_NW.png");
        saveTexture("squirrel_S", "resources/enemies/squirrel/idle/squirrel_S.png");
        saveTexture("squirrel_SE", "resources/enemies/squirrel/idle/squirrel_SE.png");
        saveTexture("squirrel_SW", "resources/enemies/squirrel/idle/squirrel_SW.png");
        saveTexture("squirrel_W", "resources/enemies/squirrel/idle/squirrel_W.png");
        //Raccoon
        saveTexture("raccoon_E", "resources/enemies/raccoon/idle/raccoon_E.png");
        saveTexture("raccoon_N", "resources/enemies/raccoon/idle/raccoon_N.png");
        saveTexture("raccoon_NE", "resources/enemies/raccoon/idle/raccoon_NE.png");
        saveTexture("raccoon_NW", "resources/enemies/raccoon/idle/raccoon_NW.png");
        saveTexture("raccoon_S", "resources/enemies/raccoon/idle/raccoon_S.png");
        saveTexture("raccoon_SE", "resources/enemies/raccoon/idle/raccoon_SE.png");
        saveTexture("raccoon_SW", "resources/enemies/raccoon/idle/raccoon_SW.png");
        saveTexture("raccoon_W", "resources/enemies/raccoon/idle/raccoon_W.png");
        //Bear
        saveTexture("bear_E", "resources/enemies/bear/bear_E.png");
        saveTexture("bear_N", "resources/enemies/bear/bear_N.png");
        saveTexture("bear_NE", "resources/enemies/bear/bear_NE.png");
        saveTexture("bear_NW", "resources/enemies/bear/bear_NW.png");
        saveTexture("bear_S", "resources/enemies/bear/bear_S.png");
        saveTexture("bear_SE", "resources/enemies/bear/bear_SE.png");
        saveTexture("bear_SW", "resources/enemies/bear/bear_SW.png");
        saveTexture("bear_W", "resources/enemies/bear/bear_W.png");
        //Moose
        saveTexture("moose_E", "resources/enemies/moose/moose_E.png");
        saveTexture("moose_N", "resources/enemies/moose/moose_N.png");
        saveTexture("moose_NE", "resources/enemies/moose/moose_NE.png");
        saveTexture("moose_NW", "resources/enemies/moose/moose_NW.png");
        saveTexture("moose_S", "resources/enemies/moose/moose_S.png");
        saveTexture("moose_SE", "resources/enemies/moose/moose_SE.png");
        saveTexture("moose_SW", "resources/enemies/moose/moose_SW.png");
        saveTexture("moose_W", "resources/enemies/moose/moose_W.png");

        //Portals
        saveTexture("desert_portal", "resources/portals/Desert_Portal.png");
        saveTexture("iceland_portal", "resources/portals/Iceland_Portal.png");
        saveTexture("volcano_portal", "resources/portals/Volcano_Portal.png");
        saveTexture("forest_portal", "resources/portals/Forest_Portal.png");
        saveTexture("sea_portal", "resources/portals/Sea_Portal.png");

        //Resources
        saveTexture("seed", "resources/resourceEntities/seed.png");
        saveTexture("food", "resources/resourceEntities/food.png");

        saveTexture("tumbleweed", "resources/resourceEntities/tumbleweed.png");
        saveTexture("cactusThorn", "resources/resourceEntities/cactusThorn.png");
        saveTexture("pricklyPear", "resources/resourceEntities/pricklyPear.png");

        saveTexture("snowBall", "resources/resourceEntities/snowBall.png");
        saveTexture("sealSkin", "resources/resourceEntities/sealSkin.png");
        saveTexture("iceCrystal", "resources/resourceEntities/iceCrystal.png");

        saveTexture("coal", "resources/resourceEntities/coal.png");
        saveTexture("bones", "resources/resourceEntities/bones.png");
        saveTexture("obsidian", "resources/resourceEntities/obsidian.png");
        saveTexture("pinecone", "resources/resourceEntities/pinecone.png");
        saveTexture("fishMeat", "resources/resourceEntities/fishMeat.png");
        saveTexture("pearl", "resources/resourceEntities/pearl.png");
        saveTexture("treasure", "resources/resourceEntities/treasure.png");

        //

        saveTexture("flash_red_left", "resources/placeholderassets/spacman_blue_2_1.png");
        saveTexture("flash_red_right", "resources/placeholderassets/spacman_blue_damage_1.png");

        saveTexture("N", "resources/player/debug/N.png");
        saveTexture("NE", "resources/player/debug/NE.png");
        saveTexture("E", "resources/player/debug/E.png");
        saveTexture("SE", "resources/player/debug/SE.png");
        saveTexture("S", "resources/player/debug/S.png");
        saveTexture("SW", "resources/player/debug/SW.png");
        saveTexture("W", "resources/player/debug/W.png");
        saveTexture("NW", "resources/player/debug/NW.png");

        // Add all wizard sprites
        for (Direction direction : Direction.values()) {

            String textureNameIdle = "wizard_idle_" + direction.name() + "_1";
            saveTexture(textureNameIdle, "resources/player/wizard/idle/" + textureNameIdle + ".png");
            String textureNameDamaged = "wizard_damaged_" + direction.name() + "_1";
            saveTexture(textureNameDamaged, "resources/player/wizard/damaged/" + textureNameDamaged + ".png");

            for (int i = 1; i <= 4; i++) {
                String textureNameAttack = "wizard_attack_" + direction.name() + "_" + i;
                saveTexture(textureNameAttack, "resources/player/wizard/attack/" + textureNameAttack + ".png");
            }

            for (int i = 1; i <= 6; i++) {
                String textureNameAttack = "wizard_interact_" + direction.name() + "_" + i;
                saveTexture(textureNameAttack, "resources/player/wizard/interact/" + textureNameAttack + ".png");
            }

            for (int i = 1; i <= 17; i++) {
                String textureNameDeath = "wizard_death_" + direction.name() + "_" + i;
                saveTexture(textureNameDeath, "resources/player/wizard/death/" + textureNameDeath + ".png");
            }

        }

        // Add all caveman sprites
        for (Direction direction : Direction.values()) {

            String textureNameIdle = "caveman_idle_" + direction.name() + "_1";
            saveTexture(textureNameIdle, "resources/player/caveman/idle/" + textureNameIdle + ".png");

            String textureNameDamaged = "caveman_damaged_" + direction.name() + "_1";
            saveTexture(textureNameDamaged, "resources/player/caveman/damaged/" + textureNameDamaged + ".png");

            for (int i = 1; i <= 5; i++) {
                String textureNameAttack = "caveman_attack_" + direction.name() + "_" + i;
                saveTexture(textureNameAttack, "resources/player/caveman/attack/" + textureNameAttack + ".png");
            }

            for (int i = 1; i <= 8; i++) {
                String textureNameWalk = "caveman_walk_" + direction.name() + "_" + i;
                saveTexture(textureNameWalk, "resources/player/caveman/walk/" + textureNameWalk + ".png");
            }

            for (int i = 1; i <= 3; i++) {
                String textureNameDeath = "caveman_death_" + direction.name() + "_" + i;
                saveTexture(textureNameDeath, "resources/player/caveman/death/" + textureNameDeath + ".png");
            }

            for (int i = 1; i <= 5; i++) {
                String textureNameInteract = "caveman_interact_" + direction.name() + "_" + i;
                saveTexture(textureNameInteract, "resources/player/caveman/interact/" + textureNameInteract + ".png");
            }

        }

        // Add all archer sprites
        for (Direction direction : Direction.values()) {

            String textureNameIdle = "archer_idle_" + direction.name() + "_1";
            saveTexture(textureNameIdle, "resources/player/archer/idle/" + textureNameIdle + ".png");

            for (int i = 1; i <= 5; i++) {
                String textureNameAttack = "archer_attack_" + direction.name() + "_" + i;
                saveTexture(textureNameAttack, "resources/player/archer/attack/" + textureNameAttack + ".png");
            }

            for (int i = 1; i <= 8; i++) {
                String textureNameWalk = "archer_walk_" + direction.name() + "_" + i;
                saveTexture(textureNameWalk, "resources/player/archer/walk/" + textureNameWalk + ".png");
            }

            for (int i = 1; i <= 7; i++) {
                String textureNameDeath = "archer_death_" + direction.name() + "_" + i;
                saveTexture(textureNameDeath, "resources/player/archer/death/" + textureNameDeath + ".png");
            }

            for (int i = 1; i <= 5; i++) {
                String textureNameInteract = "archer_interact_" + direction.name() + "_" + i;
                saveTexture(textureNameInteract, "resources/player/archer/interact/" + textureNameInteract + ".png");
            }
        }

        // Tiles
        // Maybe just load everything from this directory

        for (File file : new File("resources/tiles/").listFiles()) {
            saveTexture(file.getName().replaceFirst(".png", ""), file.getPath());
        }


        // CollisionMask debug
        saveTexture("POINT_HIGHLIGHT", "resources/collisionmasks/Point2D_highlight.png");
        saveTexture("CIRCLE_HIGHLIGHT", "resources/collisionmasks/Circle2D_highlight.png");
        saveTexture("BOX_HIGHLIGHT", "resources/collisionmasks/Box2D_highlight.png");

        saveTexture("turbofish", "resources/rustyfish/turbofish.png");
        saveTexture("rustacean", "resources/rustyfish/rustacean.png");
        saveTexture("boatman", "resources/rustyfish/boatman.png");


        //bear walk texture
        for (int i = 1; i < 9; i++) {
            saveTexture("bear_walk_E_" + i, "resources/enemies/bear/walk/bear_walk_E_" + i + ".png");
            saveTexture("bear_walk_N_" + i, "resources/enemies/bear/walk/bear_walk_N_" + i + ".png");
            saveTexture("bear_walk_NE_" + i, "resources/enemies/bear/walk/bear_walk_NE_" + i + ".png");
            saveTexture("bear_walk_NW_" + i, "resources/enemies/bear/walk/bear_walk_NW_" + i + ".png");
            saveTexture("bear_walk_S_" + i, "resources/enemies/bear/walk/bear_walk_S_" + i + ".png");
            saveTexture("bear_walk_SE_" + i, "resources/enemies/bear/walk/bear_walk_SE_" + i + ".png");
            saveTexture("bear_walk_SW_" + i, "resources/enemies/bear/walk/bear_walk_SW_" + i + ".png");
            saveTexture("bear_walk_W_" + i, "resources/enemies/bear/walk/bear_walk_W_" + i + ".png");
        }

        for(int i =1;i<8;i++){
            //bear attack texture
            saveTexture("bear_attack_E_"+i,"resources/enemies/bear/attack/bear_attack_E_"+i+".png");
            saveTexture("bear_attack_N_"+i,"resources/enemies/bear/attack/bear_attack_N_"+i+".png");
            saveTexture("bear_attack_NE_"+i,"resources/enemies/bear/attack/bear_attack_NE_"+i+".png");
            saveTexture("bear_attack_NW_"+i,"resources/enemies/bear/attack/bear_attack_NW_"+i+".png");
            saveTexture("bear_attack_S_"+i,"resources/enemies/bear/attack/bear_attack_S_"+i+".png");
            saveTexture("bear_attack_SE_"+i,"resources/enemies/bear/attack/bear_attack_SE_"+i+".png");
            saveTexture("bear_attack_SW_"+i,"resources/enemies/bear/attack/bear_attack_SW_"+i+".png");
            saveTexture("bear_attack_W_"+i,"resources/enemies/bear/attack/bear_attack_W_"+i+".png");
            //squirrel attack texture
            saveTexture("squirrel_attack_E_"+i,"resources/enemies/squirrel/attack/squirrel_attack_E_"+i+".png");
            saveTexture("squirrel_attack_N_"+i,"resources/enemies/squirrel/attack/squirrel_attack_N_"+i+".png");
            saveTexture("squirrel_attack_NE_"+i,"resources/enemies/squirrel/attack/squirrel_attack_NE_"+i+".png");
            saveTexture("squirrel_attack_NW_"+i,"resources/enemies/squirrel/attack/squirrel_attack_NW_"+i+".png");
            saveTexture("squirrel_attack_S_"+i,"resources/enemies/squirrel/attack/squirrel_attack_S_"+i+".png");
            saveTexture("squirrel_attack_SE_"+i,"resources/enemies/squirrel/attack/squirrel_attack_SE_"+i+".png");
            saveTexture("squirrel_attack_SW_"+i,"resources/enemies/squirrel/attack/squirrel_attack_SW_"+i+".png");
            saveTexture("squirrel_attack_W_"+i,"resources/enemies/squirrel/attack/squirrel_attack_W_"+i+".png");
            //squirrel walk texture
            saveTexture("squirrel_walk_E_"+i,"resources/enemies/squirrel/walk/squirrel_walk_E_"+i+".png");
            saveTexture("squirrel_walk_N_"+i,"resources/enemies/squirrel/walk/squirrel_walk_N_"+i+".png");
            saveTexture("squirrel_walk_NE_"+i,"resources/enemies/squirrel/walk/squirrel_walk_NE_"+i+".png");
            saveTexture("squirrel_walk_NW_"+i,"resources/enemies/squirrel/walk/squirrel_walk_NW_"+i+".png");
            saveTexture("squirrel_walk_S_"+i,"resources/enemies/squirrel/walk/squirrel_walk_S_"+i+".png");
            saveTexture("squirrel_walk_SE_"+i,"resources/enemies/squirrel/walk/squirrel_walk_SE_"+i+".png");
            saveTexture("squirrel_walk_SW_"+i,"resources/enemies/squirrel/walk/squirrel_walk_SW_"+i+".png");
            saveTexture("squirrel_walk_W_"+i,"resources/enemies/squirrel/walk/squirrel_walk_W_"+i+".png");
            //squirrel death texture
            saveTexture("squirrel_death_E_"+i,"resources/enemies/squirrel/death/squirrel_death_E_"+i+".png");
            saveTexture("squirrel_death_N_"+i,"resources/enemies/squirrel/death/squirrel_death_N_"+i+".png");
            saveTexture("squirrel_death_NE_"+i,"resources/enemies/squirrel/death/squirrel_death_NE_"+i+".png");
            saveTexture("squirrel_death_NW_"+i,"resources/enemies/squirrel/death/squirrel_death_NW_"+i+".png");
            saveTexture("squirrel_death_S_"+i,"resources/enemies/squirrel/death/squirrel_death_S_"+i+".png");
            saveTexture("squirrel_death_SE_"+i,"resources/enemies/squirrel/death/squirrel_death_SE_"+i+".png");
            saveTexture("squirrel_death_SW_"+i,"resources/enemies/squirrel/death/squirrel_death_SW_"+i+".png");
            saveTexture("squirrel_death_W_"+i,"resources/enemies/squirrel/death/squirrel_death_W_"+i+".png");
        }
        for(int i =1;i<7;i++){
            saveTexture("bear_death_E_"+i,"resources/enemies/bear/death/bear_death_E_"+i+".png");
            saveTexture("bear_death_N_"+i,"resources/enemies/bear/death/bear_death_N_"+i+".png");
            saveTexture("bear_death_NE_"+i,"resources/enemies/bear/death/bear_death_NE_"+i+".png");
            saveTexture("bear_death_NW_"+i,"resources/enemies/bear/death/bear_death_NW_"+i+".png");
            saveTexture("bear_death_S_"+i,"resources/enemies/bear/death/bear_death_S_"+i+".png");
            saveTexture("bear_death_SE_"+i,"resources/enemies/bear/death/bear_death_SE_"+i+".png");
            saveTexture("bear_death_SW_"+i,"resources/enemies/bear/death/bear_death_SW_"+i+".png");
            saveTexture("bear_death_W_"+i,"resources/enemies/bear/death/bear_death_W_"+i+".png");
        }

    }


    /**
     * Gets a texture object for a given string id
     *
     * @param id Texture identifier
     * @return Texture for given id
     */
    public Texture getTexture(String id) {
        return getTextureRegion(id).getTexture();
    }

    /**
     * Gets a texture region object for a given string id
     *
     * @param id Texture identifier
     * @return TextureRegion for given id
     */
    public TextureRegion getTextureRegion(String id) {
        if (textureMap.containsKey(id)) {
            return textureMap.get(id);
        } else {
            return textureMap.get("spacman_ded");
        }
    }

    /**
     * Creates multiple textures from the given texture by splitting it into a grid
     * the size of spriteNames and save each area with the corresponding string in
     * spriteNames
     *
     * @param textureId   The texture to create the sprite sheet from
     * @param spriteNames The names for all the sprite textures created
     */
    public static void saveFromSpriteSheet(String textureId, String[][] spriteNames) {
        TextureRegion region = textureMap.get(textureId);
        int height = region.getRegionHeight() / spriteNames.length;
        for (int y = 0; y < spriteNames.length; y++) {
            int width = region.getRegionWidth() / spriteNames[y].length;
            for (int x = 0; x < spriteNames[y].length; x++) {
                textureMap.put(spriteNames[y][x], new TextureRegion(region, x * width, y * height, width, height));
            }
        }
    }

    /**
     * Saves a texture with a given id
     *
     * @param id       Texture id
     * @param filename Filename within the assets folder
     */
    public static void saveTexture(String id, String filename) {
        LOGGER.info("Saving texture" + id + " with Filename " + filename);
        if (!textureMap.containsKey(id)) {
            textureMap.put(id, new TextureRegion(new Texture(filename)));
        }
    }
}
