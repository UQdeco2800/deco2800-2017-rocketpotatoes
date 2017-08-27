package com.deco2800.potatoes.networking;

import com.badlogic.gdx.graphics.Color;
import com.deco2800.potatoes.entities.HasProgress;
import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.gui.ChatGui;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.GuiManager;
import com.deco2800.potatoes.managers.PlayerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Static processor for messages
 */
public class ClientMessageProcessor {
    private static final transient Logger LOGGER = LoggerFactory.getLogger(ClientMessageProcessor.class);

    /**
     * Calls the appropriate handler for the given message
     * @param client client to alter
     * @param object message object
     */
    public static void processMessage(NetworkClient client, Object object) {
        if (object instanceof Network.HostConnectionConfirmMessage) {
            connectionConfirmMessage(client, (Network.HostConnectionConfirmMessage) object);
        }
        else if (object instanceof Network.HostDisconnectMessage) {
            disconnectMessage(client, (Network.HostDisconnectMessage) object);
        }
        else if (object instanceof Network.HostPlayReadyMessage) {
            playReadyMessage(client, (Network.HostPlayReadyMessage) object);
        }
        else if (object instanceof Network.HostNewPlayerMessage) {
            newPlayerMessage(client, (Network.HostNewPlayerMessage) object);
        }
        else if (object instanceof Network.HostPlayerDisconnectedMessage) {
            playerDisconnectMessage(client, (Network.HostPlayerDisconnectedMessage) object);
        }
        else if (object instanceof Network.HostExistingPlayerMessage) {
            existingPlayerMessage(client, (Network.HostExistingPlayerMessage) object);
        }
        else if (object instanceof Network.HostEntityCreationMessage) {
            entityCreationMessage(client, (Network.HostEntityCreationMessage) object);
        }
        else if (object instanceof Network.HostEntityDestroyMessage) {
            entityDestroyMessage(client, (Network.HostEntityDestroyMessage) object);
        }
        /* Gameplay messages. i.e. none of these should be processed until the client is ready! */
        else if (client.ready) {
            if (object instanceof Network.HostEntityUpdatePositionMessage) {
                entityUpdatePositionMessage(client, (Network.HostEntityUpdatePositionMessage) object);
            }
            else if (object instanceof Network.HostEntityUpdateProgressMessage) {
                entityUpdateProgressMessage(client, (Network.HostEntityUpdateProgressMessage) object);
            }
            else if (object instanceof Network.HostChatMessage) {
                chatMessage(client, (Network.HostChatMessage) object);
            }
            else {
                throw new IllegalArgumentException("Unhandled message type: " + object);
            }
        }
        else {
            throw new IllegalArgumentException("Unhandled message type: " + object);
        }
    }

    /**
     * Handles a connection confirm message
     *
     * This message will be the first message back from a successful connection. And supplies the client id
     *
     * @param client the network client to process this event
     * @param m the message
     */
    private static void connectionConfirmMessage(NetworkClient client, Network.HostConnectionConfirmMessage m) {

        //System.out.println("[CLIENT]: Got host connection confirm message: " + m.id);

        client.setID(m.id);
    }

    /**
     * This message informs a connecting/connected client that they have been disconnected
     *
     * Currently the disconnection is fairly abrupt and will likely crash the client.
     *
     * @param client the network client to process this event
     * @param m the message
     */
    private static void disconnectMessage(NetworkClient client, Network.HostDisconnectMessage m) {

        //System.out.println("[CLIENT]: disconnected because: " + m.message);
        client.disconnect();
        // TODO notify game somehow. (Maybe we wait for connection confirmation before we start the client
         // thread?
    }

    /**
     * Handles a ready message
     *
     * Tells the client that they have successfully joined the server and have been sent all sync data
     * The client should block when connecting until this message (client.ready is set to true)
     * This also sends a system chat message informing the connection was complete
     *
     * @param client the network client to process this event
     * @param m the message
     */
    private static void playReadyMessage(NetworkClient client, Network.HostPlayReadyMessage m) {
        //System.out.println("[CLIENT]: I'm ready to go!");
        client.sendSystemMessage("Successfully joined server!");
        client.ready = true;
    }

