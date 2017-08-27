package com.deco2800.potatoes.multiplayer;


import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.networking.ClientMessageProcessor;
import com.deco2800.potatoes.networking.Network;
import com.deco2800.potatoes.networking.NetworkClient;
import com.esotericsoftware.kryonet.Client;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ClientMessageProcessorTest {
    @Test
    public void testConnectionConfirmMessage() {
        NetworkClient nc = new NetworkClient();
        Network.HostConnectionConfirmMessage m = new Network.HostConnectionConfirmMessage();
        m.id = 8;
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
        m.name = "bob";
        m.id = 8;
        ClientMessageProcessor.processMessage(nc, m);

        assertEquals("bob", nc.getClients().get(8));

        m.name = "me";
        m.id = 2;
        nc.setID(2);
        ClientMessageProcessor.processMessage(nc, m);
        assertEquals("me", nc.getClients().get(2));
        assertNotEquals(null,
                ((PlayerManager) GameManager.get().getManager(PlayerManager.class)).getPlayer());
    }

    @Test
    public void testPlayerDisconnectedMessage() {
        NetworkClient nc = new NetworkClient();

        // Make player
        Network.HostNewPlayerMessage playerMessage = new Network.HostNewPlayerMessage();
        playerMessage.name = "bob";
        playerMessage.id = 8;
        ClientMessageProcessor.processMessage(nc, playerMessage);

        assertEquals("bob", nc.getClients().get(8));

        Network.HostPlayerDisconnectedMessage m = new Network.HostPlayerDisconnectedMessage();
        m.id = 8;
        ClientMessageProcessor.processMessage(nc, m);

        assertEquals(null, nc.getClients().get(8));
    }

    @Test
    public void testExistingPlayerMessage() {
        NetworkClient nc = new NetworkClient();
        Network.HostExistingPlayerMessage m = new Network.HostExistingPlayerMessage();
        m.name = "bob";
        m.id = 8;
        ClientMessageProcessor.processMessage(nc, m);

        assertEquals("bob", nc.getClients().get(8));
    }
}
