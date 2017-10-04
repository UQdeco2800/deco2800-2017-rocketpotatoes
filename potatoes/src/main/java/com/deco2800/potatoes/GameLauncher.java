package com.deco2800.potatoes;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * Launches RocketPotatoes.
 *
 * @author leggy
 *
 */
public class GameLauncher {
	private GameLauncher() { }

	public interface RLibrary extends Library {
		RLibrary INSTANCE = (RLibrary) Native.loadLibrary("fewefw", RLibrary.class);

		void print_stuff(String stuff);
	}

	/**
	 * Main function for the game
	 *
	 * @param args
	 *            Command line arguments (we wont use these)
	 */
	public static void main(String[] args) {
		//Native.DEBUG_LOAD = true;
		RLibrary.INSTANCE.print_stuff("Ayy I am the rust memelord");
		/*
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.WARN); // We probably need a logger config file at some point
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		config.title = "DECO2800 2017: RocketPotatoes";
		@SuppressWarnings("unused")
		LwjglApplication game = new LwjglApplication(new RocketPotatoes(), config);
		*/
	}
}
