package com.deco2800.potatoes;

import com.deco2800.potatoes.entities.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TowerTest {

    private static final int RELOAD = 100;
    private static final float HEALTH = 10f;
    private static final float RANGE = 8f;
    Tower testTower;

    @Before
    public void setup() {
    	testTower = new Tower(1, 2);
    }
    
    @Test
    public void emptyTest() {
        Tower nullTower = new Tower();
    }
    @Test
    public void stringTest() {
        assertEquals("string mismatch", "Tower at (1, 2)", testTower.toString());
    }
}
