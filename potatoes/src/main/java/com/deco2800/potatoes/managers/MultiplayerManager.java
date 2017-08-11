package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.networking.NetworkClient;
import com.deco2800.potatoes.networking.NetworkServer;

import java.io.BufferedOutputStream;
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
    private int port;

    // Our client representation (null if not connected (i.e. singleplayer)) ?? Maybe should always have a client
    private NetworkClient client = null;

    // Our server representation (null if not hosting a server)
    @SuppressWarnings("unused")
    private NetworkServer server = null;

    /* Boolean representing if this client is the master (i.e. host). Things like waves spawning should be initiated
     * by the host.
     */
    private Boolean master;

    /**
     * Initializes some values for the manager
     */
    public MultiplayerManager() {
        ip = "";
        port = -1;
        client = null;
        server = null;
        master = false;
    }

    /**
     * @return The IP string this client is connected to, "" if none is set yet
     */
    public String getIP() {
        return ip;
    }

    /**
     * @return The port number the client is connected to, -1 if none
     */
    public int getPort() {
        return port;
    }


    /**
     * Creates a host in the background with the given port, the client then has to connect to this server using
     * createHost(...); TODO error checking should throw exceptions?
     * @param port - Port this server should host on
     * @return
     *   0  : SUCCESS
     *  -1  : INVALID_PORT
     *  -2  : PORT_OCCUPIED
     *  -3  : HOST_ALREADY_EXISTS
     *  -4  : OTHER_ERROR
     */
    public int createHost(int port) {
        if (isValidPort(port)) { // TODO handle port in use?
            try {
                server = new NetworkServer(port, port);
            }
            catch (IOException ex) {
                // TODO handle errors
                System.exit(-1);
            }
            master = true;
        }

        return 0;
    }

    /**
     * Join's the given IP and port, with the given name (which is then stored in the manager).
     * @param name
     * @param IP - String representing an IP, in the format (255.255.255.255),
     * @param port - port number in range of 1024-65565 (or 0 for any port) ?? TODO 0 port
     * @return
     */
    public int joinGame(String name, String IP, int port) throws IOException {
        client = new NetworkClient(name, IP, port, port);
        return 0;
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

        }
    }

    /**
     * Broadcasts the creation of a new entity to all clients. Note that the client should not assume that this
     * succeeded but should then wait for the host to broadcast the entities creation to the client itself, which
     * will also contain a unique id.
     * TODO this is a potential optimization (i.e. predictive)
     * @param entity
     */
    public void broadcastNewEntity(AbstractEntity entity) {
        if (client != null) {
            client.broadcastNewEntity(entity);
        }
    }

    public void broadcastEntityUpdatePosition(AbstractEntity entity, int id) {
        if (client != null) {
            client.broadcastEntityUpdatePosition(entity, id);
        }
    }

    /**
     * @return if this game is multiplayer
     */
    public Boolean isMultiplayer() {
        return client != null || server != null;
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
            return 0;
        }
    }

    /**
     * Returns true if the client is ready to play
     * @return
     */
    public boolean isReady() { return client.ready; }

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
    public static Boolean isValidPort(int p) {
        // TODO 128 < ports < 1024 are avaliable if running as root/admin could check this
        if (p != 0 && p < 1024 || p > 65535) {
            return false;
        }

        return true;
    }
}