    /**
     * Handles a new player join message
     *
     * Upon a new client joining all clients (including the new one) will receive this and as a result should
     * create the player entity associated with the new clients id.
     * Also informs the client via chat of the new player
     *
     * @param client the network client to process this event
     * @param m the message
     */
    private static void newPlayerMessage(NetworkClient client, Network.HostNewPlayerMessage m) {
        //System.out.println("[CLIENT]: Got host new player message: " + m.id);


        client.getClients().set(m.id, m.name);

        // Make the player
        Player p = new Player(10 + m.id, 10 + m.id, 0);

        try {
            GameManager.get().getWorld().addEntity(p, m.id);

        } catch (Exception ex) {
            // TODO Throws when we try run this in a test, this is a hacky fix for now!
        }


        if (client.getID() == m.id) {
            System.out.println("[CLIENT]: IT'S ME!");
            // Give the player manager me
            ((PlayerManager) GameManager.get().getManager(PlayerManager.class)).setPlayer(p);

        } else {
            client.sendSystemMessage("New Player Joined:" + m.name + "(" + m.id + ")");
        }

        client.getClients().add(m.name);
    }

    /**
     * Handles a player disconnecting
     *
     * All clients upon a player disconnecting will receive this message. As a result the players entity object will
     * be destroyed and the client informed via chat
     *
     * @param client the network client to process this event
     * @param m the message
     */
    private static void playerDisconnectMessage(NetworkClient client, Network.HostPlayerDisconnectedMessage m) {

        //System.out.println("[CLIENT]: Got host player disconnected message " + m.id);
        client.sendSystemMessage("Player Disconnected: " + client.getClients().get(m.id) + "(" + m.id + ")");

        client.getClients().set(m.id, null);

        try {
            GameManager.get().getWorld().removeEntity(m.id);
        } catch (Exception ex) {
            // TODO Throws when we try run this in a test, this is a hacky fix for now!
        }

    }

    /**
     * Handles a existing player message
     *
     * Tells a joining client of an existing player. Will simply add it to the clients list of other clients
     * this is accessed via (client.getClients()). The entity will be created when syncing other entities
     * Also informs the new client via chat
     *
     * @param client the network client to process this event
     * @param m the message
     */
    private static void existingPlayerMessage(NetworkClient client, Network.HostExistingPlayerMessage m) {

        //System.out.println("[CLIENT]: Got host existing player message: " + m.id);
        client.sendSystemMessage("Existing Player: " + m.name + "(" + m.id + ")");
        client.getClients().set(m.id, m.name);
    }

    /**
     * Handles a entity creation message
     *
     * Upon syncing of state and during game play this message will often be sent. Creates an entity of the given id
     *
     * @param client the network client to process this event
     * @param m the message
     */
    private static void entityCreationMessage(NetworkClient client, Network.HostEntityCreationMessage m) {

        //System.out.format("[CLIENT]: Got host entity creation message: %s, {%f, %f}%n",
        //        m.entity.toString(), m.entity.getPosX(), m.entity.getPosY());

        // -1 is the signal for put it wherever.
        if (m.id == -1) {
            GameManager.get().getWorld().addEntity(m.entity);
        } else {
            GameManager.get().getWorld().addEntity(m.entity, m.id);
        }
    }

    /**
     * Handles a entity destroy message
     *
     * Will destroy the designated entity
     *
     * @param client the network client to process this event
     * @param m the message
     */
    private static void entityDestroyMessage(NetworkClient client, Network.HostEntityDestroyMessage m) {

        //System.out.println("[CLIENT]: Got host destroy entity message: " + m.id);
        GameManager.get().getWorld().removeEntity(m.id);
    }

    /**
     * Handles a entity update position
     *
     * Updates the position of an existing entity via id. Also used to update player positions
     *
     * @param client the network client to process this event
     * @param m the message
     */
    private static void entityUpdatePositionMessage(NetworkClient client, Network.HostEntityUpdatePositionMessage m) {

        GameManager.get().getWorld().getEntities().get(m.id).setPosX(m.x);
        GameManager.get().getWorld().getEntities().get(m.id).setPosY(m.y);
    }

    /**
     * Handles a update progress message
     *
     * Simple message to handle health, timers etc for entities. Only supports a single counter and will need to be
     * expanded.
     *
     * @param client the network client to process this event
     * @param m the message
     */
    private static void entityUpdateProgressMessage(NetworkClient client, Network.HostEntityUpdateProgressMessage m) {

        // TODO verification?
        ((HasProgress) GameManager.get().getWorld().getEntities().get(m.id)).setProgress(m.progress);

    }

    /**
     * Handles a chat message
     *
     * Received upon a chat message being sent from any client (including the receiver)
     * Adds the message to the chat gui
     *
     * @param client the network client to process this event
     * @param m the message
     */
    private static void chatMessage(NetworkClient client, Network.HostChatMessage m) {
        GuiManager g = (GuiManager) GameManager.get().getManager(GuiManager.class);
        ChatGui c = ((ChatGui) g.getGui(ChatGui.class));
        if (c != null) {
            c.addMessage(
                    client.getClients().get(m.id) + " (" + m.id + ")",
                    m.message, Color.WHITE);
        }

    }

}
