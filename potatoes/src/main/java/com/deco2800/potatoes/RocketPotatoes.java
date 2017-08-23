package com.deco2800.potatoes;

import com.badlogic.gdx.Game;
import com.deco2800.potatoes.screens.GameScreen;
import com.deco2800.potatoes.screens.MainMenuScreen;

public class RocketPotatoes extends Game {

	/**
	 * Called when the {@link Application} is first created.
	 */
	@Override
	public void create() {
		this.setScreen(new MainMenuScreen(this));
	}

	public void render() {
		super.render();
	}

	public void dispose() {
	}
}