package com.deco2800.potatoes.multiplayer;


import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.networking.ClientMessageProcessor;
import com.deco2800.potatoes.networking.Network;
import com.deco2800.potatoes.networking.NetworkClient;
import com.esotericsoftware.kryonet.Client;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ClientMessageProcessorTest {

    PlayerManager playerManager;
    Player player;

    @Before
    public void setUp() {
        playerManager = GameManager.get().getManager(PlayerManager.class);
        player = new Player();
        playerManager.setPlayer(player);

    }

    @After
    public void tearDown() {
        GameManager.get().clearManagers();
        player = null;
    }

    @Test
    public void testConnectionConfirmMessage() {
        NetworkClient nc = new NetworkClient();
        Network.HostConnectionConfirmMessage m = new Network.HostConnectionConfirmMessage();
        m.setId(8);
        ClientMessageProcessor.processMessage(nc, m);

        assertEquals(8, nc.getID());
    }

    @Test
    public void testHostDisconnectMessage() {
        NetworkClient nc = new NetworkClient();
        nc.client = new Client();
        Network.HostDisconnectMessage m = new Network.HostDisconnectMessage();
        ClientMessageProcessor.processMessage(nc, m);
        assertEquals(null, nc.client);
    }

    @Test
    public void testPlayReadyMessage() {
        NetworkClient nc = new NetworkClient();
        Network.HostPlayReadyMessage m = new Network.HostPlayReadyMessage();
        ClientMessageProcessor.processMessage(nc, m);

        assertEquals(true, nc.ready);
    }

    @Test
    public void testNewPlayerMessage() {
        NetworkClient nc = new NetworkClient();
        Network.HostNewPlayerMessage m = new Network.HostNewPlayerMessage();
        m.setName("bob");
        m.setId(8);
        ClientMessageProcessor.processMessage(nc, m);

        assertEquals("bob", nc.getClients().get(8));

        m.setName("me");
        m.setId(2);
        nc.setID(2);
        ClientMessageProcessor.processMessage(nc, m);
        assertEquals("me", nc.getClients().get(2));
        assertNotEquals(null,
                playerManager.getPlayer());
    }

    @Test
    public void testPlayerDisconnectedMessage() {
        NetworkClient nc = new NetworkClient();

        // Make player
        Network.HostNewPlayerMessage playerMessage = new Network.HostNewPlayerMessage();
        playerMessage.setName("bob");
        playerMessage.setId(8);
        ClientMessageProcessor.processMessage(nc, playerMessage);

        assertEquals("bob", nc.getClients().get(8));

        Network.HostPlayerDisconnectedMessage m = new Network.HostPlayerDisconnectedMessage();
        m.setId(8);
        ClientMessageProcessor.processMessage(nc, m);

        assertEquals(null, nc.getClients().get(8));
    }

    @Test
    public void testExistingPlayerMessage() {
        NetworkClient nc = new NetworkClient();
        Network.HostExistingPlayerMessage m = new Network.HostExistingPlayerMessage();
        m.setName("bob");
        m.setId(8);
        ClientMessageProcessor.processMessage(nc, m);

        assertEquals("bob", nc.getClients().get(8));
    }
}
