package com.deco2800.potatoes.entities.projectiles;

import java.util.Map;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.util.Box3D;

public class Projectile extends AbstractEntity implements Tickable {

    protected static final transient String TEXTURE = "rocket1";
    protected String projectileType = "rocket";
    protected int textureArrayLength = 3;
    protected String[] textureArray;
    protected boolean textureLoop = true;

    protected float goalX;
    protected float goalY;
    protected float goalZ;
    protected float changeX;
    protected float changeY;
    protected float changeZ;
    protected float deltaX, deltaY;

    protected static float xRenderLength = 1.4f;
    protected static float yRenderLength = 1.4f;
    protected static float xLength = 0.4f;
    protected static float yLength = 0.4f;
    protected static float zLength = 0.4f;

    protected Class<?> targetClass;
    protected boolean rangeReached;
    protected float maxRange;
    protected float range;
    protected float damage;
    protected float rotationAngle = 0;
    protected static final float SPEED = 0.2f;

    protected Effect startEffect;
    protected Effect endEffect;

    public Projectile() {
        textureArray = new String[textureArrayLength];
        // empty for serialization
    }

    // currently used in MeleeAttack, will probably need to change out later.
    public Projectile(float posX, float posY, float posZ, float xRenderLength, float yRenderLength, String texture) {
        super(posX, posY, posZ, 0.4f, 0.4f, 0.4f, xRenderLength, yRenderLength, true, texture);
    }

    // currently used in Player, will probably need to change out later.
    public Projectile(Class<?> targetClass, float posX, float posY, float posZ, float range, float damage, String projectileType, Effect startEffect,
                      Effect endEffect, String Directions, float TargetPosX, float TargetPosY) {
        super(posX, posY, posZ, xLength + 1f, yLength + 1f, zLength, xRenderLength, yRenderLength, true, TEXTURE);
        textureArray = new String[textureArrayLength];
        if (projectileType != "" && projectileType != null)
            this.projectileType = projectileType;

        for (int t = 0; t < textureArrayLength; t++) {
            textureArray[t] = this.projectileType + Integer.toString(t + 1);
        }

        if (targetClass != null)
            this.targetClass = targetClass;
        else
            this.targetClass = MortalEntity.class;

        this.range = damage;
        this.damage = damage;
        this.startEffect = startEffect;
        this.endEffect = endEffect;

        if (startEffect != null)
            GameManager.get().getWorld().addEntity(startEffect);

        updatePosition();
        /**
         * Shoots enemies base on their player directions
         * */
        if (Directions.equalsIgnoreCase("w")) {
            setTargetPosition(posX - 5, posY - 5, posZ);
//            setTargetPosition(TargetPosX, TargetPosY, posZ);
            updatePosition();
            setPosition();
        } else if (Directions.equalsIgnoreCase("e")) {
            setTargetPosition(posX + 5, posY + 5, posZ);
            updatePosition();
            setPosition();
//            setTargetPosition(TargetPosX, TargetPosY, posZ);
        } else if (Directions.equalsIgnoreCase("n")) {
            setTargetPosition(posX + 15, posY - 15, posZ);
            updatePosition();
            setPosition();
        } else if (Directions.equalsIgnoreCase("s")) {
            setTargetPosition(posX - 15, posY + 15, posZ);
            updatePosition();
            setPosition();
        } else if (Directions.equalsIgnoreCase("ne")) {
            setTargetPosition(posX + 15, posY +1, posZ);
            updatePosition();
            setPosition();
        } else if (Directions.equalsIgnoreCase("nw")) {
            setTargetPosition(posX - 15, posY - 200, posZ);
            updatePosition();
            setPosition();
        } else if (Directions.equalsIgnoreCase("se")) {
            setTargetPosition(posX + 20, posY + 200, posZ);
            updatePosition();
            setPosition();
        } else if (Directions.equalsIgnoreCase("sw")) {
            setTargetPosition(posX -200, posY-20, posZ);
            updatePosition();
            setPosition();
        }

        /**
         * Shoots enemies based on their last position
         */
//        setTargetPosition(TargetPosX,TargetPosY,0f);
//        updatePosition();
//            setPosition();
    }





