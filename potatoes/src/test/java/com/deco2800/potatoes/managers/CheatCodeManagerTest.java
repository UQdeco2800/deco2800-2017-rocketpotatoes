package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.managers.CheatCodeManager.Key;
import com.deco2800.potatoes.cheats.CheatExecution;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.badlogic.gdx.Input.Keys;

import static org.junit.Assert.assertEquals;

public class CheatCodeManagerTest {

    private Cheat testCheat;

    private class Cheat implements CheatExecution {
        private boolean hasRun;

        public Cheat() {
            hasRun = false;
        }

        public boolean getHasRun() {
            return hasRun;
        }

        @Override
        public void run() {
            hasRun = true;
        }

        /**
         * Clears the current "hasRun" state in preparation for the cheat code being run twice.
         */
        public void clear() {
            hasRun = false;
        }
    }

    @Before
    public void setup() {
        testCheat = new Cheat();

        CheatCodeManager cheats = GameManager.get().getManager(CheatCodeManager.class);
        cheats.addCheatCode(testCheat, Key.UP, Key.UP, Key.DOWN, Key.DOWN, Key.SELECT);
    }

    @After
    public void tearDown() {
        GameManager.get().clearManagers();
    }

    /**
     * Enters a character to the cheat code handler.
     *
     * @param key
     *          The key to be pressed.
     */
    private void pressKey(Key key) {
        int keyCode = 0;
        switch (key) {
            case A:
                keyCode = Keys.A;
                break;
            case B:
                keyCode = Keys.B;
                break;
            case LEFT:
                keyCode = Keys.LEFT;
                break;
            case UP:
                keyCode = Keys.UP;
                break;
            case RIGHT:
                keyCode = Keys.RIGHT;
                break;
            case DOWN:
                keyCode = Keys.DOWN;
                break;
            case SELECT:
                keyCode = Keys.SHIFT_RIGHT;
                break;
            case START:
                keyCode = Keys.ENTER;
                break;
        }

        CheatCodeManager cheats = GameManager.get().getManager(CheatCodeManager.class);

        cheats.handleKeyDown(keyCode);
        cheats.handleKeyUp(keyCode);
    } 

    /**
     * Enters the code correctly.
     */
    private void enterCode() {
        pressKey(Key.UP);
        pressKey(Key.UP);
        pressKey(Key.DOWN);
        pressKey(Key.DOWN);
        pressKey(Key.SELECT);
    }

    @Test
    public void correctCode() {
        assertEquals(testCheat.getHasRun(), false);
        enterCode();
        assertEquals(testCheat.getHasRun(), true);
    }

    @Test
    public void correctCodeMultiple() {
        for (int i = 0; i < 5; ++i) {
            assertEquals(testCheat.getHasRun(), false);
            enterCode();
            assertEquals(testCheat.getHasRun(), true);
            testCheat.clear();
        }
    }

    @Test
    public void incorrectCode() {
        assertEquals(testCheat.getHasRun(), false);

        pressKey(Key.UP);
        pressKey(Key.DOWN);
        pressKey(Key.LEFT);
        pressKey(Key.RIGHT);

        assertEquals(testCheat.getHasRun(), false);
    }

    @Test
    public void incorrectCodeThenCorrectCode() {
        assertEquals(testCheat.getHasRun(), false);

        pressKey(Key.UP);
        pressKey(Key.UP);
        pressKey(Key.UP);

        assertEquals(testCheat.getHasRun(), false);

        enterCode();

        assertEquals(testCheat.getHasRun(), true);
    }

}
