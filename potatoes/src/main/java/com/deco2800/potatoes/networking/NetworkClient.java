package com.deco2800.potatoes.networking;

import com.badlogic.gdx.Game;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;
import java.util.ArrayList;

import com.deco2800.potatoes.networking.Network.*;
import org.lwjgl.Sys;

import javax.swing.text.html.parser.Entity;

public class NetworkClient {
    private Client client;
    private String IP;
    private String name;
    private int tcpPort;
    private int udpPort;
    // ID of this client
    private int clientID;

    // If connection is established and everything is initialized this should be true.
    public boolean ready;

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

                    System.out.println("Got host connection confirm message: " + m.id);

                    clientID = m.id;
                    return;
                }

                if (object instanceof HostPlayReadyMessage) {
                    HostPlayReadyMessage m = (HostPlayReadyMessage) object;
                    System.out.println("I'm ready to go!");
                    ready = true;
                }

                if (object instanceof HostNewPlayerMessage) {
                    HostNewPlayerMessage m = (HostNewPlayerMessage) object;

                    System.out.println("Got host new player message: " + m.id);

                    // Make the player
                    Player p = new Player(10, 10, 0);
                    GameManager.get().getWorld().addEntity(p, m.id);

                    if (clientID == m.id) {
                        System.out.println("IT'S ME!");

                        // Give the player manager me
                        ((PlayerManager)GameManager.get().getManager(PlayerManager.class)).setPlayer(p);
                    }

                    clientList.add(m.name);

                    return;
                }


                if (object instanceof HostExistingPlayerMessage) {
                    HostExistingPlayerMessage m = (HostExistingPlayerMessage) object;

                    System.out.println("Got host existing player message: " + m.id);
                    clientList.add(m.name);

                    return;
                }

                if (object instanceof HostEntityCreationMessage) {
                    HostEntityCreationMessage m = (HostEntityCreationMessage) object;

                    System.out.format("Got host entity creation message: %s, {%f, %f}%n",
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

                if (object instanceof EntityDestroyMessage) {
                    EntityDestroyMessage m = (EntityDestroyMessage) object;

                    GameManager.get().getWorld().removeEntity(m.id);

                    return;
                }

                if (object instanceof HostEntityUpdatePositionMessage) {
                    HostEntityUpdatePositionMessage m = (HostEntityUpdatePositionMessage) object;

                    GameManager.get().getWorld().getEntities().get(m.id).setPosX(m.x);
                    GameManager.get().getWorld().getEntities().get(m.id).setPosY(m.y);

                    return;
                }
            }

            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);
            }
        });

        client.connect(5000, IP, tcpPort, udpPort);

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

    public void broadcastEntityUpdatePosition(AbstractEntity entity, int id) {
        ClientEntityUpdatePositionMessage message = new ClientEntityUpdatePositionMessage();
        message.x = entity.getPosX();
        message.y = entity.getPosY();
        message.id = id;

        client.sendUDP(message);
    }

    public int getID() {
        return clientID;
    }

    public ArrayList<String> getClients() {
        return new ArrayList<>(clientList);
    }
}
