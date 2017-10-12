package com.deco2800.potatoes;

import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.ParticleManager;
import com.deco2800.potatoes.renderering.particles.ParticleEmitter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParticleTest {

    ParticleManager particleManager;
    ParticleEmitter particleEmitter;

    @Before
    public void setUp() {
        particleManager = GameManager.get().getManager(ParticleManager.class);
    }

    @After
    public void tearDown() {
        GameManager.get().clearManagers();
        particleManager = null;
        particleEmitter = null;
    }

    @Test
    public void testInit() {

        assertEquals(true, particleManager.getEmitters().isEmpty());
    }

    @Test
    public void testAddition() {
        particleEmitter = new ParticleEmitter(0, 0);
        particleManager.addParticleEmitter(0.0f, particleEmitter);
        assertEquals(false, particleManager.getEmitters().isEmpty());
        assertEquals(particleEmitter, particleManager.getEmitters().get(0).emitter);
    }

    @Test
    public void testTimeTick() {
        particleEmitter = new ParticleEmitter(0, 0);
        particleManager.addParticleEmitter(100.0f, particleEmitter);
        particleManager.onTick(10);
        assertEquals(10, particleManager.getEmitters().get(0).currentLifeTime, 0.1f);
    }

    @Test
    public void testTimeOut() {
        particleEmitter = new ParticleEmitter(0, 0);
        particleManager.addParticleEmitter(100.0f, particleEmitter);
        particleManager.onTick(100);
        assertEquals(true, particleManager.getEmitters().isEmpty());
    }

    @Test
    public void testSlowRemoval() {
        particleEmitter = new ParticleEmitter(0, 0);
        particleManager.addParticleEmitter(100.0f, particleEmitter);
        particleManager.stopEmitter(particleEmitter);
        assertEquals(true, particleManager.getEmitters().get(0).toRemove);
        assertEquals(false, particleManager.getEmitters().get(0).emitter.isActive());
        particleManager.onTick(10);
        assertEquals(true, particleManager.getEmitters().isEmpty());
    }


    @Test
    public void testForceRemoval() {
        particleEmitter = new ParticleEmitter(0, 0);
        particleManager.addParticleEmitter(100.0f, particleEmitter);
        particleManager.forceStopEmitter(particleEmitter);
        assertEquals(true, particleManager.getEmitters().isEmpty());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testFailSlowRemoval() {
        particleEmitter = new ParticleEmitter(0, 0);
        particleManager.forceStopEmitter(particleEmitter);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testFailForceRemoval() {
        particleEmitter = new ParticleEmitter(0, 0);
        particleManager.stopEmitter(particleEmitter);

    }
    @Test
    public void testGetSet() {
        particleEmitter = new ParticleEmitter(0, 0);
        particleManager.addParticleEmitter(100.0f, particleEmitter);
        particleEmitter.start();
        particleEmitter.setOrigin(1,2);
        particleEmitter.getParticleTypes();
    }

}
