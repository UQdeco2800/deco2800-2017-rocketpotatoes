package com.deco2800.potatoes.cheats.rust;

import com.sun.jna.Structure;

import java.io.Closeable;
import java.util.List;
import java.util.Arrays;

public class RenderLine extends Structure implements Closeable {
    public static class ByReference extends RenderLine implements Structure.ByReference {
    }

    public static class ByValue extends RenderLine implements Structure.ByValue {
    }

    private int srcX;
    private int srcY;
    private int dstX;
    private int dstY;


    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("srcX", "srcY", "dstX", "dstY");
    }

    @Override
    public void close() {
        // Turn off "auto-synch". If it is on, JNA will automatically read all fields
        // from the struct's memory and update them on the Java object. This synchronization
        // occurs after every native method call. If it occurs after we drop the struct, JNA
        // will try to read from the freed memory and cause a segmentation fault.
        setAutoSynch(false);
    }

    /**
     * Returns the line's src x
     * @return srcX
     */
    public int getSrcX() {
        return srcX;
    }

    /**
     * Returns the line's src y
     * @return srcY
     */
    public int getSrcY() {
        return srcY;
    }

    /**
     * Returns the line's dst x
     * @return dstX
     */
    public int getDstX() {
        return dstX;
}

    /**
     * Returns the line's dst y
     * @return dstY
     */
    public int getDstY() {
        return dstY;
    }
}
