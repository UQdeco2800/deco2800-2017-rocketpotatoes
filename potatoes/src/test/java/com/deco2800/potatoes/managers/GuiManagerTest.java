package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.gui.Gui;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GuiManagerTest {
    @Test
    public void testGuiAssignment() {
        GuiManager m = new GuiManager();

        // Expect null with no gui
        assertEquals(null, m.getGui(Gui.class));

        Gui g = new Gui();
        m.addGui(g);

        assertEquals(g, m.getGui(Gui.class));
    }

    @Test
    public void testStage() {
        GuiManager m = new GuiManager();
        assertEquals(null, m.getStage());

        // TODO test we can set the stage (problems with gdx)
    }

}