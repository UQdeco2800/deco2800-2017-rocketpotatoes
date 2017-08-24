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
 * Sub-class of AbstractEnemy Tank Enemy has more health, slower move speed,
 * stronger attack power
 *
 */
public class SpeedyEnemy extends AbstractEnemy implements Tickable, HasProgress {


    /**
     * Default constructor for serialization
     */
    public SpeedyEnemy() {
    }

    /**
     * Constructs a new SpeedyEnemy. The entity will be rendered at the same size
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
    public SpeedyEnemy(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
                     String texture, float maxHealth) {
        super(posX, posY, posZ, xLength, yLength, zLength, xLength, yLength, false, texture, maxHealth);
    }

    /**
     * Constructs a new SpeedyEnemy with specific render lengths. Allows
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
    public SpeedyEnemy(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
                     float xRenderLength, float yRenderLength, String texture, float maxHealth) {
        super(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, texture, maxHealth);

    }

    /**
     * Constructs a new SpeedyEnemy with specific render lengths. Allows
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
    public SpeedyEnemy(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
                     float xRenderLength, float yRenderLength, boolean centered, String texture, float maxHealth) {
        super(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, centered, texture, maxHealth);
    }



}

