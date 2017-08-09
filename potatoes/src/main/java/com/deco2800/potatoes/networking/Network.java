package com.deco2800.potatoes.networking;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {

    /**
     * Registers our classes for serialization, to be used by both client and server in their initialization.
     * @param endPoint
     */
    public static void register(EndPoint endPoint) {
        Kryo k = endPoint.getKryo();
        k.register(ConnectionRegister.class);
        k.register(Message.class);

        // k.register(Type.class) <-- ex
    }

    // Define our custom types/containers for serialization here
    // (then register)

    /* Message sent when a connection is initially made,
     * should be the first message between a client and host */
    static public class ConnectionRegister {
        public String name;
    }

    /* Simple message object, TODO colours, formatting etc */
    static public class Message {
        public String message;
    }

}
