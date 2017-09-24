package com.deco2800.potatoes.worlds.terrain;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

import java.util.ArrayList;

public class TerrainTypeTest {
    TerrainType t1, t2, t3, t4, t5, t6, t7, t8, t9;

    @Before
    public void setup() {
        t1 = new TerrainType(null, new Terrain("a", 1, true),
                new Terrain("b", 1, false), new Terrain("c", 0, false));
        t2 = new TerrainType(null, new Terrain("a", 1, true),
                new Terrain("b", 1, false), new Terrain("c", 0, false));
        t3 = new TerrainType(null, new Terrain("b", 1, true),
                new Terrain("b", 1, false), new Terrain("c", 0, false));
        t4 = new TerrainType(null, new Terrain("a", 1, true),
                new Terrain("a", 1, false), new Terrain("c", 0, false));
        t5 = new TerrainType(null, new Terrain("a", 1, true),
                new Terrain("b", 1, false), new Terrain("b", 0, false));
        t6 = new TerrainType(new ArrayList<>(), new Terrain("a", 1, true),
                new Terrain("b", 1, false), new Terrain("c", 0, false));
        t7 = new TerrainType(null, null,
                new Terrain("b", 1, false), new Terrain("c", 0, false));
        t8 = new TerrainType(null, new Terrain("a", 1, true),
                null, new Terrain("c", 0, false));
        t9 = new TerrainType(null, new Terrain("a", 1, true),
                new Terrain("b", 1, false), null);
    }

    @Test
    public void equalsTests() {
    	assertFalse(t1.equals(null));
        assertTrue(t1.equals(t1));
        assertTrue(t1.equals(t2));
        assertFalse(t1.equals(t3));
        assertFalse(t1.equals(t4));
        assertFalse(t1.equals(t5));
        assertFalse(t1.equals(t6));
        assertFalse(t1.equals(t7));
        assertFalse(t1.equals(t8));
        assertFalse(t1.equals(t9));
        assertFalse(t6.equals(t1));
        assertFalse(t7.equals(t1));
        assertFalse(t8.equals(t1));
        assertFalse(t9.equals(t1));
        assertFalse(t1.equals(new Object()));
    }
}