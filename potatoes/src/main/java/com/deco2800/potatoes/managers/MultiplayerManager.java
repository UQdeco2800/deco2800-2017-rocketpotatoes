package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.entities.trees.AbstractTree;
import com.deco2800.potatoes.networking.NetworkClient;
import com.deco2800.potatoes.networking.NetworkServer;
import com.google.common.net.InetAddresses;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Handles multiplayer setup, and communication.
 *
 * Can also create a host server from this client, which this client then connects to and acts as a client.
 * This client also becomes the `head` client which means it's responsible for being the absolute state of the game
 * as well as killing the rest of the clients if the host client disconnects.
 *
 * @author tgrkzus
 *
 */
public class MultiplayerManager extends Manager {
    // IP this client is connected to ("" if none)
    private String ip;

    // Port this client is connected to (-1 if none)
    private int clientPort;
    private int serverPort;

    // Our client representation (null if not connected (i.e. singleplayer)) ?? Maybe should always have a client
    private NetworkClient client = null;

    // Our server representation (null if not hosting a server)
    @SuppressWarnings("unused")
    private NetworkServer server = null;

    /* Boolean representing if this client is the master (i.e. host). Things like waves spawning should be initiated
     * by the host.
     */
    private Boolean master;

    private Boolean multiplayer;

    /**
     * Initializes some values for the manager
     */
    public MultiplayerManager() {
        ip = "";
        clientPort = -1;
        serverPort = -1;
        client = null;
        server = null;
        master = false;
        multiplayer = false;
    }

    /**
     * @return The IP string this client is connected to, "" if none is set yet
     */
    public String getIP() {
        return ip;
    }

    /**
     * @return The port number the client is connected to
     */
    public int getClientPort() {
        return clientPort;
    }

    /**
     * @return The port number the server is listening on
     */
    public int getServerPort() {
        return serverPort;
    }

    /**
     * Creates a host in the background with the given port, the client then has to connect to this server using
     * joinGame(...); TODO error checking should throw exceptions?
     * @param port
     * @throws IllegalStateException
     * @throws IllegalArgumentException
     * @throws IOException
     */
    public void createHost(int port) throws IllegalStateException, IllegalArgumentException, IOException {
        if (!isValidPort(port)) { throw new IllegalArgumentException("Invalid port: " + port); }
        if (client != null) { throw new IllegalStateException("Client already exists!"); }

        master = true;
        serverPort = port;
        multiplayer = true;
        server = new NetworkServer(port, port);

        // Block until ready
        while (!isServerReady());
    }

    /**
     * Join's the given IP and port, with the given name (which is then stored in the manager).
     * @param name
     * @param IP - String representing an IP, in the format (255.255.255.255),
     * @param port - port number in range of 1024-65565 (or 0 for any port) ?? TODO 0 port
     * @throws IOException
     * @throws IllegalArgumentException
     */
    public void joinGame(String name, String IP, int port) throws IOException, IllegalArgumentException {
        if (!isValidPort(port)) { throw new IllegalArgumentException("Invalid port: " + port); }
        if (!isValidIP(IP)) { throw new IllegalArgumentException("Invalid IP: " + IP); }
        if (client != null) { throw new IllegalStateException("Client already exists!"); }

        // TODO move away from ALL tcp
        clientPort = port;
        multiplayer = true;
        ip = IP;
        client = new NetworkClient();
        client.connect(name, IP, port, port);

        while (!isClientReady());
    }


    /**
     * Broadcasts the supplied message to all clients currently connected to the host
     * @param message
     */
    public void broadcastMessage(String message) {
        if (client != null) {
            client.broadcastMessage(message);
        }
    }

    /**
     * Sends a message to the given clientID
     * @param clientID
     * @param message
     */
    public void sendMessageTo(int clientID, String message) {
        if (client != null) {
            // TODO
        }
    }

