package com.deco2800.potatoes.cheats;

import com.deco2800.potatoes.cheats.rust.Rustyfish;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.CheatCodeManager;
import com.deco2800.potatoes.managers.CheatCodeManager.Key;

/**
 * A list of cheat codes to be added to the game.
 */
public class CheatList {

    /**
     * empty constructor for serialization
     */
    public void CheatList(){


    }

    /**
     * Does what it says on the tin.
     */
    public static void addCheats() {
        CheatCodeManager manager = GameManager.get().getManager(CheatCodeManager.class);

        manager.addCheatCode(new Konami(),
                Key.UP,
                Key.UP,
                Key.DOWN,
                Key.DOWN,
                Key.LEFT,
                Key.RIGHT,
                Key.LEFT,
                Key.RIGHT,
                Key.B,
                Key.A,
                Key.START);

        manager.addCheatCode(new Rustyfish(),
                Key.UP,
                Key.LEFT,
                Key.RIGHT,
                Key.DOWN,
                Key.UP,
                Key.LEFT,
                Key.RIGHT,
                Key.DOWN);

        manager.addCheatCode(new DuckLife(),
                Key.UP,
                Key.LEFT,
                Key.UP,
                Key.LEFT,
                Key.UP,
                Key.LEFT,
                Key.UP,
                Key.LEFT
        );

        manager.addCheatCode(new PotatoLife(),
                Key.UP,
                Key.RIGHT,
                Key.UP,
                Key.RIGHT,
                Key.UP,
                Key.RIGHT,
                Key.UP,
                Key.RIGHT
        );
    }
}
