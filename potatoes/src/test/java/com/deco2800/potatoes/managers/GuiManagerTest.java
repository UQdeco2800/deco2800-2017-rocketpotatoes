package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.gui.Gui;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GuiManagerTest {

    GuiManager guiManager;
    Gui gui;

    @Before
    public void setUp() {
        guiManager = GameManager.get().getManager(GuiManager.class);
    }

    @After
    public void tearDown() {

        GameManager.get().clearManagers();
        gui = null;
    }

    @Test
    public void testGuiAssignment() {

        // Expect null with no gui
        assertEquals(null, guiManager.getGui(Gui.class));

        gui = new Gui();
        guiManager.addGui(gui);

        assertEquals(gui, guiManager.getGui(Gui.class));
    }

    @Test
    public void testStage() {

        assertEquals(null, guiManager.getStage());
        
    }

}