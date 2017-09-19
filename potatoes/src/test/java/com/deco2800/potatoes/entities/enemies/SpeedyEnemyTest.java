package com.deco2800.potatoes.entities.enemies;

import com.deco2800.potatoes.entities.enemies.SpeedyEnemy;
import org.junit.*;

public class SpeedyEnemyTest {
    SpeedyEnemy testSpeedy;

    @Before
    public void setup() {
        testSpeedy = new SpeedyEnemy(2,2,3);
    }

    @Test
    public void emptyTest() {
        testSpeedy = new SpeedyEnemy();
    }

    @Test
    public void toStringTest() {
        Assert.assertEquals("String mismatch", "Speedy Enemy at (2, 2)", testSpeedy.toString());
    }

}
