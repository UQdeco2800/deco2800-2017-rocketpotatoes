package com.deco2800.potatoes.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;

/**
 * SoundManager
 * Required to play sounds in the game engine.
 * @Author Tim Hadwen
 */

/*
 * "Ascending the Vale" Kevin MacLeod (incompetech.com)
 * Licensed under Creative Commons: By Attribution 3.0 License
 * http://creativecommons.org/licenses/by/3.0/
 */

public class SoundManager extends Manager {

	private static final Logger LOGGER = LoggerFactory.getLogger(SoundManager.class);

	private float effectsVolume = 1.0f;
	private float musicVolume = 0.25f;

	private Music music;

	/**
	 * Plays sound effects file at resources/sounds/soundString.
	 */
	public void playSound(String soundString) {
		LOGGER.info("Playing sound effect");
		try {
			Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/" + soundString));
			sound.play(effectsVolume);
		}
		catch (NullPointerException ex) {
			LOGGER.warn("Failed to play sound effect");
			// Sound doesn't exist, or we have no sound device (i.e. on jenkins)
		}
	}

	/**
	 * Plays music file at resources/sounds/musicString.
	 */
	public void playMusic(String musicString){
		LOGGER.info("Playing music.");
		music = Gdx.audio.newMusic(Gdx.files.internal("sounds/" + musicString));
		if (music != null) {
			music.setVolume(musicVolume);
			music.setLooping(true);
			music.play();
		}
	}

	/**
	 * Stops music playing.
	 */
	public void stopMusic(){
		if (music != null) {
			music.stop();
		}
	}

	/**
	 * Check if music is playing.
	 */
	public boolean musicPlaying() {
		if (music != null) {
			return music.isPlaying();
		}
		return false;
	}

	/**
	 * Sets a new sound effects volume.
	 */
	public void setEffectsVolume(float v) {
		effectsVolume = v;
	}

	/**
	 * Gets the current sound effects volume.
	 */
	public float getEffectsVolume(){return effectsVolume;}

	/**
	 * Sets a new music volume. (music is played at master * music volume)
	 */
	public void setMusicVolume(float v) {
		musicVolume = v;
		if (music != null) {
			music.setVolume(musicVolume);
		}
	}

	/**
	 * Gets the current music volume.
	 */
	public float getMusicVolume(){return musicVolume;}

}
