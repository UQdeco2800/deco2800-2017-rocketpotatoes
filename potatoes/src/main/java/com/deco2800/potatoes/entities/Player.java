package com.deco2800.potatoes.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.entities.effects.ExplosionEffect;
import com.deco2800.potatoes.entities.enemies.EnemyEntity;
import com.deco2800.potatoes.entities.enemies.Moose;
import com.deco2800.potatoes.entities.enemies.Squirrel;
import com.deco2800.potatoes.entities.health.*;
import com.deco2800.potatoes.entities.projectiles.PlayerProjectile;
import com.deco2800.potatoes.entities.projectiles.Projectile;
import com.deco2800.potatoes.entities.projectiles.Projectile.ProjectileType;
import com.deco2800.potatoes.entities.resources.*;
import com.deco2800.potatoes.entities.trees.*;
import com.deco2800.potatoes.gui.RespawnGui;
import com.deco2800.potatoes.gui.TreeShopGui;
import com.deco2800.potatoes.managers.*;
import com.deco2800.potatoes.renderering.Render3D;
import com.deco2800.potatoes.util.Box3D;
import com.deco2800.potatoes.util.WorldUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

/**
 * Entity for the playable character.
 * 
 * @author leggy, petercondoleon
 * 
 */
public class Player extends MortalEntity implements Tickable, HasProgressBar, HasDirection {

    private static final transient Logger LOGGER = LoggerFactory.getLogger(Player.class);
    private static final transient float HEALTH = 200f;
    private static final transient String TEXTURE_RIGHT = "player_right";
    private static final transient String TEXTURE_LEFT = "player_left";
    private static final ProgressBarEntity PROGRESS_BAR = new ProgressBarEntity("healthbar", 4);

    private float movementSpeed;
    private float speedx;
    private float speedy;
    private int respawnTime = 5000; // milliseconds
    private Inventory inventory;

    private Vector2 oldPos = Vector2.Zero; // Used to determine the player's change in direction
    private Direction currentDirection; // The direction the player faces
    private int checkKeyDown = 0; // an integer to check if key down has been pressed before key up

    public enum PlayerState { idle, walk, attack, damaged, death };    // The states a player may take
    private PlayerState currentState = PlayerState.idle;    // The current states of the player, set to idle by default
    
    private Map<Direction, Map<PlayerState, String[]>> spriteDirectionMap; // The map containing all player textures


    /**
     * Default constructor for the purposes of serialization
     */
    public Player() {
        super(0, 0, 0, 0.30f, 0.30f, 0.30f, 1f, 1f, TEXTURE_RIGHT, HEALTH);
    }

    /**
     * Creates a new Player instance.
     *
     * @param posX The x-coordinate.
     * @param posY The y-coordinate.
     * @param posZ The z-coordinate.
     */
    public Player(float posX, float posY, float posZ) {
        super(posX, posY, posZ, 0.30f, 0.30f, 0.30f, 1f, 1f, TEXTURE_RIGHT, HEALTH);
        this.movementSpeed = 0.075f;
        this.speedx = 0.0f;
        this.speedy = 0.0f;
        this.currentDirection = Direction.SouthEast;
        
        /* Initialise the inventory with the valid resources */
        addResources();
    }
    
    /**
     * Initialises the inventory with all the resources in the game.
     */
    private void addResources() {
    	HashSet<Resource> startingResources = new HashSet<Resource>();        
        this.inventory = new Inventory(startingResources);
    }

    /**
     * Add a state to the player. For example, if the player
     * is walking, then set the 'walk' state to the player.
     *
     * @param state 
     * 			The state to set.
     */
    public void setState(PlayerState state) {
        this.currentState = state;
        // TODO: Can only change state if in the base state. Prevents 
        // the player from stopping its attack or damage while walking.
    }

    /**
     * Returns true if the player is currently in the specified state.
     *
     * @param state 
     * 			A state the player may take.
     * 
     * @return true if the player has the current state and false
     * 			otherwise.
     */
    public boolean hasState(PlayerState state) {
    		return this.currentState.equals(state) ? true : false;
    }

    /**
     * Sets the player's state to the default state; that being idle.
     */
    public void clearState() {
    		this.currentState = PlayerState.idle;
    }

    /**
     * Returns the current Direction of the player.
     */
    @Override
    public Direction getDirection() {
        return currentDirection;
    }

