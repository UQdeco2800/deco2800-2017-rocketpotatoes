package com.deco2800.potatoes.networking;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;

import com.deco2800.potatoes.networking.Network.*;

import javax.swing.text.html.parser.Entity;

public class NetworkClient {
    Client client;
    private String IP;
    private int tcpPort;
    private int udpPort;

    /**
     * Initializes a client for the game,
     * when this method finishes the client should have connected successfully
     * @param IP
     * @param tcpPort
     * @param udpPort
     */
    public NetworkClient(String name, String IP, int tcpPort, int udpPort) {
        this.IP = IP;
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;

        Log.set(Log.LEVEL_DEBUG);
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
            }

            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);
            }
        });

        try {
            client.connect(5000, IP, tcpPort, udpPort);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        // Send initial connection info
        ClientConnectionRegisterMessage cr = new ClientConnectionRegisterMessage();
        cr.name = name;

        client.sendTCP(cr);
    }

    public void broadcastMessage(String message) {
        //client.sendTCP()
    }

    public void broadcastNewEntity(AbstractEntity entity) {
        // Entity creation is important so TCP!
        client.sendTCP(entity);
    }
}
