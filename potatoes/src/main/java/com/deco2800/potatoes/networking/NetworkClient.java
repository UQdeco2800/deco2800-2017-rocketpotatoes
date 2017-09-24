package com.deco2800.potatoes.networking;

import com.badlogic.gdx.graphics.Color;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.entities.trees.AbstractTree;
import com.deco2800.potatoes.gui.ChatGui;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.GuiManager;
import com.deco2800.potatoes.networking.Network.ClientBuildOrderMessage;
import com.deco2800.potatoes.networking.Network.ClientChatMessage;
import com.deco2800.potatoes.networking.Network.ClientPlayerUpdatePositionMessage;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

public class NetworkClient {
    private static final transient Logger LOGGER = LoggerFactory.getLogger(NetworkClient.class);

    public Client client;
    private String IP;
    private String name;
    private int tcpPort;
    private int udpPort;
    // ID of this client
    private int clientID;

    /* If connection is established and everything is initialized this should be true.
            (Marked volatile because (I think) java caches values when you read them often, and we can get a lockup
            if we're repeatedly reading this value waiting for it to change)
     */
    public volatile boolean ready;

    // List of clients, index is the id
    private ArrayList<String> clientList;

    /**
     * Initializes a client for the game
     */
    public NetworkClient() {
        this.clientID = -1;
        this.IP = null;
        this.name = null;
        this.tcpPort = 0;
        this.udpPort = 0;
        this.ready = false;
        this.clientList = new ArrayList<>();

        // Allow up to 16 clients
        for (int i = 0; i < 16; ++i) {
            clientList.add(null);
        }
    }

    /**
     * Connects to a server with the provided parameters
     * @param name name of the player
     * @param IP ip to connect to
     * @param tcpPort tcp port to use
     * @param udpPort udp port to use
     * @throws IOException when connection fails
     */
    public void connect(String name, String IP, int tcpPort, int udpPort) throws IOException {
        this.IP = IP;
        this.name = name;
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;


        // Kyro warning level
        Log.set(Log.LEVEL_WARN);

        // Initialize client object
        client = new Client();
        client.start();

        // Register our serializable objects
        Network.register(client);

        // Hacky!
        NetworkClient thisClient = this;

        // Setup listeners
        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                super.received(connection, object);

                if (object instanceof FrameworkMessage) {
                    return;
                }
                ClientMessageProcessor.processMessage(thisClient, object);
            }

            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);
            }
        });


        client.connect(5000, IP, tcpPort, udpPort);
        LOGGER.info("Joining " + IP + ":" + tcpPort);
        // Send initial connection info
        Network.ClientConnectionRegisterMessage cr = new Network.ClientConnectionRegisterMessage();
        cr.setName(name);

        client.sendTCP(cr);
    }


    /**
     * Broadcasts a chat message to the server to distribute to the rest of the clients
     * @param message simple message to be sent
     */
    public void broadcastMessage(String message) {
        ClientChatMessage m = new ClientChatMessage();
        m.setMessage(message);
        client.sendTCP(m);
    }

    /**
     * Updates the current player position to the server, which is then sent the rest of the clients
     * @param entity the player entity to be used for update
     */
    public void broadcastPlayerUpdatePosition(Player entity) {
        ClientPlayerUpdatePositionMessage message = new ClientPlayerUpdatePositionMessage();
        message.setX(entity.getPosX());
        message.setY(entity.getPosY());

        client.sendUDP(message);
    }

    /**
     * Tells the server we want to build something somewhere
     * @param tree Tree to build
     */
    public void broadcastBuildOrder(AbstractTree tree) {
        ClientBuildOrderMessage m = new ClientBuildOrderMessage();
        m.setTree(tree);
        client.sendTCP(m);
    }

    /**
     * Posts a system message to the chat of this client
     * @param m message to be posted
     */
    public void sendSystemMessage(String m) {
        GuiManager g = GameManager.get().getManager(GuiManager.class);
        ChatGui chat = ((ChatGui)g.getGui(ChatGui.class));
        if (chat != null) {
            chat.addMessage("System", m, Color.YELLOW);
        }
    }

    /**
     * @return the client id
     */
    public int getID() {
        return clientID;
    }

    /**
     * Changes the client id
     * @param id new id
     */
    public void setID(int id) { clientID = id; }

    /**
     * @return the list of clients we know about
     */
    public ArrayList<String> getClients() {
        return clientList;
    }


    /**
     * Rudely closes the server
     */
    public void disconnect() {
        LOGGER.info("Disconnecting from the server");
        client.close();
        client = null;
    }
}
