package com.deco2800.potatoes.cheats.rust;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import org.lwjgl.opengl.Display;

public class Rustyfish {

    private interface RLibrary extends Library {
        RLibrary INSTANCE = (RLibrary) Native.loadLibrary("rustyfish", RLibrary.class);

        void startGame(Callback startDraw, Callback endDraw,
                       Callback updateWindow, Callback clearWindow, Callback flushWindow, Callback getWindowInfo,
                       Callback drawSprite);
    }


    /**
     * Starts a draw batch
     */
    private static Callback startDraw = new Callback() {
        @SuppressWarnings("unused")
        public void run() {

        }
    };

    /**
     * Ends the current draw batch
     */
    private static Callback endDraw = new Callback() {
        @SuppressWarnings("unused")
        public void run() {

        }
    };

    /**
     * Updates the window, checking for resize events, key events etc.
     *
     * Places key information inside STRUCT TODO
     */
    private static Callback updateWindow = new Callback() {
        @SuppressWarnings("unused")
        public void run() {
            Display.update();
        }
    };

    /**
     * Clears the window with default black color
     */
    private static Callback clearWindow = new Callback() {
        @SuppressWarnings("unused")
        public void run() {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        }
    };

    /**
     * Flushes the window (swaps backbuffers)
     */
    private static Callback flushWindow = new Callback() {
        @SuppressWarnings("unused")
        public void run() {
            Gdx.gl.glFlush();
        }
    };

    /**
     * Get's the window information and places it inside the info object
     */
    private static Callback getWindowInfo = new Callback() {
        @SuppressWarnings("unused")
        public void run(RenderInfo.ByReference info) {
            info.sizeX = Gdx.graphics.getWidth();
            info.sizeY = Gdx.graphics.getHeight();

            //System.out.println(info);
        }
    };

    /**
     * Draw's a given sprite with
     *
     * name
     * x
     * y
     * etc... TODO
     */
    private static Callback drawSprite = new Callback() {
        @SuppressWarnings("unused")
        public void run() {
        }
    };

    public static void run() {
        RLibrary.INSTANCE.startGame(startDraw, endDraw, updateWindow, clearWindow, flushWindow, getWindowInfo, drawSprite);
    }
}
