package com.deco2800.potatoes.entities.projectiles;

import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.util.Box3D;

public class Projectile extends AbstractEntity implements Tickable {
    protected static final float SPEED = 0.2f;

    protected ProjectileType projectileType;
    protected boolean loopAnimation = true;
    protected boolean animate = true;

    protected Vector3 targetPos = new Vector3();
    protected Vector3 change = new Vector3();
    protected Vector2 delta = new Vector2();

    protected static float xRenderLength = 1.4f;
    protected static float yRenderLength = 1.4f;
    protected static float xLength = 0.4f;
    protected static float yLength = 0.4f;
    protected static float zLength = 0.4f;
    protected String Directions;
    protected float pPosX;
    protected float pPosY;
    protected float tPosX;
    protected float tPosY;
    protected ShootingStyles shootingStyles;

    protected Class<?> targetClass;
    protected boolean rangeReached;
    protected float maxRange;
    protected float range;
    protected float damage;
    protected float rotationAngle = 0;

    protected Effect startEffect;
    protected Effect endEffect;

    public enum ProjectileType {
        ROCKET {
            public String[] textures() {
                return new String[]{"rocket1", "rocket2", "rocket3"};
            }

        },
        CHILLI {
            public String[] textures() {
                return new String[]{"chilli1", "chilli2", "chilli3"};
            }
        },
        LEAVES {
            public String[] textures() {
                return new String[]{"leaves1"};
            }
        },
        ACORN {
            public String[] textures() {

                return new String[]{"acorn1"};

            }
        };

        public String[] textures() {
            return new String[]{"default"};
        }
    }

    public enum ShootingStyles {
        PLAYERDIRECTIONALPROJECTILE {

        },
        HOMINGPROJECTILE {

        },
        BALLISTICPROJECTILE{

        }

    }

    public Projectile() {
        // nothing yet
    }

//    // Used in player for shooting of projectiles, requires TargetPosX and Y for enemylastpos shooting styles
//    public Projectile(Class<?> targetClass, float posX, float posY, float posZ, float range, float damage,
//                      ProjectileType projectileType, Effect startEffect, Effect endEffect, String Directions, float TargetPosX,
//                      float TargetPosY, ShootingStyles shootingStyle) {
//        super(posX, posY, posZ, xLength + 1f, yLength + 1f, zLength, xRenderLength, yRenderLength, true,
//                projectileType.textures()[0]);
//
//        if (targetClass != null)
//            this.targetClass = targetClass;
//        else
//            this.targetClass = MortalEntity.class;
//
//        if (projectileType == null)
//            throw new RuntimeException("projectile type must not be null");
//        else
//            this.projectileType = projectileType;
//		this.shootingStyles = shootingStyle;
//		this.maxRange = this.range = range;
//        this.damage = damage;
//        this.startEffect = startEffect;
//        this.endEffect = endEffect;
//        this.Directions = Directions;
//
//
//        this.pPosX = posX;
//        this.pPosY = posY;
//        this.tPosX = TargetPosX;
//        this.tPosY = TargetPosY;
//
//
//
//        if (startEffect != null)
//            GameManager.get().getWorld().addEntity(startEffect);
//
//        updatePosition();
//        ShootingStyle(shootingStyle);
//        setPosition();
//    }


    public Projectile(Class<?> targetClass, Vector3 startPos, Vector3 targetPos, float range, float damage,
                      ProjectileType projectileType, Effect startEffect, Effect endEffect, String Directions, ShootingStyles shootingStyles) {
        super(startPos.x, startPos.y, startPos.z, xLength + 1f, yLength + 1f, zLength, xRenderLength, yRenderLength, true,
                projectileType.textures()[0]);

        if (targetClass != null)
            this.targetClass = targetClass;
        else
            this.targetClass = MortalEntity.class;

        if (projectileType == null)
            throw new RuntimeException("projectile type must not be null");
        else
            this.projectileType = projectileType;
        this.shootingStyles = shootingStyles;
        this.maxRange = this.range = range;
        this.damage = damage;
        this.startEffect = startEffect;
        this.endEffect = endEffect;
        this.Directions = Directions;

        this.pPosX = startPos.x;
        this.pPosY = startPos.y;
        this.tPosX = targetPos.x;
        this.tPosY = targetPos.y;


        if (startEffect != null)
            GameManager.get().getWorld().addEntity(startEffect);
        if (shootingStyles.toString().equalsIgnoreCase("PLAYERDIRECTIONALPROJECTILE")) {
            ShootingStyle(shootingStyles);
            updatePosition();
            setPosition();
        } else {
            setTargetPosition(targetPos.x, targetPos.y, targetPos.z);
            updatePosition();
            setPosition();
        }
    }


