package com.deco2800.potatoes.entities.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import com.deco2800.potatoes.entities.effects.AOEEffect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.collisions.Circle2D;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Direction;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.animation.TimeAnimation;
import com.deco2800.potatoes.entities.animation.TimeTriggerAnimation;
import com.deco2800.potatoes.entities.enemies.EnemyEntity;
import com.deco2800.potatoes.entities.health.HasProgressBar;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.entities.health.ProgressBar;
import com.deco2800.potatoes.entities.health.ProgressBarEntity;
import com.deco2800.potatoes.entities.health.RespawnEvent;
import com.deco2800.potatoes.entities.projectiles.BallisticProjectile;
import com.deco2800.potatoes.entities.projectiles.HomingProjectile;
import com.deco2800.potatoes.entities.projectiles.MineBomb;
import com.deco2800.potatoes.entities.projectiles.OrbProjectile;
import com.deco2800.potatoes.entities.projectiles.Projectile;
import com.deco2800.potatoes.entities.projectiles.Projectile.ProjectileTexture;
import com.deco2800.potatoes.entities.resources.FoodResource;
import com.deco2800.potatoes.entities.resources.Resource;
import com.deco2800.potatoes.entities.resources.ResourceEntity;
import com.deco2800.potatoes.entities.resources.SeedResource;
import com.deco2800.potatoes.entities.trees.AbstractTree;
import com.deco2800.potatoes.entities.trees.ResourceTree;
import com.deco2800.potatoes.gui.RespawnGui;
import com.deco2800.potatoes.gui.TreeShopGui;
import com.deco2800.potatoes.managers.EventManager;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.GuiManager;
import com.deco2800.potatoes.managers.InputManager;
import com.deco2800.potatoes.managers.Inventory;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.managers.SoundManager;
import com.deco2800.potatoes.managers.TreeState;
import com.deco2800.potatoes.renderering.Render3D;
import com.deco2800.potatoes.util.WorldUtil;

/**
 * Entity for the playable character.
 * <p>
 *
 * @author leggy, petercondoleon
 * <p>
 * <p>
 */
public class Player extends MortalEntity implements Tickable, HasProgressBar {

    private static final transient Logger LOGGER = LoggerFactory.getLogger(Player.class);
    private static final transient float HEALTH = 200f;
    protected ProgressBarEntity progressBar = new ProgressBarEntity("healthBarGreen", "archerIcon", 4);

    public enum PlayerShootMethod {
        DIRECTIONAL, CLOSEST, MOUSE
    }
    public enum ShootStage {
        READY, HOLDING, LOOSE
    }
    public ShootStage currentShootStage=ShootStage.READY;
    protected PlayerShootMethod shootingStyle=PlayerShootMethod.MOUSE;
    public Vector2 mousePos;
    public Projectile projectile;
    protected Class<?> projectileClass = BallisticProjectile.class;
    protected ProjectileTexture projectileTexture=ProjectileTexture.ROCKET;
    //protected Projectile projectile = null;
    protected int respawnTime = 5000;    // Time until respawn in milliseconds
    private Inventory inventory;
    private boolean holdPosition = false;    // Used to determine if the player should be held in place

    protected TimeAnimation currentAnimation;    // The current animation of the player
    protected PlayerState state;        // The current states of the player, set to idle by default
    public boolean canAttack = true;        // A boolean that determines whether the player can attack

    private static int doublePressSpeed = 300;    // double keypressed in ms
    protected float defaultSpeed;    // the default speed of each player
    protected long[] lastPressed = {0, 0, 0, 0};    // the last time WASD was pressed.
    private Optional<AbstractEntity> target;
    private boolean keyW = false;
    private boolean keyA = false;
    private boolean keyS = false;
    private boolean keyD = false;


    // ----------     PlayerState class     ---------- //

