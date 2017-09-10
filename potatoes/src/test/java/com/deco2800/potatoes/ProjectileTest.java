/**
 *
 */
package com.deco2800.potatoes;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.deco2800.potatoes.entities.effects.AOEEffect;
import com.deco2800.potatoes.entities.effects.ExplosionEffect;
import org.junit.Test;

import com.deco2800.potatoes.entities.projectiles.BallisticProjectile;
import com.deco2800.potatoes.entities.projectiles.HomingProjectile;

/**
 * @author Tristan Cargill
 */
public class ProjectileTest {
    private BallisticProjectile bProj;
    private HomingProjectile hProj;
    private AOEEffect eProj;

    private BallisticProjectile bProj2;
    private HomingProjectile hProj2;
    private AOEEffect eProj2;

    private static final int posX = 0;
    private static final float posY = 0;
    private static final float posZ = 0;

    private static final int fPosX = 0;
    private static final float fPosY = 0;
    private static final float fPosZ = 0;

    private static final int RANGE = 10;
    private static final int DAMAGE = 20;
    private static final int aoeDAMAGE = 30;


    @Test
    public void testBallisticProjectileRotation() {
        bProj = new BallisticProjectile();
        assertTrue(bProj.rotateAngle() == 0);
        bProj2 = new BallisticProjectile(posX, posY, posZ, fPosX, fPosY, fPosZ, RANGE, DAMAGE, aoeDAMAGE);
        float fPosX2 = 0;
        float fPosY2 = 0;
        float angle = (int) ((((Math.atan2(fPosX2, fPosY2) + (float) (Math.PI)) * 180 / Math.PI) + 45 + 90));
        assertTrue(bProj2.rotateAngle() == angle);
    }

    @Test
    public void testHomingProjectileRotation() {
        hProj = new HomingProjectile();
        assertTrue(hProj.rotateAngle() == 0);
        hProj2 = new HomingProjectile(posX, posY, posZ, fPosX, fPosY, fPosZ, RANGE, DAMAGE,"default");
        float fPosX2 = 0;
        float fPosY2 = 0;
        float angle = (int) ((((Math.atan2(fPosX2, fPosY2) + (float) (Math.PI)) * 180 / Math.PI) + 45 + 90));
        assertTrue(hProj2.rotateAngle() == angle);
    }


    @Test
    public void testBallisticProjectileDamage() {
        bProj = new BallisticProjectile();
        assertNotNull(bProj.getDamage());
        assertTrue(bProj.getDamage() == 1);
        //assertTrue(bProj.getAOEDamage() == 1);
        bProj2 = new BallisticProjectile(posX, posY, posZ, fPosX, fPosY, fPosZ, RANGE, DAMAGE, aoeDAMAGE);
        assertNotNull(bProj2.getDamage());
        assertTrue(bProj2.getDamage() == DAMAGE);
        //assertTrue(bProj2.getAOEDamage() == aoeDAMAGE);
    }

    @Test
    public void testHomingProjectileDamage() {
        hProj = new HomingProjectile();
        assertNotNull(hProj.getDamage());
        assertTrue(hProj.getDamage() == 1);
        hProj2 =  new HomingProjectile(posX, posY, posZ, fPosX, fPosY, fPosZ, RANGE, DAMAGE,"default");
        assertNotNull(hProj2.getDamage());
        assertTrue(hProj2.getDamage() == DAMAGE);
    }


    @Test
    public void testBallisticProjectileRange() {
        bProj2 = new BallisticProjectile(posX, posY, posZ, fPosX, fPosY, fPosZ, RANGE, DAMAGE, aoeDAMAGE);
        assertTrue(bProj2.getRange() == 10);
        bProj2.updatePos();
        assertTrue(bProj2.getRange() == (float) (10 - 0.2));
    }

    @Test
    public void testAOEExplosionDamage() {
        eProj = new AOEEffect();
        eProj2 = new AOEEffect(posX, posY, posZ, fPosX, fPosY, fPosZ, 1f,1f, 10);
        assertNotNull(eProj.getDamage());
        assertNotNull(eProj2.getDamage());
        assertTrue(eProj2.getDamage() == 10);
        assertTrue(eProj.getDamage() == 1);

    }

}