    /**
     * FOR TESTING PURPOSES, DO NOT USE THIS.
     */
    public Projectile(float posX, float posY, float posZ, float range, float damage,
                      ProjectileType projectileType, Effect startEffect, Effect endEffect, String Directions, float TargetPosX,
                      float TargetPosY, ShootingStyles shootingStyle) {
        super(posX, posY, posZ, xLength + 1f, yLength + 1f, zLength, xRenderLength, yRenderLength, true,
                projectileType.textures()[0]);


        if (targetClass != null)
            this.targetClass = targetClass;
        else
            this.targetClass = MortalEntity.class;

        if (projectileType == null)
            throw new RuntimeException("projectile type must not be null");
        else
            this.projectileType = projectileType;
        this.shootingStyles = shootingStyle;
        this.maxRange = this.range = range;
        this.damage = damage;
        this.startEffect = startEffect;
        this.endEffect = endEffect;
        this.Directions = Directions;
        this.range = range;

        this.pPosX = posX;
        this.pPosY = posY;
        this.tPosX = TargetPosX;
        this.tPosY = TargetPosY;


        if (startEffect != null)
            GameManager.get().getWorld().addEntity(startEffect);

        updatePosition();
        ShootingStyle(shootingStyle);
        setPosition();
    }

    public void setTargetPosition(float xPos, float yPos, float zPos) {
        targetPos.set(xPos, yPos, zPos);
    }

    public ShootingStyles getShootingStyles() {
        return shootingStyles;
    }

    /**
     * Initialize heading. Used if heading changes
     */
    protected void updatePosition() {
        delta.set(getPosX() - targetPos.x, getPosY() - targetPos.y);
        float angle = (float) (Math.atan2(delta.y, delta.x)) + (float) (Math.PI);
        rotationAngle = (float) ((angle * 180 / Math.PI) + 45 + 90);
        change.set((float) (SPEED * Math.cos(angle)), (float) (SPEED * Math.sin(angle)), 0);
    }

    public void ShootingStyle(ShootingStyles shootingStyle) {


        /**
         * Shoots enemies base on their player directions
         */
        if (shootingStyle.toString().equalsIgnoreCase("PLAYERDIRECTIONALPROJECTILE")) {
            if (Directions.equalsIgnoreCase("w")) {
                setTargetPosition(pPosX - 5, pPosY - 5, 0);
                // setTargetPosition(TargetPosX, TargetPosY, posZ);
                updatePosition();
                setPosition();
            } else if (Directions.equalsIgnoreCase("e")) {
                setTargetPosition(pPosX + 5, pPosY + 5, 0);
                updatePosition();
                setPosition();
                // setTargetPosition(TargetPosX, TargetPosY, posZ);
            } else if (Directions.equalsIgnoreCase("n")) {
                setTargetPosition(pPosX + 15, pPosY - 15, 0);
                updatePosition();
                setPosition();
            } else if (Directions.equalsIgnoreCase("s")) {
                setTargetPosition(pPosX - 15, pPosY + 15, 0);
                updatePosition();
                setPosition();
            } else if (Directions.equalsIgnoreCase("ne")) {
                setTargetPosition(pPosX + 15, pPosY + 1, 0);
                updatePosition();
                setPosition();
            } else if (Directions.equalsIgnoreCase("nw")) {
                setTargetPosition(pPosX - 15, pPosY - 200, 0);
                updatePosition();
                setPosition();
            } else if (Directions.equalsIgnoreCase("se")) {
                setTargetPosition(pPosX + 20, pPosY + 200, 0);
                updatePosition();
                setPosition();
            } else if (Directions.equalsIgnoreCase("sw")) {
                setTargetPosition(pPosX - 200, pPosY - 20, 0);
                updatePosition();
                setPosition();
            }}
//        } else if (shootingStyle.toString().equalsIgnoreCase("enemylastpos")) {
//            /**
//             * Shoots enemies based on their last position
//             */
//            if (tPosX == 0f && tPosY == 0f) {
//                throw new RuntimeException("Target Position X and Y cannot be 0 for ShootingStyles.ENEMYLASTPOS to work.");
//            }
//            setTargetPosition(tPosX, tPosY, 0);
//            updatePosition();
//            setPosition();
//        }
    }

    /**
     * every frame the position is set
     */
    protected void setPosition() {
        setPosX(getPosX() + change.x);
        setPosY(getPosY() + change.y);

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
        if (animate) {
            projectileEffectTimer++;
            if (loopAnimation) {
                if (projectileEffectTimer % 4 == 0) {
                    setTexture(projectileType.textures()[projectileCurrentSpriteIndexCount]);
                    if (projectileCurrentSpriteIndexCount == projectileType.textures().length - 1)
                        projectileCurrentSpriteIndexCount = 0;
                    else {
                        projectileCurrentSpriteIndexCount++;
                    }
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
                ((MortalEntity) entity).damage(damage);
                if (endEffect != null)
                    GameManager.get().getWorld().addEntity(endEffect);
                rangeReached = true;
                setPosition();
            }
        }
    }

    public Effect getStartEffect() {
        return startEffect;
    }

    public Effect getEndEffect() {
        return endEffect;
    }

}