    public Projectile(Class<?> targetClass, float posX, float posY, float posZ, float targetPosX, float targetPosY,
                      float targetPosZ, float range, float damage, String projectileType, Effect startEffect, Effect endEffect) {
        super(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, true, TEXTURE);

        setTextureArray(this.projectileType, 3);

        if (targetClass != null)
            this.targetClass = targetClass;
        else
            this.targetClass = MortalEntity.class;

        this.maxRange = this.range = range;
        this.damage = damage;
        this.startEffect = startEffect;
        this.endEffect = endEffect;

        if (startEffect != null)
            GameManager.get().getWorld().addEntity(startEffect);

        setTargetPosition(targetPosX, targetPosY, targetPosZ);
        updatePosition();
        setPosition();
    }


    protected void setTextureArray(String projectileType, int numberOfSprites) {
        textureArrayLength = numberOfSprites;
        textureArray = new String[textureArrayLength];

        if (projectileType != "" && projectileType != null)
            this.projectileType = projectileType;

        for (int t = 0; t < textureArrayLength; t++) {
            textureArray[t] = this.projectileType + Integer.toString(t + 1);
        }
    }

    public void setTargetPosition(float xPos, float yPos, float zPos) {
        this.goalX = xPos;
        this.goalY = yPos;
        this.goalZ = zPos;
    }

    /**
     * Used if heading changes
     */
    protected void updatePosition() {
        deltaX = getPosX() - this.goalX;
        deltaY = getPosY() - this.goalY;
        float angle = (float) (Math.atan2(deltaY, deltaX)) + (float) (Math.PI);
        rotationAngle = (float) ((angle * 180 / Math.PI) + 45 + 90);
        changeX = (float) (SPEED * Math.cos(angle));
        changeY = (float) (SPEED * Math.sin(angle));
    }

    /**
     * every frame the position is set
     */
    protected void setPosition() {
        setPosX(getPosX() + changeX);
        setPosY(getPosY() + changeY);

        if (range < SPEED || rangeReached) {
            GameManager.get().getWorld().removeEntity(this);
        } else {
            range -= SPEED;
        }
    }

    @Override
    public float rotationAngle() {
        return rotationAngle;
    }

    /**
     * Returns Range value
     */
    public float getRange() {
        return maxRange;
    }

    /**
     * Returns Damage value
     */
    public float getDamage() {
        return damage;
    }

    protected int projectileEffectTimer;
    protected int projectileCurrentSpriteIndexCount;

    protected void animate() {
        projectileEffectTimer++;
        if (textureLoop) {
            if (projectileEffectTimer % 4 == 0) {
                setTexture(textureArray[projectileCurrentSpriteIndexCount]);
                if (projectileCurrentSpriteIndexCount == textureArrayLength - 1)
                    projectileCurrentSpriteIndexCount = 0;
                else {
                    projectileCurrentSpriteIndexCount++;
                }
            }
        }
    }

    @Override
    public void onTick(long time) {
        animate();
        setPosition();

        Box3D newPos = getBox3D();
        newPos.setX(this.getPosX());
        newPos.setY(this.getPosY());

        Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();

        for (AbstractEntity entity : entities.values()) {
            if (!targetClass.isInstance(entity)) {
                continue;
            }
            if (newPos.overlaps(entity.getBox3D())) {
                if (entity instanceof Player) {
                    GameManager.get().getManager(PlayerManager.class).getPlayer().setDamaged(true);
                }
                ((MortalEntity) entity).damage(damage);
                if (endEffect != null)
                    GameManager.get().getWorld().addEntity(endEffect);
                rangeReached = true;
                setPosition();
            }
        }
    }
}
