package com.deco2800.potatoes.networking;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.FoodResource;
import com.deco2800.potatoes.entities.Resource;
import com.deco2800.potatoes.entities.SeedResource;
import com.deco2800.potatoes.entities.trees.AbstractTree;
import com.deco2800.potatoes.entities.trees.TreeProjectileShootEvent;
import com.deco2800.potatoes.entities.trees.TreeStatistics;
import com.deco2800.potatoes.managers.Inventory;
import com.deco2800.potatoes.util.Box3D;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import org.reflections.Reflections;

import java.util.*;

public class Network {

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
        k.register(TreeProjectileShootEvent.class); // TODO custom protocol for abitrary events?
        k.register(TreeStatistics.class);
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
        TreeSet<Class<? extends AbstractEntity>> sorted = new TreeSet<>(new Comparator<Class<? extends AbstractEntity>>() {
            @Override
            public int compare(Class<? extends AbstractEntity> aClass, Class<? extends AbstractEntity> t1) {
                // Compare by class name
                return aClass.getCanonicalName().compareTo(t1.getCanonicalName());
            }
        });

        sorted.addAll(entities);

        for (Class c : sorted) {
            //System.out.println(c.getCanonicalName());
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
        public String name;
    }

    public static class HostDisconnectMessage {
        public String message;
    }

    public static class HostPlayerDisconnectedMessage {
        public int id;
    }

    /* Message telling the client they are ready to play */
    public static class HostPlayReadyMessage {
    }

    /* Message telling other clients of a new player */
    public static class HostNewPlayerMessage {
        public String name;
        public int id;
    }

    /* Message telling new clients of an existing player, doesn't create the player entity when processing this */
    public static class HostExistingPlayerMessage {
        public String name;
        public int id;
    }


    /* Message confirming connection, gives the client their id */
    public static class HostConnectionConfirmMessage {
        public int id;
    }

    /* Direct response to a HostEntityCreationMessage, this message is sent to all clients
     * to tell them of this entities existence and it's unique identifier.
     */
    public static class HostEntityCreationMessage {
        public AbstractEntity entity;
        public int id;
    }

    public static class HostEntityDestroyMessage {
        public int id;
    }

    /* Message indicating our player moved
     * TODO support for z? Unused so far */
    public static class ClientPlayerUpdatePositionMessage {
        public float x;
        public float y;
    }

    /* Message indicating our player wants to build something
     * TODO support other types? AbstractTree?? */
    public static class ClientBuildOrderMessage {
        public AbstractTree tree;
    }

    /* Message from the host indicating a new position of an entity */
    public static class HostEntityUpdatePositionMessage {
        public float x;
        public float y;
        public int id;
    }

    /* Message from the host indicating an entity's progress has changed (using the HasProgress interface) */
    public static class HostEntityUpdateProgressMessage {
        public int progress;
        public int id;
    }

    /* Simple chat message object */
    public static class ClientChatMessage {
        public String message;
    }

    /* Chat message object sent with sender ID */
    public static class HostChatMessage {
        public String message;
        public int id;
    }

}
