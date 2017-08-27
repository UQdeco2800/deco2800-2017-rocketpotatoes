package com.deco2800.potatoes.multiplayer;


import com.deco2800.potatoes.networking.ClientMessageProcessor;
import com.deco2800.potatoes.networking.Network;
import com.deco2800.potatoes.networking.NetworkClient;
import org.junit.Test;

public class ClientMessageProcessorTest {

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMessage() {
        NetworkClient nc = new NetworkClient();
        Object message = new Object();
        ClientMessageProcessor.processMessage(nc, message);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMessageWhenReady() {
        NetworkClient nc = new NetworkClient();
        nc.ready = true;
        Object message = new Object();
        ClientMessageProcessor.processMessage(nc, message);
    }

    @Test
    public void testConnectionConfirmMessage() {
        NetworkClient nc = new NetworkClient();
        Network.HostConnectionConfirmMessage m = new Network.HostConnectionConfirmMessage();
        ClientMessageProcessor.processMessage(nc, m);
    }

    @Test
    public void testHostDisconnectMessage() {
        NetworkClient nc = new NetworkClient();
        Network.HostDisconnectMessage m = new Network.HostDisconnectMessage();
        ClientMessageProcessor.processMessage(nc, m);
    }

    @Test
    public void testNewPlayerMessage() {
        NetworkClient nc = new NetworkClient();
        Network.HostPlayReadyMessage m = new Network.HostPlayReadyMessage();
        ClientMessageProcessor.processMessage(nc, m);
    }

    @Test
    public void testPlayerDisconnectedMessage() {
        NetworkClient nc = new NetworkClient();
        Network.HostPlayerDisconnectedMessage m = new Network.HostPlayerDisconnectedMessage();
        ClientMessageProcessor.processMessage(nc, m);
    }

    @Test
    public void testExistingPlayerMessage() {
        NetworkClient nc = new NetworkClient();
        Network.HostExistingPlayerMessage m = new Network.HostExistingPlayerMessage();
        ClientMessageProcessor.processMessage(nc, m);
    }

    @Test
    public void testEntityCreationMessage() {
        NetworkClient nc = new NetworkClient();
        Network.HostEntityCreationMessage m = new Network.HostEntityCreationMessage();
        ClientMessageProcessor.processMessage(nc, m);
    }

    @Test
    public void testEntityDestroyMessage() {
        NetworkClient nc = new NetworkClient();
        Network.HostEntityDestroyMessage m = new Network.HostEntityDestroyMessage();
        ClientMessageProcessor.processMessage(nc, m);
    }

    @Test
    public void testEntityUpdatePositionMessage() {
        NetworkClient nc = new NetworkClient();
        Network.HostEntityUpdatePositionMessage m = new Network.HostEntityUpdatePositionMessage();
        ClientMessageProcessor.processMessage(nc, m);
    }

    @Test
    public void testEntityUpdateProgressMessage() {
        NetworkClient nc = new NetworkClient();
        Network.HostEntityUpdateProgressMessage m = new Network.HostEntityUpdateProgressMessage();
        ClientMessageProcessor.processMessage(nc, m);
    }

    @Test
    public void testChatMessage() {
        NetworkClient nc = new NetworkClient();
        Network.HostChatMessage m = new Network.HostChatMessage();
        ClientMessageProcessor.processMessage(nc, m);
    }
}
