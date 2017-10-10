package com.deco2800.potatoes.cheats;

import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.GuiManager;
import com.deco2800.potatoes.gui.DebugModeGui;

/**
 * SELECT (Right Shift Key).
 * Temporary shortcut for convenience to toggle debug (god) mode.
 */
public class Shortcut implements CheatExecution {
    @Override
    public void run() {
        GameManager.get().getManager(GuiManager.class).getGui(DebugModeGui.class).toggle();
    }
}


