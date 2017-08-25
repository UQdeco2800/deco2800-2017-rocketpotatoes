/**
 * 
 */
package com.deco2800.potatoes;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.deco2800.potatoes.entities.BallisticProjectile;
import com.deco2800.potatoes.entities.ExplosionProjectile;
import com.deco2800.potatoes.entities.HomingProjectile;

/**
 * @author Tristan Cargill
 *
 */
public class ProjectileTest {
	BallisticProjectile bProj;
	HomingProjectile hProj = new HomingProjectile();
	ExplosionProjectile eProj = new ExplosionProjectile();

	BallisticProjectile bProj2;
	HomingProjectile hProj2;
	ExplosionProjectile eProj2;

	private static final int posX = 0;
	private static final float posY = 0;
	private static final float posZ = 0;

	private static final int fPosX = 0;
	private static final float fPosY = 0;
	private static final float fPosZ = 0;

	private static final int RANGE = 10;
	private static final int DAMAGE = 20;

	@Test
	public void initTest() {
		testDamage();
		testRotation();
		testRange();
	}

	public void testRotation() {
		bProj = new BallisticProjectile();
		assertTrue(bProj.rotateAngle() == 0);
		bProj2 = new BallisticProjectile(posX, posY, posZ, fPosX, fPosY, fPosZ, RANGE, DAMAGE);
		float fPosX2 = 0;
		float fPosY2 = 0;
		float angle = (int) ((((Math.atan2(fPosX2, fPosY2) + (float) (Math.PI)) * 180 / Math.PI) + 45 + 90));
		assertTrue(bProj2.rotateAngle() == angle);
	}
	
	public void testDamage() {
		
		bProj = new BallisticProjectile();
		assertTrue(bProj.getDamage() == 1);
		bProj2 = new BallisticProjectile(posX, posY, posZ, fPosX, fPosY, fPosZ, RANGE, DAMAGE);
		assertTrue(bProj2.getDamage() == DAMAGE);
	}
	
	public void testRange() {
		/*
		 * boolean maxRange = false;
		if (RANGE < speed) {
			setPosX(goalX);
			setPosY(goalY);
			setPosZ(goalZ);
			maxRange = true;
		} else {
			setPosX(getPosX() + changeX);
			setPosY(getPosY() + changeY);
		}
		RANGE -= speed;
		 */
		bProj2 = new BallisticProjectile(posX, posY, posZ, fPosX, fPosY, fPosZ, RANGE, DAMAGE);
		
		assertTrue(bProj2.getRange()==10);
		bProj2.updatePos();
	
		assertTrue(bProj2.getRange()== (float)(10-0.2));
	}

}
