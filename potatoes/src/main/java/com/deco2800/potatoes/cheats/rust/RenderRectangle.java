package com.deco2800.potatoes.cheats.rust;

import com.sun.jna.Structure;

import java.io.Closeable;
import java.util.List;
import java.util.Arrays;

public class RenderRectangle extends Structure implements Closeable {
    public static class ByReference extends RenderRectangle implements Structure.ByReference {
    }

    public static class ByValue extends RenderRectangle implements Structure.ByValue {
    }

    private int x;
    private int y;
    private int w;
    private int h;
    private int color;
    private float alpha;


    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("x", "y", "w", "h", "color", "alpha");
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
     * Returns the rectangle's x
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the rectangle's y
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the rectangle's w
     * @return w
     */
    public int getW() {
        return w;
    }

    /**
     * Returns the rectangle's h
     * @return h
     */
    public int getH() {
        return h;
    }

    /**
     * Returns the rectangle's color
     * @return color
     */
    public int getColor() {
        return color;
    }

    /**
     * Returns the rectangle's alpha
     * @return alpha
     */
    public float getAlpha() {
        return alpha;
    }
}
