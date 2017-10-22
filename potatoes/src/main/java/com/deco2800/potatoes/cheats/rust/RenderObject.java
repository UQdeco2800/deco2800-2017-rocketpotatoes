package com.deco2800.potatoes.cheats.rust;

import com.sun.jna.Structure;

import java.io.Closeable;
import java.util.List;
import java.util.Arrays;

public class RenderObject extends Structure implements Closeable {
    public static class ByReference extends RenderObject implements Structure.ByReference {
    }

    public static class ByValue extends RenderObject implements Structure.ByValue {
    }

    private String asset;
    private int x;
    private int y;
    private float rotation;
    private float scale;
    private int flipX;
    private int flipY;
    private int color;


    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("asset", "x", "y", "rotation", "scale", "flipX", "flipY", "color");
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
     * Return the object's asset value
     * @return asset
     */
    public String getAsset() {
        return asset;
    }

    /**
     * Return the object's x value
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * Return the object's y value
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * Return the object's rotation value
     * @return asset
     */
    public float getRotation() {
        return rotation;
    }

    /**
     * Return the object's scale value
     * @return scale
     */
    public float getScale() {
        return scale;
    }

    /**
     * Return the object's flipX value
     * @return asset
     */
    public int getFlipX() {
        return flipX;
    }

    /**
     * Return the object's flipY value
     * @return flipY
     */
    public int getFlipY() {
        return flipY;
    }

    /**
     * Return the object's color value
     * @return color
     */
    public int getColor() {
        return color;
    }
}