    /* The states a player may take */
    public enum PlayerState {
        IDLE, WALK, ATTACK, DAMAGED, DEATH, INTERACT;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    // make usage of PlayerState less verbose for use in this class and subclasses
    static final PlayerState IDLE = PlayerState.IDLE;
    static final PlayerState WALK = PlayerState.WALK;
    static final PlayerState ATTACK = PlayerState.ATTACK;
    static final PlayerState DAMAGED = PlayerState.DAMAGED;
    static final PlayerState DEATH = PlayerState.DEATH;
    static final PlayerState INTERACT = PlayerState.INTERACT;


    // ----------     Initialisation     ---------- //

    /**
     * Default constructor for the purposes of serialization
     */
    public Player() {
        this(0, 0);
    }

    /**
     * Creates a new Player instance.
     *
     * @param posX The x-coordinate.
     * @param posY The y-coordinate.
     */
    public Player(float posX, float posY) {
        super(new Circle2D(posX, posY, 0.4f), 1f, 1f, "player_right", HEALTH);
        this.defaultSpeed = 0.08f;
        this.facing = Direction.SE;
        this.state = IDLE;
        this.setMoveSpeedModifier(0);
        this.setStatic(false);
        this.setSolid(true);
        addResources();    //Initialise the inventory with the valid resources
    }


    // ----------     Texture / Animation     ---------- //

    /**
     * Creates a map of player directions with player state animations. Uses
     * direction as a key to receive the respective animation.
     *
     * @param playerType    A string representing the type of player.
     * @param state         The state of the player.
     * @param frameCount    The number of frames in the animation.
     * @param animationTime The time per animation cycle.
     * @return A map of directions with animations for the specified state.
     */
    public static Map<Direction, TimeAnimation> makePlayerAnimation(String playerType,
                                                                    PlayerState state, int frameCount, int animationTime, Supplier<Void> completionHandler) {
        Map<Direction, TimeAnimation> animations = new HashMap<>();
        for (Direction direction : Direction.values()) {
            String[] frames = new String[frameCount];
            for (int i = 1; i <= frameCount; i++) {
                frames[i - 1] = playerType + "_" + state.toString() + "_" + direction.name() + "_" + i;
            }
            animations.put(direction, new TimeTriggerAnimation(animationTime, frames, completionHandler));
        }

        return animations;
    }

    /**
     * Generic completion handler for execution when the player
     * exits a state other than walk or idle.
     */
    protected Void completionHandler() {
        this.resetState();
        this.updateMovingAndFacing();
        return null;
    }

    /**
     * Sets the specified animation to be the player's current animation.
     *
     * @param animation The time animation to be set to the player.
     */
    public void setAnimation(TimeAnimation animation) {

        EventManager em = GameManager.get().getManager(EventManager.class);

        em.unregisterEvent(this, this.currentAnimation);
        currentAnimation = animation;
        em.registerEvent(this, currentAnimation);

        LOGGER.info("Changed animation to " + facing);
    }

    @Override
    public String getTexture() {
        if (currentAnimation != null) {
            return currentAnimation.getFrame();
        } else {
            LOGGER.warn("Rendered player without texture.");
            return "";
        }
    }

    // ----------     Input handling / Movement setup     ---------- //


    /**
     * Set the player's state. For example, if the player is walking, then
     * set the 'WALK' state to the player. The state can only be changed when
     * the player is IDLE or is WALK-ing. The reason for this is to prevent
     * situations where the player tries to attack while being hurt.
     *
     * @param newState The state to set.
     * @return true
     * if the state was successfully set. False otherwise.
     */
    public boolean setState(PlayerState newState) {
        // Check if the change is the same, if so return true.

        if (state == newState) return true;
        //Only change the state if IDLE or WALK-ing

        if (state == IDLE || state == WALK || state == DEATH) {
            stateChanged(state, newState);
            state = newState;
            // Only allow moving on WALK
            setMoveSpeedModifier((newState == WALK) ? 1 : 0);
            updateSprites();
            return true;
        } else {
            return false; // State not changed
        }
    }

    /**
     * Returns the current state of the player.
     *
     * @return The current state of the player.
     */
    public PlayerState getState() {
        return this.state;
    }

