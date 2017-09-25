package com.deco2800.potatoes;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.deco2800.potatoes.managers.GameTimeManager;

public class GameTimeManagerTest {
    @Test
    public void testSetting() {
        GameTimeManager gt = new GameTimeManager();
        gt.setCurrentTime(1);
        gt.setCurrentDay(1);
        assertEquals(gt.getCurrentTime(), 1, 0.0001f);
        assertEquals(gt.getCurrentDay(), 1 );
    }

}
