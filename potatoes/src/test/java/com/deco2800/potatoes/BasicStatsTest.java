package com.deco2800.potatoes;

import com.deco2800.potatoes.entities.Enemies.BasicStats;
import com.deco2800.potatoes.entities.EnemyEntity;
import com.deco2800.potatoes.entities.TimeEvent;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/*Borrows largely from UpgradeStatsTest.java testing by tree team*/
public class BasicStatsTest {
    List<TimeEvent<EnemyEntity>> normalEvents;
    BasicStats testStats;

    @Before
    public void setup() {
        normalEvents = new LinkedList<>();
        testStats = new BasicStats(200, 500, 4f, 500, normalEvents, "squirrel");
    }

    @Test
    public void getEventsTest() {
        assertFalse("Normal events copy returned the same object",
                testStats.getNormalEventsCopy() == testStats.getNormalEventsCopy());
        assertTrue("Normal events copy didn't return the same object",
                testStats.getNormalEventsReference() == testStats.getNormalEventsReference());
    }

    @Test
    public void getHealthTest() {
        assertEquals("incorrect health returned", testStats.getHealth(), 200, 0.01);
    }

    @Test
    public void getRangeTest() {
        assertEquals("incorrect range returned", testStats.getRange(), 4, 0.01);
    }

    @Test
    public void getSpeedTest() {
        assertEquals("incorrect speed return", testStats.getSpeed(), 500, 0.01);
    }

    @Test
    public void getTextureTest() {
        assertEquals("incorrect texture returned", testStats.getTexture(), "squirrel");
    }

    @Test
    public void getAttackSpeedTest() {
        assertEquals("incorrect attack speed returned", testStats.getAttackSpeed(), 500, 0.1);
    }

}
