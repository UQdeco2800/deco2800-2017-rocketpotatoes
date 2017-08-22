package com.deco2800.potatoes.entities.Enemies;

import java.util.Map;
import java.util.Random;

import com.deco2800.potatoes.entities.*;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.managers.SoundManager;
import com.deco2800.potatoes.util.Box3D;

import java.util.Map;


/**
 * AbstractEnemy defines a generic enemy in the game.
 */
public abstract class AbstractEnemy extends MortalEntity implements Tickable, HasProgress {

    /*Initialization of variables*/
    private transient Random random = new Random();
    private float speed = 0.1f;
    //Get the player and make player the goal of enemy
    PlayerManager playerManager = (PlayerManager) GameManager.get().getManager(PlayerManager.class);
    private AbstractEntity goalEntity = playerManager.getPlayer();

    /**
     * Default constructor for serialization
     */
    public AbstractEnemy() {
        //empty for serialization
    }

    /**
     * Constructs a new AbstractEntity. The entity will be rendered at the same size
     * used for collision between entities.
     *
     * @param posX
     *            The x-coordinate of the entity.
     * @param posY
     *            The y-coordinate of the entity.
     * @param posZ
     *            The z-coordinate of the entity.
     * @param xLength
     *            The length of the entity, in x. Used in rendering and collision
     *            detection.
     * @param yLength
     *            The length of the entity, in y. Used in rendering and collision
     *            detection.
     * @param zLength
     *            The length of the entity, in z. Used in rendering and collision
     *            detection.
     * @param texture
     *            The id of the texture for this entity.
     * @param maxHealth
     *            The initial maximum health of the enemy
     */
    public AbstractEnemy(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
                         String texture, float maxHealth) {
        super(posX, posY, posZ, xLength, yLength, zLength, xLength, yLength, false, texture, maxHealth);
    }

    /**
     * Constructs a new AbstractEntity with specific render lengths. Allows
     * specification of rendering dimensions different to those used for collision.
     * For example, could be used to have collision on the trunk of a tree but not
     * the leaves/branches.
     *
     * @param posX
     *            The x-coordinate of the entity.
     * @param posY
     *            The y-coordinate of the entity.
     * @param posZ
     *            The z-coordinate of the entity.
     * @param xLength
     *            The length of the entity, in x. Used in collision detection.
     * @param yLength
     *            The length of the entity, in y. Used in collision detection.
     * @param zLength
     *            The length of the entity, in z. Used in collision detection.
     * @param xRenderLength
     *            The length of the entity, in x. Used in collision detection.
     * @param yRenderLength
     *            The length of the entity, in y. Used in collision detection.
     * @param texture
     *            The id of the texture for this entity.
     * @param maxHealth
     *            The initial maximum health of the enemy
     */
    public AbstractEnemy(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
                       float xRenderLength, float yRenderLength, String texture, float maxHealth) {
        super(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, texture, maxHealth);
    }

    /**
     * Constructs a new AbstractEntity with specific render lengths. Allows
     * specification of rendering dimensions different to those used for collision.
     * For example, could be used to have collision on the trunk of a tree but not
     * the leaves/branches. Allows rendering of entities to be centered on their
     * coordinates if centered is true.
     *
     * @param posX
     *            The x-coordinate of the entity.
     * @param posY
     *            The y-coordinate of the entity.
     * @param posZ
     *            The z-coordinate of the entity.
     * @param xLength
     *            The length of the entity, in x. Used in collision detection.
     * @param yLength
     *            The length of the entity, in y. Used in collision detection.
     * @param zLength
     *            The length of the entity, in z. Used in collision detection.
     * @param xRenderLength
     *            The length of the entity, in x. Used in collision detection.
     * @param yRenderLength
     *            The length of the entity, in y. Used in collision detection.
     * @param centered
     *            True if the entity is to be rendered centered, false otherwise.
     * @param texture
     *            The id of the texture for this entity.
     * @param maxHealth
     *            The initial maximum health of the enemy
     */
    public AbstractEnemy(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
                       float xRenderLength, float yRenderLength, boolean centered, String texture, float maxHealth) {
        super(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, centered, texture, maxHealth);
    }

    
//    public void testGetEntities() {
//        System.out.println(GameManager.get().getWorld().getEntities());
//    }
    

    /*Movement towards a particular goal
     */
    @Override
    public void onTick(long i) {
        PlayerManager playerManager = (PlayerManager) GameManager.get().getManager(PlayerManager.class);
        SoundManager soundManager = (SoundManager) GameManager.get().getManager(SoundManager.class);

//      float goalX = goalEntity.getPosX() + random.nextFloat() * 6 - 3;
//      float goalY = goalEntity.getPosY() + random.nextFloat() * 6 - 3;
//      testGetEntities();

        float goalX = playerManager.getPlayer().getPosX() + random.nextFloat() * 6 - 3;
        float goalY = playerManager.getPlayer().getPosY() + random.nextFloat() * 6 - 3;

        if(this.distance(playerManager.getPlayer()) < speed) {
            this.setPosX(goalX);
            this.setPosY(goalY);
            return;
        }

        float deltaX = getPosX() - goalX;
        float deltaY = getPosY() - goalY;

        float angle = (float)(Math.atan2(deltaY, deltaX)) + (float)(Math.PI);

        float changeX = (float)(speed * Math.cos(angle));
        float changeY = (float)(speed * Math.sin(angle));

        Box3D newPos = getBox3D();
        newPos.setX(getPosX() + changeX);
        newPos.setY(getPosY() + changeY);

        Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();
        boolean collided = false;
        for (AbstractEntity entity : entities.values()) {
            if (!this.equals(entity) && !(entity instanceof Projectile) && newPos.overlaps(entity.getBox3D()) ) {
                if(entity instanceof Player) {
                    //soundManager.playSound("ree1.wav");
                }
                collided = true;
            }
        }

        if (!collided) {
            setPosX(getPosX() + changeX);
            setPosY(getPosY() + changeY);
        }
    }

    /**
     * Sets a single goal entity for the enemy
     *
     * @param goal
     *          An AbstractEntity to set as enemy's goal
     */
    public void setGoalEntity(AbstractEntity goal) {
        this.goalEntity = goal;
    }

    /**
     * Get the current goal of this enemy
     *
     * @return the goal AbstractEntity of this enemy
     */
    public AbstractEntity getGoalEntity() {
        return this.goalEntity;
    }

    /**
     * Get the health of this enemy
     * @return current health of enemy as a integer
     */
    @Override
    public int getProgress() {
        return (int)health;
    }

    
    @Override
    public boolean showProgress() {
        return true;
    }

    
    /**
     * Set current health of the enemy to given int
     * @param p given health
     */
    @Override
    public void setProgress(int p) { health = p; }

    
    /**
     * Give damage to the enemy if the enemy is shot by projectile
     * @param projectile 
     */
    public void getShot(Projectile projectile) {
        this.damage(projectile.getDamage());
        //System.out.println(this + " was shot. Health now " + getHealth());
    }
}
