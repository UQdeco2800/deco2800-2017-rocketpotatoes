package com.deco2800.potatoes.entities.projectiles;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.collisions.Circle2D;
import com.deco2800.potatoes.collisions.Shape2D;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.effects.AOEEffect;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.entities.enemies.EnemyEntity;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.InputManager;
import com.deco2800.potatoes.renderering.Render3D;
import org.lwjgl.Sys;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MineBomb extends AbstractEntity implements Tickable {
    protected float pPosX;
    protected float pPosY;
    protected float range;
    protected float damage = 100;

//    public Projectile projectile;
    protected Effect startEffect;
    protected Effect endEffect;
    protected BombTexture bombTexture = BombTexture.MINES;
    protected boolean stopAnimation = false;
    protected boolean loopAnimation = true;
    protected boolean explodable = false;
    protected boolean animated = true;
    protected Class<?> targetClass;
    protected int projectileEffectTimer;
    protected int projectileCurrentSpriteIndexCount;


    public MineBomb() {
        // Blank comment to please the lord Sonar
    }

    public enum BombTexture {
        MINES {
            @Override
            public String[] textures() {
                return new String[]{"mines1", "mines2", "mines3", "mines4"};
            }
        };

        public String[] textures() {
            return new String[]{"default"};
        }
    }

    /**
     * Creates a new projectile. A projectile is the vehicle used to deliver damage
     * to a target over a distance
     *
     * @param startPos
     * @param range
     * @param damage      damage of projectile
     * @param startEffect the effect to play at the start of the projectile being fired
     * @param endEffect   the effect to be played if a collision occurs
     */

    public MineBomb(Vector3 startPos, float range, float damage,
                    Effect startEffect, Effect endEffect) {
        super(new Circle2D(startPos.x, startPos.y, 1f), 0.8f, 0.8f,
                BombTexture.MINES.textures()[0]);
        this.pPosX = startPos.x;
        this.pPosY = startPos.y;
        this.range = range;
        this.damage = damage;
        this.startEffect = startEffect;
        this.bombTexture = BombTexture.MINES;
        this.endEffect = endEffect;
        this.targetClass = EnemyEntity.class;

    }

    @Override
    public void onTick(long time) {

        if (!stopAnimation) {
            animate();
        }
        Shape2D newPos = getMask();
        newPos.setX(this.getPosX());
        newPos.setY(this.getPosY());
        Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();
        for (AbstractEntity entity : entities.values()) {
            if (!targetClass.isInstance(entity)) {
                continue;
            }

            if (newPos.overlaps(entity.getMask())) {
                if (stopAnimation && explodable) {

                    ((MortalEntity) entity).damage(damage);
                    AOEEffect aoe = new AOEEffect(targetClass.getClass(),
                            new Vector3(pPosX, pPosY, 0),
                            100, 8f);

                    GameManager.get().getWorld().removeEntity(this);
                    GameManager.get().getWorld().addEntity(aoe);
                    if (endEffect != null)
                        GameManager.get().getWorld().addEntity(endEffect);
                }

            }
        }
    }

    /**
     * Loops through texture array and sets sprite every frame. Looking into using
     * AnimationFactory as animation controller
     */
    protected void animate() {
        if (animated) {
            projectileEffectTimer++;
            if (loopAnimation && projectileEffectTimer % 18 == 0) {
                setTexture(bombTexture.textures()[projectileCurrentSpriteIndexCount]);
                if (projectileCurrentSpriteIndexCount == bombTexture.textures().length - 1) {
                    projectileCurrentSpriteIndexCount = 0;
                    stopAnimation = true;
                    if (stopAnimation) {
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                explodable = true;
                            }
                        }, 1500);
                    }
                } else {
                    projectileCurrentSpriteIndexCount++;
                }
            }
        }
    }


    public float getPosX() {
        return pPosX;
    }

    public float getPosY() {
        return pPosY;
    }

    public float getRange() {
        return range;
    }

    public float getDamage() {
        return damage;
    }

    public Effect getEndEffect() {
        return endEffect;
    }

    public Effect getStartEffect() {
        return startEffect;
    }
}