    /**
     * Sets the direction of the player based on a specified direction.
     *
     * @param direction 
     * 			The direction to set the player to.
     */
    public void setDirection(Direction direction) {
        if (this.currentDirection != direction) {
            this.currentDirection = direction;
            LOGGER.info("Set player direction to " + direction);
            updateSprites();
        }
    }

    /**
     * Updates the direction of the player based on change in position.
     */
    private void updateDirection() {
        if ((this.getPosX() - oldPos.x == 0) && (this.getPosY() - oldPos.y == 0)) {
            return;    // Not moving
        }
        double angularDirection = Math.atan2(this.getPosY() - oldPos.y, this.getPosX() - oldPos.x) * (180 / Math.PI);

        if (angularDirection >= -180 && angularDirection < -157.5) {
            this.setDirection(Direction.SouthWest);
        } else if (angularDirection >= -157.5 && angularDirection < -112.5) {
            this.setDirection(Direction.West);
        } else if (angularDirection >= -112.5 && angularDirection < -67.5) {
            this.setDirection(Direction.NorthWest);
        } else if (angularDirection >= -67.5 && angularDirection < -22.5) {
            this.setDirection(Direction.North);
        } else if (angularDirection >= -22.5 && angularDirection < 22.5) {
            this.setDirection(Direction.NorthEast);
        } else if (angularDirection >= 22.5 && angularDirection < 67.5) {
            this.setDirection(Direction.East);
        } else if (angularDirection >= 67.5 && angularDirection < 112.5) {
            this.setDirection(Direction.SouthEast);
        } else if (angularDirection >= 112.5 && angularDirection < 157.5) {
            this.setDirection(Direction.South);
        } else if (angularDirection >= 157.5 && angularDirection <= 180) {
            this.setDirection(Direction.SouthWest);
        }

        oldPos = new Vector2(this.getPosX(), this.getPosY());
    }

    /**
     * Updates the player sprite based on it's state and direction.
     */
    public void updateSprites() {
        String type = "wizard";
        String state = "_idle";
        String direction = "_" + this.getDirection().toString();
        String frame = "_1";


        // Determine the player state
        if (this.hasState(PlayerState.damaged)) {
            state = "_damaged";
            if (type.equals("caveman")) {
                state = "_attack";
                frame = "_3";
            }
        }

        this.setTexture(type + state + direction + frame);
    }

    /**
     * Returns the player inventory.
     * <p>
     * Returns the inventory specific to the player.
     */
    public Inventory getInventory() {
        return this.inventory;
    }
    
    /**
     * A method for damaging the player's health. Allows the damaged
     * state to be enabled and respective animations to play.
     * 
     * @param amount
     * 			The amount of damage to deal to the player.
     */
    @Override
    public boolean damage(float amount) {
    		if (!this.hasState(PlayerState.damaged)) {
    			this.setState(PlayerState.damaged);
                this.updateSprites();
                EventManager eventManager = GameManager.get().getManager(EventManager.class);
                eventManager.registerEvent(this, new PlayerDamagedEvent(200));
    		}
    		return super.damage(amount);
    }

    @Override
    public void onTick(long arg0) {
        float newPosX = this.getPosX() + speedx;
        float newPosY = this.getPosY() + speedy;
        float length = GameManager.get().getWorld().getLength();
        float width = GameManager.get().getWorld().getWidth();

        Box3D newPos = getBox3D();
        newPos.setX(newPosX);
        newPos.setY(newPosY);

        float speedScale = GameManager.get().getWorld()
                .getTerrain(Math.round(Math.min(newPosX, width - 1)), Math.round(Math.min(newPosY, length - 1)))
                .getMoveScale();
        newPosX -= speedx * (1 - speedScale);
        newPosY -= speedy * (1 - speedScale);

        Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();
        boolean collided = false;
        for (AbstractEntity entity : entities.values()) {
            if (!this.equals(entity) && !(entity instanceof Squirrel) && !(entity instanceof Moose) && !(entity instanceof Projectile) && !(entity instanceof Effect)
                    && newPos.overlaps(entity.getBox3D())) {
                LOGGER.info(this + " colliding with " + entity);
                collided = true;
            }

            if (!this.equals(entity) && (entity instanceof EnemyEntity) && newPos.overlaps(entity.getBox3D())&& !(entity instanceof Moose)) {
                collided = true;
            }
        }
        if (!collided) {
            this.setPosX(Math.max((float) Math.min(newPosX, width), 0));
            this.setPosY(Math.max((float) Math.min(newPosY, length), 0));
        }

        updateDirection();
    }

