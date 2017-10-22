package com.deco2800.potatoes.entities.portals;

import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.health.HasProgressBar;
import com.deco2800.potatoes.entities.health.ProgressBar;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;
import com.deco2800.potatoes.entities.resources.ResourceEntity;
import com.deco2800.potatoes.gui.WorldChangeGui;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.GuiManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class for creating the base portal. This class differs from AbstractPortals
 * because the base portal needs health.
 *
 * @author Jordan Holder, Katie Gray
 */
public class BasePortal extends AbstractPortal implements Tickable, HasProgressBar {

    /*
     * Progress bar to display health of base portal
     */
    private static final ProgressBarEntity progressBar = new ProgressBarEntity("healthBarGreen", "portalIcon", 1);
    /*
     * Base portal's texture
    */
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
     * @param maxHealth the maximum health for the base portal
     */
    public BasePortal(float posX, float posY, float maxHealth) {
        super(posX, posY, TEXTURE, maxHealth);
        setXRenderOffset(5);
        setYRenderOffset(5);
    }

    @Override
    public void onTick(long time) {
        boolean collided = this.preTick(time);
        // remove from game world and add to inventory if a player has collided with
        // this resource
        if (collided) {
            try {
                LOGGER.info("Entered portal");
				// Bring up portal interface
                GameManager.get().getManager(GuiManager.class).getGui(WorldChangeGui.class).show();

            } catch (Exception e) {
                LOGGER.warn("Issue entering portal; " + e);
            }

        }
    }

    @Override
    public ProgressBar getProgressBar() {
        return progressBar;
    }
    @Override
    public boolean showProgress() {
        return true;
    }

}