    /**
     * This method, unlike the set state method, always resets the state to
     * IDLE. Use this method to clear that state after being in a state like
     * ATTACK, INTERACT or DAMAGED.
     */
    public void resetState() {
        stateChanged(state, IDLE);
        state = IDLE;
        updateSprites();
    }

    /**
     * This method allows for handling of state changes. For example, if the
     * player changes to the WALK state, then walking sound effects can start
     * playing. If the state changes from the WALK state to another state,
     * then walking sound effects can be stopped.
     *
     * @param from The state that was changed from
     * @param to   The state that was changed to
     */
    private void stateChanged(PlayerState from, PlayerState to) {
        // Handle changing in and out of WALK
        if (to == WALK) {
            walk(true);
        } else if (from == WALK) {
            walk(false);
        }
    }

    /**
     * Handle movement when keyboard keys are pressed down
     *
     * @param keycode The key pressed
     */
    public void handleKeyDown(int keycode) {
        // stop input if player is dead.
        if (state == DEATH) {
            return;
        }

        switch (keycode) {
            case Input.Keys.W:
                keyW = true;
                checkDoublePress(0);
                updateMovingAndFacing();
                break;
            case Input.Keys.S:
                keyS = true;
                checkDoublePress(1);
                updateMovingAndFacing();
                break;
            case Input.Keys.A:
                keyA = true;
                checkDoublePress(2);
                updateMovingAndFacing();
                break;
            case Input.Keys.D:
                keyD = true;
                checkDoublePress(3);
                updateMovingAndFacing();
                break;
            case Input.Keys.SHIFT_LEFT:
                holdPosition = true;
                setState(IDLE);
                break;
            case Input.Keys.T:
                tossItem(new SeedResource());
                break;
            case Input.Keys.F:
                tossItem(new FoodResource());
                break;
            case Input.Keys.E:
                // If successfully harvest, play animation
                if (harvestResources()) {
                    interact();
                }
                break;
            case Input.Keys.SPACE:
                attack();
                break;
            default:
                break;
        }
    }

    /**
     * Handle movement when keyboard keys are released
     *
     * @param keycode The key that was released
     */
    public void handleKeyUp(int keycode) {

        // stop input if player is dead.
        if (state == DEATH) {
            return;
        }

        switch (keycode) {
            case Input.Keys.W:
                keyW = false;
                updateMovingAndFacing();
                break;
            case Input.Keys.S:
                keyS = false;
                updateMovingAndFacing();
                break;
            case Input.Keys.A:
                keyA = false;
                updateMovingAndFacing();
                break;
            case Input.Keys.D:
                keyD = false;
                updateMovingAndFacing();
                break;
            case Input.Keys.SHIFT_LEFT:
                holdPosition = false;
                setState((keyA || keyD || keyS || keyW) ? WALK : IDLE);
                break;
            default:
                break;
        }
    }

    /**
     * Sets the direction of the player based on a current WASD keys pressed.
     */
    void updateMovingAndFacing() {
        Direction newFacing;

        // get direction based on current keys
        // considers if opposite keys are pressed

        int direcEnum = 4;   // default not moving

        // vertical keys
        if (keyW && !keyS) {
            direcEnum -= 3;
        } else if (!keyW && keyS) {
            direcEnum += 3;
        }

        // at this point direcEnum = 1 or 4 or 7, North or Middle or South

        // horizontal keys
        if (!keyA && keyD) {
            direcEnum++;
        } else if (keyA && !keyD) {
            direcEnum--;
        }

        // get direction based on enumeration
        switch (direcEnum) {
            case 0:
                newFacing = Direction.NW;
                break;
            case 1:
                newFacing = Direction.N;
                break;
            case 2:
                newFacing = Direction.NE;
                break;
            case 3:
                newFacing = Direction.W;
                break;
            case 4:
                newFacing = facing; // Not moving, keep existing direction
                break;
            case 5:
                newFacing = Direction.E;
                break;
            case 6:
                newFacing = Direction.SW;
                break;
            case 7:
                newFacing = Direction.S;
                break;
            default:        //(case 8)
                newFacing = Direction.SE;
                break;
        }

        // Firstly, if the player position is held, keep in IDLE but allow changing direction for aiming.
        if (holdPosition) {
            setState(IDLE);
            super.setMoveSpeedModifier(0);
            super.setMoveSpeed(defaultSpeed);
            super.setMoveAngle(newFacing.getAngleRad());
            facing = newFacing;
            updateSprites();
            return;
        }

        if (direcEnum == 4) {
            setState(IDLE);
            super.setMoveSpeed(defaultSpeed);
            super.setMoveSpeedModifier(0);
        } else {
            if (setState(WALK)) {
                super.setMoveAngle(newFacing.getAngleRad());
                super.setMoveSpeedModifier(1);
                facing = newFacing;
            }
        }

        updateSprites();
    }

