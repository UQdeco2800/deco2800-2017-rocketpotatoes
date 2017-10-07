package com.deco2800.potatoes.cheats.rust;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;

public class Rustyfish {

    private interface RLibrary extends Library {
        RLibrary INSTANCE = (RLibrary) Native.loadLibrary("rustyfish", RLibrary.class);

        void startGame(Callback startDraw, Callback endDraw,
                       Callback updateWindow, Callback getWindowInfo,
                       Callback drawSprite);
    }

    private static Callback startDraw = new Callback() {
        @SuppressWarnings("unused")
        public void run() {
            System.out.println("Start draw");
        }
    };

    private static Callback endDraw = new Callback() {
        @SuppressWarnings("unused")
        public void run() {
            System.out.println("End draw");
        }
    };

    private static Callback updateWindow = new Callback() {
        @SuppressWarnings("unused")
        public void run() {
            System.out.println("Update window");
        }
    };

    private static Callback getWindowInfo = new Callback() {
        @SuppressWarnings("unused")
        public void run(RenderInfo.ByReference info) {
            System.out.println("Get window info:");

            info.sizeX = 100;
            info.sizeY = 100;

            System.out.println(info);
        }
    };

    private static Callback drawSprite = new Callback() {
        @SuppressWarnings("unused")
        public void run() {
            System.out.println("Draw sprite");
        }
    };

    public static void run() {
        RLibrary.INSTANCE.startGame(startDraw, endDraw, updateWindow, getWindowInfo, drawSprite);
    }
}
