package com.deco2800.potatoes.networking;

import com.badlogic.gdx.graphics.Color;
import com.deco2800.potatoes.entities.player.Player;
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

        client.setID(m.getId());
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

        client.disconnect();
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
        client.getClients().set(m.getId(), m.getName());

        // Make the player
        Player p = new Player(10 + m.getId(), 10 + m.getId());

        try {
            GameManager.get().getWorld().addEntity(p, m.getId());

        } catch (Exception ex) {
            // Throws when we try run this in a test, this is a hacky fix for now!
        }


        if (client.getID() == m.getId()) {
            System.out.println("[CLIENT]: IT'S ME!");
            // Give the player manager me
            GameManager.get().getManager(PlayerManager.class).setPlayer(p);

        } else {
            client.sendSystemMessage("New Player Joined:" + m.getName() + "(" + m.getId() + ")");
        }

        client.getClients().add(m.getName());
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

        client.sendSystemMessage("Player Disconnected: " + client.getClients().get(m.getId()) + "(" + m.getId() + ")");

        client.getClients().set(m.getId(), null);

        try {
            GameManager.get().getWorld().removeEntity(m.getId());
        } catch (Exception ex) {
            // Throws when we try run this in a test, this is a hacky fix for now!
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
        client.sendSystemMessage("Existing Player: " + m.getName() + "(" + m.getId() + ")");
        client.getClients().set(m.getId(), m.getName());
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

        // -1 is the signal for put it wherever.
        if (m.getId() == -1) {
            GameManager.get().getWorld().addEntity(m.getEntity());
        } else {
            GameManager.get().getWorld().addEntity(m.getEntity(), m.getId());
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

        GameManager.get().getWorld().removeEntity(m.getId());
    }

    /**
     * Handles a entity update position
     *
     * Updates the position of an existing entity via id. Also used to update player calculatePositions
     *
     * @param client the network client to process this event
     * @param m the message
     */
    private static void entityUpdatePositionMessage(NetworkClient client, Network.HostEntityUpdatePositionMessage m) {

        GameManager.get().getWorld().getEntities().get(m.getId()).setPosX(m.getX());
        GameManager.get().getWorld().getEntities().get(m.getId()).setPosY(m.getY());
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

		LOGGER.error("Trying to use setProgress to update the progress. This is no longer currently"
				+ " part of the HasProgress interface and needs to be fixed.");
        //((HasProgress) GameManager.get().getWorld().getEntities().get(m.id)).setProgress(m.progress);

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
        GuiManager g = GameManager.get().getManager(GuiManager.class);
        ChatGui c = g.getGui(ChatGui.class);
        if (c != null) {
            c.addMessage(
                    client.getClients().get(m.getId()) + " (" + m.getId() + ")",
                    m.getMessage(), Color.WHITE);
        }

    }

}
