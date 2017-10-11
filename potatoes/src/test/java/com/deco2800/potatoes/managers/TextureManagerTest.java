package com.deco2800.potatoes.managers;

import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
	
	@Test
	public void getTextureTest() {
		TextureManager textureManager = new TextureManager();
		Texture texture = textureManager.getTexture("undefined");
        textureManager.saveTexture("healthbar", "resources/healthproperties/Full_Health_Bar.png");
		texture = textureManager.getTexture("healthbar");
		Assert.assertNotNull("healthbar sprite should have been saved", texture);
	}
	
}