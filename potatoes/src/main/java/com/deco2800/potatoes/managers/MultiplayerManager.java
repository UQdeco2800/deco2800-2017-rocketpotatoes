package com.deco2800.potatoes.managers;

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

    /**
     * Initializes some values for the manager
     */
    public MultiplayerManager() {
        ip = "";
        port = -1;
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
     * Creates a host in the background with the given port, this method also automatically has the client join this
     * host as a client
     * @param port - Port this server should host on
     * @return
     *   0  : SUCCESS
     *  -1  : INVALID_PORT
     *  -2  : PORT_OCCUPIED
     *  -3  : HOST_ALREADY_EXISTS
     *  -4  : OTHER_ERROR
     */
    public int createHost(int port) {
        return 0;
    }

    /**
     * Join's the given IP and port, with the given name (which is then stored in the manager).
     * @param name
     * @param IP - String representing an IP, in the format (255.255.255.255),
     * @param port -
     * @return
     */
    public int joinGame(String name, String IP, int port) {
        return 0;
    }


    /**
     * Broadcasts the supplied message to all clients currently connected to the host
     * @param message
     */
    public void broadcastMessage(String message) {

    }

    /**
     * Sends a message to the given clientID
     * @param clientID
     * @param message
     */
    public void sendMessageTo(int clientID, String message) {

    }

    /**
     * Broadcasts an object to all clients, TODO unique identification system?
     * @param object
     */
    public void broadcastObject(Object object) {

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
