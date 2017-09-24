/**
 *
 */
package com.deco2800.potatoes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.effects.AOEEffect;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.entities.effects.ExplosionEffect;
import com.deco2800.potatoes.entities.enemies.EnemyEntity;
import com.deco2800.potatoes.entities.projectiles.PlayerProjectile;
import com.deco2800.potatoes.entities.projectiles.Projectile;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.WorldUtil;
import com.deco2800.potatoes.worlds.World;
import org.junit.Test;

import com.deco2800.potatoes.entities.projectiles.BallisticProjectile;
import com.deco2800.potatoes.entities.projectiles.HomingProjectile;

import javax.print.attribute.standard.MediaSizeName;
import java.util.Optional;

/**
 * @author Tristan Cargill
 */
public class ProjectileTest {
    protected Projectile testProjectile;
    protected PlayerProjectile testPlayerProjectile;
    protected BallisticProjectile testBallisticProjectile;
    protected HomingProjectile testHomingProjectile;
    protected Optional<AbstractEntity> target = null;
    protected Class<?> targetClass;
    protected float posX = 5f;
    protected float posY = 10f;
    protected float posZ = 0;
    protected float range = 8;
    protected float damage = 10;
    protected Projectile.ProjectileType projectileType = Projectile.ProjectileType.ROCKET;
    protected Effect startEffect = new AOEEffect();
    protected Effect endEffect = null;
    protected String Directions = "E";
    protected float TargetPosX = 9;
    protected float TargetPosY = 12;
    protected Projectile.ShootingStyles shootingStyles = Projectile.ShootingStyles.PLAYERDIRECTIONALPROJECTILE;

    private class TestWorld extends World {

    }

    @Test
    public void TestProjectile() {
        GameManager.get().setWorld(new ProjectileTest.TestWorld());
        target = WorldUtil.getClosestEntityOfClass(EnemyEntity.class, 0, 0);
        assertTrue(target.toString().equalsIgnoreCase("optional.empty"));
//        testProjectile = new Projectile(posX,posY,posZ,range,damage,projectileType,null,null,Directions,TargetPosX,TargetPosY, shootingStyles);
        assertNotNull(testProjectile);
        assertNotNull(target);
        assertTrue(testProjectile.getDamage() == 10);
        assertTrue(testProjectile.getTexture().contains("rocket"));
        assertTrue(testProjectile.getRange() == 8);
        assertEquals(5.2828426,testProjectile.getPosX(),0.2);
        assertEquals(10.282843,testProjectile.getPosY(),0.2);
        assertEquals(0,testProjectile.getPosZ(),0.2);
        assertEquals(Projectile.ShootingStyles.PLAYERDIRECTIONALPROJECTILE,shootingStyles);

    }




}