    /**
     * Broadcasts the creation of a new entity. Should only be used by master!
     * @param id
     */
    public void broadcastNewEntity(int id) {
        if (client != null) {
            if (!isMaster()) {
                // Probably just want to crash the program here, since there should be no logical way to get here
                // if the client is in multiplayer and not master. As in this function shouldn't even be called?
                throw new IllegalStateException("Non-master clients shouldn't broadcast any new entities.");
            }
            // Tell server directly
            server.broadcastNewEntity(id);
        }
    }


    /**
     * Broadcasts an entities new position. Should only be used by master!
     * @param id
     */
    public void broadcastEntityUpdatePosition(int id) {
        if (client != null) {
            if (!isMaster()) {
                throw new IllegalStateException("Non-master clients shouldn't broadcast any new entity positions!");
            }
            // Tell server directly
            server.broadcastEntityUpdatePosition(id);
        }
    }

    public void broadcastEntityUpdateProgress(int id) {
        if (client != null) {
            if (!isMaster()) {
                throw new IllegalStateException("Non-master clients shouldn't broadcast any progress updates!");
            }

            server.broadcastEntityUpdateProgress(id);
        }
    }


    public void broadcastEntityUpdateTimeEvents(int id) {
    }

    /**
     * Broadcasts an entities destruction. Should only be used by master!
     * @param id
     */
    public void broadcastEntityDestroy(int id) {
        if (client != null) {
            if (!isMaster()) {
                throw new IllegalStateException("Non-master clients shouldn't broadcast any entity destruction!");
            }
            // Tell server directly
            server.broadcastEntityDestroy(id);
        }
    }

    /**
     * Updates the client's player position.
     */
    public void broadcastPlayerUpdatePosition() {
        Player p = GameManager.get().getManager(PlayerManager.class).getPlayer();
        if (client != null) {
            client.broadcastPlayerUpdatePosition(p);
        }
    }


    /**
     * Broadcasts a build order from a client (should only be used by non-master)
     * @param tree The tree to be built
     */
    public void broadcastBuildOrder(AbstractTree tree) {
        if (client != null) {
            client.broadcastBuildOrder(tree);
        }
    }

    /**
     * @return if this game is multiplayer
     */
    public Boolean isMultiplayer() {
        return multiplayer;
    }

    /**
     * @return if this client is a master
     */
    public Boolean isMaster() {
        return master;
    }

    public int getID() {
        if (client != null) {
            return client.getID();
        }
        else {
            return -1;
        }
    }

    /**
     * Returns true if the client is ready to play
     * @return
     */
    public boolean isClientReady() {
        if (client != null) {
            return client.ready;
        }
        else {
            return true;
        }
    }

    /**
     * Returns true if the server is ready to play
     * @return
     */
    public boolean isServerReady() {
        if (server != null) {
            return server.ready;
        }
        else {
            return true;
        }
    }

    public ArrayList<String> getClients() {
        if (client != null) {
            return client.getClients();
        }
        else {
            return null;
        }
    }

    /**
     * Checks if a port number is valid: anything larger than 1024 is avaliable
     * Port 0 is also a valid port (will be assigned to the first avaliable port)
     * @param p - port number to check
     * @return if a port is within a valid range or not
     */
    public static boolean isValidPort(int p) {
        // TODO 128 < ports < 1024 are avaliable if running as root/admin could check this
        if (p != 0 && p < 1024 || p > 65535) {
            return false;
        }

        return true;
    }

    /**
     * Returns if an ip is a valid IP address.
     * @param ip
     * @return
     */
    public static boolean isValidIP(String ip) {
        return InetAddresses.isInetAddress(ip);
    }


    public void disconnectClient() {
        if (client != null) {
            client.disconnect();
            client = null;
        }
    }

    public void shutdownServer() {
        if (server != null) {
            server.shutdown();
            server = null;
        }
    }

    /**
     * Shuts down all multiplayer components and resets the multiplayer manager to it's default state.
     */
    public void shutdownMultiplayer() {
        shutdownServer();
        disconnectClient();

        ip = "";
        clientPort = -1;
        serverPort = -1;
        client = null;
        server = null;
        master = false;
        multiplayer = false;
    }
}
