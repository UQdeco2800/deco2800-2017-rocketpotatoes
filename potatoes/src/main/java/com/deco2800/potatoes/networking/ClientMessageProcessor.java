package com.deco2800.potatoes.networking;

import com.badlogic.gdx.graphics.Color;
import com.deco2800.potatoes.entities.HasProgress;
import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.gui.ChatGui;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.GuiManager;
import com.deco2800.potatoes.managers.PlayerManager;

/*
    Static processor for messages
 */
public class ClientMessageProcessor {

    public static void connectionConfirmMessage(NetworkClient client, Network.HostConnectionConfirmMessage m) {

        //System.out.println("[CLIENT]: Got host connection confirm message: " + m.id);

        client.setID(m.id);
    }

    public static void disconnectMessage(NetworkClient client, Network.HostDisconnectMessage m) {

        //System.out.println("[CLIENT]: disconnected because: " + m.message);
        client.close();
        // TODO notify game somehow. (Maybe we wait for connection confirmation before we start the client
         // thread?
    }

    public static void playReadyMessage(NetworkClient client, Network.HostPlayReadyMessage m) {
        //System.out.println("[CLIENT]: I'm ready to go!");
        client.sendSystemMessage("Successfully joined server!");
        client.ready = true;
    }

    public static void newPlayerMessage(NetworkClient client, Network.HostNewPlayerMessage m) {
        //System.out.println("[CLIENT]: Got host new player message: " + m.id);


        client.getClients().set(m.id, m.name);

        try {
            // Make the player
            Player p = new Player(10 + m.id, 10 + m.id, 0);
            GameManager.get().getWorld().addEntity(p, m.id);

            if (client.getID() == m.id) {
                //System.out.println("[CLIENT]: IT'S ME!");
                // Give the player manager me
                ((PlayerManager) GameManager.get().getManager(PlayerManager.class)).setPlayer(p);

            } else {
                client.sendSystemMessage("New Player Joined:" + m.name + "(" + m.id + ")");
            }
        } catch (Exception ex) {
            // TODO Throws when we try run this in a test, this is a hacky fix for now!
        }

        client.getClients().add(m.name);
    }

    public static void playerDisconnectMessage(NetworkClient client, Network.HostPlayerDisconnectedMessage m) {

        //System.out.println("[CLIENT]: Got host player disconnected message " + m.id);
        client.sendSystemMessage("Player Disconnected: " + client.getClients().get(m.id) + "(" + m.id + ")");

        client.getClients().set(m.id, null);
        GameManager.get().getWorld().removeEntity(m.id);

        return;
    }

    public static void existingPlayerMessage(NetworkClient client, Network.HostExistingPlayerMessage m) {

        //System.out.println("[CLIENT]: Got host existing player message: " + m.id);
        client.sendSystemMessage("Existing Player: " + m.name + "(" + m.id + ")");
        client.getClients().set(m.id, m.name);
    }

    public static void entityCreationMessage(NetworkClient client, Network.HostEntityCreationMessage m) {

        //System.out.format("[CLIENT]: Got host entity creation message: %s, {%f, %f}%n",
        //        m.entity.toString(), m.entity.getPosX(), m.entity.getPosY());

        // -1 is the signal for put it wherever.
        if (m.id == -1) {
            GameManager.get().getWorld().addEntity(m.entity);
        } else {
            GameManager.get().getWorld().addEntity(m.entity, m.id);
        }
    }

    public static void entityDestroyMessage(NetworkClient client, Network.HostEntityDestroyMessage m) {

        //System.out.println("[CLIENT]: Got host destroy entity message: " + m.id);
        GameManager.get().getWorld().removeEntity(m.id);
    }

    public static void entityUpdatePositionMessage(NetworkClient client, Network.HostEntityUpdatePositionMessage m) {

        GameManager.get().getWorld().getEntities().get(m.id).setPosX(m.x);
        GameManager.get().getWorld().getEntities().get(m.id).setPosY(m.y);
    }

    public static void entityUpdateProgressMessage(NetworkClient client, Network.HostEntityUpdateProgressMessage m) {

        // TODO verification?
        ((HasProgress) GameManager.get().getWorld().getEntities().get(m.id)).setProgress(m.progress);

    }

    public static void chatMessage(NetworkClient client, Network.HostChatMessage m) {
        GuiManager g = (GuiManager) GameManager.get().getManager(GuiManager.class);
        ((ChatGui) g.getGui(ChatGui.class)).addMessage(
                client.getClients().get(m.id) + " (" + m.id + ")",
                m.message, Color.WHITE);

    }
}
