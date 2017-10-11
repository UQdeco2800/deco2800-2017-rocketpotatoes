package com.deco2800.potatoes.managers;

import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
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

	TextureManager textureManager;
	Texture texture;
	HeadlessApplicationConfiguration configuration;

	@Before
	public void setUp() {
		textureManager = GameManager.get().getManager(TextureManager.class);
		configuration = new HeadlessApplicationConfiguration();

	}

	@After
	public void tearDown() {
		GameManager.get().clearManagers();
		texture = null;
		configuration = null;
	}

	@Test
    public void loadTexture() {

		textureManager = new TextureManager();
		new HeadlessApplication(new GdxTestApplication(), configuration);
		Gdx.gl = mock(GL20.class);
		texture = textureManager.getTexture("undefined");
		TextureManager.loadTextures();
    }
	
	@Test
	public void getTextureTest() {

        TextureManager.saveTexture("healthbar", "resources/healthproperties/Full_Health_Bar.png");
		texture = textureManager.getTexture("healthbar");
		Assert.assertNotNull("healthbar sprite should have been saved", texture);
	}
	
}