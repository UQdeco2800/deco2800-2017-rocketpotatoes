package com.deco2800.potatoes.entities.portals;

import java.util.Arrays;

import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.deco2800.potatoes.entities.health.HasProgressBar;
import com.deco2800.potatoes.entities.health.ProgressBar;

import com.deco2800.potatoes.managers.SoundManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.entities.ResourceEntity;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;
import com.deco2800.potatoes.entities.trees.DamageTree;
import com.deco2800.potatoes.gui.DebugModeGui;
import com.deco2800.potatoes.gui.WorldChangeGui;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.GuiManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.managers.SoundManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.util.Box3D;
import com.deco2800.potatoes.worlds.WorldType;

/**
 * A class for creating the base portal. This class differs from AbstracPortals
 * because the base portal needs health.
 *
 * @author Jordan Holder, Katie Gray
 */
public class BasePortal extends MortalEntity implements Tickable, HasProgressBar {

    /*
     * Progress bar to display health of base portal
     */
    // private static final ProgressBarEntity PROGRESS_BAR = new ProgressBarEntity("healthbar", 2);

    private static final ProgressBarEntity progressBar = new ProgressBarEntity("healthbar", 1);
    private static final transient String TEXTURE = "forest_portal";


    /*
     * Logger for all info/warning/error logs
     */
    private static final transient Logger LOGGER = LoggerFactory.getLogger(ResourceEntity.class);
    /*
     *  Create a player manager.
     */
    private PlayerManager playerManager = GameManager.get().getManager(PlayerManager.class);
    /*
     * The radius of which a collision can be detected
	 */

    private static final float CHANGE = (float) 0.2;
    /*
     * The array of calculatePositions where a collision needs to be checked
     */
    private static final float[][] POSITIONS = {{CHANGE, 0}, {CHANGE, CHANGE}, {0, CHANGE}, {-CHANGE, CHANGE},
            {-CHANGE, 0}, {-CHANGE, -CHANGE}, {0, -CHANGE}, {-CHANGE, -CHANGE}};

    /**
     * This instantiates an BasePortal given the appropriate parameters.
     *
     * @param posX      the x coordinate of the spite
     * @param posY      the y coordinate of the sprite
     * @param posZ      the z coordinate of the sprite
     * @param maxHealth the maximum health for the base portal
     */
    public BasePortal(float posX, float posY, float posZ, float maxHealth) {
        super(posX, posY, posZ, 3, 2.3f, 3, TEXTURE, maxHealth);
    }

    @Override
    public void onTick(long time) {
        float xPos = getPosX();
        float yPos = getPosY();
        boolean collided = false;
        AbstractEntity player = null;

        Box3D newPos = getBox3D();
        newPos.setX(xPos);
        newPos.setY(yPos);

        Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();
        // Check surroundings
        for (AbstractEntity entity : entities.values()) {
            if (!(entity instanceof Player)) {
                continue;
            }

            // Player detected
            player = entity;

            for (int i = 0; i < 8; i++) {
                newPos.setX(xPos + POSITIONS[i][0]);
                newPos.setY(yPos + POSITIONS[i][1]);
                // Player next to this resource
                if (newPos.overlaps(entity.getBox3D())) {
                    collided = true;
                }
            }
        }

        // remove from game world and add to inventory if a player has collided with
        // this resource
        if (collided) {
            try {
                LOGGER.info("Entered portal");
                //play warping sound effect
                SoundManager soundManager = new SoundManager();
                soundManager.playSound("warpSound.wav");
                //remove player from old world
                GameManager.get().getWorld().removeEntity(player);
				//change to new world
				GameManager.get().getManager(WorldManager.class).setWorld(WorldType.VOLCANO_WORLD);
				//add player to new world
	            GameManager.get().getWorld().addEntity(playerManager.getPlayer());
	            //add some entities to the test world (adds every time, kinda bad)
	            GameManager.get().getWorld().addEntity(new DamageTree(16, 11, 0));
	            GameManager.get().getWorld().addEntity(new AbstractPortal(1, 2, 0, "iceland_portal"));
				// Bring up portal interface
                ((WorldChangeGui) GameManager.get().getManager(GuiManager.class).getGui(WorldChangeGui.class)).show();

            } catch (Exception e) {
                LOGGER.warn("Issue entering portal; " + e);
            }

        }
    }

    @Override
    public ProgressBar getProgressBar() {
        return progressBar;
    }
}