    private void checkDoublePress(int wasd) {
        if ((System.currentTimeMillis() - lastPressed[wasd]) < doublePressSpeed) {
            this.setMoveSpeed(defaultSpeed * 2);
        } else {
            lastPressed[wasd] = System.currentTimeMillis();
        }
    }

    // ----------     OnTick     ---------- //

    @Override
    public void onTick(long arg0) {
        //mouse input

        mousePos = Render3D.screenToTile(GameManager.get().getManager(InputManager.class).getMouseX(),
                GameManager.get().getManager(InputManager.class).getMouseY());

        //Get terrainModifier of the current tile
        float myX = super.getPosX();
        float myY = super.getPosY();
        float length = GameManager.get().getWorld().getLength();
        float width = GameManager.get().getWorld().getWidth();
        float terrainModifier = GameManager.get().getWorld()
                .getTerrain(Math.round(Math.min(myX - 1, width - 1)), Math.round(Math.min(myY, length - 1)))
                .getMoveScale();
        float moveDist = getMoveSpeed() * terrainModifier;
        float newX = moveDist * (float) Math.cos(this.getMoveAngle());
        float newY = moveDist * (float) Math.sin(this.getMoveAngle());
        float terrainModifierCheck = GameManager.get().getWorld()
                .getTerrain(Math.round(Math.min(myX - 1 + newX, width - 1)), Math.round(Math.min(myY + newY, length - 1)))
                .getMoveScale();
        if (terrainModifierCheck <= 0) {
            terrainModifier = 0;
        }

        if (state == WALK) {
            super.setMoveSpeedModifier(terrainModifier);
        }
        super.onTickMovement();
    }


    // ----------     Inventory Management     ---------- //

