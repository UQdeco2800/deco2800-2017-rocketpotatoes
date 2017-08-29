package com.deco2800.potatoes.multiplayer;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.managers.MultiplayerManager;
import com.deco2800.potatoes.networking.Network;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.EndPoint;
import org.junit.Test;
import org.reflections.Reflections;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MultiplayerTest {

    @Test
    public void testInit() {
        MultiplayerManager m = new MultiplayerManager();
        assertEquals("", m.getIP());
        assertEquals(-1, m.getID());
        assertEquals(-1, m.getClientPort());
        assertEquals(-1, m.getServerPort());
        assertEquals(false, m.isMultiplayer());
        assertEquals(false, m.isMaster());
        assertEquals(true, m.isClientReady());

        // Might not work well as a test? What if port 1337 is occupied?
        // TODO this test sometimes throws a NullPointerException in the client thread when it tries to create a player
        try {
            m.createHost(1337);
            while (!m.isServerReady());
            m.joinGame("Test", "127.0.0.1", 1337);
        }
        catch (IOException ex) {
            System.out.println("Failed to start test server");
            fail();
        }

        while(!m.isClientReady());

        assertEquals("127.0.0.1", m.getIP());
        assertEquals(1, m.getID());
        assertEquals(1337, m.getClientPort());
        assertEquals(1337, m.getServerPort());
        assertEquals(true, m.isMultiplayer());
        assertEquals(true, m.isMaster());

        m.shutdownMultiplayer();

        assertEquals("", m.getIP());
        assertEquals(-1, m.getID());
        assertEquals(-1, m.getClientPort());
        assertEquals(-1, m.getServerPort());
        assertEquals(false, m.isMultiplayer());
        assertEquals(false, m.isMaster());
        assertEquals(true, m.isClientReady());

        // TODO test protocols
    }

    @Test
    public void testPorts() {
        assertEquals(true, MultiplayerManager.isValidPort(0));
        assertEquals(false, MultiplayerManager.isValidPort(5));
        assertEquals(true, MultiplayerManager.isValidPort(1024));
        assertEquals(false, MultiplayerManager.isValidPort(700000));
    }

    @Test
    public void testIP() {
        assertEquals(true, MultiplayerManager.isValidIP("0.0.0.0"));
        assertEquals(true, MultiplayerManager.isValidIP("127.0.0.1"));
        assertEquals(true, MultiplayerManager.isValidIP("255.255.255.255"));


        assertEquals(false, MultiplayerManager.isValidIP("dqwfwqfqwffq"));
        assertEquals(false, MultiplayerManager.isValidIP("255.255.255.256"));
        assertEquals(false, MultiplayerManager.isValidIP("-10.42.152.64"));
    }

    @Test
    public void testEntitiesAreRegistered() {
        // This test ensures all AbstractEntities are serializable and are registered.
        // Note this test doesn't ensure serialization will work across the network. You should ensure your
        // entities have all transient properties marked transient!

        EndPoint e = new Client();
        Network.register(e);


        // Reflection black magic to get all entity types and check that they are 1. serializable and 2. registered
        Reflections reflections = new Reflections("com.deco2800");

        Set<Class<? extends AbstractEntity>> modules =
                reflections.getSubTypesOf(com.deco2800.potatoes.entities.AbstractEntity.class);

        for (Class c : modules) {
            // Check registered!
            e.getKryo().getRegistration(c);
        }
    }
}
