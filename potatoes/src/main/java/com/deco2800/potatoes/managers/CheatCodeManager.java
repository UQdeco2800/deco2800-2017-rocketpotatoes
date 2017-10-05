package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.cheats.CheatExecution;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.Input.Keys;

/**
 * CheatCodeManager for managing the entering of cheat codes, and execution of associated actions.
 */
public class CheatCodeManager extends Manager {

    private List<CheatCode> codes;
    private Set<Key> keys;

    /**
     * Basic constructor.
     */
    public CheatCodeManager() {
        codes = new ArrayList<>();
        keys = new HashSet<>();

        InputManager input = GameManager.get().getManager(InputManager.class);

        input.addKeyUpListener(this::handleKeyUp);
        input.addKeyDownListener(this::handleKeyDown);
    }

    public void handleKeyUp(int keyCode) {
        Key key = getKey(keyCode);
        if (key != null) {
            keys.remove(key);
        }
    }

    public void handleKeyDown(int keyCode) {
        Key key = getKey(keyCode);
        if (key != null && keys.add(key)) {
            for (CheatCode cheat : codes ) {
                cheat.handleKey(getKey(keyCode));
            }
        }
    }

    /**
     * Turns a keyCode integer from LibGDX into a cheat code Key.
     */
    private Key getKey(int keyCode) {
        switch (keyCode) {
            case Keys.A:
                return Key.A;
            case Keys.B:
                return Key.B;
            case Keys.LEFT:
                return Key.LEFT;
            case Keys.UP:
                return Key.UP;
            case Keys.RIGHT:
                return Key.RIGHT;
            case Keys.DOWN:
                return Key.DOWN;
            case Keys.SHIFT_RIGHT:
                return Key.SELECT;
            case Keys.ENTER:
                return Key.START;
            default:
                return null;
        }
    }

    /**
     * Adds a new cheat code to the game.
     *
     * @param cheat
     *          The cheat execution object that will be run when the code is entered.
     * @param head
     *          The first key in the cheat code. This is kept separate to the varargs to enforce a
     *          minimum length of 1 for cheat codes.
     * @param tail
     *          The remaining keys in the cheat code.
     */
    public void addCheatCode(CheatExecution cheat, Key head, Key...tail) {
        List<Key> keys = new ArrayList<>();
        keys.add(head);
        for (Key key : tail) {
            keys.add(key);
        }
        codes.add(new CheatCode(keys, cheat));
    }

    /**
     * The Keys that can be entered as parts of cheat codes.
     *
     * The arrow keys and A/B are themselves, START is the ENTER key, and SELECT is the right 
     * SHIFT key.
     */
    public enum Key { LEFT, UP, RIGHT, DOWN, A, B, START, SELECT };

    /**
     * A single cheat code that is being managed.
     */
    private class CheatCode {
        private List<Key> goal;
        private ArrayDeque<Key> entered;
        private CheatExecution cheat;

        /**
         * Creates a new CheatCode.
         *
         * @require keys must have non-zero length.
         *
         * @param keys
         *          The keys that must be pressed for this cheat code to execute.
         * @param cheat
         *          The cheat execution object that will be run once these keys are entered.
         */
        public CheatCode(List<Key> keys, CheatExecution cheat) {
            goal = keys;
            this.cheat = cheat;
            entered = new ArrayDeque<>();
        }

        /**
         * Processes a single key being entered. If that key is the last key in the cheat code, the
         * cheat code is run.
         *
         * @param input
         *          The key that has been input.
         */
        public void handleKey(Key input) {
            entered.addLast(input);
            if (entered.size() > goal.size()) {
                entered.removeFirst();
            }

            if (entered.size() == goal.size()) {
                int i = 0;
                for (Key key : entered) {
                    if (key != goal.get(i++)) {
                        return;
                    }
                }

                entered.clear();
                cheat.run();
            }
        }
    }

}
