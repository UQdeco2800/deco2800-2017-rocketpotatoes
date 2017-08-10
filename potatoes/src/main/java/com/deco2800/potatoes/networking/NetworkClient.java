package com.deco2800.potatoes.networking;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;

import com.deco2800.potatoes.networking.Network.*;
import org.lwjgl.Sys;

import javax.swing.text.html.parser.Entity;

public class NetworkClient {
    Client client;
    private String IP;
    private int tcpPort;
    private int udpPort;
    // ID of this client
    private int clientID;

    /**
     * Initializes a client for the game,
     * when this method finishes the client should have connected successfully
     * @param IP
     * @param tcpPort
     * @param udpPort
     */
    public NetworkClient(String name, String IP, int tcpPort, int udpPort) {
        this.clientID = -1;
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

                // Connection message (gives us our client id)
                if (object instanceof HostConnectionConfirmMessage) {
                    HostConnectionConfirmMessage m = (HostConnectionConfirmMessage) object;

                    System.out.println("Got host connection confirm message: " + m.id);

                    clientID = m.id;
                    return;
                }

                if (object instanceof HostNewPlayerMessage) {
                    HostNewPlayerMessage m = (HostNewPlayerMessage) object;

                    System.out.println("Got host new player message: " + m.id);

                    // Make the player
                    Player p = new Player(0, 0, 0);
                    GameManager.get().getWorld().addEntity(p, m.id);

                    if (clientID == m.id) {
                        System.out.println("IT'S ME!");

                        // Give the player manager me
                        ((PlayerManager)GameManager.get().getManager(PlayerManager.class)).setPlayer(p);
                    }

                    return;
                }

                if (object instanceof HostEntityCreationMessage) {
                    HostEntityCreationMessage m = (HostEntityCreationMessage) object;

                    System.out.println("Got host entity creation message :" + m.entity);

                    GameManager.get().getWorld().addEntity(m.entity);

                    return;
                }

                if (object instanceof EntityUpdateMessage) {
                    EntityUpdateMessage m = (EntityUpdateMessage) object;

                    System.out.println("Got host entity update message :" + m.id + " : " + m.entity);

                    GameManager.get().getWorld().getEntities().get(m.id).setPosition(
                            m.entity.getPosX(), m.entity.getPosY(), m.entity.getPosZ());

                    return;
                }
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
        Message m = new Message();
        m.message = message;
        client.sendTCP(m);
    }

    public void broadcastNewEntity(AbstractEntity entity) {
        ClientEntityCreationMessage message = new ClientEntityCreationMessage();
        message.entity = entity;
        // Entity creation is important so TCP!
        client.sendTCP(message);
    }

    public void broadcastEntityUpdate(AbstractEntity entity, int id) {
        EntityUpdateMessage message = new EntityUpdateMessage();
        message.entity = entity;
        message.id = id;

        client.sendUDP(message);
    }

    public int getID() {
        return clientID;
    }
}
