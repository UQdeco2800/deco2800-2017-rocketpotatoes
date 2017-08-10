package com.deco2800.potatoes.networking;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;
import java.util.Map;

import com.deco2800.potatoes.networking.Network.*;

public class NetworkServer {
    private Server server;
    private int tcpPort;
    private int udpPort;

    /**
     * Starts a server for the game // TODO ports occupied?
     * @param tcpPort tcp port to use, presumed to be correct
     * @param udpPort udp port to use, presumed to be correct
     * @throws IOException
     */
    public NetworkServer(int tcpPort, int udpPort) throws IOException {
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;

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

        // Setup our listeners

        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                super.received(connection, object);

                // Cast connection to our custom type (will always be this type because of our custom newConnection())
                NetworkConnection c = (NetworkConnection) connection;

                // Process our objects
                if (object instanceof ClientConnectionRegisterMessage) {
                    ClientConnectionRegisterMessage m = (ClientConnectionRegisterMessage) object;

                    c.name = m.name;
                    System.out.println("Got name: " + c.name);

                    // Tell the new client their id
                    HostConnectionConfirmMessage cResponse = new HostConnectionConfirmMessage();
                    cResponse.id = c.getID();
                    server.sendToTCP(c.getID(), cResponse);

                    // Tell the new client about all the entities (unless it's master)
                    if (c.getID() != 1) {
                        System.out.println("Sending entity state...");
                        for (Map.Entry<Integer, AbstractEntity> e : GameManager.get().getWorld().getEntities().entrySet()) {
                            HostEntityCreationMessage create = new HostEntityCreationMessage();
                            create.entity = e.getValue();
                            create.id = e.getKey();
                            System.out.println(e.getValue() + " : " + e.getKey());

                            server.sendToTCP(c.getID(), create);
                        }
                    }

                    // Tell everyone of a new player
                    HostNewPlayerMessage response = new HostNewPlayerMessage();
                    response.id = c.getID();
                    response.name = m.name;

                    server.sendToAllTCP(response);
                    return;
                }

                if (object instanceof ClientEntityCreationMessage) {
                    ClientEntityCreationMessage m = (ClientEntityCreationMessage) object;

                    System.out.println("Got entity message: " + m.entity);

                    HostEntityCreationMessage response = new HostEntityCreationMessage();
                    response.entity = m.entity;
                    response.id = 1;
                    server.sendToAllTCP(response);

                    return;
                }

                if (object instanceof EntityUpdateMessage) {
                    EntityUpdateMessage m = (EntityUpdateMessage) object;

                    //System.out.println("Got client entity update message :" + m.id + " : " + m.entity);

                    server.sendToAllUDP(m);

                    return;
                }

                if (object instanceof Message) {
                    Message m = (Message) object;

                    System.out.println(c.name + " : " + m.message);

                    return;
                }
            }

            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);
            }
        });
        server.bind(tcpPort, udpPort);
        server.start();
    }

    /* Connection object containing information about a single connection, various attributes should be kept in this
    * i.e. name, statistics, resources etc */
    static class NetworkConnection extends Connection {
        public String name;
    }
}
