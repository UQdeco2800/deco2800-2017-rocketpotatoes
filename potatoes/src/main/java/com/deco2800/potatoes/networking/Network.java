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

        // k.register(Type.class) <-- ex
    }

    // Define our custom types/containers for serialization here
    // (then register)
}
