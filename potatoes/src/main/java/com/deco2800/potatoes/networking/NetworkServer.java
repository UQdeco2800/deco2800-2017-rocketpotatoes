package com.deco2800.potatoes.networking;

import com.badlogic.gdx.graphics.Color;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.health.HasProgress;
import com.deco2800.potatoes.gui.ChatGui;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.GuiManager;
import com.deco2800.potatoes.networking.Network.*;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;

public class NetworkServer {
    // Master will always be the first connection
    private static final int MASTER_ID = 1;

    Server server;
    private int tcpPort;
    private int udpPort;
    private String ipAddress;

    // If connection is established and everything is initialized this should be true.
    public volatile boolean ready;

    /**

     * Starts a server for the game
     *
     * @param tcpPort tcp port to use, presumed to be correct
     * @param udpPort udp port to use, presumed to be correct
     * @throws IOException
     */
    public NetworkServer(int tcpPort, int udpPort) throws IOException {
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
        this.ready = false;

        Log.set(Log.LEVEL_WARN);
        // Create server object
        server = new Server() {
            @Override
            protected Connection newConnection() {
                return new NetworkConnection();
            }
        };

        // Register classes for serialization
        Network.register(server);

        //Get IP Address
        ipAddress = "Local Host";

        // Hacky
        NetworkServer thisServer = this;

        // Setup our listeners

        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                super.connected(connection);
            }

            @Override
            public void received(Connection connection, Object object) {
                super.received(connection, object);

                if (object instanceof FrameworkMessage) {
                    return;
                }

                // Cast connection to our custom type (will always be this type because of our custom newConnection())
                NetworkConnection c = (NetworkConnection) connection;

                ServerMessageProcessor.processMessage(thisServer, c, object);
            }

            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);

                // Cast connection to our custom type (will always be this type because of our custom newConnection())
                NetworkConnection c = (NetworkConnection) connection;

                // Send disconnection message
                HostPlayerDisconnectedMessage m = new HostPlayerDisconnectedMessage();
                m.setId(c.getID());
                server.sendToAllTCP(m);
            }
        });
        server.bind(tcpPort, udpPort);
        server.start();
        this.ready = true;
        sendSystemMessage("Broadcasting on " + ipAddress + ":" + tcpPort);
    }

    public void broadcastNewEntity(int id) {
        HostEntityCreationMessage message = new HostEntityCreationMessage();
        message.setEntity(GameManager.get().getWorld().getEntities().get(id));
        message.setId(id);

        // TCP because important info and I haven't made the super awesome safe UDP yet.
        server.sendToAllExceptTCP(MASTER_ID, message);
    }

    public void broadcastEntityUpdatePosition(int id) {
        HostEntityUpdatePositionMessage message = new HostEntityUpdatePositionMessage();

        AbstractEntity entity = GameManager.get().getWorld().getEntities().get(id);
        message.setId(id);
        message.setX(entity.getPosX());
        message.setY(entity.getPosY());

        // Tell everyone except the master.
        server.sendToAllExceptUDP(MASTER_ID, message);
    }

    public void broadcastEntityUpdateProgress(int id) {
        HostEntityUpdateProgressMessage message = new HostEntityUpdateProgressMessage();

        AbstractEntity entity = GameManager.get().getWorld().getEntities().get(id);
        if (entity instanceof HasProgress) {
            HasProgress e = (HasProgress) entity;
            message.setId(id);
            message.setProgress(e.getProgress());

            // Tell everyone except the master.
            server.sendToAllExceptUDP(MASTER_ID, message);
        }
        else {
            throw new IllegalArgumentException("Entity doesn't implement HasProgress!");
        }
    }

    public void broadcastEntityDestroy(int id) {
        HostEntityDestroyMessage message = new HostEntityDestroyMessage();

        message.setId(id);

        server.sendToAllExceptTCP(MASTER_ID, message);
    }

    /**
     * Posts a system message to the chat of this client
     * @param m
     */
    private void sendSystemMessage(String m) {
        GuiManager g = GameManager.get().getManager(GuiManager.class);
        ChatGui chat = g.getGui(ChatGui.class);
        if (chat != null) {
            chat.addMessage("System", m, Color.YELLOW);
        }
    }


    /**
     * @param c the connection object to check
     * @return Returns if this connection is master
     */
    public boolean isMaster(Connection c) {
        return c.getID() == MASTER_ID;
    }

    public void shutdown() {
        server.close();
    }

    /* Connection object containing information about a single connection, various attributes should be kept in this
    * i.e. name, statistics, resources etc */
    static class NetworkConnection extends Connection {
        public String name;
    }
}
