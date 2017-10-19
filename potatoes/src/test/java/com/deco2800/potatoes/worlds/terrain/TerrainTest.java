package com.deco2800.potatoes.worlds.terrain;

import org.junit.After;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

public class TerrainTest {
    Terrain t1, t2, t3, t4, t5, t6, t7;

    @Before
    public void setup() {
        t1 = new Terrain("a", 1f, true);
        t2 = new Terrain("a", 1f, true);
        t3 = new Terrain("b", 2f, false);
        t4 = new Terrain(null, 1f, true);
        
    }

    @After
    public void tearDown() {
        t1 = null;
        t2 = null;
        t3 = null;
        t4 = null;
    }

    @Test
    public void setTests() {
        assertTrue(t1.getMoveScale() == 1);
        assertTrue(t1.isPlantable());
    }

    @Test
    public void equalsTests() {
        assertTrue(t1.equals(t1));
        assertTrue(t1.equals(t2));
        assertFalse(t1.equals(t3));
        assertFalse(t1.equals(t4));
        assertFalse(t4.equals(t1));
        assertFalse(t1.equals(new Object()));
    }
}