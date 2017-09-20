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
import com.deco2800.potatoes.entities.portals.AbstractPortal;
import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;
import com.deco2800.potatoes.entities.resources.ResourceEntity;
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
public class BasePortal extends AbstractPortal implements Tickable {

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



    /**
     * This instantiates an BasePortal given the appropriate parameters.
     *
     * @param posX      the x coordinate of the spite
     * @param posY      the y coordinate of the sprite
     * @param posZ      the z coordinate of the sprite
     * @param maxHealth the maximum health for the base portal
     */
    public BasePortal(float posX, float posY, float posZ, float maxHealth) {
        super(posX, posY, posZ, TEXTURE);
    }
    
    /**
     * Brings up the world change GUI.
     */
    @Override
    public void changeWorld() {
    	// Bring up portal interface
        ((WorldChangeGui) GameManager.get().getManager(GuiManager.class).
        		getGui(WorldChangeGui.class)).show();
    }

  //  @Override
    public ProgressBar getProgressBar() {
        return progressBar;
    }
  //  @Overide
    public boolean showProgress() {
        return false;
    }
   // @Override
    public int getMaxProgress() {
        // TODO Auto-generated method stub
        return 1;
    }

}

