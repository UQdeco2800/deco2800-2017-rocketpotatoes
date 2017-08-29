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
public class SoundManager extends Manager {

	private static final Logger LOGGER = LoggerFactory.getLogger(SoundManager.class);

	private float effectsVolume = 1f;
	private float musicVolume = 1f;

	private Music music;

	/**
	 * Plays sound effects file at resources/sounds/soundString.
	 */
	public void playSound(String soundString) {
		LOGGER.info("Playing sound effect");
		Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/" + soundString));
		sound.play(effectsVolume);
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
	public void  stopMusic(){
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
		if (music != null) {
			music.setVolume(effectsVolume);
		}
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
