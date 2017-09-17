package com.deco2800.potatoes.networking;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.WorldUtil;
import com.esotericsoftware.kryonet.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Static processor for messages
 */
public class ServerMessageProcessor {
    private static final transient Logger LOGGER = LoggerFactory.getLogger(ClientMessageProcessor.class);

    /**
     * Calls the appropriate handler for the given message
     *
     * @param server server to alter
     * @param c      NetworkConnection object, describes the sender
     * @param object message object
     */
    public static void processMessage(NetworkServer server, NetworkServer.NetworkConnection c, Object object) {
        if (object instanceof Network.ClientConnectionRegisterMessage) {
            processConnectionRegisterMessage(server, c, (Network.ClientConnectionRegisterMessage) object);
        }
        else if (object instanceof Network.ClientPlayerUpdatePositionMessage) {
            processEntityUpdateMessage(server, c, (Network.ClientPlayerUpdatePositionMessage) object);
        }
        else if (object instanceof Network.ClientBuildOrderMessage) {
            processBuildOrderMessage(server, c, (Network.ClientBuildOrderMessage) object);
        }
        else if (object instanceof Network.ClientChatMessage) {
            processChatMessage(server, c, (Network.ClientChatMessage) object);
        }
        else {
            throw new IllegalArgumentException("Unhandled message type");
        }
    }

    /**
     * Processes a connection register message
     *
     * Responsible for syncing initial state, informing existing clients, setting up ids/names
     * And informing the new client when they are ready
     *
     * @param server the server object to use
     * @param c the connection object that holds details about the sender
     * @param m the message containing specific data
     */
    private static void processConnectionRegisterMessage(NetworkServer server, NetworkServer.NetworkConnection c,
                                                         Network.ClientConnectionRegisterMessage m) {
        // If too many clients reject.
        if (c.getID() > 16) {
            Network.HostDisconnectMessage mes = new Network.HostDisconnectMessage();
            mes.message = "Server full!";
            server.server.sendToTCP(c.getID(), mes);
            return;
        }

        // Set connection name
        c.name = m.name;

        // Tell the new client their id
        Network.HostConnectionConfirmMessage cResponse = new Network.HostConnectionConfirmMessage();
        cResponse.id = (byte) c.getID();
        server.server.sendToTCP(c.getID(), cResponse);

        // Tell the client of all the other clients in order
        for (Connection con : server.server.getConnections()) {
            // Don't tell the new client about itself through this
            if (con.getID() == c.getID()) {
                continue;
            }

            NetworkServer.NetworkConnection nCon = (NetworkServer.NetworkConnection) con;
            Network.HostExistingPlayerMessage newMess = new Network.HostExistingPlayerMessage();
            newMess.id = nCon.getID();
            newMess.name = nCon.name;
            server.server.sendToTCP(c.getID(), newMess);

            // Tell the new client about all the entities (unless it's master)
            if (!server.isMaster(c)) {
                for (Map.Entry<Integer, AbstractEntity> e : GameManager.get().getWorld().getEntities().entrySet()) {
                    Network.HostEntityCreationMessage create = new Network.HostEntityCreationMessage();
                    create.entity = e.getValue();
                    create.id = e.getKey();
                    server.server.sendToTCP(c.getID(), create);
                }
            }
        }

        // Tell everyone of a new player
        Network.HostNewPlayerMessage response = new Network.HostNewPlayerMessage();
        response.id = (byte) c.getID();
        response.name = m.name;
        server.server.sendToAllTCP(response);

        // Finally tell the client they are ready to play
        Network.HostPlayReadyMessage playMess = new Network.HostPlayReadyMessage();
        server.server.sendToTCP(c.getID(), playMess);

    }

    /**
     * Processes an entity update
     *
     * Takes x, y coords from the message and updates the position of an entity.
     * Assumes it exists
     *
     * Currently only used to update player calculatePositions (master uses a different method)
     *
     * @param server the server object to use
     * @param c the connection object that holds details about the sender
     * @param m the message containing specific data
     */
    private static void processEntityUpdateMessage(NetworkServer server, NetworkServer.NetworkConnection c,
                                                    Network.ClientPlayerUpdatePositionMessage m) {
        Network.HostEntityUpdatePositionMessage response = new Network.HostEntityUpdatePositionMessage();
        response.x = m.x;
        response.y = m.y;
        response.id = c.getID();

        server.server.sendToAllExceptUDP(c.getID(), response);
    }

    /**
     * Processes a build order
     *
     * Builds a tower entity at the given position if possible, otherwise does nothing
     *
     * @param server the server object to use
     * @param c the connection object that holds details about the sender
     * @param m the message containing specific data
     */
    private static void processBuildOrderMessage(NetworkServer server, NetworkServer.NetworkConnection c,
                                                 Network.ClientBuildOrderMessage m) {
        if (!WorldUtil.getEntityAtPosition(m.tree.getPosX(), m.tree.getPosY()).isPresent()) {
            GameManager.get().getWorld().addEntity(m.tree);
        }
    }

    /**
     * Processes a chat message
     *
     * Simply broadcasts the message to all clients
     *
     * @param server the server object to use
     * @param c the connection object that holds details about the sender
     * @param m the message containing specific data
     */
    private static void processChatMessage(NetworkServer server, NetworkServer.NetworkConnection c,
                                           Network.ClientChatMessage m) {
        Network.HostChatMessage response = new Network.HostChatMessage();
        response.id = c.getID();
        response.message = m.message;
        server.server.sendToAllTCP(response);

    }
}
