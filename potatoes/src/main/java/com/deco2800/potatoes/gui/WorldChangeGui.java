package com.deco2800.potatoes.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.entities.portals.AbstractPortal;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.managers.SoundManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.screens.GameScreen;
import com.deco2800.potatoes.worlds.DesertWorld;
import com.deco2800.potatoes.worlds.IceWorld;
import com.deco2800.potatoes.worlds.OceanWorld;
import com.deco2800.potatoes.worlds.VolcanoWorld;
import com.deco2800.potatoes.worlds.World;
import com.deco2800.potatoes.worlds.WorldType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A GUI that is displayed when the player enters the base portal. Allows players to teleport to
 * one of the 4 other worlds.
 * 
 * @author Jordan Holder
 *
 */
public class WorldChangeGui extends Gui {
    // logger for error/warning/info messages
    private static final Logger LOGGER = LoggerFactory.getLogger(WorldChangeGui.class);

    private GameScreen screen;
    private Stage stage;
    private Skin uiSkin;
    private VerticalGroup worldsButtonGroup;

    // buttons
    private TextButton worldOneButton;
    private TextButton worldTwoButton;
    private TextButton worldThreeButton;
    private TextButton worldFourButton;
    private TextButton exitButton;

    // table to hold buttons
    private Table table;

    // create a player manager
    private PlayerManager playerManager = GameManager.get().getManager(PlayerManager.class);

    /**
     * Creates an instance of the World Change Gui
     *
     * @param stage
     * @param screen
     */
    public WorldChangeGui(Stage stage, GameScreen screen){

        this.screen = screen;
        this.stage = stage;
        uiSkin = new Skin(Gdx.files.internal("menu/uiskin.json"));
        table = new Table(uiSkin);

        // actors initialisation
        worldOneButton = new TextButton("Desert World", uiSkin);
        worldTwoButton = new TextButton("Ice World", uiSkin);
        worldThreeButton = new TextButton("Volcano World", uiSkin);
        worldFourButton = new TextButton("Sea World", uiSkin);
        exitButton = new TextButton("Exit", uiSkin);

        // adding actors
        worldsButtonGroup = new VerticalGroup();
        worldsButtonGroup.addActor(worldOneButton);
        worldsButtonGroup.addActor(worldTwoButton);
        worldsButtonGroup.addActor(worldThreeButton);
        worldsButtonGroup.addActor(worldFourButton);
        worldsButtonGroup.addActor(exitButton);
        worldsButtonGroup.space(10);
        table.add(worldsButtonGroup);

        // create listeners for each button
        setupListeners();

        // set position of the gui
        resetGui(stage);
        // set the gui to initially be hidden
        table.setVisible(false);
        // add the table/buttons to the stage
        stage.addActor(table);
    }

    /**
     * Sets the position of the gui.
     *
     * @param stage
     * 			The game screen
     */
    private void resetGui(Stage stage) {
        table.reset();
        table.center();
        table.setWidth(stage.getWidth());
        table.setHeight(stage.getHeight());
        table.setPosition(0, 0);
        table.add(worldsButtonGroup);
    }

    /**
     * Creates listeners for each of the buttons.
     */
    private void setupListeners() {

        // Listener for the exit button.
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {            	
                // hide the gui
                hide();
                
                LOGGER.info("Exited base portal");
            }

        });

        // Listener to change to world 4
        worldFourButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                //play warping sound effect
                SoundManager soundManager = new SoundManager();
                soundManager.playSound("warpSound.wav");

                //change to world 4
                changeWorld(OceanWorld.get());
                
                LOGGER.info("Teleported to world 4");
            }
            
        });

        // Listener to change to world 3
        worldThreeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                //play warping sound effect
                SoundManager soundManager = new SoundManager();
                soundManager.playSound("warpSound.wav");

                //change to world 3
                changeWorld(VolcanoWorld.get());
                
                LOGGER.info("Teleported to world 3");
            }

        });


        // Listener to change to world 2
        worldTwoButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                //play warping sound effect
                SoundManager soundManager = new SoundManager();
                soundManager.playSound("warpSound.wav");

                //change to world 2
                changeWorld(IceWorld.get());
                
                LOGGER.info("Teleported to world 2");
            }

        });

        // Listener to change to world 1
        worldOneButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                //play warping sound effect
                SoundManager soundManager = new SoundManager();
                soundManager.playSound("warpSound.wav");

                //change to world 1
                changeWorld(DesertWorld.get());
                
                LOGGER.info("Teleported to world 1");
            }

        });

    }

    /**
     * Teleports the player to a different world.
     *
     * @param world
     * 			The key of the world to change to
     */
    public void changeWorld(WorldType world) {
        //remove player from old world
        Player p = GameManager.get().getManager(PlayerManager.class).getPlayer();
        GameManager.get().getWorld().removeEntity(p);
        // change to new world
        GameManager.get().getManager(WorldManager.class).setWorld(world);
        // The new world
        World newWorld = GameManager.get().getWorld();
        
        // Find the portal in the world
        for (AbstractEntity entity: newWorld.getEntities().values()) {
        	if (AbstractPortal.class.isAssignableFrom(entity.getClass())) {
        		
        		//set player to be next to the portal
        		playerManager.getPlayer().setPosition(entity.getPosX() + entity.getXRenderLength(),
        				entity.getPosY() + entity.getYRenderLength());
        	}               	
        	
        }
        
        // add player to new world
        GameManager.get().getWorld().addEntity(playerManager.getPlayer());

        // hide the world change gui
        hide();
    }
    
    /**
     * Shows the world change GUI.
     */
    @Override
    public void show() {
        table.setVisible(true);
        stage.addActor(table);
    }

    /**
     * Hides the world change GUI.
     */
    @Override
    public void hide() {
        table.setVisible(false);
    }
}
