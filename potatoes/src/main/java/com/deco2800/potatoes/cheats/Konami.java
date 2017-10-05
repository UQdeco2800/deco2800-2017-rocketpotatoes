package com.deco2800.potatoes.cheats;

import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.GuiManager;
import com.deco2800.potatoes.gui.DebugModeGui;

/**
 * UP, UP, DOWN, DOWN, LEFT, RIGHT, LEFT, RIGHT, A, B, ENTER
 * Konami cheat code to turn on debug (god) mode.
 */
public class Konami implements CheatExecution {
    @Override
    public void run() {
        GameManager.get().getManager(GuiManager.class).getGui(DebugModeGui.class).toggle();
    }
}
