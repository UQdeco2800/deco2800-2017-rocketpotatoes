package com.deco2800.potatoes;

import com.badlogic.gdx.Game;
import com.deco2800.potatoes.screens.MainMenuScreen;

public class RocketPotatoes extends Game {
	
	/**
	 * Called when the {@link Application} is first created.
	 */
	@Override
	public void create() {
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void dispose() {
		System.exit(0);
	}
}
