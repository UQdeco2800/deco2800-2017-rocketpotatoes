package com.deco2800.potatoes.networking;

import com.deco2800.potatoes.entities.*;
import com.deco2800.potatoes.util.Box3D;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {

    /**
     * Registers our classes for serialization, to be used by both client and server in their initialization.
     * @param endPoint
     */
    public static void register(EndPoint endPoint) {
        Kryo k = endPoint.getKryo();
        k.register(ClientConnectionRegisterMessage.class);
        k.register(HostNewPlayerMessage.class);
        k.register(ClientEntityCreationMessage.class);
        k.register(HostConnectionConfirmMessage.class);
        k.register(HostEntityCreationMessage.class);
        k.register(EntityUpdateMessage.class);
        k.register(EntityDestroyMessage.class);
        k.register(Message.class);

        k.register(Player.class);
        k.register(Squirrel.class);
        k.register(EnemyEntity.class);
        k.register(GoalPotate.class);
        k.register(Peon.class);
        k.register(Projectile.class);
        k.register(BallisticProjectile.class);
        k.register(Tower.class);
        k.register(java.util.Optional.class);
        k.register(Tree.class);
        k.register(Box3D.class);

    }

    // Define our custom types/containers for serialization here
    // (then register)

    // Client...Message is the format for a message to the host
    // Host...Message is the format for a message sent to clients
    // Anything else can be used for either

    /* Message sent when a connection is initially made,
     * should be the first message between a client and host */
    static public class ClientConnectionRegisterMessage {
        public String name;
    }

    /* Message telling other clients of a new player */
    static public class HostNewPlayerMessage {
        public String name;
        public int id;
    }

    /* Message confirming connection, gives the client their id */
    static public class HostConnectionConfirmMessage {
        public int id;
    }

    /* Message for the host to create a new entity */
    static public class ClientEntityCreationMessage {
        public AbstractEntity entity;
    }

    /* Direct response to a HostEntityCreationMessage, this message is sent to all clients
     * to tell them of this entities existence and it's unique identifier.
     */
    static public class HostEntityCreationMessage {
        public AbstractEntity entity;
        public int id;
    }

    static public class EntityDestroyMessage {
        public int id;
    }

    /* EntityUpdateMessage with the given id */
    static public class EntityUpdateMessage {
        public AbstractEntity entity;
        public int id;
    }

    /* Simple message object, TODO colours, formatting etc */
    static public class Message {
        public String message;
    }

}
