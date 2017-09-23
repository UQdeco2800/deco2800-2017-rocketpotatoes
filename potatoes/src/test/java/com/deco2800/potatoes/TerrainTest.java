package com.deco2800.potatoes;

import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.worlds.terrain.Terrain;
import com.deco2800.potatoes.util.WorldUtil;
import org.junit.Test;
import org.junit.Before;
import com.badlogic.gdx.Input;

import static org.junit.Assert.*;

public class TerrainTest {
    Terrain t1;
    Terrain t2;
    Terrain t3;

    @Before
    public void setup() {
        t1 = new Terrain("a", 1f, true);
        t2 = new Terrain("a", 1f, true);
        t2 = new Terrain("b", 2f, false);
    }

    @Test
    public void setTests() {
        assertTrue(t1.getMoveScale() == 1);
        assertTrue(t1.isPlantable());
    }

    @Test
    public void equalsTests() {
        assertTrue(t1.equals(t1));
        assertFalse(t1.equals(t2));
        assertFalse(t1.equals(t3));
    }
}