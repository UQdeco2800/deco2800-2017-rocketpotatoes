package com.deco2800.potatoes.networking;

import com.badlogic.gdx.graphics.Color;
import com.deco2800.potatoes.entities.HasProgress;
import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.gui.ChatGui;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.GuiManager;
import com.deco2800.potatoes.managers.PlayerManager;
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

        // Setup listeners
        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                super.received(connection, object);

                // Connection message (gives us our client id)
                if (object instanceof HostConnectionConfirmMessage) {
                    HostConnectionConfirmMessage m = (HostConnectionConfirmMessage) object;

                    System.out.println("[CLIENT]: Got host connection confirm message: " + m.id);

                    clientID = m.id;
                    return;
                }

                if (object instanceof HostDisconnectMessage) {
                    HostDisconnectMessage m = (HostDisconnectMessage) object;

                    System.out.println("[CLIENT]: disconnected because: " + m.message);
                    client.close();
                    // TODO notify game somehow. (Maybe we wait for connection confirmation before we start the client
                    // thread?
                }

                if (object instanceof HostPlayReadyMessage) {
                    HostPlayReadyMessage m = (HostPlayReadyMessage) object;
                    System.out.println("[CLIENT]: I'm ready to go!");
                    sendSystemMessage("Successfully joined server!");
                    ready = true;
                }

                if (object instanceof HostNewPlayerMessage) {
                    HostNewPlayerMessage m = (HostNewPlayerMessage) object;

                    System.out.println("[CLIENT]: Got host new player message: " + m.id);


                    clientList.set(m.id, m.name);

                    try {
                        // Make the player
                        Player p = new Player(10 + m.id, 10 + m.id, 0);
                        GameManager.get().getWorld().addEntity(p, m.id);

                        if (clientID == m.id) {
                            System.out.println("[CLIENT]: IT'S ME!");


                            // Give the player manager me
                            ((PlayerManager) GameManager.get().getManager(PlayerManager.class)).setPlayer(p);
                        } else {
                            sendSystemMessage("New Player Joined:" + m.name + "(" + m.id + ")");
                        }
                    }
                    catch (Exception ex) {
                        // TODO Throws when we try run this in a test, this is a hacky fix for now!
                    }

                    clientList.add(m.name);

                    return;
                }

                if (object instanceof HostPlayerDisconnectedMessage) {
                    HostPlayerDisconnectedMessage m = (HostPlayerDisconnectedMessage) object;

                    System.out.println("[CLIENT]: Got host player disconnected message " + m.id);
                    sendSystemMessage("Player Disconnected: " + clientList.get(m.id) + "(" + m.id + ")");

                    clientList.set(m.id, null);
                    GameManager.get().getWorld().removeEntity(m.id);

                    return;
                }


                if (object instanceof HostExistingPlayerMessage) {
                    HostExistingPlayerMessage m = (HostExistingPlayerMessage) object;

                    System.out.println("[CLIENT]: Got host existing player message: " + m.id);
                    sendSystemMessage("Existing Player: " + m.name + "(" + m.id + ")");
                    clientList.set(m.id, m.name);

                    return;
                }

                if (object instanceof HostEntityCreationMessage) {
                    HostEntityCreationMessage m = (HostEntityCreationMessage) object;

                    System.out.format("[CLIENT]: Got host entity creation message: %s, {%f, %f}%n",
                            m.entity.toString(), m.entity.getPosX(), m.entity.getPosY());

                    // -1 is the signal for put it wherever.
                    if (m.id == -1) {
                        GameManager.get().getWorld().addEntity(m.entity);
                    }
                    else {
                        GameManager.get().getWorld().addEntity(m.entity, m.id);
                    }

                    return;
                }

                if (object instanceof HostEntityDestroyMessage) {
                    HostEntityDestroyMessage m = (HostEntityDestroyMessage) object;

                    System.out.println("[CLIENT]: Got host destroy entity message: " + m.id);
                    GameManager.get().getWorld().removeEntity(m.id);

                    return;
                }

                /* Gameplay messages. i.e. none of these should be processed until the client is ready! */

                if (ready) {
                    if (object instanceof HostEntityUpdatePositionMessage) {
                        HostEntityUpdatePositionMessage m = (HostEntityUpdatePositionMessage) object;

                        GameManager.get().getWorld().getEntities().get(m.id).setPosX(m.x);
                        GameManager.get().getWorld().getEntities().get(m.id).setPosY(m.y);

                        return;
                    }

                    if (object instanceof HostEntityUpdateProgressMessage) {
                        HostEntityUpdateProgressMessage m = (HostEntityUpdateProgressMessage) object;

                        // TODO verification?
                        ((HasProgress)GameManager.get().getWorld().getEntities().get(m.id)).setProgress(m.progress);

                        return;
                    }

                    /* Generic chat message */
                    if (object instanceof Message) {
                        Message m = (Message) object;

                        GuiManager g = (GuiManager)GameManager.get().getManager(GuiManager.class);
                        ((ChatGui)g.getGui(ChatGui.class)).addMessage(
                                clientList.get(connection.getID()) + "(" + connection.getID() + ")"
                                , m.message, Color.WHITE);

                        return;
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
        Message m = new Message();
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
    private void sendSystemMessage(String m) {
        GuiManager g = (GuiManager)GameManager.get().getManager(GuiManager.class);
        ((ChatGui)g.getGui(ChatGui.class)).addMessage("System", m, Color.YELLOW);
    }

    public int getID() {
        return clientID;
    }

    public ArrayList<String> getClients() {
        return new ArrayList<>(clientList);
    }

    public void disconnect() {
        client.close();
    }
}
