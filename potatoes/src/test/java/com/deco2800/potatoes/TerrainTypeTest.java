package com.deco2800.potatoes;

import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.worlds.terrain.*;
import com.deco2800.potatoes.util.WorldUtil;
import org.junit.Test;
import org.junit.Before;
import com.badlogic.gdx.Input;

import static org.junit.Assert.*;

public class TerrainTypeTest {
    TerrainType t1, t2, t3, t4, t5;

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
    }

    @Test
    public void equalsTests() {
        assertTrue(t1.equals(t1));
        assertTrue(t1.equals(t2));
        assertFalse(t1.equals(t3));
        assertFalse(t1.equals(t4));
        assertFalse(t1.equals(t5));
        assertFalse(t1.equals(new Object()));
    }
}