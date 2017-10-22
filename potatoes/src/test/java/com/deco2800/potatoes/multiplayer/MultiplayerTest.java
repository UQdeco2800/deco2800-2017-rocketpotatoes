package com.deco2800.potatoes.multiplayer;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.MultiplayerManager;
import com.deco2800.potatoes.networking.Network;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.EndPoint;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.reflections.Reflections;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MultiplayerTest {

    MultiplayerManager multiplayerManager;

    @Before
    public void setUp() {
        multiplayerManager = GameManager.get().getManager(MultiplayerManager.class);
    }

    @After
    public void tearDown() {
        GameManager.get().clearManagers();
    }

    @Test
    public void testInit() {

        assertEquals("", multiplayerManager.getIP());
        assertEquals(-1, multiplayerManager.getID());
        assertEquals(-1, multiplayerManager.getClientPort());
        assertEquals(-1, multiplayerManager.getServerPort());
        assertEquals(false, multiplayerManager.isMultiplayer());
        assertEquals(false, multiplayerManager.isMaster());
        assertEquals(true, multiplayerManager.isClientReady());

        // Might not work well as a test? What if port 1337 is occupied?
        // this test sometimes throws a NullPointerException in the client thread when it tries to create a player
        try {
            multiplayerManager.createHost(1337);
            while (!multiplayerManager.isServerReady());
            multiplayerManager.joinGame("Test", "127.0.0.1", 1337);
        }
        catch (IOException ex) {
            System.out.println("Failed to start test server");
            fail();
        }

        while(!multiplayerManager.isClientReady());

        assertEquals("127.0.0.1", multiplayerManager.getIP());
        assertEquals(1, multiplayerManager.getID());
        assertEquals(1337, multiplayerManager.getClientPort());
        assertEquals(1337, multiplayerManager.getServerPort());
        assertEquals(true, multiplayerManager.isMultiplayer());
        assertEquals(true, multiplayerManager.isMaster());

        multiplayerManager.shutdownMultiplayer();

        assertEquals("", multiplayerManager.getIP());
        assertEquals(-1, multiplayerManager.getID());
        assertEquals(-1, multiplayerManager.getClientPort());
        assertEquals(-1, multiplayerManager.getServerPort());
        assertEquals(false, multiplayerManager.isMultiplayer());
        assertEquals(false, multiplayerManager.isMaster());
        assertEquals(true, multiplayerManager.isClientReady());

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
