package com.deco2800.potatoes.networking;

import com.badlogic.gdx.graphics.Color;
import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.gui.ChatGui;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.GuiManager;
import com.deco2800.potatoes.networking.Network.*;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;
import java.util.ArrayList;

public class NetworkClient {
    private Client client;
    private String IP;
    private String name;
    private int tcpPort;
    private int udpPort;
    // ID of this client
    private int clientID;

    /* If connection is established and everything is initialized this should be true.
            (Marked volatile because (I think) java caches values when you read them often, and we can get a lockup
            if we're repeatedly reading this value waiting for it to change)
     */
    public volatile boolean ready;

    // List of clients, index is the id
    private ArrayList<String> clientList;

    /**
     * Initializes a client for the game,
     * when this method finishes the client should have connected successfully
     * @param IP
     * @param tcpPort
     * @param udpPort
     */
    public NetworkClient(String name, String IP, int tcpPort, int udpPort) throws IOException {
        this.clientID = -1;
        this.IP = IP;
        this.name = name;
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
        this.ready = false;
        clientList = new ArrayList<>();
        // Allow up to 16 clients
        for (int i = 0; i < 16; ++i) {
            clientList.add(null);
        }

        Log.set(Log.LEVEL_WARN);
        // Initialize client object
        client = new Client();
        client.start();

        // Register our serializable objects
        Network.register(client);

        // Hacky!
        NetworkClient thisClient = this;

        // Setup listeners
        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                super.received(connection, object);

                // Connection message (gives us our client id)
                if (object instanceof HostConnectionConfirmMessage) {
                    ClientMessageProcessor.connectionConfirmMessage(thisClient, (HostConnectionConfirmMessage) object);
                }
                if (object instanceof HostDisconnectMessage) {
                    ClientMessageProcessor.disconnectMessage(thisClient, (HostDisconnectMessage) object);
                }
                if (object instanceof HostPlayReadyMessage) {
                    ClientMessageProcessor.playReadyMessage(thisClient, (HostPlayReadyMessage) object);
                }
                if (object instanceof HostNewPlayerMessage) {
                    ClientMessageProcessor.newPlayerMessage(thisClient, (HostNewPlayerMessage) object);
                }
                if (object instanceof HostPlayerDisconnectedMessage) {
                    ClientMessageProcessor.playerDisconnectMessage(thisClient, (HostPlayerDisconnectedMessage) object);
                }
                if (object instanceof HostExistingPlayerMessage) {
                    ClientMessageProcessor.existingPlayerMessage(thisClient, (HostExistingPlayerMessage) object);
                }
                if (object instanceof HostEntityCreationMessage) {
                    ClientMessageProcessor.entityCreationMessage(thisClient, (HostEntityCreationMessage) object);
                }
                if (object instanceof HostEntityDestroyMessage) {
                    ClientMessageProcessor.entityDestroyMessage(thisClient, (HostEntityDestroyMessage) object);
                }

                /* Gameplay messages. i.e. none of these should be processed until the client is ready! */

                if (ready) {
                    if (object instanceof HostEntityUpdatePositionMessage) {
                        ClientMessageProcessor.entityUpdatePositionMessage(thisClient, (HostEntityUpdatePositionMessage) object);
                    }
                    if (object instanceof HostEntityUpdateProgressMessage) {
                        ClientMessageProcessor.entityUpdateProgressMessage(thisClient, (HostEntityUpdateProgressMessage) object);
                    }
                    /* Generic chat message */
                    if (object instanceof HostChatMessage) {
                        ClientMessageProcessor.chatMessage(thisClient, (HostChatMessage) object);
                    }
                }
            }

            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);
            }
        });


        client.connect(5000, IP, tcpPort, udpPort);
        sendSystemMessage("Joining " + IP + ":" + tcpPort);
        // Send initial connection info
        ClientConnectionRegisterMessage cr = new ClientConnectionRegisterMessage();
        cr.name = name;

        client.sendTCP(cr);
    }

    /* Messages to be sent to the server from clients. Note that master should always be directly interacting with the
     * server. And as such methods such as creation/destruction of entities are located in the NetworkServer class. */

    public void broadcastMessage(String message) {
        ClientChatMessage m = new ClientChatMessage();
        m.message = message;
        client.sendTCP(m);
    }

    public void broadcastPlayerUpdatePosition(Player entity) {
        ClientPlayerUpdatePositionMessage message = new ClientPlayerUpdatePositionMessage();
        message.x = entity.getPosX();
        message.y = entity.getPosY();

        client.sendUDP(message);
    }

    public void broadcastBuildOrder(int x, int y) {
        ClientBuildOrderMessage m = new ClientBuildOrderMessage();
        m.x = x;
        m.y = y;

        client.sendTCP(m);
    }

    /**
     * Posts a system message to the chat of this client
     * @param m
     */
    public void sendSystemMessage(String m) {
        GuiManager g = (GuiManager)GameManager.get().getManager(GuiManager.class);
        ChatGui chat = ((ChatGui)g.getGui(ChatGui.class));
        if (chat != null) {
            chat.addMessage("System", m, Color.YELLOW);
        }
    }

    public void close() { client.close(); }

    public int getID() {
        return clientID;
    }

    public void setID(int id) { clientID = id; }

    public ArrayList<String> getClients() {
        return clientList;a
    }

    public void disconnect() {
        client.close();
    }
}
