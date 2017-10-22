package com.deco2800.potatoes.cheats.rust;

import com.sun.jna.Structure;

import java.io.Closeable;
import java.util.List;
import java.util.Arrays;

public class RenderInfo extends Structure implements Closeable {
    public static class ByReference extends RenderInfo implements Structure.ByReference {
    }

    public static class ByValue extends RenderInfo implements Structure.ByValue {
    }

    public int sizeX;
    public int sizeY;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("sizeX", "sizeY");
    }

    @Override
    public void close() {
        // Turn off "auto-synch". If it is on, JNA will automatically read all fields
        // from the struct's memory and update them on the Java object. This synchronization
        // occurs after every native method call. If it occurs after we drop the struct, JNA
        // will try to read from the freed memory and cause a segmentation fault.
        setAutoSynch(false);
    }
}
