package com.deco2800.potatoes.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * SoundManager
 * Required to play sounds in the game engine.
 * @Author Tim Hadwen
 */
public class SoundManager extends Manager {

	private static final Logger LOGGER = LoggerFactory.getLogger(SoundManager.class);

	/**
	 * Plays a fun test sound on a new thread
	 */
	public void playSound(String soundString) {
		LOGGER.info("Playing sound effect");
		Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/" + soundString));
		sound.play(1f);
	}
}
