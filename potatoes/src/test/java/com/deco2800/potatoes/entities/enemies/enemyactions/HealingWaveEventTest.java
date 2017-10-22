package com.deco2800.potatoes.entities.enemies.enemyactions;

import com.deco2800.potatoes.BaseTest;
import com.deco2800.potatoes.entities.enemies.Moose;
import com.deco2800.potatoes.entities.enemies.SpeedyEnemy;
import com.deco2800.potatoes.entities.enemies.Squirrel;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.ParticleManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.worlds.ForestWorld;
import com.deco2800.potatoes.worlds.WorldType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;

/***
 * Test for HealingWaveEvent
 *
 */
public class HealingWaveEventTest extends BaseTest {
    private Squirrel squirrel = new Squirrel(20,20);
    int eventRate = 300;
    float waveRadius = 10f;
    float healAmount = 10f;
    private HealingWaveEvent healWave;
    ParticleManager particleManager;

    @Before
    public void setUp() throws Exception {
        GameManager.get().getManager(WorldManager.class).setWorld(ForestWorld.get());
        GameManager.get().getWorld().addEntity(squirrel);
        healWave = new HealingWaveEvent(eventRate, waveRadius, healAmount);
        particleManager = GameManager.get().getManager(ParticleManager.class);
    }

    @After
    public void cleanUp() {
        GameManager.get().clearManagers();
        healWave = null;
        squirrel = null;
        particleManager = null;
    }

    @Test
    public void actionTest() {
        Assert.assertEquals("Particles exist before healing wave has occurred",true,
                particleManager.getEmitters().isEmpty());

        squirrel.setMoving(false);
        healWave.action(squirrel);

        Assert.assertTrue("Healing wave has not started enemy movement of a channelling enemy",
                squirrel.getMoving());

        //test whether a particle effect has been created following action
        assertEquals("Particles were not produced by healing wave",
                false, particleManager.getEmitters().isEmpty());

        //Create an enemy outside of radius for healing
        SpeedyEnemy outsideEnemy = new SpeedyEnemy(waveRadius + squirrel.getPosX() + 1,
                waveRadius + squirrel.getPosY() + 1);
        Moose insideEnemy = new Moose(squirrel.getPosX() + waveRadius/2, squirrel.getPosY() + waveRadius/2);

        GameManager.get().getWorld().addEntity(outsideEnemy);
        GameManager.get().getWorld().addEntity(insideEnemy);

        float damageAmount = 15f;
        outsideEnemy.damage(damageAmount);
        float damagedOutsideHealth = outsideEnemy.getHealth();

        insideEnemy.damage(damageAmount);
        float damagedInsideHealth = insideEnemy.getHealth();

        healWave.action(squirrel);

        Assert.assertEquals("enemy outside radius was healed by heal wave",
                damagedOutsideHealth, outsideEnemy.getHealth(), .1f);
        Assert.assertEquals("enemy within radius was not healed by heal amount of heal wave",
                damagedInsideHealth+healAmount, insideEnemy.getHealth(), .1f);
    }

    @Test
    public void toStringTest() {
        Assert.assertEquals("String mismatch", healWave.toString(),
                String.format("Healing wave with %d rate, %f radius, %f heal amount", healWave.getResetAmount(),
                waveRadius, healAmount));
    }
}
