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

	private float masterVolume = 1f;
	private float musicVolume = 1f;

	private Music music;

	/**
	 * Plays sound effects on a new thread
	 */
	public void playSound(String soundString) {
		LOGGER.info("Playing sound effect");
		Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/" + soundString));
		sound.play(masterVolume);
	}

	/**
	 * Plays music.
	 */
	public void playMusic(String musicString){
		LOGGER.info("Playing music.");
		music = Gdx.audio.newMusic(Gdx.files.internal("sounds/" + musicString));
		music.setVolume(masterVolume *  musicVolume);
		music.setLooping(true);
		music.play();
	}

	/**
	 * Stops music playing.
	 */
	public void  stopMusic(){
		music.stop();
	}

	/**
	 * Sets a new master volume.
	 */
	public void setMasterVolume(float v) {masterVolume = v;}

	/**
	 * Gets the current master volume.
	 */
	public float getMasterVolume(){return masterVolume;}

	/**
	 * Sets a new music volume. (music is played at master * music volume)
	 */
	public void setMusicVolume(float v) {musicVolume = v;}

}
