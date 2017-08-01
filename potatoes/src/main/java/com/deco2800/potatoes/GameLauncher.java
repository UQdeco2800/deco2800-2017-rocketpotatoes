package com.deco2800.potatoes;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * Launches RocketPotatoes.
 * 
 * @author leggy
 *
 */
public class GameLauncher {
	/**
	 * Main function for the game
	 * 
	 * @param arg
	 *            Command line arguments (we wont use these)
	 */
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		config.title = "DECO2800 2017: RocketPotatoes";
		@SuppressWarnings("unused")
		LwjglApplication game = new LwjglApplication(new RocketPotatoes(), config);
	}
}