    /**
     * Handle movement when wasd keys are pressed down
     *
     * @param keycode
     */
    public void handleKeyDown(int keycode) {

        switch (keycode) {
            case Input.Keys.W:
                speedy -= movementSpeed;
                speedx += movementSpeed;
                break;
            case Input.Keys.S:
                speedy += movementSpeed;
                speedx -= movementSpeed;
                break;
            case Input.Keys.A:
                speedx -= movementSpeed;
                speedy -= movementSpeed;
                break;
            case Input.Keys.D:
                speedx += movementSpeed;
                speedy += movementSpeed;
                break;
            case Input.Keys.T:
                tossItem(new SeedResource());
                break;
            case Input.Keys.F:
                tossItem(new FoodResource());
                break;
            case Input.Keys.E:
                harvestResources();
                break;
            case Input.Keys.NUM_1:
                if (!WorldUtil.getEntityAtPosition(getCursorCoords().x, getCursorCoords().y).isPresent()) {
                    AbstractTree.constructTree(new Tower(getCursorCoords().x, getCursorCoords().y, 0));
                }
                break;
            case Input.Keys.NUM_2:
                if (!WorldUtil.getEntityAtPosition(getCursorCoords().x, getCursorCoords().y).isPresent()) {
                    AbstractTree.constructTree(new ResourceTree(getCursorCoords().x, getCursorCoords().y, 0));
                }
                break;
            case Input.Keys.NUM_3:
                if (!WorldUtil.getEntityAtPosition(getCursorCoords().x, getCursorCoords().y).isPresent()) {
                    AbstractTree.constructTree(
                            new ResourceTree(getCursorCoords().x, getCursorCoords().y, 0, new FoodResource(), 8));
                }
            case Input.Keys.NUM_4:
                if (!WorldUtil.getEntityAtPosition(getCursorCoords().x, getCursorCoords().y).isPresent()) {
                    AbstractTree.constructTree(
                            new DamageTree(getCursorCoords().x, getCursorCoords().y, 0, new IceTree()));
                }
            case Input.Keys.NUM_5:
                if (!WorldUtil.getEntityAtPosition(getCursorCoords().x, getCursorCoords().y).isPresent()) {
                    AbstractTree.constructTree(
                            new DamageTree(getCursorCoords().x, getCursorCoords().y, 0, new LightningTree()));
                }
            case Input.Keys.NUM_6:
                if (!WorldUtil.getEntityAtPosition(getCursorCoords().x, getCursorCoords().y).isPresent()) {
                    AbstractTree.constructTree(
                            new DamageTree(getCursorCoords().x, getCursorCoords().y, 0, new FireTree()));
                }
            case Input.Keys.NUM_7:
                if (!WorldUtil.getEntityAtPosition(getCursorCoords().x, getCursorCoords().y).isPresent()) {
                    AbstractTree.constructTree(
                            new DamageTree(getCursorCoords().x, getCursorCoords().y, 0, new AcornTree()));
                }
            case Input.Keys.SPACE:
                float pPosX = GameManager.get().getManager(PlayerManager.class).getPlayer().getPosX();
                float pPosY = GameManager.get().getManager(PlayerManager.class).getPlayer().getPosY();
                float pPosZ = GameManager.get().getManager(PlayerManager.class).getPlayer().getPosZ();
                
                Optional<AbstractEntity> target = null;
                target = WorldUtil.getClosestEntityOfClass(EnemyEntity.class, pPosX, pPosY);
                    
                if (target.isPresent()) {
                		float targetPosX = target.get().getPosX();
                		float targetPosY = target.get().getPosY();
                    
                		switch (this.getDirection()) {
                		case North:
                			break;
                		case NorthEast:
                			pPosY -= 1;
                			pPosX += 1.5;
                			break;
                		case East:
                			pPosY -= 1;
                			pPosX += 1.5;
                			break;
                		case SouthEast:
                			pPosX += 1;
                			break;
                		case South:
                			pPosX += 1.2;
                			break;
                		case SouthWest:
                			pPosY += 1;
                			pPosX += 1;
                			break;
                		case West:
                			break;
                		case NorthWest:
                			break;
                		default:
                			break;
                		}
                		GameManager.get().getWorld().addEntity(new PlayerProjectile(target.get().getClass(), pPosX-1, pPosY, pPosZ,  1f, 100, ProjectileType.ROCKET, null,
                                /*new ExplosionEffect(target1.get().getClass(), target1.get().getPosX() -2, target1.get().getPosY(), target1.get().getPosZ(), 0, 2f)*/null, this.getDirection().toString(),targetPosX,targetPosY));
                } else if (!target.isPresent()) {
                    //Disable shooting when no enemies is present until new fix is found.
                }
                break;
            case Input.Keys.ESCAPE:
                ((TreeShopGui) (GameManager.get().getManager(GuiManager.class).getGui(TreeShopGui.class))).closeShop();
            default:
                break;
        }
        checkKeyDown++;
    }

