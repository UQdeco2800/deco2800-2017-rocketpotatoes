package com.deco2800.potatoes;

import com.badlogic.gdx.Game;
import com.deco2800.potatoes.screens.GameScreen;

public class RocketPotatoes extends Game {

	/**
	 * Called when the {@link Application} is first created.
	 */
	@Override
	public void create() {
		this.setScreen(new GameScreen(this));
	}

	public void render() {
		super.render(); //important!
	}

	public void dispose() {
	}
}