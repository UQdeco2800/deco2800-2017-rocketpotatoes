package com.deco2800.potatoes;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.deco2800.potatoes.entities.GameTime;

public class GameTimeTest {

    /**
     * Because GameTime is an abstract class normal testing methods cannot be applied,
     * so a concrete class was used to test.
     */
    public static class TestGameTime<T> extends GameTime<T> {

        public TestGameTime(){
            setCurrentTime(1);
            setCurrentDay(1);
        }


        @Override
        public GameTime copy() {
            TestGameTime<T> copy = new TestGameTime<>();
            copy.setCurrentTime(getCurrentTime());
            copy.setCurrentDay(getCurrentDay());
            return copy;
        }

        @Override
        public void onTick(long time) {

        }
    }


    @Test
    public void testSetting() {
        TestGameTime<Object> event = new TestGameTime<Object>();
        event.setCurrentTime(1);
        event.setCurrentDay(1);
        assertEquals(event.getCurrentTime(), 1 );
        assertEquals(event.getCurrentDay(), 1 );
    }

}
