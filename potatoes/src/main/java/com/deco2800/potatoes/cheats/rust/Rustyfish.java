package com.deco2800.potatoes.cheats.rust;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.TextureManager;
import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import org.lwjgl.opengl.Display;

public class Rustyfish {

    private static SpriteBatch batch = new SpriteBatch();

    private interface RLibrary extends Library {
        RLibrary INSTANCE = (RLibrary) Native.loadLibrary("rustyfish", RLibrary.class);

        void startGame(Callback startDraw, Callback endDraw,
                       Callback updateWindow, Callback isSpacePressed,
                       Callback clearWindow, Callback flushWindow, Callback getWindowInfo,
                       Callback drawSprite);
    }


    /**
     * Starts a draw batch
     */
    private static Callback startDraw = new Callback() {
        @SuppressWarnings("unused")
        public void run() {
            batch.begin();
        }
    };

    /**
     * Ends the current draw batch
     */
    private static Callback endDraw = new Callback() {
        @SuppressWarnings("unused")
        public void run() {
            batch.end();
        }
    };

    /**
     * Updates the window, checking for resize events, key events etc.
     *
     * Places key information inside STRUCT TODO
     */
    private static Callback updateWindow = new Callback() {
        @SuppressWarnings("unused")
        public boolean run() {
            Display.update(true);
            int w = (int)(Display.getWidth() * Display.getPixelScaleFactor());
            int h = (int)(Display.getHeight() * Display.getPixelScaleFactor());
            Gdx.gl.glViewport(0, 0, w, h);
            batch = new SpriteBatch();
            Gdx.graphics.setTitle("Rustyfish");

            return true;
        }
    };

    /**
     * Returns true if space is pressed
     */
    private static Callback isSpacePressed = new Callback() {
        @SuppressWarnings("unused")
        public boolean run() {
            return Gdx.input.isKeyPressed(Input.Keys.SPACE);
        }
    };

    /**
     * Clears the window with default black color
     */
    private static Callback clearWindow = new Callback() {
        @SuppressWarnings("unused")
        public void run() {
            Gdx.gl.glClearColor(0, 0.4f, 0.8f, 1);
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
        public void run(RenderObject.ByValue obj) {
            TextureManager m = GameManager.get().getManager(TextureManager.class);
            Texture t = m.getTexture(obj.asset);
            batch.draw(t, obj.x, Gdx.graphics.getHeight() - t.getWidth() - obj.y, t.getWidth() / 2.0f, t.getHeight() / 2.0f,
                    t.getWidth(), t.getHeight(), obj.scale, obj.scale, obj.rotation,
                    0, 0, t.getWidth(), t.getHeight(), false, false);
        }
    };

    public static void run() {
        RLibrary.INSTANCE.startGame(startDraw, endDraw, updateWindow, isSpacePressed, clearWindow, flushWindow, getWindowInfo, drawSprite);
    }
}
