package com.deco2800.potatoes;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Launches RocketPotatoes.
 *
 * @author leggy
 *
 */
public class GameLauncher {
	private GameLauncher() { }

	public interface RLibrary extends Library {
		RLibrary INSTANCE = (RLibrary) Native.loadLibrary("rustyfish", RLibrary.class);

		void startGame(Callback startDraw, Callback endDraw,
					   Callback updateWindow, Callback getWindowInfo,
					   Callback drawSprite);
	}

	/**
	 * Main function for the game
	 *
	 * @param args
	 *            Command line arguments (we wont use these)
	 */
	public static void main(String[] args) {
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.WARN); // We probably need a logger config file at some point
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		config.title = "DECO2800 2017: RocketPotatoes";
		@SuppressWarnings("unused")
		LwjglApplication game = new LwjglApplication(new RocketPotatoes(), config);
	}
}
