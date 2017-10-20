package com.deco2800.potatoes.gui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.potatoes.entities.*;
import com.deco2800.potatoes.entities.enemies.Moose;
import com.deco2800.potatoes.entities.enemies.Squirrel;
import com.deco2800.potatoes.entities.enemies.TankEnemy;
import com.deco2800.potatoes.entities.enemies.SpeedyEnemy;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.entities.resources.*;
import com.deco2800.potatoes.entities.trees.ProjectileTree;
import com.deco2800.potatoes.entities.trees.ResourceTree;
import com.deco2800.potatoes.managers.*;
import com.deco2800.potatoes.observers.KeyDownObserver;
import com.deco2800.potatoes.renderering.Render3D;
import com.deco2800.potatoes.screens.GameScreen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.deco2800.potatoes.worlds.terrain.Terrain;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

import static com.badlogic.gdx.utils.Align.left;

/**
 * Debug Mode Gui Class.
 * A feature to provide numerous abilities of illegal modifications of the game state.
 * More details at https://github.com/UQdeco2800/deco2800-2017-rocketpotatoes/wiki/Debug-'God'-Mode
 *
 *@author Tze Lok Cheng
 */
public class DebugModeGui extends Gui {

    private GameScreen screen;
    private Stage stage;

    //Labels
    private Skin uiSkin;
    private VerticalGroup debugButtonGroup;
    private Label debugOn;
    private Label spawnCommands;
    private Label speedtoggle;
    private Label gamespeed;
    private Label f1;
    private Label f2;
    private Label f3;
    private Label f4;
    private Label f5;
    private Label f6;
    private Label f7;
    private Label f8;
    private Label f9;

    //Buttons
    private TextButton resetButton;
    private TextButton addResourcesButton;
    private TextButton entitiesIMTButton;
    private TextButton immortalButton;
    private TextButton exitButton;
    private Table table;

    // State indicator
    private enum States {
        DEBUGON,
        DEBUGOFF
    }

    private States state = States.DEBUGOFF;

    /**
     * Constructor.
     *
     * @param stage The internal stage variable passed by the gui manager.
     * @param screen The game's GameScreen
     */
    public DebugModeGui(Stage stage, GameScreen screen){

        this.screen = screen;
        this.stage = stage;
        hidden = true;
        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
        table = new Table(uiSkin);

        // actors initialisation
        debugOn = new Label("Debug Options",uiSkin);
        speedtoggle = new Label("Slow down [F10]/Speed up[F11]",uiSkin);
        gamespeed = new Label("Game Speed: 1.0x",uiSkin);
        resetButton = new TextButton("Reset Map", uiSkin);
        addResourcesButton = new TextButton("All Resources +10", uiSkin);
        immortalButton = new TextButton("Immortality", uiSkin);
        entitiesIMTButton = new TextButton("Other Existing Entities Immortal", uiSkin);
        spawnCommands = new Label("SPAWN COMMANDS",uiSkin);
        f1 = new Label("F1: Projectile Tree",uiSkin);
        f2 = new Label("F2: Squirrel",uiSkin);
        f3 = new Label("F3: Tank Enemy",uiSkin);
        f4 = new Label("F4: Seed Resource",uiSkin);
        f5 = new Label("F5: Seed Tree",uiSkin);
        f6 = new Label("F6: Moose Enemy",uiSkin);
        f7 = new Label("F7: Speedy Enemy",uiSkin);
        f8 = new Label("F8: Tile Ground",uiSkin);
        f9 = new Label("F9: Tile Water",uiSkin);
        exitButton = new TextButton("Exit Debug", uiSkin);

        // adding actors
        debugButtonGroup = new VerticalGroup();
        debugButtonGroup.addActor(debugOn);
        debugButtonGroup.addActor(exitButton);
        debugButtonGroup.addActor(speedtoggle);
        debugButtonGroup.addActor(gamespeed);
        debugButtonGroup.addActor(immortalButton);
        debugButtonGroup.addActor(entitiesIMTButton);
        debugButtonGroup.addActor(resetButton);
        debugButtonGroup.addActor(addResourcesButton);
        debugButtonGroup.addActor(spawnCommands);
        debugButtonGroup.addActor(f1);
        debugButtonGroup.addActor(f2);
        debugButtonGroup.addActor(f3);
        debugButtonGroup.addActor(f4);
        debugButtonGroup.addActor(f5);
        debugButtonGroup.addActor(f6);
        debugButtonGroup.addActor(f7);
        debugButtonGroup.addActor(f8);
        debugButtonGroup.addActor(f9);
        table.add(debugButtonGroup);

        setupListeners();
        resetGui(stage);
        table.setVisible(false);
        stage.addActor(table);
    }

    /**
     *Sets table and positions it to the left-middle side of the screen.
     * Adds all libgdx controls.
     *
     * @param stage The game's stage to be reset
     */
    private void resetGui(Stage stage) {

        table.reset();
        table.left();
        table.setPosition(0,stage.getHeight()/2, left);
        table.add(debugButtonGroup);
    }

