package com.deco2800.potatoes;

import com.deco2800.potatoes.managers.MultiplayerManager;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MultiplayerTest {
    private MultiplayerManager m = null;

    @Test
    public void testInit() {
        m = new MultiplayerManager();
        assertEquals("", m.getIP());
        assertEquals(-1, m.getPort());
    }

    @Test
    public void testPorts() {
        m = new MultiplayerManager();
        assertEquals(true, MultiplayerManager.isValidPort(0));
        assertEquals(false, MultiplayerManager.isValidPort(5));
        assertEquals(true, MultiplayerManager.isValidPort(1024));
        assertEquals(false, MultiplayerManager.isValidPort(700000));
    }
}
