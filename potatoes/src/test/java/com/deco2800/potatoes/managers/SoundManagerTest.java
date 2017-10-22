package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.BaseTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SoundManagerTest extends BaseTest {

    SoundManager soundManager;

    @Before
    public void setUp() {
        soundManager = GameManager.get().getManager(SoundManager.class);
    }

    @After
    public void tearDown() {
        GameManager.get().clearManagers();
    }

    @Test
    public void initAndReadTest(){

        float sfxVolume = soundManager.getEffectsVolume();
        float musVolume = soundManager.getMusicVolume();
        assertEquals(1.0f,sfxVolume, 0.01f);
        assertEquals(0.25f, musVolume, 0.01f);
    }

    @Test
    public void playMusicTest() {

        String soundString = "menu_blip.wav";
        assertFalse(soundManager.musicPlaying());
        soundManager.playMusic(soundString);
        //assertTrue(m.musicPlaying());
        soundManager.stopMusic();
        assertFalse(soundManager.musicPlaying());
        soundManager.playMusic(null);
    }

    @Test
    public void changingVolumesTest() {

        String soundString = "menu_blip.wav";
        soundManager.playMusic(soundString);
        soundManager.setEffectsVolume(0.6f);
        soundManager.setMusicVolume(0.4f);
        assertEquals(0.6f, soundManager.getEffectsVolume(),0.01f);
        assertEquals(0.4f, soundManager.getMusicVolume(),0.01f);
    }
    @Test
    public void extraTest() {
        soundManager.stopMusic();
        soundManager.setMusicVolume(6);
        String soundString = "menu_blip.wav";
        soundManager.playMusic(soundString);
        soundManager.playMusic(soundString);
        soundManager.playSound("fail");
    }

}
