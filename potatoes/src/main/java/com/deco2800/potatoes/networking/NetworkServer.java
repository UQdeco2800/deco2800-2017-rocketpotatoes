package com.deco2800.potatoes.networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;

public class NetworkServer {
    Server server;

    /**
     * Starts a server for the game // TODO ports occupied?
     * @param tcpPort tcp port to use, presumed to be correct
     * @param udpPort udp port to use, presumed to be correct
     * @throws IOException
     */
    public NetworkServer(int tcpPort, int udpPort) throws IOException {
        Log.set(Log.LEVEL_DEBUG);
        // Create server object
        server = new Server() {
            protected Connection connection() {
                return new NetworkConnection();
            }
        };

        // Register classes for serialization
        Network.register(server);

        // Setup our listeners

        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                super.connected(connection);
            }

            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);
            }

            @Override
            public void received(Connection connection, Object object) {
                super.received(connection, object);
            }

            @Override
            public void idle(Connection connection) {
                super.idle(connection);
            }
        });
        System.out.println("Server: Binding to " + tcpPort + ":" + udpPort);
        server.bind(tcpPort, udpPort);
        server.start();
    }


    static class NetworkConnection extends Connection {
        public String name;
    }
}
