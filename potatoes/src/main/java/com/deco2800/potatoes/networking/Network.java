package com.deco2800.potatoes.networking;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.resources.FoodResource;
import com.deco2800.potatoes.entities.resources.Resource;
import com.deco2800.potatoes.entities.resources.SeedResource;
import com.deco2800.potatoes.entities.trees.AbstractTree;
import com.deco2800.potatoes.entities.trees.TreeProjectileShootEvent;
import com.deco2800.potatoes.entities.trees.TreeProperties;
import com.deco2800.potatoes.managers.Inventory;
import com.deco2800.potatoes.util.Box3D;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import org.reflections.Reflections;

import java.util.*;

public class Network {

    private Network() {
        // Hide public constructor
    }

    /**
     * Registers our classes for serialization, to be used by both client and server in their initialization.
     * @param endPoint
     */
    public static void register(EndPoint endPoint) {
        Kryo k = endPoint.getKryo();

        /* Message types */
        k.register(ClientConnectionRegisterMessage.class);
        k.register(ClientPlayerUpdatePositionMessage.class);
        k.register(ClientBuildOrderMessage.class);

        k.register(HostPlayerDisconnectedMessage.class);
        k.register(HostDisconnectMessage.class);
        k.register(HostPlayReadyMessage.class);
        k.register(HostNewPlayerMessage.class);
        k.register(HostConnectionConfirmMessage.class);
        k.register(HostEntityCreationMessage.class);
        k.register(HostEntityDestroyMessage.class);
        k.register(HostEntityUpdatePositionMessage.class);
        k.register(HostEntityUpdateProgressMessage.class);
        k.register(HostExistingPlayerMessage.class);

        k.register(ClientChatMessage.class);
        k.register(HostChatMessage.class);
        // Register member variables here:

        k.register(java.util.Optional.class);
        k.register(Box3D.class);
        k.register(LinkedList.class);
        k.register(TreeProjectileShootEvent.class);
        k.register(TreeProperties.class);
        k.register(Resource.class);
        k.register(FoodResource.class);
        k.register(SeedResource.class);
        k.register(Inventory.class);
        k.register(TreeMap.class);
        k.register(float[][].class);
        k.register(float[].class);
        k.register(String[].class);
        k.register(Class.class);

        /* Maybe don't serialize entire entities at all. But rather have custom generalized messages for different
         * actions? Requires as much abstraction as possible with regards to custom behaviour, shouldn't be too tedious
         */

        Reflections reflections = new Reflections("com.deco2800");

        Set<Class<? extends AbstractEntity>> entities =
                reflections.getSubTypesOf(com.deco2800.potatoes.entities.AbstractEntity.class);

        // Order matters so let's order them
        TreeSet<Class<? extends AbstractEntity>> sorted = new TreeSet<>(Comparator.comparing(Class::getCanonicalName));

        sorted.addAll(entities);

        for (Class c : sorted) {
            // Auto register entities!
            k.register(c);
        }


    }

    // Define our custom types/containers for serialization here
    // (then register)

    // Client...Message is the format for a message to the host
    // Host...Message is the format for a message sent to clients
    // Anything else can be used for either

    /* Message sent when a connection is initially made,
     * should be the first message between a client and host */
    public static class ClientConnectionRegisterMessage {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class HostDisconnectMessage {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class HostPlayerDisconnectedMessage {
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    /* Message telling the client they are ready to play */
    public static class HostPlayReadyMessage {
    }

    /* Message telling other clients of a new player */
    public static class HostNewPlayerMessage {
        private String name;
        private int id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    /* Message telling new clients of an existing player, doesn't create the player entity when processing this */
    public static class HostExistingPlayerMessage {
        private String name;
        private int id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }


    /* Message confirming connection, gives the client their id */
    public static class HostConnectionConfirmMessage {
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    /* Direct response to a HostEntityCreationMessage, this message is sent to all clients
     * to tell them of this entities existence and it's unique identifier.
     */
    public static class HostEntityCreationMessage {
        private AbstractEntity entity;
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public AbstractEntity getEntity() {
            return entity;
        }

        public void setEntity(AbstractEntity entity) {
            this.entity = entity;
        }
    }

    public static class HostEntityDestroyMessage {
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    /* Message indicating our player moved */
    public static class ClientPlayerUpdatePositionMessage {
        private float x, y;

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }

    /* Message indicating our player wants to build something */
    public static class ClientBuildOrderMessage {
        private AbstractTree tree;

        public AbstractTree getTree() {
            return tree;
        }

        public void setTree(AbstractTree tree) {
            this.tree = tree;
        }
    }

    /* Message from the host indicating a new position of an entity */
    public static class HostEntityUpdatePositionMessage {
        private float x, y;
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }

    /* Message from the host indicating an entity's progress has changed (using the HasProgress interface) */
    public static class HostEntityUpdateProgressMessage {
        private int progress;
        private int id;

        public int getProgress() {
            return progress;
        }

        public void setProgress(int progress) {
            this.progress = progress;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    /* Simple chat message object */
    public static class ClientChatMessage {
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        private String message;
    }

    /* Chat message object sent with sender ID */
    public static class HostChatMessage {
        private String message;
        private int id;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

}
