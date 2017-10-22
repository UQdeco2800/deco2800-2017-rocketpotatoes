package com.deco2800.potatoes.entities.portals;

import com.deco2800.potatoes.collisions.Circle2D;
import com.deco2800.potatoes.collisions.Shape2D;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.entities.resources.ResourceEntity;
import com.deco2800.potatoes.gui.GameOverGui;
import com.deco2800.potatoes.gui.WorldChangeGui;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.GuiManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.worlds.ForestWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * A class that can create portals which are not the base portal. Because these
 * are not in the first world, there are no enemies and therefore these portals
 * do not have health. AbstractPortals need to be instantiated with an
 * appropriate texture.
 *
 * @author Jordan Holder, Katie Gray
 */
public class AbstractPortal extends MortalEntity implements Tickable {
    /*
     * Logger for all info/warning/error logs
     */
    private static final transient Logger LOGGER = LoggerFactory.getLogger(ResourceEntity.class);
    /* Create a player manager. */
    private PlayerManager playerManager = GameManager.get().getManager(PlayerManager.class);
    /*
     * The radius of which a collision can be detected
     */
    private static final float CHANGE = 0.2f;
    /*
     * The array of calculatePositions where a collision needs to be checked
     */
    private static final float[][] POSITIONS = {{CHANGE, 0}, {CHANGE, CHANGE}, {0, CHANGE}, {-CHANGE, CHANGE},
            {-CHANGE, 0}, {-CHANGE, -CHANGE}, {0, -CHANGE}, {-CHANGE, -CHANGE}};

    /*
     * The player entity.
     */
    private AbstractEntity player;

    // If the player was previous colliding
    boolean prevCollided = false;

    /**
     * This instantiates an AbstractPortal given the appropriate parameters.
     *
     * @param posX    the x coordinate of the spite
     * @param posY    the y coordinate of the sprite
     * @param texture the texture which represents the portal
     */
    public AbstractPortal(float posX, float posY, String texture) {
        this(posX, posY, texture, 999);
    }

    public AbstractPortal(float posX, float posY, String texture, float maxHealth) {
        super(new Circle2D(posX, posY, 2.11f), 3, 3, texture, maxHealth);
    }

    /**
     * This method is used to perform actions shared by abstract and base portals OnTick Methods.
     *
     * @param time    indicates length of time passed
     * @return collided Boolean value associated with collision events
     */
    public boolean preTick(long time) {
        float xPos = getPosX();
        float yPos = getPosY();
        boolean collided = false;

        Shape2D newPos = getMask();
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
                if (newPos.overlaps(entity.getMask())) {
                    collided = true;
                }
            }
        }

        if (prevCollided && !collided) {
            prevCollided = false;
            // Hide gui
            GameManager.get().getManager(GuiManager.class).getGui(WorldChangeGui.class).hide();
            return false;
        }
        else if (!prevCollided && collided) {
            prevCollided = true;
            return true;
        }
        return false;
    }

    /**
     * Returns the player entity.
     *
     * @return player
     * The entity associated with the player.
     */
    public AbstractEntity getPlayer() {
        return player;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deathHandler() {
        super.deathHandler();

        // End game
        GameManager.get().getManager(GuiManager.class).getGui(GameOverGui.class).show();
        GameManager.get().getWorld()
                .removeEntity(GameManager.get().getManager(PlayerManager.class).getPlayer());
    }

    @Override
    public void onTick(long time) {
        // Hacky fix for not changing worlds
        if (!(this instanceof BasePortal) && preTick(time)) {
            GameManager.get().getManager(GuiManager.class).getGui(WorldChangeGui.class).changeWorld(ForestWorld.get());
        }
    }
}
