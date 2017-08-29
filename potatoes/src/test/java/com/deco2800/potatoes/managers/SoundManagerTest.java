package com.deco2800.potatoes.managers;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SoundManagerTest {
    @Test
    public void initAndReadTest(){
        SoundManager m = new SoundManager();
        float sfxVolume = m.getEffectsVolume();
        float musVolume = m.getMusicVolume();
        assertEquals(1f,sfxVolume, 0.01f);
        assertEquals(1f, musVolume, 0.01f);
    }

    /*
    @Test
    public void playMusicTest() {
        SoundManager m = new SoundManager();
        String soundString = "menu_blip.wav";
        m.playMusic(soundString);
        Assert.assertTrue(m.musicPlaying());
        m.stopMusic();
        Assert.assertFalse(m.musicPlaying());
    }
    */

    @Test
    public void changingVolumesTest() {
        SoundManager m = new SoundManager();
        m.setEffectsVolume(0.6f);
        m.setMusicVolume(0.4f);
        assertEquals(0.6f,m.getEffectsVolume(),0.01f);
        assertEquals(0.4f,m.getMusicVolume(),0.01f);
    }

}
