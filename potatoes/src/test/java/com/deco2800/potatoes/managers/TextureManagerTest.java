package com.deco2800.potatoes.managers;

import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;

public class TextureManagerTest {
	
	private static class GdxTestApplication extends ApplicationAdapter {
		
	}
	
	@Test
    public void loadTexture() {
		HeadlessApplicationConfiguration conf = new HeadlessApplicationConfiguration();
		new HeadlessApplication(new GdxTestApplication(), conf);
		Gdx.gl = mock(GL20.class);
		
		TextureManager.loadTextures();
    }

}