    private Vector2 getCursorCoords() {
        Vector3 worldCoords = Render3D.screenToWorldCoordiates(Gdx.input.getX(), Gdx.input.getY(), 0);
        Vector2 coords = Render3D.worldPosToTile(worldCoords.x, worldCoords.y);
        return new Vector2((int) Math.floor(coords.x), (int) Math.floor(coords.y));
    }

    /**
     * Handles removing an item from an inventory and placing it on the map.
     *
     * @param item The resource to be thrown.
     */
    private void tossItem(Resource item) {
        // tosses a item in front of player
        float x = this.getPosX();
        float y = this.getPosY();
        float z = this.getPosZ();

        x = (currentDirection == Direction.SouthWest) ? x - 1 : x + 1;
        y = (currentDirection == Direction.SouthWest) ? y - 2 : y + 2;

        // only toss an item if there are items to toss
        if (this.getInventory().updateQuantity(item, -1) == 1) {
            GameManager.get().getWorld().addEntity(new ResourceEntity(x, y, z, item));
        }

    }

    /**
     * Handles harvesting resources from resource tree that are in range. Resources
     * are added to the player's inventory.
     */
    private void harvestResources() {
        double interactRange = 3f; // TODO: Could this be a class variable?
        Collection<AbstractEntity> entities = GameManager.get().getWorld().getEntities().values();
        boolean didHarvest = false;
        for (AbstractEntity entitiy : entities) {
            if (entitiy instanceof ResourceTree && entitiy.distance(this) <= interactRange
                    && ((ResourceTree) entitiy).getGatherCount() > 0) {
                didHarvest = true;
                ((ResourceTree) entitiy).transferResources(this.inventory);

            }
        }
        if (didHarvest) {
            GameManager.get().getManager(SoundManager.class).playSound("harvesting.mp3");
        }
    }

    /**
     * Checks to see whether the player moving out of the map
     */
    private boolean outOfBounds() {
        int width = GameManager.get().getWorld().getWidth();
        int height = GameManager.get().getWorld().getLength();
        if (this.getPosX() > width - 0.2 || this.getPosX() < 0 || this.getPosY() > height - 0.2 || this.getPosY() < 0) {
            return true;
        }
        return false;
    }

    /**
     * Handle movement when wasd keys are released
     *
     * @param keycode
     */
    public void handleKeyUp(int keycode) {
        // checks if key down is pressed first
        if (checkKeyDown <= 0)
            return;
        switch (keycode) {
            case Input.Keys.W:
                speedy += movementSpeed;
                speedx -= movementSpeed;
                break;
            case Input.Keys.S:
                speedy -= movementSpeed;
                speedx += movementSpeed;
                break;
            case Input.Keys.A:
                speedx += movementSpeed;
                speedy += movementSpeed;
                break;
            case Input.Keys.D:
                speedx -= movementSpeed;
                speedy -= movementSpeed;
                break;
            default:
                break;
        }
        checkKeyDown--;
    }

    @Override
    public String toString() {
        return "The player";
    }

    @Override
    public ProgressBar getProgressBar() {
        return PROGRESS_BAR;
    }

    @Override
    public void deathHandler() {
        LOGGER.info(this + " is dead.");
        // destroy the player
        GameManager.get().getWorld().removeEntity(this);
        // play Wilhelm scream sound effect TODO Probably find something better for this...if you can ;)
        SoundManager soundManager = new SoundManager();
        soundManager.playSound("wilhelmScream.wav");
        // get the event manager
        EventManager eventManager = GameManager.get().getManager(EventManager.class);
        // add the respawn event
        eventManager.registerEvent(this, new RespawnEvent(respawnTime));

        GameManager.get().getManager(GuiManager.class).getGui(RespawnGui.class).show();
    }

}
