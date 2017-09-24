package com.deco2800.potatoes;

import com.deco2800.potatoes.managers.ParticleManager;
import com.deco2800.potatoes.renderering.particles.ParticleEmitter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParticleTest {

    @Test
    public void testInit() {
        ParticleManager manager = new ParticleManager();
        assertEquals(true, manager.getEmitters().isEmpty());
    }

    @Test
    public void testAddition() {
        ParticleManager manager = new ParticleManager();
        ParticleEmitter em = new ParticleEmitter(0, 0);
        manager.addParticleEmitter(0.0f, em);
        assertEquals(false, manager.getEmitters().isEmpty());
        assertEquals(em, manager.getEmitters().get(0).emitter);
    }

    @Test
    public void testTimeTick() {
        ParticleManager manager = new ParticleManager();
        ParticleEmitter em = new ParticleEmitter(0, 0);
        manager.addParticleEmitter(100.0f, em);
        manager.onTick(10);
        assertEquals(10, manager.getEmitters().get(0).currentLifeTime, 0.1f);
    }

    @Test
    public void testTimeOut() {
        ParticleManager manager = new ParticleManager();
        ParticleEmitter em = new ParticleEmitter(0, 0);
        manager.addParticleEmitter(100.0f, em);
        manager.onTick(100);
        assertEquals(true, manager.getEmitters().isEmpty());
    }

    @Test
    public void testSlowRemoval() {
        ParticleManager manager = new ParticleManager();
        ParticleEmitter em = new ParticleEmitter(0, 0);
        manager.addParticleEmitter(100.0f, em);
        manager.stopEmitter(em);
        assertEquals(true, manager.getEmitters().get(0).toRemove);
        assertEquals(false, manager.getEmitters().get(0).emitter.isActive());
        manager.onTick(10);
        assertEquals(true, manager.getEmitters().isEmpty());
    }


    @Test
    public void testForceRemoval() {
        ParticleManager manager = new ParticleManager();
        ParticleEmitter em = new ParticleEmitter(0, 0);
        manager.addParticleEmitter(100.0f, em);
        manager.forceStopEmitter(em);
        assertEquals(true, manager.getEmitters().isEmpty());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testFailSlowRemoval() {
        ParticleManager manager = new ParticleManager();
        ParticleEmitter em = new ParticleEmitter(0, 0);
        manager.forceStopEmitter(em);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testFailForceRemoval() {
        ParticleManager manager = new ParticleManager();
        ParticleEmitter em = new ParticleEmitter(0, 0);
        manager.stopEmitter(em);

    }
}
