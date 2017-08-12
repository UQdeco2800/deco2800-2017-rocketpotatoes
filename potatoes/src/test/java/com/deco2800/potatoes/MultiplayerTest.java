package com.deco2800.potatoes;

import com.deco2800.potatoes.entities.Tower;
import com.deco2800.potatoes.managers.MultiplayerManager;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MultiplayerTest {
    private MultiplayerManager m = null;

    @Test
    public void testInit() {
        m = new MultiplayerManager();
        assertEquals("", m.getIP());
        assertEquals(-1, m.getClientPort());
        assertEquals(-1, m.getServerPort());
        assertEquals(false, m.isMultiplayer());
        assertEquals(false, m.isMaster());
        assertEquals(true, m.isReady());

        // Might now work well as a test? What if port 1337 is occupied?
        m.createHost(1337);
        try {
            m.joinGame("Test", "127.0.0.1", 1337);
        }
        catch (IOException ex) {
            System.out.println("Failed to start test server");
            assertEquals(true, false); // lol
        }

        assertEquals("", m.getIP());
        assertEquals(1337, m.getClientPort());
        assertEquals(1337, m.getServerPort());
        assertEquals(true, m.isMultiplayer());
        assertEquals(true, m.isMaster());

        // TODO test protocols
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