    /**
     * Adds all button and key listeners for the activation of debug features.
     */
    private void setupListeners() {

        /* Listener for the exit debug button. */
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hide();
            }
        });

        /* Listener for the immortality button */
        immortalButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playerImmortal();
            }
        });

        /* Listener for the other entities immortality button */
        entitiesIMTButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                entitiesImmortal();
            }
        });
        
        
        /* Listener for the add resources button */
        addResourcesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addResources();
            }
        });

        /* Listener for the reset button */
        resetButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                removeEntities();
            }
        });

        GameManager.get().getManager(InputManager.class).addKeyDownListener(new KeyDownObserver() {
            @Override
            public void notifyKeyDown(int keycode) {
                float x = GameManager.get().getManager(InputManager.class).getMouseX();
                float y = GameManager.get().getManager(InputManager.class).getMouseY();

                //Converting mouse coordinates to tiles
                Vector3 coords = Render3D.screenToWorldCoordiates(x,y);
                Vector2 coords2 = Render3D.worldPosToTile(coords.x,coords.y);

                if (state == States.DEBUGON) {
                    if (keycode == Input.Keys.F1) {
                    	ProjectileTree tower = new ProjectileTree((int)coords2.x,(int)coords2.y);
                        tower.setProgress(0);
                        GameManager.get().getWorld().addEntity(tower);
                    }

                    if (keycode == Input.Keys.F2) {
                        GameManager.get().getWorld().addEntity(new Squirrel(coords2.x, coords2.y));
                    }

                    if (keycode == Input.Keys.F3) {
                        GameManager.get().getWorld().addEntity(new TankEnemy(coords2.x, coords2.y));
                    }

                    if (keycode == Input.Keys.F4) {
                        SeedResource seedResource = new SeedResource();
                        GameManager.get().getWorld().addEntity(new ResourceEntity(coords2.x, coords2.y, seedResource));
                    }

                    if (keycode == Input.Keys.F5) {
                         ResourceTree rscTree = new ResourceTree((int)coords2.x,(int)coords2.y);
                         rscTree.setProgress(0);
                         GameManager.get().getWorld().addEntity(rscTree);
                    
                     }
                    
                     if (keycode == Input.Keys.F6) {
                         GameManager.get().getWorld().addEntity(new Moose(coords2.x, coords2.y));
                     }
                     

                     if (keycode == Input.Keys.F7) {
                         GameManager.get().getWorld().addEntity(new SpeedyEnemy(coords2.x, coords2.y));
                     }
                     
                     if (keycode == Input.Keys.F8) {
                         Terrain g = new Terrain("mud_tile_1", 1, false);
                         GameManager.get().getWorld().setTile((int)coords2.y, (int)coords2.x,g);
                     }
                     
                     if (keycode == Input.Keys.F9) {
                         Terrain w = new Terrain("water_tile_1", 0, false);
                         GameManager.get().getWorld().setTile((int)coords2.y, (int)coords2.x,w);
                     
                     }
                     
                     if (keycode == Input.Keys.F10) {
                         screen.setTickrate(screen.getTickrate() * 2);
                         gamespeed.setText("Game Speed: " + 1/screen.getTickrate() + "x");
                     }
                     
                     if (keycode == Input.Keys.F11) {
                         screen.setTickrate(screen.getTickrate() / 2);
                         gamespeed.setText("Game Speed: " + 1/screen.getTickrate() + "x");
                     }
                }
            }
        });



    }

    /**
     * Shows the menu and sets the state of debug ON, allowing features to be be active.
     */
    @Override
	public void show() {
        table.setVisible(true);
        stage.addActor(table);
        state = States.DEBUGON;
        hidden = false;
    }

    /**
     * Hides the menu and sets the state of the debug OFF, disabling features.
     */
    @Override
	public void hide() {
        table.setVisible(false);
        state = States.DEBUGOFF;
        hidden = true;
    }

    /**
     * Toggles whether the menu is shown or hidden, using the hide() and show() methods.
     */
    public void toggle() {
        if (hidden) {
            show();
        } else {
            hide();
        }
    }

    /**
     * Heals the live player to full health and sets it to no longer take damage.
     */
    public void playerImmortal(){
        screen.menuBlipSound();
        immortalButton.setText("Player is already Immortal");
        GameManager.get().getManager(PlayerManager.class).getPlayer().heal(200);
        GameManager.get().getManager(PlayerManager.class).getPlayer().addDamageScaling(0);
    }

    /**
     * Sets all entities currently in the world (other than the player) to no longer take damage.
     */
    public void entitiesImmortal(){
        Map<Integer, AbstractEntity> entitiesMap = GameManager.get().getWorld().getEntities();
        for (AbstractEntity ent: entitiesMap.values()){
            if (ent instanceof MortalEntity && !(ent instanceof Player)){
                ((MortalEntity) ent).addDamageScaling(0);
            }
        }

    }

    /**
     * Adds +10 amount for every resource to the player's inventory.
     */
    public void addResources(){
        Set<Resource> rsc = new HashSet<Resource>();

        rsc.add(new BonesResource());
        rsc.add(new CoalResource());
        rsc.add(new CactusThornResource());
        rsc.add(new FishMeatResource());
        rsc.add(new IceCrystalResource());
        rsc.add(new ObsidianResource());
        rsc.add(new PearlResource());
        rsc.add(new PineconeResource());
        rsc.add(new PricklyPearResource());
        rsc.add(new SealSkinResource());
        rsc.add(new SeedResource());
        rsc.add(new SnowBallResource());
        rsc.add(new TreasureResource());
        rsc.add(new TumbleweedResource());
        rsc.add(new WoodResource());

        for (Resource rscname: rsc) {
            GameManager.get().getManager(PlayerManager.class).getPlayer().getInventory()
                    .updateQuantity(rscname, 10);
        }
    }

    /**
     * Removes all non-player entities currently in the world.
     */
    public void removeEntities(){
        Map<Integer, AbstractEntity> entitiesMap = GameManager.get().getWorld().getEntities();

        //Excludes the player
        for (AbstractEntity ent: entitiesMap.values()){
            if (!(ent instanceof Player)){
                GameManager.get().getWorld().removeEntity(ent);
            }
        }
    }
}
