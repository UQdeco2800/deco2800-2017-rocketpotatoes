package com.deco2800.potatoes.entities.trees;

import java.util.Optional;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.entities.effects.AOEEffect;
import com.deco2800.potatoes.entities.effects.Effect.EffectTexture;
import com.deco2800.potatoes.entities.effects.ExplosionEffect;
import com.deco2800.potatoes.entities.effects.LazerEffect;
import com.deco2800.potatoes.entities.effects.LightningEffect;
import com.deco2800.potatoes.entities.enemies.EnemyEntity;
import com.deco2800.potatoes.entities.projectiles.BallisticProjectile;
import com.deco2800.potatoes.entities.projectiles.HomingProjectile;
import com.deco2800.potatoes.entities.projectiles.Projectile.ProjectileTexture;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.WorldUtil;

/**
 * Represents a projectile shot from a tree, may be generalised to all entities
 * later
 */
public class TreeProjectileShootEvent extends TimeEvent<AbstractTree> {

    private Enum<?> fireObjectType;
    private Class<? extends AbstractEntity> fireObjectClass;

    /**
     * Default constructor for serialization
     */
    public TreeProjectileShootEvent() {
        // default constructer
    }

    /**
     * @param shootDelay the delay between shots
     */
    public TreeProjectileShootEvent(int shootDelay, Class<? extends AbstractEntity> fireObjectClass,
                                    Enum<?> fireObjectType) {
        this.fireObjectType = fireObjectType;
        this.fireObjectClass = fireObjectClass;
        setDoReset(true);
        setResetAmount(shootDelay);
        reset();
    }

    @Override
    public void action(AbstractTree tree) {
        Optional<AbstractEntity> target1 = WorldUtil.getClosestEntityOfClass(EnemyEntity.class, tree.getPosX(),
                tree.getPosY());
        if (target1.isPresent() && tree.distanceTo(target1.get()) <= tree.getUpgradeStats().getAttackRange()) {
            AbstractEntity e = null;
            if (BallisticProjectile.class.isAssignableFrom(fireObjectClass)) {
                e = new BallisticProjectile(target1.get().getClass(),
                        new Vector3(tree.getPosX() + 0.5f, tree.getPosY() + 0.5f, tree.getPosZ()),
                        new Vector3(target1.get().getPosX(), target1.get().getPosY(), target1.get().getPosZ()),
                        tree.getUpgradeStats().getAttackRange(), 100, (ProjectileTexture) fireObjectType, null,
                       null);
            } else if (HomingProjectile.class.isAssignableFrom(fireObjectClass)) {
                e = new HomingProjectile(target1.get().getClass(),
                        new Vector3(tree.getPosX() + 0.5f, tree.getPosY() + 0.5f, tree.getPosZ()),
                        new Vector3(target1.get().getPosX(), target1.get().getPosY(), target1.get().getPosZ()),
                        tree.getUpgradeStats().getAttackRange(), 100, (ProjectileTexture) fireObjectType, null,
                        null);
            } else if (LightningEffect.class.isAssignableFrom(fireObjectClass)) {
                e = new LightningEffect(target1.get().getClass(),
                        new Vector3(tree.getPosX(), tree.getPosY(), tree.getPosZ()),
                        new Vector3(target1.get().getPosX(), target1.get().getPosY(), target1.get().getPosZ()), 2f,
                        tree.getUpgradeStats().getAttackRange(), (EffectTexture) fireObjectType);
            } else if (LazerEffect.class.isAssignableFrom(fireObjectClass)) {
                e = new LazerEffect(target1.get().getClass(),
                        new Vector3(tree.getPosX(), tree.getPosY(), tree.getPosZ()),
                        new Vector3(target1.get().getPosX(), target1.get().getPosY(), target1.get().getPosZ()), 1,
                        tree.getUpgradeStats().getAttackRange());
            } else {
                e = new BallisticProjectile(target1.get().getClass(),
                        new Vector3(tree.getPosX() + 0.5f, tree.getPosY() + 0.5f, tree.getPosZ()),
                        new Vector3(target1.get().getPosX(), target1.get().getPosY(), target1.get().getPosZ()),
                        tree.getUpgradeStats().getAttackRange(), 100, ProjectileTexture.LEAVES, null,
                        new ExplosionEffect(target1.getClass(),
                                new Vector3(target1.get().getPosX(), target1.get().getPosY(), target1.get().getPosZ()),
                                1, tree.getUpgradeStats().getAttackRange()));
            }
            GameManager.get().getWorld().addEntity(e);
        }

    }

    @Override
    public TimeEvent<AbstractTree> copy() {
        return new TreeProjectileShootEvent(getResetAmount(), fireObjectClass, fireObjectType);
    }
}