    /**
     * Initialises the inventory with all the resources in the game.
     */
    private void addResources() {

        HashSet<Resource> startingResources = new HashSet<Resource>();
        startingResources.add(new SeedResource());
        this.inventory = new Inventory(startingResources);
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
     * Handles removing an item from an inventory and placing it on the map.
     *
     * @param item The resource to be thrown.
     */
    private void tossItem(Resource item) {
        // Tosses a item in front of player
        float x = this.getPosX();
        float y = this.getPosY();

        x = facing == Direction.SW ? x - 1 : x + 1;
        y = facing == Direction.SW ? y - 2 : y + 2;

        // Only toss an item if there are items to toss
        if (this.getInventory().updateQuantity(item, -1) == 1) {
            GameManager.get().getWorld().addEntity(new ResourceEntity(x, y, item));
        }
    }

    /**
     * Returns true if the user can buy this tree
     */
    public boolean canAfford(AbstractTree tree) {
        if (tree == null || inventory == null) {
            return false;
        }

        try {
            GameManager.get().getManager
                    (GuiManager.class).getGui(TreeShopGui.class).getTreeStateByTree(tree);
        } catch (Exception e) {
            return false;
        }


        TreeState treeState = GameManager.get().getManager
                (GuiManager.class).getGui(TreeShopGui.class).getTreeStateByTree(tree);
        if (treeState == null) {
            return false;
        }

        Inventory cost = treeState.getCost();
        for (Resource resource : cost.getInventoryResources()) {
            if (inventory.getQuantity(resource) < cost.getQuantity(resource)) {
                return false;
            }

        }
        return true;
    }

    /**
     * Handles harvesting resources from resource tree that are in range. Resources
     * are added to the player's inventory.
     */
    private boolean harvestResources() {
        double interactRange = 1.5f;
        Collection<AbstractEntity> entities = GameManager.get().getWorld().getEntities().values();
        boolean didHarvest = false;
        for (AbstractEntity entitiy : entities) {
            if (entitiy instanceof ResourceTree && entitiy.distanceTo(this) <= interactRange
                    && ((ResourceTree) entitiy).getGatherCount() > 0) {
                didHarvest = true;
                ((ResourceTree) entitiy).transferResources(this.inventory);
            }
        }
        if (didHarvest) {
            GameManager.get().getManager(SoundManager.class).playSound("harvesting.mp3");
        }
        return didHarvest;
    }


    // ----------     Death     ---------- //

    @Override
    public void deathHandler() {
        LOGGER.info(this + " is dead.");
        // set state to death
        this.state = DEATH;
        // reset all movement
        keyW = false;
        keyA = false;
        keyS = false;
        keyD = false;
        // reset to default speed
        this.setMoveSpeed(defaultSpeed);
        // destroy the player
        GameManager.get().getWorld().removeEntity(this);
        // play Wilhelm scream sound effect
        SoundManager soundManager = new SoundManager();
        soundManager.playSound("death.wav");
        // get the event manager
        EventManager eventManager = GameManager.get().getManager(EventManager.class);
        // add the respawn event
        eventManager.registerEvent(this, new RespawnEvent(respawnTime));

        GameManager.get().getManager(GuiManager.class).getGui(RespawnGui.class).show();
    }


    // ----------     Abstract Methods     ---------- //

    /**
     * A method for damaging the player's health. Allows the damaged
     * state to be enabled and respective animations to play.
     *
     * @param amount The amount of damage to deal to the player.
     */
    @Override
    public boolean damage(float amount) {
        if (state != DAMAGED) {
            setState(DAMAGED);
            this.updateSprites();
        }
        return super.damage(amount);
    }

    @Override
    public ProgressBar getProgressBar() {
        return progressBar;
    }
    ArrayList<Projectile> tr=new ArrayList<Projectile>();
    /**
     * A method for making the player attack based on the direction it
     * faces. Allows the attack state to be enabled and respective
     * animations to play.
     */
    protected void attack() {
        // Override in subclasses to allow custom attacking.
        if (!canAttack) {
            return;
        } else {
            canAttack = false;
            EventManager em = GameManager.get().getManager(EventManager.class);
            em.registerEvent(this, new  AttackCooldownEvent(500));
        }
        if (this.setState(ATTACK)) {
            currentShootStage=ShootStage.READY;
            GameManager.get().getManager(SoundManager.class).playSound("attack.wav");

            float pPosX = GameManager.get().getManager(PlayerManager.class).getPlayer().getPosX();
            float pPosY = GameManager.get().getManager(PlayerManager.class).getPlayer().getPosY();
            float pPosZ = GameManager.get().getManager(PlayerManager.class).getPlayer().getPosZ();

            target = WorldUtil.getClosestEntityOfClass(EnemyEntity.class, pPosX, pPosY);
            float targetPosX = 0;
            float targetPosY = 0;

            Vector3 startPos = new Vector3(pPosX - 1, pPosY, pPosZ);
            Vector3 endPos = new Vector3(targetPosX, targetPosY, 0);


            if (BallisticProjectile.class.isAssignableFrom(projectileClass)) {
                projectile = new BallisticProjectile(!target.isPresent() ? EnemyEntity.class : target.get().getClass(), startPos, endPos, 10, 1, projectileTexture,
                        null, null);
            } else if (OrbProjectile.class.isAssignableFrom(projectileClass)) {
                projectile = new OrbProjectile(!target.isPresent() ? EnemyEntity.class : target.get().getClass(),  startPos, endPos, 10, 1, projectileTexture,
                        null, null);
            } // else if (BombProjectile.class.isAssignableFrom(shootObjectClass)) {
            // projectile = new BombProjectile(targetClass, startPos, targetPos, range,
            // damage, projectileTexture,
            // startEffect, endEffect);
            // }
            else if (HomingProjectile.class.isAssignableFrom(projectileClass)) {//could be t.getclass
                projectile = new HomingProjectile(!target.isPresent() ? EnemyEntity.class : target.get().getClass(),  startPos, endPos, 10, 1, projectileTexture,
                        null, null);
            } else {
                projectile = new BallisticProjectile(!target.isPresent() ? EnemyEntity.class : target.get().getClass(),  startPos, endPos, 10, 1, projectileTexture,
                        null, null);
            }

            switch (facing) {
                case N:
                    break;
                case NE:
                    pPosY -= 1;
                    pPosX += 1.5;
                    break;
                case E:
                    pPosY -= 1;
                    pPosX += 1.5;
                    break;
                case SE:
                    pPosX += 1;
                    break;
                case S:
                    pPosX += 1.2;
                    break;
                case SW:
                    pPosY += 1;
                    pPosX += 1;
                    break;
                case W:
                    break;
                case NW:
                    break;
                default:
                    break;
            }
            if (shootingStyle == PlayerShootMethod.MOUSE) {
                if (HomingProjectile.class.isAssignableFrom(projectileClass)) {
                    ((HomingProjectile) projectile).setHomingDelay(10);
                }
                projectile.setTargetPosition(mousePos.x, mousePos.y, 0);
            }
            if (target.isPresent()) {
                targetPosX = target.get().getPosX();
                targetPosY = target.get().getPosY();

            } else {
                if (shootingStyle == PlayerShootMethod.DIRECTIONAL) {
                    switch (facing) {
                        case W:
                            projectile.setTargetPosition(pPosX - 5, pPosY - 5, 0);
                            break;
                        case E:
                            projectile.setTargetPosition(pPosX + 5, pPosY + 5, 0);
                            break;
                        case N:
                            projectile.setTargetPosition(pPosX + 15, pPosY - 15, 0);
                            break;
                        case S:
                            projectile.setTargetPosition(pPosX - 15, pPosY + 15, 0);
                            break;
                        case SE:
                            projectile.setTargetPosition(pPosX + 15, pPosY + 1, 0);
                            break;
                        case NW:
                            projectile.setTargetPosition(pPosX - 15, pPosY - 200, 0);
                            break;
                        case NE:
                            projectile.setTargetPosition(pPosX + 20, pPosY + 200, 0);
                            break;
                        case SW:
                            projectile.setTargetPosition(pPosX - 200, pPosY - 20, 0);
                            break;
                    }
                }

            }
            GameManager.get().getWorld().addEntity(projectile);
        }
    }

    /**
     * A method for making the player interact based on the direction it
     * faces. Allows the interact state to be enabled and respective
     * animations to play.
     */
    protected void interact() {
        // Override in subclasses to allow custom interacting.
        if (this.setState(INTERACT)) {
            GameManager.get().getManager(SoundManager.class).playSound("interact.wav");
        }
    }

    /**
     * A method allowing subclasses to handle the player entering and
     * exiting the walk state. The method is automatically called every time
     * this transition occurs.
     *
     * @param active True if the player starts walking and false
     *               if the player stops walking.
     */
    protected void walk(boolean active) {
        // Override in subclasses to allow handling of custom walking.
    }

    /**
     * Updates the player sprite based on it's state and direction. Must
     * handle setting the animations for every player state.
     */
    public void updateSprites() {
        // Override in subclasses to update the sprite based on state and direciton.
    }


    // ----------     Generic Object Methods    ---------- //

    @Override
    public String toString() {
        return "The player";
    }

}
