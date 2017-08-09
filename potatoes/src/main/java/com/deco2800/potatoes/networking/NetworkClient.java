package com.deco2800.potatoes.networking;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

public class NetworkClient {
    Client client;

    /**
     * Initializes a client for the game,
     * when this method finishes the client should have connected successfully
     * @param IP
     * @param tcpPort
     * @param udpPort
     */
    public NetworkClient(String IP, int tcpPort, int udpPort) {
        // Initialize client object
        client = new Client();
        client.start();

        // Register our serializable objects
        Network.register(client);

        // Setup listeners
        client.addListener(new Listener() {
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

        try {
            client.connect(5000, IP, tcpPort, udpPort);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }
}
