package com.deco2800.potatoes;

import static org.junit.Assert.assertEquals;

import com.deco2800.potatoes.managers.GameManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.deco2800.potatoes.managers.GameTimeManager;

public class GameTimeManagerTest {
    
    GameTimeManager gameTimeManager;
    
    @Before
    public void setUp() {
        gameTimeManager = GameManager.get().getManager(GameTimeManager.class);
    }

    @After
    public void tearDown() {
        GameManager.get().clearManagers();
    }

    @Test
    public void testSetting() {
        
        gameTimeManager.setCurrentTime(1);
        gameTimeManager.setCurrentDay(1);
        assertEquals(gameTimeManager.getCurrentTime(), 1, 0.0001f);
        assertEquals(gameTimeManager.getCurrentDay(), 1 );
    }

    @Test
    public void setGetTests(){
        
        gameTimeManager.resetCurrentDay(1);
        gameTimeManager.onTick(1);
        gameTimeManager.getColour();
        gameTimeManager.nightTime();
        gameTimeManager.resetCurrentTime(2);
    }

}
